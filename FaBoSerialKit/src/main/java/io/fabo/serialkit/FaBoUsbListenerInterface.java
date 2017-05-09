package io.fabo.serialkit;

import android.hardware.usb.UsbDevice;

import java.util.EventListener;

public interface FaBoUsbListenerInterface extends EventListener {

    /**
     * Find usb device.
     * @param device usb device.
     * @param type device type
     */
    public void onFind(UsbDevice device, int type);

    /**
     * Status change.
     * @param device
     * @param status
     */
    public void onStatusChanged(UsbDevice device, int status);

    /**
     * Read buffer.
     * @param deviceId
     * @param buffer
     */
    public void readBuffer(int deviceId, byte[] buffer);
}