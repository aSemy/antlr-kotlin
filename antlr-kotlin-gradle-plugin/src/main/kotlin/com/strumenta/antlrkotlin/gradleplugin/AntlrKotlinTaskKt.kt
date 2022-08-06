package com.strumenta.antlrkotlin.gradleplugin

import com.strumenta.antlrkotlin.gradleplugin.internal.AntlrSourceGenerationException
import com.strumenta.antlrkotlin.gradleplugin.internal.AntlrSpecFactory
import com.strumenta.antlrkotlin.gradleplugin.internal.AntlrWorkerManager
import org.gradle.api.file.*
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.process.internal.worker.WorkerProcessFactory
import org.gradle.work.ChangeType
import org.gradle.work.InputChanges
import java.io.File
import javax.inject.Inject

/** Generates parsers from Antlr grammars. */
@CacheableTask
abstract class AntlrKotlinTaskKt @Inject constructor(
    private val workerProcessBuilderFactory: WorkerProcessFactory,
    private val files: FileSystemOperations,
) : SourceTask() {

    @get:Input
    abstract val workingDir : DirectoryProperty

    /** Specifies that all rules call `traceIn`/`traceOut`. */
    @get:Input
    @get:Optional
    abstract val traceEnabled: Property<Boolean>

    /** Specifies that all lexer rules call `traceIn`/`traceOut`. */
    @get:Input
    @get:Optional
    abstract val traceLexerEnabled: Property<Boolean>

    /** Specifies that all parser rules call `traceIn`/`traceOut`. */
    @get:Input
    @get:Optional
    abstract val traceParserEnabled: Property<Boolean>

    /** Specifies that all tree walker rules call `traceIn`/`traceOut`. */
    @get:Input
    @get:Optional
    abstract val traceTreeWalkerEnabled: Property<Boolean>

    /** List of command-line arguments passed to the antlr process */
    @get:Input
    @get:Optional
    abstract val arguments: ListProperty<String>

    /** The classpath containing the Ant ANTLR task implementation. */
    @get:Classpath
    abstract val antlrClasspath: ConfigurableFileCollection

    /** The directory to generate the parser source files into. */
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    /** The maximum heap size for the forked antlr process (ex: `1g`). */
    @get:Internal
    abstract val maxHeapSize: Property<String>

    /** The package name of the generated files. */
    @get:Input
    @get:Optional
    abstract val packageName: Property<String>

    private var sourceDirectorySet: SourceDirectorySet? = null

    /**
     * Sets the source for this task. Delegates to [SourceTask.setSource].
     *
     * If the source is of type [SourceDirectorySet], then the relative path of each source grammar files
     * is used to determine the relative output path of the generated source
     * If the source is *not* of type [SourceDirectorySet], then the generated source files end up
     * flattened in the specified output directory.
     *
     * @param source The source.
     */
    override fun setSource(source: Any) {
        setSourceInternal(source)
    }

    /**
     * Sets the source for this task.
     *
     * If the source is of type [SourceDirectorySet], then the relative path of each source grammar files
     * is used to determine the relative output path of the generated source
     * If the source is not of type [SourceDirectorySet], then the generated source files end up
     * flattened in the specified output directory.
     *
     * @param source The source.
     * @since 4.0
     */
    override fun setSource(source: FileTree) {
        setSourceInternal(source)
    }

    private fun setSourceInternal(source: Any) {
        super.setSource(source)
        if (source is SourceDirectorySet) {
            sourceDirectorySet = source
        }
    }

    /**
     * Returns the source for this task, after the `include` and `exclude` patterns have been applied.
     *
     * Ignores source files which do not exist.
     *
     * @return The source.
     */
    @PathSensitive(PathSensitivity.RELATIVE)
    override fun getSource(): FileTree = super.getSource()

    @TaskAction
    fun execute(inputChanges: InputChanges) {

        val sourceChanges = inputChanges.getFileChanges(source)

        val cleanRebuild = sourceChanges.any { change ->
            change.changeType == ChangeType.REMOVED || change.changeType == ChangeType.ADDED
        }

        val changedFiles = sourceChanges.map { fileChange -> fileChange.file }.toSet()

        val grammarFiles: Set<File> = if (cleanRebuild) {
            files.delete { delete(outputDirectory) }
            source.files + changedFiles
        } else {
            changedFiles
        }

        val manager = AntlrWorkerManager()
        logger.debug("AntlrWorkerManager created")
        val spec = AntlrSpecFactory().create(this, grammarFiles, sourceDirectorySet)
        logger.debug("AntlrSpec created")
        val result = manager.runWorker(
            workingDir.asFile.get(),
            workerProcessBuilderFactory,
            antlrClasspath,
            spec
        )
        logger.debug("AntlrResult obtained")
        if (result.errorCount != 0)
            throw AntlrSourceGenerationException(
                "There were errors during grammar generation (${result.errorCount})",
                result.exception
            )
    }
}
