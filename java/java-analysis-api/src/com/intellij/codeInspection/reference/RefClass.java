// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.codeInspection.reference;

import com.intellij.lang.jvm.JvmClass;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.List;
import java.util.Set;

public interface RefClass extends RefJavaElement {

  @NotNull
  Set<RefClass> getBaseClasses();

  @NotNull
  Set<RefClass> getSubClasses();

  @NotNull
  List<RefMethod> getConstructors();

  @NotNull
  Set<RefElement> getInTypeReferences();

  @NotNull
  Set<RefElement> getInstanceReferences();

  RefMethod getDefaultConstructor();

  @NotNull
  List<RefMethod> getLibraryMethods();

  boolean isAnonymous();

  boolean isInterface();

  boolean isUtilityClass();

  boolean isAbstract();

  boolean isApplet();

  boolean isServlet();

  boolean isTestCase();

  boolean isLocalClass();

  @SuppressWarnings("DeprecatedIsStillUsed")
  @Deprecated
  default boolean isSelfInheritor(PsiClass psiClass) {
    throw new UnsupportedOperationException();
  }

  default boolean isSelfInheritor(UClass uClass) {
    return isSelfInheritor((PsiClass) uClass);
  }

  @Override
  default UClass getUastElement() {
    throw new UnsupportedOperationException();
  }

  @Deprecated
  @Override
  default PsiClass getElement() {
    return ObjectUtils.tryCast(getPsiElement(), PsiClass.class);
  }
}
