import dev.lambdaurora.mcdev.api.McVersionLookup

plugins {
	alias(libs.plugins.loom)
	alias(libs.plugins.lambdamcdev)
	alias(libs.plugins.licenser)
	`java-library`
	`maven-publish`
}

lambdamcdev.namespace = project.property("mod_namespace").toString()
base.archivesName.set(lambdamcdev.namespace)
version = "${project.property("version")}+${McVersionLookup.getVersionTag(libs.versions.minecraft.get())}"
val javaVersion = Integer.parseInt(project.property("java_version") as String)

val modName = project.property("mod_name").toString()
val modDescription = project.property("mod_description").toString()

val fabricModules = setOf(
	"fabric-api-base",
	"fabric-lifecycle-events-v1",
	"fabric-rendering-v1",
	"fabric-resource-loader-v1",
	"fabric-screen-api-v1",
	"fabric-key-mapping-api-v1"
)

java {
	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)

	withSourcesJar()
}

val testmod: SourceSet by sourceSets.creating {
	this.compileClasspath += sourceSets.main.get().compileClasspath
	this.runtimeClasspath += sourceSets.main.get().runtimeClasspath
}

lambdamcdev {
	manifests {
		val fmj = fmj {
			withName(modName)
			withDescription(modDescription)
			withAuthors(listOf("LambdAurora"))
			withContact {
				val projectLink = "https://github.com/LambdAurora/SpruceUI"

				it.withHomepage(projectLink)
				it.withSources("$projectLink.git")
				it.withIssues("$projectLink/issues")
			}
			withLicense("MIT")
			withIcon("assets/${lambdamcdev.namespace.get()}/icon.png")
			withEnvironment("client")
			withDepend("fabricloader", ">=${libs.versions.fabric.loader.get()}")
			withDepend("minecraft", "~26.1-")
			withDepend("fabric-resource-loader-v1", ">=2.0.5")
			withDepend("java", ">=${project.property("java_version")}")
			withDepend("yumi_mc_core", "^${libs.versions.yumi.mc.foundation.get()}")
			withMixins("spruceui.mixins.json")

			withModMenu {
				it.withBadges("library")
					.withDiscord("https://discord.lambdaurora.dev/")
					.withLink("modmenu.bluesky", "https://bsky.app/profile/lambdaurora.dev")
			}
		}
		nmt {
			fmj.copyTo(this)
			withBlurIcon(false)
			withLoaderVersion("[2,)")
			withMixins("spruceui.mixins.json")
			withDepend("minecraft", "[${libs.versions.minecraft.get()},)")
			withDepend("yumi_mc_core", "[${libs.versions.yumi.mc.foundation.get()},)")
		}
	}

	setupActionsRefCheck()
}

loom {
	mixin {
		useLegacyMixinAp = false
	}
	runs {
		register("testmodClient") {
			client()
			source(testmod)
		}
	}
}

repositories {
	mavenCentral()
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
		url = uri("https://maven.neoforged.net/releases/")
		content {
			includeGroupByRegex("net\\.neoforged.*")
			includeGroupByRegex("cpw\\.mods.*")
		}
	}
	mavenLocal()
}

dependencies {
	minecraft(libs.minecraft)
	implementation(libs.fabric.loader)

	api(libs.yumi.mc.foundation)

	fabricModules.stream().map { fabricApi.module(it, libs.versions.fabric.api.get()) }.forEach {
		implementation(it)
	}

	/*modLocalRuntime(libs.modmenu) {
		isTransitive = false
	}*/

	"testmodCompileOnly"(libs.neoforge.loader)
	"testmodImplementation"(sourceSets.main.get().output)
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	options.release.set(javaVersion)
	options.isDeprecation = true
	options.isIncremental = true
}

tasks.jar {
	inputs.property("archivesName", base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}

tasks.named<Jar>("sourcesJar") {
	inputs.property("archivesName", base.archivesName)

	from("LICENSE") {
		rename { "${it}_${inputs.properties["archivesName"]}" }
	}
}

val testmodJar = tasks.register<Jar>("testmodJar") {
	this.group = "build"
	this.from(testmod.output)
	this.archiveClassifier = "testmod"
}
tasks.build.get().dependsOn(testmodJar)

license {
	rule(file("HEADER"))
}

// Configure the maven publication.
publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])

			pom {
				name.set(modName)
				description.set(modDescription)
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
