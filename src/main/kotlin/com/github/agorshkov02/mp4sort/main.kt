package com.github.agorshkov02.mp4sort

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.mp4.media.Mp4VideoDirectory
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("directory or target directory doesn't specified")
        exitProcess(1)
    }

    val directory = Path.of(args[0])
    if (!directory.exists()) {
        println("directory path doesn't exists")
        exitProcess(1)
    }

    val targetDirectoryPath = Path.of(args[1])
    if (!targetDirectoryPath.exists()) {
        println("target directoryPath path doesn't exists")
        exitProcess(1)
    }

    copy(directory, targetDirectoryPath)
}

fun copy(directory: Path, targetDirectory: Path) {
    val files = directory.toFile().listFiles()
    files?.forEach { file ->
        if (!file.isDirectory) {
            val metadata = ImageMetadataReader.readMetadata(file)
            metadata.directories.forEach { directory ->
                if (directory is Mp4VideoDirectory) {
                    val width = directory.getInt(Mp4VideoDirectory.TAG_WIDTH)
                    val height = directory.getInt(Mp4VideoDirectory.TAG_HEIGHT)

                    @Suppress("NAME_SHADOWING")
                    val targetDirectory = when {
                        isHD(width, height) -> targetDirectory.resolve("HD")
                        isFullHD(width, height) -> targetDirectory.resolve("fullHD")
                        is4k(width, height) -> targetDirectory.resolve("4k")
                        is8k(width, height) -> targetDirectory.resolve("8k")
                        else -> null
                    }

                    if (targetDirectory != null) {
                        if (!targetDirectory.exists()) {
                            println("target directory is not exists. Create directory: $targetDirectory")
                            targetDirectory.createDirectory()
                        }

                        val target = targetDirectory.resolve(file.name)
                        if (!target.exists()) {
                            println("file is not exists, copy: $target")
                            Files.copy(file.toPath(), target, StandardCopyOption.COPY_ATTRIBUTES)
                        } else {
                            println("file is exists, skip: $target")
                        }
                    }
                }
            }
        }
    }
}

fun isHD(width: Int, height: Int) = width == 1280 && height == 720

fun isFullHD(width: Int, height: Int) = width == 1920 && height == 1080

fun is4k(width: Int, height: Int) = width == 3840 && height == 2160

fun is8k(width: Int, height: Int) = width == 7680 && height == 4320
