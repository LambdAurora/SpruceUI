import net.fabricmc.loom.task.RemapJarTask

plugins {
	id("fabric-loom").version("1.10.+")
	id("dev.lambdaurora.mcdev").version("1.0.+")
	id("dev.yumi.gradle.licenser").version("2.1.+")
	`java-library`
	`maven-publish`
}

group = project.property("maven_group") as String
base.archivesName.set(project.property("archives_base_name") as String)

val mcVersion = project.property("minecraft_version") as String
version = project.property("mod_version") as String

val targetJavaVersion = 21

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

dependencies {
	minecraft("com.mojang:minecraft:${mcVersion}")
	@Suppress("UnstableApiUsage")
	mappings(lambdamcdev.layered {
		officialMojangMappings()
		// Parchment is currently broken when used with the hacked mojmap layer due to remapping shenanigans.
		//parchment("org.parchmentmc.data:parchment-${mcVersion}:${project.property("parchment_mappings")}@zip")
		mappings("dev.lambdaurora:yalmm:${mcVersion}+build.${project.property("yalmm_mappings")}")
	})
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	fabricModules.stream().map { fabricApi.module(it, project.property("fabric_api_version") as String) }.forEach {
		modImplementation(it)
	}

	modLocalRuntime("com.terraformersmc:modmenu:${project.property("modmenu_version")}") {
		isTransitive = false
	}

	"testmodImplementation"(sourceSets.main.get().output)
}

java {
	sourceCompatibility = JavaVersion.toVersion(targetJavaVersion)
	targetCompatibility = JavaVersion.toVersion(targetJavaVersion)

	withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"
	options.isDeprecation = true
	options.isIncremental = true
	options.release.set(targetJavaVersion)
}

tasks.processResources {
	inputs.property("version", project.version)

	filesMatching("fabric.mod.json") {
		expand("version" to inputs.properties["version"])
	}
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
				name.set("SpruceUI")
				description.set("A Minecraft mod API which adds some GUI utilities.")
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
