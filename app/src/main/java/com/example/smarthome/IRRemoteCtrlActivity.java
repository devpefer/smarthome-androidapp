package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class IRRemoteCtrlActivity extends AppCompatActivity implements View.OnClickListener {

    String mac;
    ImageView ivPower;
    ImageView ivSubirTemp;
    ImageView ivBajarTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_remote_ctrl);

        mac = getIntent().getExtras().getString("mac");
        ivPower = findViewById(R.id.ivPower);
        ivSubirTemp = findViewById(R.id.ivSubirTemp);
        ivBajarTemp = findViewById(R.id.ivBajarTemp);

        ivPower.setOnClickListener(this);
        ivSubirTemp.setOnClickListener(this);
        ivBajarTemp.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!DeviceListActivity.getMqttAndroidClient().getMqttAndroidClient().isConnected()) {
            DeviceListActivity.getMqttAndroidClient().conectar();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ivPower:
                DeviceListActivity.getMqttAndroidClient().publishMessage("cmnd/" + mac + "/irsend","{\"Protocol\":\"NEC_LIKE\",\"Bits\":32,\"Data\":\"0x032504DE\",\"DataLSB\":\"0xC0A4207B\",\"Repeat\":0}");
                break;
            case R.id.ivSubirTemp:
                DeviceListActivity.getMqttAndroidClient().publishMessage("cmnd/" + mac + "/irsend","{\"Protocol\":\"NEC_LIKE\",\"Bits\":32,\"Data\":\"0x032468A0\",\"DataLSB\":\"0xC0241605\",\"Repeat\":0}");
                break;
            case R.id.ivBajarTemp:
                DeviceListActivity.getMqttAndroidClient().publishMessage("cmnd/" + mac + "/irsend","{\"Protocol\":\"NEC_LIKE\",\"Bits\":32,\"Data\":\"0x0324E820\",\"DataLSB\":\"0xC0241704\",\"Repeat\":0}");
                break;
        }
    }
}
