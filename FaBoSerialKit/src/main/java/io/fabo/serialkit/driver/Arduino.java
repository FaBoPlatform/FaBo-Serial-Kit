package io.fabo.serialkit.driver;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDeviceConnection;

import io.fabo.serialkit.FaBoUsbConst;
import io.fabo.serialkit.FaBoUsbParams;

public class Arduino implements DriverInterface {

    private final static int USB_CONTROL_OUT = UsbConstants.USB_TYPE_CLASS | 0x01;
    private final static int SET_LINE_CODING = 0x20;
    private final static int SET_CONTROL_LINE_STATE = 0x22;

    public void setParameter(UsbDeviceConnection connection, FaBoUsbParams usbParams){

        byte[] params = getParams(usbParams);
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
    }

    private byte[] getParams(FaBoUsbParams usbParams) {
        int settingValue = 0;

        switch (usbParams.getParityBit()) {
            case FaBoUsbConst.PARITY_NONE:
                settingValue = 0x00;
                break;
            case FaBoUsbConst.PARITY_ODD:
                settingValue = 0x01;
                break;
            case FaBoUsbConst.PARITY_EVEN:
                settingValue = 0x02;
                break;
            case FaBoUsbConst.PARITY_MARK:
                settingValue = 0x03;
                break;
            case FaBoUsbConst.PARITY_SPACE:
                settingValue = 0x04;
                break;
            default:
                settingValue = 0x00;
        }

        switch (usbParams.getStopBit()) {
            case FaBoUsbConst.STOP_1:
                settingValue |= 0x00 << 3;
                break;
            case FaBoUsbConst.STOP_1_5:
                settingValue |=  0x01 << 3;
                break;
            case FaBoUsbConst.STOP_2:
                settingValue |= 0x02 << 3;
                break;
            default:
                settingValue |= 0x00 << 3;
        }

        switch (usbParams.getFlow()) {
            case FaBoUsbConst.FLOW_CONTROL_OFF:
                settingValue |=  0x00 << 6;
            case FaBoUsbConst.FLOW_CONTROL_RTS_CTS:
                settingValue |=  0x01 << 6;
            case FaBoUsbConst.FLOW_CONTROL_DSR_DTR:
                settingValue |=  0x02 << 6;
            case FaBoUsbConst.FLOW_CONTROL_XON_XOFF:
                settingValue |=  0x03 << 6;
            default:
                settingValue |=  0x00 << 6;
        }

        byte configParams[] = new byte[]{(byte)(usbParams.getBaudrate() & 255),
                (byte)(usbParams.getBaudrate() >> 8 & 255),
                (byte)(usbParams.getBaudrate() >> 16 & 255),
                (byte)(usbParams.getBaudrate() >> 24 & 255),
                (byte)0,
                (byte)settingValue,
                (byte)usbParams.getBitrate()};

        return configParams;
    }
}
