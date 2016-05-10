package com.healthywatch.brands.healthywatch.helper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.SocketHandler;

/**
 * Created by brandon on 10/05/16.
 */
public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothAdapter mBluetoothAdapter;
    private SocketHandler mSocket;
    private ConnectedThread mConnectedThread;

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        mmDevice = device;
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
    public void run() {
        mBluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
            mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();
        } catch (IOException connectException) {
            mSocket.close();
            return;
        }
    }
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
