import dev.lambdaurora.mcdev.api.McVersionLookup
import spruceui.Constants
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
	id("fabric-loom")
	id("dev.lambdaurora.mcdev")
}

// Seriously you should not worry about it, definitely not a hack.
// https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()
Constants.finalizeInit(libs)

version = "${project.property("version")}+${McVersionLookup.getVersionTag(Constants.mcVersion())}"
lambdamcdev.namespace = Constants.NAMESPACE

val javaVersion = Integer.parseInt(project.property("java_version") as String)

repositories {
	mavenCentral()
}

dependencies {
	minecraft(libs.minecraft)
}

java {
	sourceCompatibility = JavaVersion.toVersion(javaVersion)
	targetCompatibility = JavaVersion.toVersion(javaVersion)

	withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
	options.encoding = "UTF-8"

	options.release.set(javaVersion)
}

loom {
	@Suppress("UnstableApiUsage")
	mixin {
		useLegacyMixinAp = false
	}
}
