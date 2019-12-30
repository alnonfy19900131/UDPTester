package com.sky.udp_tester;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ac_main";
    private static final String BROAD_CAST_ADDRESS = "255.255.255.255";

    private EditText ipEdt,portEdt,msgEdt;
    private Button startBtn,sendBtn;
    private ListView msgLv;
    private MsgAdapter msgAdapter;
    private ArrayList<MsgEntity> msgList = new ArrayList<>();
    private Toast toast;

    private boolean isStart = true;
    private UDPCommUtil udpCommUtil;
    private MulCommUtil mulCommUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (udpCommUtil != null) {
            udpCommUtil.stop();
            udpCommUtil = null;
        }
        if (mulCommUtil != null) {
            mulCommUtil.stop();
            mulCommUtil = null;
        }
    }

    private void initViews(){
        toast = Toast.makeText(this,"", Toast.LENGTH_SHORT);

        ipEdt = findViewById(R.id.ac_main_ip_edt);
        portEdt = findViewById(R.id.ac_main_port_edt);
        msgEdt = findViewById(R.id.ac_main_msg_input_edt);

        msgLv = findViewById(R.id.ac_main_msg_lv);
        msgAdapter = new MsgAdapter(this,msgList);
        msgLv.setAdapter(msgAdapter);

        startBtn = findViewById(R.id.ac_main_start_btn);
        sendBtn = findViewById(R.id.ac_main_send_btn);
    }

    private void initListeners(){
        // 启动
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    isStart = true;
                    startBtn.setText(R.string.stop);
                    String ip_addr = ipEdt.getText().toString();
                    if ("".equals(ip_addr)) {
                        toast.setText(R.string.toast_ip_is_null);
                        toast.show();
                        return;
                    }
                    if (!Tools.isIP(ip_addr)) {
                        toast.setText(R.string.toast_is_not_ip_address);
                        toast.show();
                        return;
                    }

                    String portStr = portEdt.getText().toString();
                    if ("".equals(portStr)) {
                        toast.setText(R.string.toast_port_is_null);
                        toast.show();
                        return;
                    }
                    try {
                        int portNum = Integer.parseInt(portStr);
                        if (portNum <= 0 || portNum > 65535) {
                            toast.setText(R.string.toast_is_not_port);
                            toast.show();
                            return;
                        }
                    } catch (Exception e) {
                        toast.setText(R.string.toast_is_not_port);
                        toast.show();
                        return;
                    }
                    // ip address and port is ok
                    ipEdt.setEnabled(false);
                    portEdt.setEnabled(false);
                    msgEdt.setEnabled(true);
                    if (Tools.isMulcastAddress(ip_addr)) {
                        // 组播
                        toast.setText(R.string.toast_ip_is_multicast);
                        toast.show();
                        mulCommUtil = new MulCommUtil(ip_addr, Integer.parseInt(portStr), mulRecCallback);
                        return;
                    }
                    if (BROAD_CAST_ADDRESS.equals(ip_addr)) {
                        toast.setText(R.string.toast_ip_is_broadcast);
                        toast.show();
                    } else {
                        toast.setText(R.string.toast_msg_communication_start);
                        toast.show();
                    }
                    udpCommUtil = new UDPCommUtil(ip_addr, Integer.parseInt(portStr), udpRecCallback);

                } else {
                    isStart = false;
                    ipEdt.setEnabled(true);
                    portEdt.setEnabled(true);
                    msgEdt.setEnabled(false);
                    startBtn.setText(R.string.start);
                    if (udpCommUtil != null) {
                        udpCommUtil.stop();
                        udpCommUtil = null;
                    }
                    if (mulCommUtil != null) {
                        mulCommUtil.stop();
                        mulCommUtil = null;
                    }
                }
            }
        });
        // 发送
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStart) {
                    toast.setText(R.string.toast_msg_is_not_start);
                    toast.show();
                    return;
                }
                String sendMsg = msgEdt.getText().toString();
                if ("".equals(sendMsg)) {
                    toast.setText(R.string.toast_msg_is_null);
                    toast.show();
                    return;
                }
                String dst_ip = ipEdt.getText().toString();
                int port = Integer.parseInt(portEdt.getText().toString());
                MsgEntity entity = new MsgEntity(MsgEntity.TYPE_SEND, dst_ip, System.currentTimeMillis(), sendMsg);
                entity.setDst_port(port);
                if (udpCommUtil != null) {
                    udpCommUtil.sendMsg(entity);
                }
                if (mulCommUtil != null) {
                    mulCommUtil.sendMsg(entity);
                }
                msgList.add(entity);
                MLog.i(TAG, "send msg ; list size = "+ msgList.size() +" ; msg = " + entity.getMsg());
                msgAdapter.notifyDataSetChanged();
            }
        });

    }

    private UDPCommUtil.RecMsgCallback udpRecCallback = new UDPCommUtil.RecMsgCallback() {
        @Override
        public void recMsg(MsgEntity entity) {
            msgList.add(entity);
            MLog.i(TAG, "rec udp msg ; list size = "+ msgList.size() +" ; msg = " + entity.getMsg());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private MulCommUtil.RecMsgCallback mulRecCallback = new MulCommUtil.RecMsgCallback() {
        @Override
        public void recMsg(MsgEntity entity) {
            msgList.add(entity);
            MLog.i(TAG, "rec multicast msg ; list size = "+ msgList.size() +" ; msg = " + entity.getMsg());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    msgAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
