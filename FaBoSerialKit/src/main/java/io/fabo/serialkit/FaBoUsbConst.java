package io.fabo.serialkit;

public class FaBoUsbConst {
    // Serial
    public static final int BITRATE_8 = 8;
    public static final int BITRATE_7 = 7;
    public static final int BITRATE_6 = 6;
    public static final int BITRATE_5 = 5;
    public static final int PARITY_NONE = 0;
    public static final int PARITY_ODD = 1;
    public static final int PARITY_EVEN = 2;
    public static final int PARITY_MARK = 3;
    public static final int PARITY_SPACE = 4;
    public static final int STOP_1 = 0;
    public static final int STOP_1_5 = 1;
    public static final int STOP_2 = 2;
    public static final int FLOW_CONTROL_OFF = 0;
    public static final int FLOW_CONTROL_RTS_CTS= 1;
    public static final int FLOW_CONTROL_DSR_DTR = 2;
    public static final int FLOW_CONTROL_XON_XOFF = 3;
    // For Arduino
    public static final int ARDUINO_UNO_VID = 10755;
    public static final int ARDUINO_UNO_PID = 67;
    public static final int ARDUINO_CC_UNO_VID = 9025;
    public static final int ARDUINO_CC_UNO_PID = 67;
    public static final int GENUINO_UNO_VID = 9025;
    public static final int GENUINO_UNO_PID = 579;
    public static final int ARDUINO_LEONARDO_VID = 9025;
    public static final int ARDUINO_LEONARDO_PID = 32822;
    public static final int G27_VID = 1133;
    public static final int G27_PID = 49819;
    public static final int FT232R_VID = 1027;
    public static final int FT232R_PID = 24577;
    public static final int FT232RQ_PID = 24597;

    // Type of usb device.
    public static final int TYPE_ARDUINO_UNO = 1;
    public static final int TYPE_ARDUINO_LEONARDO = 2;
    public static final int TYPE_ARDUINO_CC_UNO = 11;
    public static final int TYPE_GENUINO_UNO = 21;
    public static final int TYPE_G27 = 31;
    public static final int TYPE_FT232R = 41;
    public static final int TYPE_UNSUPPORTED = 99;
    // Baundrate
    public static final int BAUNDRATE_9600 = 9600;
    public static final int BAUNDRATE_19200 = 19200;
    public static final int BAUNDRATE_38400 = 38400;
    public static final int BAUNDRATE_57600 = 57600;
    public static final int BAUNDRATE_74800 = 74800;
    public static final int BAUNDRATE_115200 = 115200;
    public static final int BAUNDRATE_230400 = 230400;
    public static final int BAUNDRATE_250000 = 250000;
    public static final int DEFAULT_BAUNDRATE = BAUNDRATE_9600;
    // Status.
    public static final int ATTACHED = 1;
    public static final int DETACHED = 2;
    public static final int CONNECTED = 3;
    public static final int DISCONNECTED = 4;

}
