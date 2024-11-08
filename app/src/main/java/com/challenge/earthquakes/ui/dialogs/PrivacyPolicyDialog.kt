package com.challenge.earthquakes.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.DialogProperties
import com.challenge.earthquakes.ui.theme.PrivacyDialogThemeOverlay


@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit,
    onConfirm: @Composable () -> Unit,
    bodyText: String,
    titleText: String
) {
    val openDialog = remember { mutableStateOf(true) }
    PrivacyDialogThemeOverlay {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                openDialog.value = false // 关闭对话框
            },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                        openDialog.value = false // 关闭对话框
                    },
                    content = { Text("拒绝") }
                )
            },
            confirmButton = {
                onConfirm()
                Button(
                    onClick = {
                        openDialog.value = false // 关闭对话框
                    },
                    content = { Text("同意") }
                )
            },
            text = { Text(bodyText) },
            title = { Text(titleText) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}