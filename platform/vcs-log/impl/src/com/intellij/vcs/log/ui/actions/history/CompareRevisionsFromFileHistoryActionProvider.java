// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.vcs.log.ui.actions.history;

import com.intellij.openapi.ListSelection;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.AnActionExtensionProvider;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsDataKeys;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vcs.changes.actions.diff.ShowDiffAction;
import com.intellij.vcs.log.VcsLog;
import com.intellij.vcs.log.VcsLogBundle;
import com.intellij.vcs.log.VcsLogDataKeys;
import com.intellij.vcs.log.statistics.VcsLogUsageTriggerCollector;
import com.intellij.vcs.log.ui.VcsLogInternalDataKeys;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.util.Arrays;

abstract public class CompareRevisionsFromFileHistoryActionProvider implements AnActionExtensionProvider {
  @Override
  public boolean isActive(@NotNull AnActionEvent e) {
    FilePath filePath = e.getData(VcsDataKeys.FILE_PATH);
    return e.getData(VcsLogInternalDataKeys.FILE_HISTORY_UI) != null && filePath != null && !filePath.isDirectory();
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    Project project = e.getProject();
    FilePath filePath = e.getData(VcsDataKeys.FILE_PATH);
    VcsLog log = e.getData(VcsLogDataKeys.VCS_LOG);
    if (project == null || filePath == null || filePath.isDirectory() || log == null) {
      e.getPresentation().setEnabledAndVisible(false);
      return;
    }

    updateActionText(e, log);
    e.getPresentation().setVisible(true);

    if (e.getInputEvent() instanceof KeyEvent) {
      e.getPresentation().setEnabled(true);
    }
    else {
      Change[] changes = e.getData(VcsDataKeys.SELECTED_CHANGES);
      e.getPresentation().setEnabled(changes != null && changes.length == 1 && changes[0] != null);
    }
  }

  protected void updateActionText(@NotNull AnActionEvent e, @NotNull VcsLog log) { }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    VcsLogUsageTriggerCollector.triggerUsage(e, this);

    Project project = e.getRequiredData(CommonDataKeys.PROJECT);
    Change[] changes = e.getData(VcsDataKeys.SELECTED_CHANGES);
    if (changes == null || changes.length != 1 || changes[0] == null) return;

    ListSelection<Change> selection = ListSelection.createAt(Arrays.asList(changes), 0)
      .asExplicitSelection();
    ShowDiffAction.showDiffForChange(project, selection);
  }

  public static void setTextAndDescription(@NotNull AnActionEvent e, @NotNull VcsLog log) {
    if (log.getSelectedCommits().size() >= 2) {
      e.getPresentation().setText(VcsLogBundle.messagePointer("action.presentation.CompareRevisionsFromFileHistoryActionProvider.text.compare"));
      e.getPresentation().setDescription(VcsLogBundle.messagePointer("action.presentation.CompareRevisionsFromFileHistoryActionProvider.description.compare"));
    }
    else {
      e.getPresentation().setText(VcsLogBundle.messagePointer("action.presentation.CompareRevisionsFromFileHistoryActionProvider.text.show.diff"));
      e.getPresentation().setDescription(VcsLogBundle.messagePointer("action.presentation.CompareRevisionsFromFileHistoryActionProvider.description.show.diff"));
    }
  }

  static class ShowDiff extends CompareRevisionsFromFileHistoryActionProvider {
    @Override
    protected void updateActionText(@NotNull AnActionEvent e, @NotNull VcsLog log) {
      setTextAndDescription(e, log);
    }
  }

  static class ShowStandaloneDiff extends CompareRevisionsFromFileHistoryActionProvider {}
}
