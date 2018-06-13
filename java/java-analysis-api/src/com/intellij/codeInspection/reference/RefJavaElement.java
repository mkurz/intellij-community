// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.intellij.codeInspection.reference;

import com.intellij.lang.jvm.JvmElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifier;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.uast.UElement;

import java.util.Collection;

public interface RefJavaElement extends RefElement {

  @Deprecated
  default PsiElement getElement() {
    throw new UnsupportedOperationException();
  }

  default PsiElement getPsiElement() {
    //noinspection deprecation
    return getElement();
  }

  default UElement getUastElement() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the collection of references used in this element.
   * @return the collection of used types
   */
  @NotNull
  Collection<RefClass> getOutTypeReferences();


  /**
   * Checks if the element is {@code final}.
   *
   * @return true if the element is final, false otherwise.
   */
  boolean isFinal();

  /**
   * Checks if the element is {@code static}.
   *
   * @return true if the element is static, false otherwise.
   */
  boolean isStatic();

  /**
   * Checks if the element is, or belongs to, a synthetic class or method created for a JSP page.
   *
   * @return true if the element is a synthetic JSP element, false otherwise.
   */
  boolean isSyntheticJSP();

  /**
   * Returns the access modifier for the element, as one of the keywords from the
   * {@link PsiModifier} class.
   *
   * @return the modifier, or null if the element does not have any access modifier.
   */
  @NotNull
  @PsiModifier.ModifierConstant
  String getAccessModifier();
}
