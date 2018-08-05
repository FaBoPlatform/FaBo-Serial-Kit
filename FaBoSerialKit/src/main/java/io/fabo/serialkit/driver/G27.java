package io.fabo.serialkit.driver;

import android.hardware.usb.UsbDeviceConnection;

import io.fabo.serialkit.FaBoUsbParams;

public class G27 implements DriverInterface {

    public void setParameter(UsbDeviceConnection connection, FaBoUsbParams params){

        /*
        connection.controlTransfer(USB_CONTROL_OUT,
                SET_LINE_CODING, //requestType
                0, //value
                0, //index
                params, // buffer
                params.length, // length
                TIMEOUT);

        // Enable DTR/RTS.
        connection.controlTransfer(USB_CONTROL_OUT,
                SET_CONTROL_LINE_STATE, //requestType
                0x02, //value
                0,  //index
                null, // buffer
                0, // length
                TIMEOUT);
                */
    }
}
