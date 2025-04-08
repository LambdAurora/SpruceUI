package spruceui

import org.gradle.accessors.dm.LibrariesForLibs

object Constants {
	const val GROUP = "dev.lambdaurora"
	const val NAMESPACE = "spruceui"
	const val PRETTY_NAME = "SpruceUI"
	const val VERSION = "7.0.1"
	const val JAVA_VERSION = 21

	const val DESCRIPTION = "Just a Minecraft GUI library."

	@JvmField
	val AUTHORS = listOf("LambdAurora")

	const val PROJECT_LINK = "https://github.com/LambdAurora/SpruceUI"
	const val SOURCES_LINK = "$PROJECT_LINK.git"
	const val ISSUES_LINK = "$PROJECT_LINK/issues"
	const val LICENSE = "MIT"

	private var minecraftVersion: String? = null

	fun finalizeInit(libs: LibrariesForLibs) {
		this.minecraftVersion = libs.versions.minecraft.get()
	}

	fun mcVersion(): String {
		return this.minecraftVersion!!
	}
}
