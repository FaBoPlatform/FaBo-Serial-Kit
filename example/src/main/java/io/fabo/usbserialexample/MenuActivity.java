package io.fabo.usbserialexample;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.fabo.serialkit.FaBoUsbManager;

public class MenuActivity extends AppCompatActivity {
    private UsbDevice device;
    FaBoUsbManager mFaBoUsbManager;

    /**
     * TAG
     */
    private final String TAG = "USB";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Life::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Adapterの作成
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        // 値を追加
        mAdapter.add("Serial Example");
        mAdapter.add("Firmata Example");

        // ListViewの取り込み
        mListView = (ListView) findViewById(R.id.menu_listview);

        // アダプターをセット
        mListView.setAdapter(mAdapter);

        // クリックされた時のイベントを追加
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    Intent mIntent = new Intent();
                    mIntent.setClassName("io.fabo.usbserialexample", "io.fabo.usbserialexample.SerialExample");
                    startActivity(mIntent);
                } else if(position == 1) {
                    Intent mIntent = new Intent();
                    mIntent.setClassName("io.fabo.usbserialexample", "io.fabo.usbserialexample.FirmataExample");
                    startActivity(mIntent);
                }
            }
        });
    }
}


