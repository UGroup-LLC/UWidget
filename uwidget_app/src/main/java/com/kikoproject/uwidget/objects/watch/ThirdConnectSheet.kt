package com.kikoproject.uwidget.objects.watch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kikoproject.uwidget.objects.cards.StandardBottomSheet
import com.kikoproject.uwidget.objects.text.colorize
import tej.wifitoolslib.DevicesFinder
import tej.wifitoolslib.interfaces.OnDeviceFindListener
import tej.wifitoolslib.models.DeviceItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdConnectSheet(
    dialogVisibleState: MutableState<Boolean>,
    fourthDialogVisibleState: MutableState<Boolean>,
    chosenIp: MutableState<TextFieldValue>
) {
    val context = LocalContext.current
    val devices = remember { mutableStateListOf<DeviceItem>() }

    val finishedScan = remember {
        mutableStateOf(false)
    }

    val devicesFinder = DevicesFinder(context, object : OnDeviceFindListener {
        override fun onStart() {}
        override fun onDeviceFound(deviceItem: DeviceItem) {
            devices.add(deviceItem)
        }

        override fun onComplete(deviceItems: List<DeviceItem>) {
            devices.clear()
            deviceItems.forEach {
                devices.add(it)
            }
            finishedScan.value = true
        }

        override fun onFailed(errorCode: Int) {
            finishedScan.value = true
        }
    })

    devicesFinder.start()

    StandardBottomSheet(dialogVisibleState = dialogVisibleState) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDCAB Третий шаг \uD83D\uDCAB",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp
            )
            Divider(
                modifier = Modifier.fillMaxWidth(0.5f),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.25f)
            )
            Text(
                text = (if (finishedScan.value) "@Поиск закончен@, выберите IP часов"
                else "@Идет поиск@, подождите").colorize(),

                style = MaterialTheme.typography.titleSmall,
            )
            if (!finishedScan.value) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(5.dp)
                        .clip(CircleShape)
                        .fillMaxWidth()
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                devices.forEach { device ->
                    Button(
                        onClick = {
                            chosenIp.value = TextFieldValue(device.ipAddress)
                            fourthDialogVisibleState.value = true
                            dialogVisibleState.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp)
                    ) {
                        if (device.isDeviceNameAndIpAddressSame) {
                            Text(text = "${device.vendorName}, ${device.ipAddress}")
                        } else {
                            Text(text = "${device.deviceName}, ${device.ipAddress}")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = "Если ничего не нашлось введите вручную IP который выдали вам часы")
            OutlinedTextField(
                value = chosenIp.value,
                onValueChange = { chosenIp.value = it },
                label = {
                    Text(text = "IP адрес")
                })
            if(chosenIp.value.text.length > 8) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        fourthDialogVisibleState.value = true
                        dialogVisibleState.value = false
                    }) {
                    Text(
                        text = "Выполнено",
                    )
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}