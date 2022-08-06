/*
 * Copyright 2010 the original author or authors.
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

package com.strumenta.antlrkotlin.gradleplugin;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.withType

abstract class AntlrKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        val akExtension = target.extensions.create(ANTLR_KOTLIN_EXTENSION_NAME, AntlrKotlinExtension::class)

        target.tasks.withType<AntlrKotlinTaskKt>().configureEach {
            this.workingDir.convention(akExtension.workingDir)
            this.traceEnabled.convention(akExtension.traceEnabled)
            this.traceLexerEnabled.convention(akExtension.traceLexerEnabled)
            this.traceParserEnabled.convention(akExtension.traceParserEnabled)
            this.traceTreeWalkerEnabled.convention(akExtension.traceTreeWalkerEnabled)
            this.arguments.convention(akExtension.arguments)
//            this.antlrClasspath.convention(akExtension.antlrClasspath)
            this.outputDirectory.convention(akExtension.outputDirectory)
            this.maxHeapSize.convention(akExtension.maxHeapSize)
            this.packageName.convention(akExtension.packageName)
        }

    }

    companion object {
        const val ANTLR_KOTLIN_EXTENSION_NAME = "antlrKotlin"
    }
}
