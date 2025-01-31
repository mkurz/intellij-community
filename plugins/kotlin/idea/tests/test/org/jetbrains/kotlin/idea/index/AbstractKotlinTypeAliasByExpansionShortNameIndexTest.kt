// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.kotlin.idea.index

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.ThrowableRunnable
import org.jetbrains.kotlin.idea.stubindex.KotlinTypeAliasByExpansionShortNameIndex
import org.jetbrains.kotlin.idea.test.KotlinLightCodeInsightFixtureTestCase
import org.jetbrains.kotlin.idea.test.runAll
import org.jetbrains.kotlin.idea.test.InTextDirectivesUtils
import org.junit.Assert
import kotlin.reflect.KMutableProperty0

abstract class AbstractKotlinTypeAliasByExpansionShortNameIndexTest : KotlinLightCodeInsightFixtureTestCase() {
    private lateinit var scope: GlobalSearchScope

    override fun setUp() {
        super.setUp()
        scope = GlobalSearchScope.allScope(project)
    }

    override fun tearDown() {
        runAll(
            ThrowableRunnable {
                @Suppress("UNCHECKED_CAST")
                (this::scope as KMutableProperty0<GlobalSearchScope?>).set(null)
            },
            ThrowableRunnable { super.tearDown() }
        )
    }

    override fun getProjectDescriptor() = super.getProjectDescriptorFromTestName()

    fun doTest(file: String) {
        myFixture.configureByFile(file)
        val fileText = myFixture.file.text
        InTextDirectivesUtils.findLinesWithPrefixesRemoved(fileText, "CONTAINS").forEach {
            assertIndexContains(it)
        }
    }

    private val regex = "\\(key=\"(.*?)\"[, ]*value=\"(.*?)\"\\)".toRegex()

    fun assertIndexContains(record: String) {
        val index = KotlinTypeAliasByExpansionShortNameIndex
        val (_, key, value) = regex.find(record)!!.groupValues
        val result = index.get(key, project, scope)
        if (value !in result.map { it.name }) {
            Assert.fail(buildString {
                appendLine("Record $record not found in index")
                appendLine("Index contents:")
                index.getAllKeys(project).asSequence().forEach {
                    appendLine("KEY: $it")
                    index.get(it, project, scope).forEach {
                        appendLine("    ${it.name}")
                    }
                }
            })
        }
    }

}