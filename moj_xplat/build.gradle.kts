import dev.lambdaurora.mcdev.api.manifest.Nmt
import net.fabricmc.loom.LoomGradleExtension
import net.fabricmc.loom.api.mappings.layered.MappingsNamespace
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import spruceui.Constants
import spruceui.task.XplatTransformJar

plugins {
	id("spruceui-common")
}

base.archivesName.set(Constants.NAMESPACE + "-mojmap")

lambdamcdev.manifests {
	nmt(
		rootProject.lambdamcdev.manifests.fmj().get().derive(::Nmt)
			.withBlurIcon(false)
			.withLoaderVersion("[2,)")
			.withMixins("spruceui.mixins.json", "spruceui.neoforge.mixins.json")
			.withDepend("minecraft", "[" + libs.versions.minecraft.get() + ",)")
	)
}

dependencies {
	mappings(loom.officialMojangMappings())
}

loom {
	mixin {
		useLegacyMixinAp = false
	}
}

val baseProject = rootProject

tasks.remapJar {
	val remapJar = baseProject.tasks.named("remapJar", RemapJarTask::class)
	dependsOn(remapJar)

	classpath.setFrom((loom as LoomGradleExtension).getMinecraftJarsCollection(MappingsNamespace.INTERMEDIARY))
	inputFile.convention(remapJar.flatMap { it.archiveFile })
	destinationDirectory.set(layout.buildDirectory.map { it.dir("devlibs") })
	archiveClassifier = "intermediary"
	sourceNamespace = "intermediary"
	targetNamespace = "named"
}

val xplatTransformJar by tasks.registering(XplatTransformJar::class) {
	val mainSourceSet = sourceSets.main.get()

	dependsOn(tasks.remapJar)
	dependsOn(tasks.named(mainSourceSet.processResourcesTaskName))

	inputJar.set(tasks.remapJar.flatMap { it.archiveFile })
	from(mainSourceSet.output.resourcesDir)
}

// Add the remapped JAR artifact
baseProject.configurations["mojmapApiElements"].artifacts.removeIf {
	true
}
baseProject.artifacts.add("mojmapApiElements", xplatTransformJar) {
	classifier = "mojmap"
}
baseProject.configurations["mojmapRuntimeElements"].artifacts.removeIf {
	true
}
baseProject.artifacts.add("mojmapRuntimeElements", xplatTransformJar) {
	classifier = "mojmap"
}

tasks.remapSourcesJar {
	val remapJar = baseProject.tasks.named("remapSourcesJar", RemapSourcesJarTask::class)
	dependsOn(remapJar)

	classpath.setFrom((loom as LoomGradleExtension).getMinecraftJarsCollection(MappingsNamespace.INTERMEDIARY))
	inputFile.set(remapJar.flatMap { it.archiveFile })
	archiveClassifier = "preprocessed-sources"
	sourceNamespace = "intermediary"
	targetNamespace = "named"
}

val xplatTransformSourcesJar by tasks.registering(XplatTransformJar::class) {
	val mainSourceSet = sourceSets.main.get()

	dependsOn(tasks.remapSourcesJar)
	dependsOn(tasks.named(mainSourceSet.processResourcesTaskName))

	inputJar.set(tasks.remapSourcesJar.flatMap { it.archiveFile })
	from(mainSourceSet.output.resourcesDir)
	archiveClassifier = "sources"
}

// Add the remapped sources artifact
baseProject.configurations["mojmapSourcesElements"].artifacts.removeIf {
	true
}
baseProject.artifacts.add("mojmapSourcesElements", xplatTransformSourcesJar) {
	classifier = "mojmap-sources"
}

val remapTestmodJar by tasks.registering(RemapJarTask::class) {
	val remapTask = baseProject.tasks.named("remapJar", RemapJarTask::class)
	val remapTestmodTask = baseProject.tasks.named("remapTestmodJar", RemapJarTask::class)

	dependsOn(remapTask, remapTestmodTask)

	classpath.setFrom(
		(loom as LoomGradleExtension).getMinecraftJarsCollection(MappingsNamespace.INTERMEDIARY),
		remapTask
	)
	inputFile.convention(remapTestmodTask.flatMap { it.archiveFile })
	archiveClassifier = "preprocessed"
	sourceNamespace = "intermediary"
	targetNamespace = "named"
	archiveClassifier = "testmod"
}

tasks.build.configure {
	dependsOn(xplatTransformJar, xplatTransformSourcesJar, remapTestmodJar)
}
