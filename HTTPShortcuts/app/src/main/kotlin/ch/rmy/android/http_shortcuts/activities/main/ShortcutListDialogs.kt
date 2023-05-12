package ch.rmy.android.http_shortcuts.activities.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.components.ConfirmDialog
import ch.rmy.android.http_shortcuts.components.FontSize
import ch.rmy.android.http_shortcuts.components.HelpText
import ch.rmy.android.http_shortcuts.components.MessageDialog
import ch.rmy.android.http_shortcuts.components.ProgressDialog
import ch.rmy.android.http_shortcuts.components.SelectDialog
import ch.rmy.android.http_shortcuts.components.SelectDialogEntry
import ch.rmy.android.http_shortcuts.components.Spacing
import ch.rmy.android.http_shortcuts.data.domains.shortcuts.ShortcutId

@Composable
fun ShortcutListDialogs(
    dialogState: ShortcutListDialogState?,
    onPlaceOnHomeScreenOptionSelected: () -> Unit,
    onExecuteOptionSelected: () -> Unit,
    onCancelPendingExecutionOptionSelected: () -> Unit,
    onEditOptionSelected: () -> Unit,
    onMoveOptionSelected: () -> Unit,
    onDuplicateOptionSelected: () -> Unit,
    onDeleteOptionSelected: () -> Unit,
    onShowInfoOptionSelected: () -> Unit,
    onExportOptionSelected: () -> Unit,
    onExportToFileOptionSelected: () -> Unit,
    onExportViaSharingOptionSelected: () -> Unit,
    onExportAsCurlOptionSelected: () -> Unit,
    onExportAsFileOptionSelected: () -> Unit,
    onDeletionConfirmed: () -> Unit,
    onCurlExportCopyButtonClicked: () -> Unit,
    onCurlExportShareButtonClicked: () -> Unit,
    onDismissed: () -> Unit,
) {
    when (dialogState) {
        is ShortcutListDialogState.CurlExport -> {
            CurlExportDialog(
                shortcutName = dialogState.shortcutName,
                command = dialogState.command,
                onCopyButtonClicked = onCurlExportCopyButtonClicked,
                onShareButtonClicked = onCurlExportShareButtonClicked,
                onDismissed = onDismissed,
            )
        }
        is ShortcutListDialogState.ShortcutInfo -> {
            ShortcutInfoDialog(
                shortcutId = dialogState.shortcutId,
                shortcutName = dialogState.shortcutName,
                onDismissed = onDismissed,
            )
        }
        is ShortcutListDialogState.Deletion -> {
            DeletionDialog(
                shortcutName = dialogState.shortcutName,
                onConfirmed = onDeletionConfirmed,
                onDismissed = onDismissed,
            )
        }
        is ShortcutListDialogState.ExportOptions -> {
            ExportOptionsDialog(
                onExportAsCurlOptionSelected = onExportAsCurlOptionSelected,
                onExportAsFileOptionSelected = onExportAsFileOptionSelected,
                onDismissed = onDismissed,
            )
        }
        is ShortcutListDialogState.ExportDestinationOptions -> {
            ExportDestinationOptionsDialog(
                onExportToFileOptionSelected = onExportToFileOptionSelected,
                onExportViaSharingOptionSelected = onExportViaSharingOptionSelected,
                onDismissed = onDismissed,
            )
        }
        is ShortcutListDialogState.ContextMenu -> {
            ContextMenuDialog(
                shortcutName = dialogState.shortcutName,
                isPending = dialogState.isPending,
                onPlaceOnHomeScreenOptionSelected = onPlaceOnHomeScreenOptionSelected,
                onExecuteOptionSelected = onExecuteOptionSelected,
                onCancelPendingExecutionOptionSelected = onCancelPendingExecutionOptionSelected,
                onEditOptionSelected = onEditOptionSelected,
                onMoveOptionSelected = onMoveOptionSelected,
                onDuplicateOptionSelected = onDuplicateOptionSelected,
                onDeleteOptionSelected = onDeleteOptionSelected,
                onShowInfoOptionSelected = onShowInfoOptionSelected,
                onExportOptionSelected = onExportOptionSelected,
                onDismissed = onDismissed,
            )
        }
        is ShortcutListDialogState.ExportError -> {
            MessageDialog(
                message = stringResource(R.string.export_failed_with_reason, dialogState.message),
                onDismissRequest = onDismissed,
            )
        }
        is ShortcutListDialogState.ExportProgress -> {
            ProgressDialog(
                text = stringResource(R.string.export_in_progress),
                onDismissRequest = onDismissed,
            )
        }
        null -> Unit
    }
}

@Composable
private fun CurlExportDialog(
    shortcutName: String,
    command: String,
    onCopyButtonClicked: () -> Unit,
    onShareButtonClicked: () -> Unit,
    onDismissed: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(shortcutName)
        },
        text = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = command,
                onValueChange = {},
                textStyle = TextStyle(
                    fontSize = FontSize.SMALL,
                    fontFamily = FontFamily.Monospace,
                ),
                readOnly = true,
            )
        },
        confirmButton = {
            TextButton(onClick = onCopyButtonClicked) {
                Text(stringResource(R.string.button_copy_curl_export))
            }
        },
        dismissButton = {
            TextButton(onClick = onShareButtonClicked) {
                Text(stringResource(R.string.share_button))
            }
        },
        onDismissRequest = onDismissed,
    )
}

@Composable
private fun ShortcutInfoDialog(
    shortcutId: ShortcutId,
    shortcutName: String,
    onDismissed: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(shortcutName)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.SMALL)
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.label_shortcut_id))
                    },
                    value = shortcutId,
                    onValueChange = {},
                    textStyle = TextStyle(
                        fontSize = FontSize.SMALL,
                        fontFamily = FontFamily.Monospace,
                    ),
                    readOnly = true,
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(stringResource(R.string.label_deep_link_url))
                    },
                    value = "http-shortcuts://$shortcutId",
                    onValueChange = {},
                    textStyle = TextStyle(
                        fontSize = FontSize.SMALL,
                        fontFamily = FontFamily.Monospace,
                    ),
                    readOnly = true,
                )

                HelpText(stringResource(R.string.message_deep_link_instructions))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissed) {
                Text(stringResource(R.string.dialog_ok))
            }
        },
        onDismissRequest = onDismissed,
    )
}

@Composable
private fun DeletionDialog(
    shortcutName: String,
    onConfirmed: () -> Unit,
    onDismissed: () -> Unit,
) {
    ConfirmDialog(
        title = shortcutName,
        message = stringResource(R.string.confirm_delete_shortcut_message),
        confirmButton = stringResource(R.string.dialog_delete),
        onConfirmRequest = onConfirmed,
        onDismissRequest = onDismissed,
    )
}

@Composable
private fun ExportOptionsDialog(
    onExportAsCurlOptionSelected: () -> Unit,
    onExportAsFileOptionSelected: () -> Unit,
    onDismissed: () -> Unit,
) {
    SelectDialog(
        title = stringResource(R.string.title_export_shortcut_as),
        onDismissRequest = onDismissed,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SelectDialogEntry(
                label = stringResource(R.string.action_export_as_curl),
                onClick = onExportAsCurlOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_export_as_file),
                onClick = onExportAsFileOptionSelected,
            )
        }
    }
}

@Composable
private fun ExportDestinationOptionsDialog(
    onExportToFileOptionSelected: () -> Unit,
    onExportViaSharingOptionSelected: () -> Unit,
    onDismissed: () -> Unit,
) {
    SelectDialog(
        title = stringResource(R.string.title_export),
        onDismissRequest = onDismissed,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SelectDialogEntry(
                label = stringResource(R.string.button_export_to_general),
                onClick = onExportToFileOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.button_export_send_to),
                onClick = onExportViaSharingOptionSelected,
            )
        }
    }
}

@Composable
private fun ContextMenuDialog(
    shortcutName: String,
    isPending: Boolean,
    onPlaceOnHomeScreenOptionSelected: () -> Unit,
    onExecuteOptionSelected: () -> Unit,
    onCancelPendingExecutionOptionSelected: () -> Unit,
    onEditOptionSelected: () -> Unit,
    onMoveOptionSelected: () -> Unit,
    onDuplicateOptionSelected: () -> Unit,
    onDeleteOptionSelected: () -> Unit,
    onShowInfoOptionSelected: () -> Unit,
    onExportOptionSelected: () -> Unit,
    onDismissed: () -> Unit,
) {
    SelectDialog(
        title = shortcutName,
        onDismissRequest = onDismissed,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            SelectDialogEntry(
                label = stringResource(R.string.action_place),
                onClick = onPlaceOnHomeScreenOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_run),
                onClick = onExecuteOptionSelected,
            )
            if (isPending) {
                SelectDialogEntry(
                    label = stringResource(R.string.action_cancel_pending),
                    onClick = onCancelPendingExecutionOptionSelected,
                )
            }
            Divider(
                modifier = Modifier.padding(vertical = Spacing.MEDIUM)
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_edit),
                onClick = onEditOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_move),
                onClick = onMoveOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_duplicate),
                onClick = onDuplicateOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_delete),
                onClick = onDeleteOptionSelected,
            )
            Divider(
                modifier = Modifier.padding(vertical = Spacing.MEDIUM)
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_shortcut_information),
                onClick = onShowInfoOptionSelected,
            )
            SelectDialogEntry(
                label = stringResource(R.string.action_export),
                onClick = onExportOptionSelected,
            )
        }
    }
}
