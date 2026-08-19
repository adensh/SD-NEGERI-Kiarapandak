package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

enum class AlertType {
    SUCCESS,
    ERROR,
    WARNING,
    INFO,
    LOADING
}

data class AlertState(
    val isVisible: Boolean = false,
    val type: AlertType = AlertType.INFO,
    val title: String = "",
    val message: String = "",
    val confirmText: String = "OK",
    val cancelText: String? = null,
    val onConfirm: () -> Unit = {},
    val onCancel: () -> Unit = {}
)

@Composable
fun SweetAlertModal(
    state: AlertState,
    onDismissRequest: () -> Unit = {}
) {
    if (!state.isVisible) return

    Dialog(onDismissRequest = {
        if (state.type != AlertType.LOADING) {
            onDismissRequest()
            state.onCancel()
        }
    }) {
        AnimatedVisibility(
            visible = state.isVisible,
            enter = scaleIn(initialScale = 0.85f, animationSpec = tween(220, easing = FastOutSlowInEasing)) + fadeIn()
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1E293B), // Premium dark glass/slate
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("sweet_alert_modal")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Alert Icon Badge
                    AlertIconBadge(type = state.type)

                    Spacer(modifier = Modifier.height(18.dp))

                    // Title
                    Text(
                        text = state.title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Message
                    Text(
                        text = state.message,
                        fontSize = 14.sp,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    if (state.type == AlertType.LOADING) {
                        Text(
                            text = "Sedang memproses kecerdasan buatan...",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                        ) {
                            if (state.cancelText != null) {
                                OutlinedButton(
                                    onClick = {
                                        state.onCancel()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF94A3B8)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("alert_cancel_btn")
                                ) {
                                    Text(state.cancelText, fontWeight = FontWeight.Medium)
                                }
                            }

                            val confirmColor = when (state.type) {
                                AlertType.SUCCESS -> Color(0xFF10B981)
                                AlertType.ERROR -> Color(0xFFEF4444)
                                AlertType.WARNING -> Color(0xFFF59E0B)
                                else -> Color(0xFF3B82F6)
                            }

                            Button(
                                onClick = {
                                    state.onConfirm()
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = confirmColor,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("alert_confirm_btn")
                            ) {
                                Text(state.confirmText, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertIconBadge(type: AlertType) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    when (type) {
        AlertType.SUCCESS -> {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(Color(0xFF065F46).copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFF10B981), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        AlertType.ERROR -> {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(Color(0xFF7F1D1D).copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFFEF4444), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Error",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        AlertType.WARNING -> {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(Color(0xFF78350F).copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFFF59E0B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        AlertType.INFO -> {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(Color(0xFF1E3A8A).copy(alpha = 0.4f), CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFF3B82F6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        AlertType.LOADING -> {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp),
                    color = Color(0xFF38BDF8),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}
