package io.fabo.serialkit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;

import io.fabo.serialkit.driver.Arduino;
import io.fabo.serialkit.driver.DriverInterface;
import io.fabo.serialkit.driver.G27;
import io.fabo.serialkit.driver.Monostick;

public class FaBoUsbManager {

    /**
     * Tag.
     */
    private final String TAG = "FaBoUSBManager";

    /**
     * Flag
     */
    private final boolean DEBUG = false;

    /**
     * UsbManager.
     */
    private UsbManager mUsbManager;

    /**
     * Name of Filter.
     */
    private static final String ACTION_USB_PERMISSION = "io.fabo.usrserial.USB_PERMISSION";

    /**
     * Connection.
     */
    private ConnectionThread mConnection = null;

    /**
     * DeviceType.
     */
    private int deviceType = 0;

    /**
     * Baudrate.
     */
    private int baudrate = FaBoUsbConst.DEFAULT_BAUNDRATE;

    /**
     * Parity bit.
     */
    private int parityBit = 0;

    /**
     * Stop bit.
     */
    private int stopBit = 0;

    /**
     * flow.
     */
    private int flow = 0;

    /**
     * Bitrate.
     */
    private int bitrate = 0;

    /**
     * Device.
     */
    private UsbDevice mDevice = null;

    /**
     * FaBo USBListener.
     */
    private FaBoUsbListenerInterface listener = null;

    /**
     * Context.
     */
    private Context mContext;

    /**
     * Flag of processing permission.
     */
    private boolean checkingFlag;

    /**
     * Constructor.
     */
    public FaBoUsbManager(Context context) {
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        this.mContext = context;
        this.setParameter(FaBoUsbConst.BAUNDRATE_9600,
                FaBoUsbConst.PARITY_NONE,
                FaBoUsbConst.STOP_1,
                FaBoUsbConst.FLOW_CONTROL_OFF,
                FaBoUsbConst.BITRATE_8);
        registerMyReceiver();
        this.checkingFlag = false;
    }

    /**
     * Register usb receiver.
     */
    public void registerMyReceiver() {
        // Register receiver.
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        this.mContext.registerReceiver(mUsbReceiver, filter);
    }

    /**
     * Unregister usb receiver.
     */
    public void unregisterMyReceiver() {
        this.mContext.unregisterReceiver(mUsbReceiver);
    }

    /**
     * Find usb device.
     */
    public void findDevice() {

        // Get UsbManager.
        UsbManager manager = (UsbManager) this.mContext.getSystemService(this.mContext.USB_SERVICE);

        // check vid and pid.
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            checkDevice(device);
        }
    }

    public void checkDevice(UsbDevice device){
        String deviceName = device.getDeviceName();
        int deviceId = device.getDeviceId();
        int deviceVID = device.getVendorId();
        int devicePID = device.getProductId();
        int type = 0;
        if(DEBUG) {
            Log.d(TAG, "-------------------");
            Log.d(TAG, "[UsbSerial] Found usb device");
            Log.d(TAG, "-------------------");
            Log.d(TAG, "DeviceName:" + deviceName);
            Log.d(TAG, "deviceId:" + deviceId);
            Log.d(TAG, "deviceVID:" + deviceVID);
            Log.d(TAG, "devicePID:" + devicePID);
            Log.d(TAG, "-------------------");
        }

        // Arduino
        if (deviceVID == FaBoUsbConst.ARDUINO_UNO_VID && devicePID == FaBoUsbConst.ARDUINO_UNO_PID) {
            deviceType = FaBoUsbConst.TYPE_ARDUINO_UNO;
        } else if (deviceVID == FaBoUsbConst.ARDUINO_CC_UNO_VID && devicePID == FaBoUsbConst.ARDUINO_CC_UNO_PID) {
            deviceType = FaBoUsbConst.TYPE_ARDUINO_CC_UNO;
        } else if (deviceVID == FaBoUsbConst.ARDUINO_LEONARDO_VID && devicePID == FaBoUsbConst.ARDUINO_LEONARDO_PID) {
            deviceType = FaBoUsbConst.TYPE_ARDUINO_LEONARDO;
        } else if (deviceVID == FaBoUsbConst.GENUINO_UNO_VID && devicePID == FaBoUsbConst.GENUINO_UNO_PID) {
            deviceType = FaBoUsbConst.TYPE_GENUINO_UNO;
        } else if (deviceVID == FaBoUsbConst.G27_VID && devicePID == FaBoUsbConst.G27_PID) {
            deviceType = FaBoUsbConst.TYPE_G27;
        } else if (deviceVID == FaBoUsbConst.MONOSTICK_VID && devicePID == FaBoUsbConst.MONOSTICKK_PID) {
            deviceType = FaBoUsbConst.TYPE_MONOSTICK;
        } else {
            listener.onFind(device, FaBoUsbConst.TYPE_UNSUPPORTED);
            return;
        }

        Log.i(TAG, "mUsbManager.hasPermission(device):" + mUsbManager.hasPermission(device));

        if(!mUsbManager.hasPermission(device)) {
            if(!this.checkingFlag) {
                this.checkingFlag = true;
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
                mUsbManager.requestPermission(device, mPermissionIntent);
            }
        } else {
            this.mDevice = device;
            listener.onFind(device, deviceType);
        }
    }

    /**
     * deviceTypeからデバイス名を取得.
     * @param deviceType
     * @return
     */
    public String getDeviceName(int deviceType) {
        if (deviceType == FaBoUsbConst.TYPE_ARDUINO_CC_UNO) {
            return "Arduino.cc Uno";
        } else if (deviceType == FaBoUsbConst.TYPE_ARDUINO_LEONARDO) {
            return "Arduino Leonardo";
        } else if (deviceType == FaBoUsbConst.TYPE_ARDUINO_UNO) {
            return "Arduino Uno";
        } else if (deviceType == FaBoUsbConst.TYPE_GENUINO_UNO) {
            return "Genuino Uno";
        } else if (deviceType == FaBoUsbConst.TYPE_MONOSTICK) {
            return "Monostick";
        } else {
            return "Unkown";
        }
    }

    /**
     * Connect device.
     * @param device UsbDevice.
     */
    public void connection(UsbDevice device) {
        mConnection =  new ConnectionThread(device, listener);
        mConnection.start();
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action)) {
                if (DEBUG) {
                    Log.d(TAG, "-------------------");
                    Log.d(TAG, "[UsbSerial] ACTION_USB_PERMISSION");
                    Log.d(TAG, "-------------------");
                }
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            if (DEBUG) {
                                // Got permissionは、デバイス検出の後に呼ばれる。
                                Log.d(TAG, "-------------------");
                                Log.d(TAG, "[UsbSerial] Got permission");
                                Log.d(TAG, "-------------------");
                            }
                            listener.onStatusChanged(device, FaBoUsbConst.ATTACHED);
                            checkingFlag = false;
                        }
                    } else {
                        if (DEBUG == true) {
                            Log.d(TAG, "-------------------");
                            Log.d(TAG, "permission denied for device " + device);
                            Log.d(TAG, "-------------------");
                        }
                    }
                }
            }
            else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (DEBUG == true) {
                    Log.d(TAG, "-------------------");
                    Log.d(TAG, "[UsbSerial] ACTION_USB_DEVICE_DETACHED");
                    Log.d(TAG, "-------------------");
                }
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (device != null) {
                    if (DEBUG == true) {
                        Log.d(TAG, "-------------------");
                        Log.d(TAG, "[UsbSerial] Disconnect");
                        Log.d(TAG, "-------------------");
                    }
                }
                listener.onStatusChanged(device, FaBoUsbConst.DETACHED);
            }
        }
    };

    /**
     * Write to UsbSerial.
     * @param buffer
     */
    public void writeBuffer(byte[] buffer) {
        mConnection.writeMessage(buffer);
    }

    public void setParameter(int baudrate, int parityBit, int stopBit, int flow, int bitrate) {
        setBaudrate(baudrate);
        setBitrate(bitrate);
        setParityBit(parityBit);
        setStopBit(stopBit);
        setFlow(flow);

    }

    public void setBaudrate(int baudrate) {
        switch (baudrate) {
            case FaBoUsbConst.BAUNDRATE_9600:
            case FaBoUsbConst.BAUNDRATE_19200:
            case FaBoUsbConst.BAUNDRATE_38400:
            case FaBoUsbConst.BAUNDRATE_57600:
            case FaBoUsbConst.BAUNDRATE_74800:
            case FaBoUsbConst.BAUNDRATE_115200:
            case FaBoUsbConst.BAUNDRATE_230400:
            case FaBoUsbConst.BAUNDRATE_250000:
                this.baudrate = baudrate;
                break;
            default:
                this.baudrate = FaBoUsbConst.BAUNDRATE_9600;
        }
    }

    public void setBitrate(int bitrate) {
        switch (bitrate) {
            case FaBoUsbConst.BITRATE_8:
            case FaBoUsbConst.BITRATE_7:
            case FaBoUsbConst.BITRATE_6:
            case FaBoUsbConst.BITRATE_5:
                this.bitrate = bitrate;
                break;
            default:
                this.bitrate = FaBoUsbConst.BITRATE_8;
        }
    }

    public void setParityBit (int parityBit) {

        switch (parityBit) {
            case FaBoUsbConst.PARITY_NONE:
                parityBit = 0x00;
                break;
            case FaBoUsbConst.PARITY_ODD:
                parityBit = 0x01;
                break;
            case FaBoUsbConst.PARITY_EVEN:
                parityBit = 0x02;
                break;
            case FaBoUsbConst.PARITY_MARK:
                parityBit = 0x03;
                break;
            case FaBoUsbConst.PARITY_SPACE:
                parityBit = 0x04;
                break;
            default:
                parityBit = 0x00;
        }
    }

    public void setStopBit (int stopBit) {
        switch (stopBit) {
            case FaBoUsbConst.STOP_1:
                this.stopBit = 0x00;
                break;
            case FaBoUsbConst.STOP_1_5:
                this.stopBit = 0x01;
                break;
            case FaBoUsbConst.STOP_2:
                //settingValue = settingValue | 0x02 << 3;
                this.stopBit = 0x02;
                break;
            default:
                this.stopBit = 0x00;
        }
    }

    public void setFlow(int flow) {
        switch (flow) {
            case FaBoUsbConst.FLOW_CONTROL_OFF:
                this.flow = 0x00;
            case FaBoUsbConst.FLOW_CONTROL_RTS_CTS:
                this.flow = 0x01;
            case FaBoUsbConst.FLOW_CONTROL_DSR_DTR:
                this.flow = 0x02;
            case FaBoUsbConst.FLOW_CONTROL_XON_XOFF:
                this.flow = 0x03;
                //settingValue = settingValue | 0x03 << 6;
            default:
                this.flow = 0x00;
        }
    }

    public byte[] getParams() {
        int settingValue = this.parityBit | this.stopBit << 3 | this.flow << 6;
        byte params[] = new byte[]{(byte)(baudrate & 255),
                (byte)(baudrate >> 8 & 255),
                (byte)(baudrate >> 16 & 255),
                (byte)(baudrate >> 24 & 255),
                (byte)0,
                (byte)settingValue,
                (byte)bitrate};

        return params;
    }


    /**
     * Close connection.
     */
    public void closeConnection() {
        //unregisterMyReceiver();
        if(mConnection != null) {
            mConnection.closeConnection();
        }
    }

    /**
     * Add listener.
     *
     * @param listener
     */
    public void setListener(FaBoUsbListenerInterface listener) {
        this.listener = listener;
    }

    /**
     * Remove listener.
     */
    public void removeListener() {
        this.listener = null;
    }

    /**
     * Thread of communicating with USB.
     */
    private class ConnectionThread extends Thread {

        /**
         * Temporary of UsbInterface.
         */
        private UsbInterface usbInterfaceTemp = null;

        /**
         * UsbDevice.
         */
        private UsbDevice mDevice;

        /**
         * Buffer of read(64bytes).
         */
        private byte[] readBuffer = new byte[64];

        /**
         * ForceClaim.
         */
        private boolean forceClaim = true;

        /**
         * Connection.
         */
        private UsbDeviceConnection connection;

        /**
         * Endpoint for input.
         */
        private UsbEndpoint endpointIN;

        /**
         * Endpoint for output.
         */
        private UsbEndpoint endpointOUT;

        /**
         * Timeout.
         */
        private final int TIMEOUT = 0;

        /**
         * Usb Interface.
         */
        private UsbInterface usbInterface = null;

        /**
         * UsbListener.
         */
        private FaBoUsbListenerInterface listener;

        /**
         * Thread flag.
         */
        public boolean runningFlag;

        /**
         * Constructor.
         * @param device UsbDevice.
         * @param listener EventListener.
         */
        public ConnectionThread(UsbDevice device, FaBoUsbListenerInterface listener) {
            this.mDevice = device;
            this.listener = listener;
            runningFlag = true;
        }

        @Override
        public void run() {

            if(DEBUG) {
                Log.d(TAG, "-------------------");
                Log.d(TAG, "[UsbSerial] This device");
                Log.d(TAG, "-------------------");
                Log.d(TAG, "this.mDevice.getInterfaceCount():" + this.mDevice.getInterfaceCount());
                for (int i = 0; i < this.mDevice.getInterfaceCount(); i++) {
                    usbInterfaceTemp = this.mDevice.getInterface(i);
                    Log.d(TAG, "usbInterfaceTemp.getEndpointCount():" + usbInterfaceTemp.getEndpointCount());
                    for (int d = 0; d < usbInterfaceTemp.getEndpointCount(); d++) {
                        UsbEndpoint endpoint = usbInterfaceTemp.getEndpoint(d);
                        if(endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){
                            Log.d(TAG, "USB_ENDPOINT_XFER_BULK");
                            if (endpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                                Log.d(TAG, " USB_DIR_IN");
                            } else if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                                Log.d(TAG, " USB_DIR_OUT");
                            }
                        } else if(endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_CONTROL){
                            Log.d(TAG, "USB_ENDPOINT_XFER_CONTROL");
                        } else if(endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_ISOC){
                            Log.d(TAG, "USB_ENDPOINT_XFER_ISOC");
                        } else if(endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_INT){
                            Log.d(TAG, "USB_ENDPOINT_XFER_INT");
                        }
                    }
                }
            }

            /*endpointIN = null;
            endpointOUT = null;
            usbInterface = null;*/

            // Set endpoint of input and output.
            for (int i = 0; i < this.mDevice.getInterfaceCount(); i++) {
                usbInterfaceTemp = this.mDevice.getInterface(i);
                for (int d = 0; d < usbInterfaceTemp.getEndpointCount(); d++) {
                    UsbEndpoint endpoint = usbInterfaceTemp.getEndpoint(d);
                    if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                        if (endpoint.getDirection() == UsbConstants.USB_DIR_IN) {
                            endpointIN = endpoint;
                        } else if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT) {
                            endpointOUT = endpoint;
                        }
                    }
                }
            }

            if (endpointIN != null && endpointOUT != null) {
                usbInterface = usbInterfaceTemp;
            }

            if (usbInterface == null) {
                return;
            }

            // Open device.
            if(this.mDevice == null) {
                return;
            }

            // Connect Devices.
            connection = mUsbManager.openDevice(this.mDevice);
            connection.claimInterface(usbInterface, forceClaim);

            // Control of Arduino UNO.
            if(deviceType == FaBoUsbConst.TYPE_ARDUINO_UNO
                    || deviceType == FaBoUsbConst.TYPE_ARDUINO_LEONARDO
                    || deviceType == FaBoUsbConst.TYPE_ARDUINO_CC_UNO
                    || deviceType == FaBoUsbConst.TYPE_GENUINO_UNO) {
                DriverInterface driver = new Arduino();
                driver.setParameter(connection, getParams());
            } else if(deviceType == FaBoUsbConst.TYPE_G27){
                DriverInterface driver = new G27();
                setBaudrate(FaBoUsbConst.BAUNDRATE_57600);
                driver.setParameter(connection, getParams());
            } else if(deviceType == FaBoUsbConst.TYPE_MONOSTICK){
                DriverInterface driver = new Monostick();
                driver.setParameter(connection, getParams());
            }

            this.listener.onStatusChanged(mDevice, FaBoUsbConst.CONNECTED);

            while (true) {
                synchronized (this) {
                    if (!runningFlag) {
                        return;
                    }
                }
                if (endpointIN == null) {
                    continue;
                }

                int length = connection.bulkTransfer(endpointIN, readBuffer, readBuffer.length, 0);
                if (length > 0) {
                    byte[] read = new byte[length];
                    System.arraycopy(readBuffer, 0, read, 0, length);

                    this.listener.readBuffer(mDevice.getDeviceId(), read);
                }
            }
        }

        /**
         * Write message.
         *
         * @param writeBuffer
         */
        public void writeMessage(byte[] writeBuffer) {

            for(int i = 0; i < writeBuffer.length; i++) {
                String s = "";
                if((0xff & writeBuffer[i])<16) {
                    s += "0x0";
                } else {
                    s += "0x";
                }
                s += Integer.toHexString(0xff & writeBuffer[i]);
                if(DEBUG) {
                    Log.i(TAG, s);
                }
            }
            connection.bulkTransfer(endpointOUT, writeBuffer, writeBuffer.length, 100);
        }

        /**
         * Connection close.
         */
        public void closeConnection() {
            runningFlag = false;
            if(connection != null) {
                try {
                    connection.close();
                } catch(Exception e){}
            }
        }
    }
}