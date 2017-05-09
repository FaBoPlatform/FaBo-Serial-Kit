package io.fabo.serialkit.driver;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDeviceConnection;

public interface DriverInterface
{
    public final static int USB_CONTROL_OUT = UsbConstants.USB_TYPE_CLASS | 0x01; //0x21

    // CDC(Communication Device Class) Requests
    public final static int SEND_ENCAPSULATED_COMMAND = 0x00;
    public final static int GET_ENCAPSULATED_COMMAND = 0x01;
    public final static int SET_LINE_CODING = 0x20;
    public final static int GET_LINE_CODING = 0x21;
    public final static int SET_CONTROL_LINE_STATE = 0x22;
    public final static int SEND_BREAK = 0x23;

    // Timeout
    public final static int TIMEOUT = 5000;

    public void setParameter(UsbDeviceConnection connection, byte[] params);

}