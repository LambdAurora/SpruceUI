import net.fabricmc.loom.task.RemapJarTask

plugins {
	id("fabric-loom").version("1.8.+")
	id("dev.yumi.gradle.licenser").version("1.1.+")
	`java-library`
	`maven-publish`
}

group = project.property("maven_group") as String
base.archivesName.set(project.property("archives_base_name") as String)

val mcVersion = project.property("minecraft_version") as String
version = "${project.property("mod_version")}+${mcVersion}"

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
		name = "QuiltMC"
		url = uri("https://maven.quiltmc.org/repository/release")
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${mcVersion}")
	mappings("org.quiltmc:quilt-mappings:${mcVersion}+build.${project.property("quilt_mappings")}:intermediary-v2")
	modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

	fabricModules.stream().map { fabricApi.module(it, project.property("fabric_api_version") as String) }.forEach {
		modImplementation(it)
	}

	modLocalRuntime("com.terraformersmc:modmenu:${project.property("modmenu_version")}") {
		isTransitive = false
	}
	modLocalRuntime(fabricApi.module("fabric-key-binding-api-v1", project.property("fabric_api_version") as String))

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
		expand("version" to project.version)
	}
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${base.archivesName.get()}" }
	}
}

loom {
	runtimeOnlyLog4j = true
	runs {
		register("testmodClient") {
			client()
			source(testmod)
		}
	}
}

val testmodJar = tasks.create<Jar>("testmodJar") {
	this.group = "build"
	this.from(testmod.output)
	this.archiveClassifier = "testmod-dev"
	this.destinationDirectory = project.file("build/devlibs")
}

val remapTestmodJar = tasks.register<RemapJarTask>("remapTestmodJar") {
	this.group = "build"
	this.dependsOn(testmodJar)
	this.inputFile.set(testmodJar.archiveFile)
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
