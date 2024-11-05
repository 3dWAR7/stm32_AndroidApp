package com.example.app3

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.app3.ui.theme.App3Theme
import java.io.IOException
import java.io.OutputStream
import java.util.UUID
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    private val mmSocket: BluetoothSocket? = null
    private val mmDevice: BluetoothDevice? = null
    private var mSocketType: String? = null
    var globalSocket: BluetoothSocket? = null
//    val MY_UUID_SECURE: UUID = UUID.fromString("19af7954-94ac-452a-9309-bd6784484069")
    val MY_UUID_SECURE: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App3Theme {
                StartScaffold()
            }
        }
    }
    private fun checkBluetooth(deviceName: MutableState<String>) {

    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScaffold()
{
    var deviceName by remember { mutableStateOf("") }
    var presses by remember { mutableIntStateOf(0) }
    Scaffold(modifier = Modifier.fillMaxSize(),

        bottomBar = {

            BottomAppBar(
                actions = {
                    IconButton(
                        onClick = { presses--},
                        modifier = Modifier.padding(20.dp, 0.dp)
                    )
                    {
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(
                        onClick = { /* do something */
                            val bta = byteArrayOf(0xed.toByte())

                            val thr1 = Thread {
//                    connect_socket(mmSocket)
                                write_socket(bta)
                            }.start()
                        },
                        modifier = Modifier.padding(20.dp, 0.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(
                        onClick = { /* do something */
                            val bta = byteArrayOf(0xde.toByte())

                            val thr2 = Thread {
//                    connect_socket(mmSocket)
                                write_socket(bta)
                            }.start()
                        },
                        modifier = Modifier.padding(50.dp, 0.dp, 20.dp, 0.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(
                        onClick = { presses++},
                        modifier = Modifier.padding(20.dp, 0.dp),
                    ) {
                        Icon(
                            Icons.Filled.KeyboardArrowUp,
                            contentDescription = "Localized description"
                        )
                    }
                },
                modifier = Modifier.height(200.dp)
            )
        },

        floatingActionButton = {
            FloatingActionButton(

                onClick = {
                       if (presses == 0) {

                            deviceName = bluetooth_list()
                        } else {
                            cancel_socket()
                        }

                    //deviceName = deviceName.toString() + "\n"
                    /* do something */
                },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Build, "Localized description")

            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("Small Top App Bar \n")
                },
                actions = {
                    Text(
                        text = "$presses  p"
                    )
                }
            )
        }
    ) { innerPadding ->
        Text(
            text = "$deviceName",
            modifier = Modifier.padding(innerPadding)
        )


    }
}
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID_SECURE)
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
//            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.let { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket.connect()
                    manageMyConnectedSocket(socket)
                } catch (e: IOException) {
                    // Close the socket
                    try {
                        mmSocket!!.close()
                    } catch (e2: IOException) {

                    }


                }


                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.

            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
//                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }

fun manageMyConnectedSocket(socket: BluetoothSocket) {
    globalSocket = socket
}
fun socket_isconnected(): Boolean
{
//    if(globalSocket == null)
//    {
//        return false
//    }else {
//        return globalSocket.isConnected()
//    }
    return true
}
fun bluetooth_list(): String {
    var local_devices :String = ""
    val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
    if (bluetoothAdapter == null) {
        // Device doesn't support Bluetooth
    }

    if (bluetoothAdapter?.isEnabled != false) {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->

            if(device.name.equals("HC-06")) {
                local_devices = local_devices + device.name + "\n"
                local_devices = local_devices + device.address
                val deviceHardwareAddress = device.address // MAC address
                val deviceToSend: BluetoothDevice = device
                val mmnewThr = ConnectThread(deviceToSend)
//                mmnewThr.start()
                bluetoothAdapter.cancelDiscovery()

//                device.createRfcommSocketToServiceRecord("SV_66_EDW")
//                val mmSocket: BluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE)
                val thr = Thread {
//                    connect_socket(mmSocket)
                    mmnewThr.run()
                }.start()
//                }.start()
//                thr.start()
            }
        }

    }
    return local_devices
}
fun cancel_socket()
{
    globalSocket?.close()
}
fun write_socket(byte_edw: ByteArray)
{
    val outStr: OutputStream?
    if(globalSocket != null)
    {
        outStr = globalSocket?.getOutputStream()
        outStr?.write(byte_edw)
    }

}
fun connect_socket(socket: BluetoothSocket )
{
    globalSocket?.connect()
    manageMyConnectedSocket(socket)
}
    @Composable
fun createButtons()
{

}

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
//        var presses by remember { mutableIntStateOf(0) }
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        App3Theme {
            Greeting("Android")
        }
    }
}