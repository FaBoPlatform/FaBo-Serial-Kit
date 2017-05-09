package io.fabo.usbserialexample;

import android.hardware.usb.UsbDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import io.fabo.serialkit.FaBoUsbConst;
import io.fabo.serialkit.FaBoUsbListenerInterface;
import io.fabo.serialkit.FaBoUsbManager;

public class SerialExample extends AppCompatActivity implements FaBoUsbListenerInterface {
    private UsbDevice device;
    FaBoUsbManager mFaBoUsbManager;

    /**
     * TAG
     */
    private final String TAG = "USB";

    private Button mConnectButton;
    private Button mDisconnectButton;
    private Button mSendButton1;
    private Button mSendButton2;
    private Button mSendButton3;
    private UsbDevice mDevice;
    private Spinner mSpeedSpinner;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Life::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial);

        mFaBoUsbManager = new FaBoUsbManager(this);
        mFaBoUsbManager.setParameter(FaBoUsbConst.BAUNDRATE_115200,
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
                    mFaBoUsbManager.writeBuffer("1".getBytes());
                }
            }
        });

        mSendButton2 = (Button) findViewById(R.id.send_button2);
        mSendButton2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    mFaBoUsbManager.writeBuffer("2".getBytes());
                }
            }
        });

        mSendButton3 = (Button) findViewById(R.id.send_button3);
        mSendButton3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(mDevice != null) {
                    mFaBoUsbManager.writeBuffer("3".getBytes());
                }
            }
        });

        mSpeedSpinner = (Spinner) findViewById(R.id.speed_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.speed_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpeedSpinner.setAdapter(adapter);
        mSpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String speed = parent.getItemAtPosition(position).toString();
                int serialSpeed = Integer.parseInt(speed);
                mFaBoUsbManager.setParameter(serialSpeed,
                        FaBoUsbConst.PARITY_NONE,
                        FaBoUsbConst.STOP_1,
                        FaBoUsbConst.FLOW_CONTROL_OFF,
                        FaBoUsbConst.BITRATE_8);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        final String result = new String(buffer);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(result);
            }
        });

    }
}


