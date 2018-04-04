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
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Test9 implements FaBoUsbListenerInterface {

    FaBoUsbManager mFaBoUsbManger;

    private String TEST_WORD = "74800";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        mFaBoUsbManger = new FaBoUsbManager(appContext);
        mFaBoUsbManger.setParameter(FaBoUsbConst.BAUNDRATE_74800,
                FaBoUsbConst.PARITY_NONE,
                FaBoUsbConst.STOP_1,
                FaBoUsbConst.FLOW_CONTROL_OFF,
                FaBoUsbConst.BITRATE_8);
        mFaBoUsbManger.setListener(this);
        mFaBoUsbManger.findDevice();
    }

    @Override
    public void onFind(UsbDevice device, int type) {
        // Find Arduino
        if(type == TestConst.TEST_TYPE) {
            mFaBoUsbManger.connection(device);
        }
    }

    @Override
    public void onStatusChanged(UsbDevice device, int status) {
        if(status == FaBoUsbConst.CONNECTED) {
            mFaBoUsbManger.writeBuffer(TEST_WORD.getBytes());
        }
    }

    @Override
    public void readBuffer(int deviceId, byte[] buffer) {
        String answer = new String(buffer);
        assertEquals(TEST_WORD, answer);
    }
}
