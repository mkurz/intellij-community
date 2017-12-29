/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.uast.java

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLambdaExpression
import com.intellij.psi.PsiType
import org.jetbrains.uast.UElement
import org.jetbrains.uast.ULambdaExpression
import org.jetbrains.uast.UastEmptyExpression

class JavaULambdaExpression(
  override val psi: PsiLambdaExpression,
  givenParent: UElement?
) : JavaAbstractUExpression(givenParent), ULambdaExpression {
  override val functionalInterfaceType: PsiType?
    get() = psi.functionalInterfaceType

  override val valueParameters by lz {
    psi.parameterList.parameters.map { JavaUParameter(it, this) }
  }

  override val body by lz {
    val b = psi.body
    when (b) {
      is PsiCodeBlock -> JavaConverter.convertBlock(b, this)
      is PsiExpression -> JavaConverter.convertOrEmpty(b, this)
      else -> UastEmptyExpression(this)
    }
  }
}