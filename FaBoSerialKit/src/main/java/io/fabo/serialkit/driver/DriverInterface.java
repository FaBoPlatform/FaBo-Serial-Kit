package io.fabo.serialkit.driver;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDeviceConnection;

import io.fabo.serialkit.FaBoUsbParams;

public interface DriverInterface
{

    // CDC(Communication Device Class) Requests
    public final static int SET_LINE_CODING = 0x20;
    public final static int SET_CONTROL_LINE_STATE = 0x22;

    // Timeout
    public final static int TIMEOUT = 5000;

    public void setParameter(UsbDeviceConnection connection, FaBoUsbParams usbParams);

}