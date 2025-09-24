import dev.lambdaurora.mcdev.api.manifest.Nmt
import dev.lambdaurora.mcdev.task.GenerateNmtTask
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.api.mappings.layered.MappingsNamespace
import net.fabricmc.loom.task.RemapJarTask
import spruceui.Constants
import spruceui.task.XplatTransformJar

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
			withEntrypoints("client", "dev.lambdaurora.spruceui.hud.HudManager::initialize")
			withDepend("fabricloader", ">=${libs.versions.fabric.loader.get()}")
			withDepend("minecraft", "~1.21 <1.21.2-")
			withDepend("fabric-resource-loader-v0", ">=0.4.7")
			withDepend("java", ">=${project.property("java_version")}")
			withAccessWidener("spruceui.accesswidener")
			withMixins("spruceui.mixins.json")

			withModMenu {
				it.withBadges("library")
					.withDiscord("https://discord.lambdaurora.dev/")
					.withLink("modmenu.bluesky", "https://bsky.app/profile/lambdaurora.dev")
			}
		}
	}
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
}

val mojmap = lambdamcdev.setupMojmapRemapping()
configurations.getByName("mojmapApi") {
	this.extendsFrom(configurations["api"])
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

	fabricModules.stream().map { fabricApi.module(it, libs.versions.fabric.api.get()) }.forEach {
		modImplementation(it)
	}

	modLocalRuntime(libs.modmenu) {
		isTransitive = false
	}

	"testmodImplementation"(sourceSets.main.get().output)
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

//region Mojmap
val remapMojmap = mojmap.registerRemap(tasks.remapJar) {
	this.destinationDirectory.set(layout.buildDirectory.map { it.dir("devlibs") })
}

val remapMojmapSourcesJar = mojmap.registerSourcesRemap(tasks.remapSourcesJar) {
	this.classpath.from(configurations["minecraftClientLibraries"])
	this.archiveClassifier = "preprocessed-mojmap-sources"
}

val nmtGeneration by tasks.registering(GenerateNmtTask::class) {
	this.nmt = lambdamcdev.manifests.fmj().map {
		it.derive(::Nmt)
			.withBlurIcon(false)
			.withLoaderVersion("[2,)")
			.withDepend("minecraft", "[" + libs.versions.minecraft.get() + ",)")
	}
	this.outputDir = project.layout.buildDirectory.map { it.dir("generated/generated_neoforge_resources") }
}

val xplatTransformJar by tasks.registering(XplatTransformJar::class) {
	dependsOn(remapMojmap, nmtGeneration)

	inputJar.set(remapMojmap.flatMap { it.archiveFile })
	from(nmtGeneration)
	this.archiveClassifier = "mojmap"
}
mojmap.setJarArtifact(xplatTransformJar)

val xplatTransformSourcesJar by tasks.registering(XplatTransformJar::class) {
	dependsOn(remapMojmapSourcesJar, nmtGeneration)

	inputJar.set(remapMojmapSourcesJar.flatMap { it.archiveFile })
	from(nmtGeneration)
	this.archiveClassifier = "mojmap-sources"
}
mojmap.setSourcesArtifact(xplatTransformSourcesJar)

val remapTestmodMojmapJar = mojmap.registerRemap("remapTestmodJarToMojmap") {
	dependsOn(remapTestmodJar)

	classpath.setFrom(
		(loom as LoomGradleExtension).getMinecraftJarsCollection(MappingsNamespace.INTERMEDIARY),
		tasks.remapJar
	)
	inputFile.convention(remapTestmodJar.flatMap { it.archiveFile })
	archiveClassifier = "preprocessed"
	sourceNamespace = "intermediary"
	targetNamespace = "named"
	archiveClassifier = "testmod-mojmap"
}

tasks.build.configure {
	dependsOn(xplatTransformJar, xplatTransformSourcesJar, remapTestmodMojmapJar)
}
//endregion

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
