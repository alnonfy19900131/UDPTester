package com.sky.udp_tester;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * 显示所有的网络接口信息内容
 */
public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "ac_first";
    private Button enterBtn;
    private Button refreshBtn;
    private ListView interfaceLv;

    private static final int WHAT_SHOW_INTERFACE = 0x10;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == WHAT_SHOW_INTERFACE) {
                ArrayList<InterfaceEntity> list = (ArrayList<InterfaceEntity>) msg.obj;
                if (list == null || list.isEmpty()) {
                    Toast.makeText(FirstActivity.this,R.string.toast_network_error,Toast.LENGTH_SHORT).show();
                } else {
                    NetInterfaceAdapter adapter = new NetInterfaceAdapter(FirstActivity.this, list);
                    interfaceLv.setAdapter(adapter);
                    enterBtn.setEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initViews();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initViews(){
        enterBtn = findViewById(R.id.ac_first_enter_btn);
        enterBtn.setEnabled(false);
        refreshBtn = findViewById(R.id.ac_first_refresh_btn);
        interfaceLv = findViewById(R.id.ac_first_interface_lv);
        getInterfaceList();
    }

    private void initListeners(){

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this,R.string.refresh,Toast.LENGTH_SHORT).show();
                getInterfaceList();
            }
        });

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getInterfaceList () {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                ArrayList<InterfaceEntity> list = new ArrayList<>();
                try {
                    Enumeration nis = NetworkInterface.getNetworkInterfaces();
                    InetAddress ia = null;
                    Outter:while (nis.hasMoreElements()) {
                        NetworkInterface ni = (NetworkInterface) nis.nextElement();
                        Enumeration<InetAddress> ias = ni.getInetAddresses();
                        MLog.i(TAG, "network interface = " + ni.toString());
                        if (!ni.isUp()){
                            continue;
                        }
                        InterfaceEntity entity = new InterfaceEntity();
                        entity.setIndex(ni.getIndex());
                        entity.setHardwareAddress(ni.getHardwareAddress());
                        entity.setIsloopback(ni.isLoopback());
                        entity.setUp(ni.isUp());
                        entity.setMtu(ni.getMTU());
                        entity.setName(ni.getDisplayName());
                        entity.setPointToPoint(ni.isPointToPoint());
                        entity.setVirtual(ni.isVirtual());
                        entity.setSupportMulticast(ni.supportsMulticast());
                        while (ias.hasMoreElements()) {
                            ia = ias.nextElement();
                            if (ia instanceof Inet6Address) {
                                entity.setIp_v6_addr(ia.getHostAddress());
                                continue;// skip ipv6
                            }
                            String ip = ia.getHostAddress();
                            entity.setIp_addr(ip);
//                            continue Outter;
                        }
                        list.add(entity);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = WHAT_SHOW_INTERFACE;
                    msg.obj = list;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();

    }
}
