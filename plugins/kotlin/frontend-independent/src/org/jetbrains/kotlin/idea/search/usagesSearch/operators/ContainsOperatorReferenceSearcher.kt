// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.jetbrains.kotlin.idea.search.usagesSearch.operators

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchRequestCollector
import com.intellij.psi.search.SearchScope
import com.intellij.util.Processor
import org.jetbrains.kotlin.idea.references.KtSimpleNameReference
import org.jetbrains.kotlin.idea.search.ideaExtensions.KotlinReferencesSearchOptions
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstance

class ContainsOperatorReferenceSearcher(
    targetFunction: PsiElement,
    searchScope: SearchScope,
    consumer: Processor<in PsiReference>,
    optimizer: SearchRequestCollector,
    options: KotlinReferencesSearchOptions
) : OperatorReferenceSearcher<KtOperationReferenceExpression>(
    targetFunction,
    searchScope,
    consumer,
    optimizer,
    options,
    wordsToSearch = listOf("in")
) {
    private companion object {
        private val OPERATION_TOKENS = setOf(KtTokens.IN_KEYWORD, KtTokens.NOT_IN)
    }

    override fun processPossibleReceiverExpression(expression: KtExpression) {
        when (val parent = expression.parent) {
            is KtBinaryExpression -> {
                if (parent.operationToken in OPERATION_TOKENS && expression == parent.right) {
                    processReferenceElement(parent.operationReference)
                }
            }

            is KtWhenConditionInRange -> {
                processReferenceElement(parent.operationReference)
            }
        }
    }

    override fun isReferenceToCheck(ref: PsiReference): Boolean {
        if (ref !is KtSimpleNameReference) return false
        val element = ref.element as? KtOperationReferenceExpression ?: return false
        return element.getReferencedNameElementType() in OPERATION_TOKENS
    }

    override fun extractReference(element: KtElement): PsiReference? {
        val referenceExpression = element as? KtOperationReferenceExpression ?: return null
        if (referenceExpression.getReferencedNameElementType() !in OPERATION_TOKENS) return null
        return referenceExpression.references.firstIsInstance<KtSimpleNameReference>()
    }
}