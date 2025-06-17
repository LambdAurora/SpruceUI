import net.fabricmc.loom.task.RemapJarTask
import spruceui.Constants

plugins {
	id("spruceui-common")
	id("dev.yumi.gradle.licenser").version("2.1.+")
	`java-library`
	`maven-publish`
}

base.archivesName.set(Constants.NAMESPACE)

val fabricModules = setOf(
	"fabric-api-base",
	"fabric-lifecycle-events-v1",
	"fabric-rendering-v1",
	"fabric-resource-loader-v0",
	"fabric-screen-api-v1",
	"fabric-key-binding-api-v1"
)

val testmod: SourceSet by sourceSets.creating {
	this.compileClasspath += sourceSets.main.get().compileClasspath
	this.runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

lambdamcdev {
	manifests {
		fmj {
			withName(Constants.PRETTY_NAME)
			withDescription(Constants.DESCRIPTION)
			withAuthors(Constants.AUTHORS)
			withContact {
				it.withHomepage(Constants.PROJECT_LINK)
					.withSources(Constants.SOURCES_LINK)
					.withIssues(Constants.ISSUES_LINK)
			}
			withLicense(Constants.LICENSE)
			withIcon("assets/${Constants.NAMESPACE}/icon.png")
			withEnvironment("client")
			withDepend("fabricloader", ">=${libs.versions.fabric.loader.get()}")
			withDepend("minecraft", "~1.21.6-")
			withDepend("fabric-resource-loader-v0", ">=0.4.7")
			withDepend("java", ">=${Constants.JAVA_VERSION}")
			withDepend("yumi-commons-core", "^${libs.versions.yumi.commons.get()}")
			withDepend("yumi-commons-collections", "^${libs.versions.yumi.commons.get()}")
			withDepend("yumi-commons-event", "^${libs.versions.yumi.commons.get()}")
			withAccessWidener("spruceui.accesswidener")
			withMixins("spruceui.mixins.json")

			withModMenu {
				it.withBadges("library")
					.withDiscord("https://discord.lambdaurora.dev/")
					.withLink("modmenu.bluesky", "https://bsky.app/profile/lambdaurora.dev")
			}
		}
	}

	setupJarJarCompat()
}

repositories {
	mavenLocal()
	maven {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/releases")
	}
	maven {
		name = "Gegy"
		url = uri("https://maven.gegy.dev/releases/")
	}
	maven {
		name = "NeoForge"
		url = uri("https://maven.neoforged.net/")
		content {
			includeGroupByRegex("net\\.neoforged.*")
			includeGroupByRegex("cpw\\.mods.*")
		}
	}
}

dependencies {
	@Suppress("UnstableApiUsage")
	mappings(lambdamcdev.layered {
		officialMojangMappings()
		// Parchment is currently broken when used with the hacked mojmap layer due to remapping shenanigans.
		//parchment("org.parchmentmc.data:parchment-${mcVersion}:${project.property("parchment_mappings")}@zip")
		mappings("dev.lambdaurora:yalmm:${Constants.mcVersion()}+build.${libs.versions.mappings.yalmm.get()}")
	})
	modImplementation(libs.fabric.loader)

	api(libs.yumi.commons.event) {
		// Exclude Minecraft and loader-provided libraries.
		exclude(group = "org.slf4j")
		exclude(group = "org.ow2.asm")
	}

	fabricModules.stream().map { fabricApi.module(it, libs.versions.fabric.api.get()) }.forEach {
		modImplementation(it)
	}

	modLocalRuntime(libs.modmenu) {
		isTransitive = false
	}

	"testmodCompileOnly"(libs.neoforge.loader)
	"testmodImplementation"(sourceSets.main.get().output)

	include(libs.yumi.commons.core)
	include(libs.yumi.commons.collections)
	include(libs.yumi.commons.event)
}

val mojmap by sourceSets.creating {}

java {
	registerFeature("mojmap") {
		usingSourceSet(mojmap)
		withSourcesJar()
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.isDeprecation = true
	options.isIncremental = true
}

tasks.jar {
	inputs.property("archivesName", base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}

loom {
	accessWidenerPath = file("src/main/resources/spruceui.accesswidener")
	runs {
		register("testmodClient") {
			client()
			source(testmod)
		}
	}
}

val testmodJar = tasks.register<Jar>("testmodJar") {
	this.group = "build"
	this.from(testmod.output)
	this.archiveClassifier = "testmod-dev"
	this.destinationDirectory = project.file("build/devlibs")
}

val remapTestmodJar = tasks.register<RemapJarTask>("remapTestmodJar") {
	this.group = "build"
	this.dependsOn(testmodJar.get())
	this.inputFile.set(testmodJar.get().archiveFile)
	this.classpath.from(testmod.compileClasspath)
	this.archiveClassifier = "testmod"
}
tasks.build.get().dependsOn(remapTestmodJar)

license {
	rule(file("HEADER"))
}

// Configure the maven publication.
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])

			pom {
				name.set(Constants.PRETTY_NAME)
				description.set(Constants.DESCRIPTION)
			}
		}
	}

	repositories {
		mavenLocal()
		maven {
			name = "BuildDirLocal"
			url = uri("${rootProject.layout.buildDirectory.get()}/repo")
		}
		maven {
			name = "GithubPackages"
			url = uri("https://maven.pkg.github.com/LambdAurora/SpruceUI")
			credentials {
				username = (project.findProperty("gpr.user") as? String) ?: System.getenv("USERNAME")
				password = (project.findProperty("gpr.key") as? String) ?: System.getenv("TOKEN")
			}
		}

		val spruceuiMaven = System.getenv("SPRUCEUI_MAVEN")
		if (spruceuiMaven != null) {
			maven {
				name = "SpruceUIMaven"
				url = uri(spruceuiMaven)
				credentials {
					username = (project.findProperty("gpr.user") as? String) ?: System.getenv("MAVEN_USERNAME")
					password = (project.findProperty("gpr.key") as? String) ?: System.getenv("MAVEN_PASSWORD")
				}
			}
		}
	}
}
