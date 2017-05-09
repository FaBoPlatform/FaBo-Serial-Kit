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
public class Test8 implements FaBoUsbListenerInterface {

    FaBoUsbManager mFaBoUsbManger;

    private String TEST_WORD = "57600";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        mFaBoUsbManger = new FaBoUsbManager(appContext);
        mFaBoUsbManger.setParameter(FaBoUsbConst.BAUNDRATE_57600);
        mFaBoUsbManger.setListener(this);
        mFaBoUsbManger.findDevice();
    }

    @Override
    public void onFind(int type, UsbDevice device) {
        // Find Arduino
        if(type == TestConst.TEST_TYPE) {
            mFaBoUsbManger.connection(device);
        }
    }

    @Override
    public void onStatusChanged(int deviceId, int status) {
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
