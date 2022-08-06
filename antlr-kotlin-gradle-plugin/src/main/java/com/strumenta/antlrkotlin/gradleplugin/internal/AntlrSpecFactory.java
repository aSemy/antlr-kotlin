/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.strumenta.antlrkotlin.gradleplugin.internal;

import com.strumenta.antlrkotlin.gradleplugin.AntlrKotlinTaskKt;
import org.gradle.api.file.SourceDirectorySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AntlrSpecFactory {

    public AntlrSpec create(
            @NotNull
            AntlrKotlinTaskKt antlrTask,
            @NotNull
            Set<File> grammarFiles,
            @Nullable
            SourceDirectorySet sourceDirectorySet
    ) {
        List<String> arguments = new LinkedList<>(antlrTask.getArguments().get());
        File outputDirectory = antlrTask.getOutputDirectory().get().getAsFile();

        String packageName = antlrTask.getPackageName().getOrNull();
        if (packageName != null && !arguments.contains("-package")) {
            arguments.add("-package");
            arguments.add(packageName);
            outputDirectory = new File(outputDirectory, packageName.replace(".", "/"));
        }

        if (antlrTask.getTraceEnabled().getOrElse(false) && !arguments.contains("-trace")) {
            arguments.add("-trace");
        }
        if (antlrTask.getTraceLexerEnabled().getOrElse(false) && !arguments.contains("-traceLexer")) {
            arguments.add("-traceLexer");
        }
        if (antlrTask.getTraceParserEnabled().getOrElse(false) && !arguments.contains("-traceParser")) {
            arguments.add("-traceParser");
        }
        if (antlrTask.getTraceTreeWalkerEnabled().getOrElse(false) && !arguments.contains("-traceTreeWalker")) {
            arguments.add("-traceTreeWalker");
        }

        return new AntlrSpec(
                arguments,
                grammarFiles,
                sourceDirectorySet.getSrcDirs(),
                outputDirectory,
                antlrTask.getMaxHeapSize().getOrNull()
        );
    }
}
