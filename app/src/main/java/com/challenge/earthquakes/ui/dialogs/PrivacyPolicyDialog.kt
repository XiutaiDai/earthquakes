package com.challenge.earthquakes.ui.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit,
    onAgree: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("高德地图隐私政策") },
        text = {
            Text("我们需要您同意高德地图的隐私政策才能使用地图功能。以下是隐私政策的详细内容...")
        },
        confirmButton = {
            Button(
                onClick = onAgree,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("同意")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("不同意")
            }
        }
    )
}