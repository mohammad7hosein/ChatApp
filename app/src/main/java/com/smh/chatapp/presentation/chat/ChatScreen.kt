package com.smh.chatapp.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.smh.chatapp.ui.theme.Purple200
import com.smh.chatapp.ui.theme.Purple500
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    username: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.connectToChat()
            } else if (event == Lifecycle.Event.ON_STOP) {
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val state = viewModel.state.value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(state.messages) { message ->
                val isOwnMessage = message.username == username
                Box(
                    contentAlignment =
                    if (isOwnMessage)
                        Alignment.CenterEnd
                    else
                        Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .width(200.dp)
                            .drawBehind {
                                val cornerRadius = 15.dp.toPx()
                                val triangleHeight = 15.dp.toPx()
                                val triangleWidth = 25.dp.toPx()
                                val trianglePath = Path().apply {
                                    if (isOwnMessage) {
                                        moveTo(size.width, size.height - cornerRadius)
                                        lineTo(size.width, size.height + triangleHeight)
                                        lineTo(
                                            size.width - triangleWidth,
                                            size.height - cornerRadius
                                        )
                                        close()
                                    } else {
                                        moveTo(0f, size.height - cornerRadius)
                                        lineTo(0f, size.height + triangleHeight)
                                        lineTo(triangleWidth, size.height - cornerRadius)
                                        close()
                                    }
                                }
                                drawPath(
                                    path = trianglePath,
                                    color = if (isOwnMessage) Purple200 else Purple500
                                )
                            }
                            .background(
                                color = if (isOwnMessage) Purple200 else Purple500,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = message.username,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = message.text,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                        Text(
                            text = message.formattedTime,
                            color = MaterialTheme.colors.onSurface,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = viewModel.messageText.value,
                onValueChange = viewModel::onMessageChange,
                textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                placeholder = { Text(text = "Enter your message...") },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = viewModel::sendMessage) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }

}