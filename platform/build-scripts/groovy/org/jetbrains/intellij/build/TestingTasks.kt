// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.intellij.build

import org.jetbrains.intellij.build.impl.TestingTasksImpl
import java.io.File
import java.nio.file.Path
import java.util.function.Predicate

interface TestingTasks {
  companion object {
    @JvmStatic
    @JvmOverloads
    fun create(context: CompilationContext, options: TestingOptions = TestingOptions()): TestingTasks {
      return TestingTasksImpl(context, options)
    }
  }

  /**
   * @param defaultMainModule    main module to be used instead of [TestingOptions.mainModule] if it isn't specified
   * @param rootExcludeCondition if not `null` tests from modules which sources are fit this predicate will be skipped
   */
  fun runTests(additionalJvmOptions: List<String> = emptyList(), defaultMainModule: String?, rootExcludeCondition: Predicate<File>? = null)

  /**
   * Run all tests annotated with [SkipInHeadlessEnvironment]
   */
  fun runTestsSkippedInHeadlessEnvironment()

  fun createSnapshotsDirectory(): Path

  /**
   *
   * Updates given jvm args, system properties and classpath with common parameters used for running tests
   * (Xmx, debugging, config path) etc.
   *
   *
   * The values passed as parameters have priority over the default ones, added in this method.
   *
   *
   * Mutates incoming collections.
   */
  fun prepareEnvForTestRun(jvmArgs: MutableList<String>,
                           systemProperties: MutableMap<String, String>,
                           classPath: MutableList<String>,
                           remoteDebugging: Boolean)
}