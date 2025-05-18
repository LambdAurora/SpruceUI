package spruceui.task

import dev.lambdaurora.mcdev.api.AccessWidenerToTransformer
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.jvm.tasks.Jar
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import javax.inject.Inject

abstract class XplatTransformJar @Inject constructor() : Jar() {
	@InputFile
	val inputJar: RegularFileProperty = project.objects.fileProperty()

	override fun copy() {
		super.copy()

		val inputJar = this.inputJar.asFile.get().toPath()
		val outputJar = this.archiveFile.get().asFile.toPath()

		FileSystems.newFileSystem(outputJar).use { outFs ->
			this.copyJar(inputJar, outFs)
			this.generateAccessTransformer(outFs)
		}
	}

	private fun copyJar(inputJar: Path, outFs: FileSystem) {
		FileSystems.newFileSystem(inputJar).use { inFs ->
			val excludeFiles = listOf(
				inFs.getPath("/fabric.mod.json"),
				inFs.getPath("/spruceui-refmap.json"),
			)
			val excludeDirs = listOf<Path>()

			inFs.rootDirectories.forEach { root ->
				Files.walkFileTree(root, object : SimpleFileVisitor<Path>() {
					override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
						if (dir in excludeDirs) {
							return FileVisitResult.SKIP_SUBTREE
						}

						Files.createDirectories(outFs.getPath("$dir"))

						return FileVisitResult.CONTINUE
					}

					override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
						if (file in excludeFiles) {
							return FileVisitResult.CONTINUE
						}

						Files.copy(file, outFs.getPath("$file"), StandardCopyOption.REPLACE_EXISTING)

						return FileVisitResult.CONTINUE
					}
				})
			}
		}
	}

	private fun generateAccessTransformer(fs: FileSystem) {
		val awPath = fs.getPath("spruceui.accesswidener")

		AccessWidenerToTransformer.convert(
			awPath,
			fs.getPath("META-INF/accesstransformer.cfg")
		)
	}
}
