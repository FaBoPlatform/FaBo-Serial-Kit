package io.fabo.serialkit;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Echo test.
 * Two arduinos detected.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Test5 implements FaBoUsbListenerInterface {

    FaBoUsbManager mFaBoUsbMangerArduino1;
    FaBoUsbManager mFaBoUsbMangerArduino2;
    int arduinoDeviceId1 = 0;
    int arduinoDeviceId2 = 0;
    private String TEST_WORD1 = "aaa";
    private String TEST_WORD2 = "bbb";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        mFaBoUsbMangerArduino1 = new FaBoUsbManager(appContext);
        mFaBoUsbMangerArduino1.setListener(this);
        mFaBoUsbMangerArduino1.findDevice();

        mFaBoUsbMangerArduino2 = new FaBoUsbManager(appContext);
        mFaBoUsbMangerArduino2.setListener(this);
        mFaBoUsbMangerArduino2.findDevice();
    }

    @Override
    public void onFind(int type, UsbDevice device) {
        // Find Arduino
        if(type == TestConst.TEST_TYPE) {
            if(arduinoDeviceId1 == 0) {
                mFaBoUsbMangerArduino1.connection(device);
                arduinoDeviceId1 = device.getDeviceId();
            } else if(arduinoDeviceId2 == 0) {
                mFaBoUsbMangerArduino2.connection(device);
                arduinoDeviceId2 = device.getDeviceId();
            }
        }
    }

    @Override
    public void onStatusChanged(int deviceId, int status) {
        if(status == FaBoUsbConst.CONNECTED && arduinoDeviceId1 == deviceId) {
            mFaBoUsbMangerArduino1.writeBuffer(TEST_WORD1.getBytes());
        } else if(status == FaBoUsbConst.CONNECTED && arduinoDeviceId2 == deviceId) {
            mFaBoUsbMangerArduino2.writeBuffer(TEST_WORD2.getBytes());
        }
    }

    @Override
    public void readBuffer(int deviceId, byte[] buffer) {
        if(deviceId == arduinoDeviceId1) {
            String answer = new String(buffer);
            assertEquals(TEST_WORD1, answer);
        } else if(deviceId == arduinoDeviceId2) {
            String answer = new String(buffer);
            assertEquals(TEST_WORD2, answer);
        }
    }
}
