package io.fabo.usbserialexample;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import io.fabo.serialkit.FaBoUsbConst;
import io.fabo.serialkit.FaBoUsbListenerInterface;
import io.fabo.serialkit.FaBoUsbManager;

public class FirmataExample extends AppCompatActivity implements FaBoUsbListenerInterface {
    private UsbDevice device;
    FaBoUsbManager mFaBoUsbManager;

    private final byte START_SYSEX = (byte)0xF0;
    private final byte END_SYSEX = (byte)0xF7;
    private final byte I2C_REQUEST= 0x76;
    private final byte I2C_REPLY= 0x77;
    private final byte I2C_CONFIG= 0x78;
    private final byte EXTENDED_ANALOG = 0x6F;
    private final byte DRV8830_FORWARD = 0x01;
    private final byte DRV8830_BACK = 0x02;
    private final byte DRV8830_STOP = 0x00;
    private final byte DRV8830_ADDRESS = 0x64;
    private final byte SET_PIN_MODE = (byte)0xF4;
    /**
     * TAG
     */
    private final String TAG = "USB";

    private Button mConnectButton;
    private Button mDisconnectButton;
    private Button mSendButton1;
    private Button mSendButton2;
    private Button mSendButton3;
    private Button mSendButton4;
    private Button mSendButton5;
    private Button mSendButton6;
    private Button mSendButton7;
    private Button mSendButton8;
    private Button mSendButton9;
    private UsbDevice mDevice;
    private Spinner mSpeedSpinner;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Life::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmata);

        mFaBoUsbManager = new FaBoUsbManager(this);
        mFaBoUsbManager.setParameter(FaBoUsbConst.BAUNDRATE_57600,
                FaBoUsbConst.PARITY_NONE,
                FaBoUsbConst.STOP_1,
                FaBoUsbConst.FLOW_CONTROL_OFF,
                FaBoUsbConst.BITRATE_8);
        mFaBoUsbManager.setListener(this);

        mConnectButton = (Button) findViewById(R.id.connect_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    mFaBoUsbManager.connection(mDevice);
                }
            }
        });

        mDisconnectButton = (Button) findViewById(R.id.disconnect_button);
        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    mFaBoUsbManager.closeConnection();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEditText.setText("");
                    }
                });
            }


        });

        mSendButton1 = (Button) findViewById(R.id.send_button1);
        mSendButton1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    byte[] commandData = {(byte)0xF9};
                    //byte[] commandData = {(byte)0xF0, (byte)0x6B, (byte)0xF7};
                    mFaBoUsbManager.writeBuffer(commandData);
                }
                //
            }
        });

        mSendButton2 = (Button) findViewById(R.id.send_button2);
        mSendButton2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {

                    byte[] configCommandData = {START_SYSEX, I2C_CONFIG, (byte)0x00, (byte)0x00, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(configCommandData);

                    byte speedLsb = (byte)(((50 << 2) |DRV8830_FORWARD) & 0x7f);
                    byte speedMsb = (byte)((((50 << 2) |DRV8830_FORWARD) >> 7 )& 0x7f);

                    byte[] commandData = {START_SYSEX, I2C_REQUEST, DRV8830_ADDRESS, 0x00, 0x00, 0x00, speedLsb, speedMsb, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(commandData);
                }
            }
        });

        mSendButton3 = (Button) findViewById(R.id.send_button3);
        mSendButton3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {

                    byte[] configCommandData = {START_SYSEX, I2C_CONFIG, (byte)0x00, (byte)0x00, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(configCommandData);
                    int speed = 50;
                    byte speedLsb = (byte)(((speed << 2) | DRV8830_BACK) & 0x7f);
                    byte speedMsb = (byte)((((speed << 2) | DRV8830_BACK) >> 7 ) & 0x7f);

                    byte[] commandData = {START_SYSEX, I2C_REQUEST, DRV8830_ADDRESS, 0x00, 0x00, 0x00, speedLsb, speedMsb, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(commandData);
                }
            }
        });

        mSendButton4 = (Button) findViewById(R.id.send_button4);
        mSendButton4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {

                    byte[] configCommandData = {START_SYSEX, I2C_CONFIG, (byte)0x00, (byte)0x00, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(configCommandData);
                    int speed = 50;
                    byte speedLsb = (byte)(((speed << 2) | DRV8830_BACK) & 0x7f);
                    byte speedMsb = (byte)((((speed << 2) | DRV8830_BACK) >> 7 ) & 0x7f);

                    byte[] commandData = {START_SYSEX, I2C_REQUEST, DRV8830_ADDRESS, 0x00, 0x00, 0x00, 0x00, 0x00, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(commandData);
                }
            }
        });

        mSendButton5 = (Button) findViewById(R.id.send_button5);
        mSendButton5.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    //0x91 0x04(0b11000100) 0x00(0b00000000)
                    byte[] commandData = {(byte)SET_PIN_MODE, (byte)0x09, (byte)0x04};
                    mFaBoUsbManager.writeBuffer(commandData);

                    int pwm = 120;
                    byte pwmLsb = (byte)(pwm & 0x7f);
                    byte pwmMsb = (byte)((pwm >> 7 ) & 0x7f);

                    byte[] commandSend = {START_SYSEX, EXTENDED_ANALOG, 0x09, pwmLsb, pwmMsb, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(commandSend);
                }
            }
        });

        mSendButton6 = (Button) findViewById(R.id.send_button6);
        mSendButton6.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    int pwm = 90;
                    byte pwmLsb = (byte)(pwm & 0x7f);
                    byte pwmMsb = (byte)((pwm >> 7 ) & 0x7f);

                    byte[] commandSend = {START_SYSEX, EXTENDED_ANALOG, 0x09, pwmLsb, pwmMsb, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(commandSend);
                }
            }
        });

        mSendButton7 = (Button) findViewById(R.id.send_button7);
        mSendButton7.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    int pwm = 150;
                    byte pwmLsb = (byte)(pwm & 0x7f);
                    byte pwmMsb = (byte)((pwm >> 7 ) & 0x7f);

                    byte[] commandSend = {START_SYSEX, EXTENDED_ANALOG, 0x09, pwmLsb, pwmMsb, END_SYSEX};
                    mFaBoUsbManager.writeBuffer(commandSend);
                }
            }
        });
        mSendButton8 = (Button) findViewById(R.id.send_button8);
        mSendButton8.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    //0x91 0x04(0b11000100) 0x00(0b00000000)
                    byte[] commandData = {(byte)0x90, (byte)0x44, (byte)0x01};
                    mFaBoUsbManager.writeBuffer(commandData);
                }
            }
        });

        mSendButton9 = (Button) findViewById(R.id.send_button9);
        mSendButton9.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    //0x91 0x00(0b11000000) 0x00(0b00000000)
                    byte[] commandData = {(byte)0x90, (byte)0x40, (byte)0x01};
                    mFaBoUsbManager.writeBuffer(commandData);
                }
            }
        });



        mEditText = (EditText)findViewById(R.id.receive_text);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Life::onPause()");
        mFaBoUsbManager.closeConnection();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "Life::onResume()");
        super.onResume();

        mFaBoUsbManager.findDevice();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Life::onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFaBoUsbManager.unregisterMyReceiver();
        Log.i(TAG, "Life::onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "Life::onRestart()");
    }

    @Override
    public void onFind(UsbDevice device, int type) {
        Log.i(TAG, "onFind()");
        Log.i(TAG, "deivceName:" + device.getDeviceName());
        mDevice = device;
    }

    @Override
    public void onStatusChanged(UsbDevice device, int status) {
        Log.i(TAG, "onStatusChanged:" + status);
        if(status == FaBoUsbConst.ATTACHED) {
            mDevice = device;
        }
        else if(status == FaBoUsbConst.CONNECTED) {
            Log.i(TAG, "Write");
            mDevice = device;
        }
    }

    @Override
    public void readBuffer(int deviceId, byte[] buffer) {

        String result = "";
        for(int i = 0; i < buffer.length; i++) {
            String s = "";
            Log.i(TAG, "buffer[i]"+ (0xff & buffer[i]));
            if((0xff & buffer[i])<16) {
                s += "0x0";
            } else {
                s += "0x";
            }
            s += Integer.toHexString(0xff & buffer[i]);
            result += s + " ";
        }
        Log.i(TAG, "result:"+result);
        final String textResult = result;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(textResult);
            }
        });

    }
}

