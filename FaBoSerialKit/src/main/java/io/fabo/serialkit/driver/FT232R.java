package io.fabo.serialkit.driver;

import android.hardware.usb.UsbDeviceConnection;
import java.util.HashMap;
import java.util.Map;
import io.fabo.serialkit.FaBoUsbConst;
import io.fabo.serialkit.FaBoUsbParams;

public class FT232R implements DriverInterface {

    Map<Integer, Float> mapBuandrate = new HashMap<Integer, Float>() {
        {
            put(FaBoUsbConst.BAUNDRATE_9600, (float) 321.5);
            put(FaBoUsbConst.BAUNDRATE_19200, (float) 156);
            put(FaBoUsbConst.BAUNDRATE_38400, (float) 78);
            put(FaBoUsbConst.BAUNDRATE_57600, (float) 52);
            put(FaBoUsbConst.BAUNDRATE_74800, (float) 39);
            put(FaBoUsbConst.BAUNDRATE_115200, (float) 26);
            put(FaBoUsbConst.BAUNDRATE_230400, (float) 13);
            put(FaBoUsbConst.BAUNDRATE_250000, (float) 10);
        }
    };

    private static final int RESET_REQUEST = 0;
    private static final int BAUD_RATE_REQUEST = 3;
    private static final int DATA_REQUEST = 4;

    public void setParameter(UsbDeviceConnection connection, FaBoUsbParams usbParams){
        // Reset.
        int result = connection.controlTransfer(0x40, RESET_REQUEST, 0, 0 /* index */, null, 0, TIMEOUT);
        if(result == -1) {
            connection.controlTransfer(0x80, RESET_REQUEST, 0, 0 /* index */, null, 0, TIMEOUT);
        }

        float buadrate = mapBuandrate.get(usbParams.getBaudrate());
        // Setting buadrate.
        result = connection.controlTransfer(0x40, BAUD_RATE_REQUEST, (int) buadrate, 1, null, 0, TIMEOUT);
        if(result == -1) {
            connection.controlTransfer(0x80, BAUD_RATE_REQUEST, (int) buadrate, 1, null, 0, TIMEOUT);
        }

        // Setting config.
        int configValue = getParams(usbParams);
        result = connection.controlTransfer(0x40,  DATA_REQUEST, configValue, 0, null, 0, TIMEOUT);
        if(result == -1) {
            connection.controlTransfer(0x80,  DATA_REQUEST, configValue, 0, null, 0, TIMEOUT);
        }
    }


    private int getParams(FaBoUsbParams usbParams) {
        int configValue = usbParams.getBitrate();

        switch(usbParams.getParityBit()){
            case FaBoUsbConst.PARITY_NONE:
                configValue |= (0x00 << 8);
                break;
            case FaBoUsbConst.PARITY_ODD:
                configValue |= (0x01 << 8);
                break;
            case FaBoUsbConst.PARITY_EVEN:
                configValue |= (0x02 << 8);
                break;
            case FaBoUsbConst.PARITY_MARK:
                configValue |= (0x03 << 8);
                break;
            case FaBoUsbConst.PARITY_SPACE:
                configValue |= (0x04 << 8);
                break;
            default:
                configValue |= (0x00 << 8);
        }

        switch(usbParams.getStopBit()){
            case FaBoUsbConst.STOP_1:
                configValue |= (0x00 << 11);
                break;
            case FaBoUsbConst.STOP_1_5:
                configValue |= (0x01 << 11);
                break;
            case FaBoUsbConst.STOP_2:
                configValue |= (0x02 << 11);
                break;
            default:
                configValue |= (0x00 << 11);
        }

        return configValue;
    }
}
