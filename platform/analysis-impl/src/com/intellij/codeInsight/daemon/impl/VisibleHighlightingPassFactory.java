/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

/*
 * @author max
 */
package com.intellij.codeInsight.daemon.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.ProperTextRange;
import org.jetbrains.annotations.NotNull;

public abstract class VisibleHighlightingPassFactory  {
  @NotNull
  public static ProperTextRange calculateVisibleRange(@NotNull Editor editor) {
    return VisibleRangeCalculator.SERVICE.getInstance().getVisibleTextRange(editor);
  }
}
