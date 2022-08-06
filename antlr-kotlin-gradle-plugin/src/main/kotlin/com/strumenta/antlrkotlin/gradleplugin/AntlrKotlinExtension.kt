package com.strumenta.antlrkotlin.gradleplugin

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

abstract class AntlrKotlinExtension {

    abstract val workingDir: DirectoryProperty
    abstract val traceEnabled: Property<Boolean>
    abstract val traceLexerEnabled: Property<Boolean>
    abstract val traceParserEnabled: Property<Boolean>
    abstract val traceTreeWalkerEnabled: Property<Boolean>
    abstract val arguments: ListProperty<String>

    //    abstract val antlrClasspath: ConfigurableFileCollection
    abstract val outputDirectory: DirectoryProperty
    abstract val maxHeapSize: Property<String>
    abstract val packageName: Property<String>

}
