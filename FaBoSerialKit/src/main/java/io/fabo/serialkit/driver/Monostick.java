package io.fabo.serialkit.driver;

import android.hardware.usb.UsbDeviceConnection;

public class Monostick implements DriverInterface {

    public void setParameter(UsbDeviceConnection connection, byte[] params){
        connection.controlTransfer(0x40, 0x03, 26, 1, null, 0, 0); // set
    }
}
