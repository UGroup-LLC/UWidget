package com.kikoproject.uwidget.utils

import java.io.File
import java.io.FileNotFoundException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

data class ZipIO (val entry: ZipEntry, val output: File)

fun File.unzip(filename: String,content: () -> Unit) {

    val currentFile = File(this.absolutePath,filename)
    val rootFolder = this
    if (!rootFolder.exists()) {
        rootFolder.mkdirs()
    }

    ZipFile(currentFile).use { zip ->
        zip
            .entries()
            .asSequence()
            .map {
                val outputFile = File(rootFolder.absolutePath + File.separator + it.name)
                ZipIO(it, outputFile)
            }
            .map {
                it.output.parentFile?.run{
                    if (!exists()) mkdirs()
                }
                it
            }
            .filter { !it.entry.isDirectory }
            .forEach { (entry, output) ->
                try {
                    zip.getInputStream(entry).use { input ->
                        output.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
                catch (exception: FileNotFoundException){
                    output.delete()
                    zip.getInputStream(entry).use { input ->
                        output.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        content()
    }

}