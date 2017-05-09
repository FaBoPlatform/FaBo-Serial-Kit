package io.fabo.serialkit;

import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import android.hardware.usb.UsbDevice;

/**
 * Check device name and device type.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Test1 implements FaBoUsbListenerInterface {

    FaBoUsbManager mFaBoUsbManger;

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        mFaBoUsbManger = new FaBoUsbManager(appContext);
        mFaBoUsbManger.setListener(this);
        mFaBoUsbManger.findDevice();
    }

    @Override
    public void onFind(int type, UsbDevice device) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            assertEquals(TestConst.TEST_DEVICE, device.getProductName());
        }
        assertEquals(TestConst.TEST_TYPE, type);
    }

    @Override
    public void onStatusChanged(int deviceId, int status) {
    }

    @Override
    public void readBuffer(int deviceId, byte[] buffer) {
    }
}
