// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.idea.maven.utils;

public class MavenProcessCanceledException extends Exception {
  public MavenProcessCanceledException() { }

  public MavenProcessCanceledException(Throwable cause) {
    super(cause);
  }
}