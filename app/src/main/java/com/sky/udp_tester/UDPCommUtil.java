package com.sky.udp_tester;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class UDPCommUtil {
    private static final String TAG = "comm_udp";
    // 在本地端口上
    private String dst_ip = "";
    private int port = -1;
    private UdpThread udpThread;
    private ArrayList<MsgEntity> msgList = new ArrayList<>();
    private Object lock = new Object();
    private RecMsgCallback mCallback;

    public UDPCommUtil(String ip, int port, RecMsgCallback callback){
        this.dst_ip = ip;
        this.port = port;
        try {
            udpThread = new UdpThread(port);
            udpThread.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.mCallback = callback;
    }

    public void stop(){
        if (udpThread != null) {
            udpThread.setStop();
            udpThread = null;
        }
    }

    public void sendMsg(MsgEntity entity){
        msgList.add(entity);
    }

    private class UdpThread extends Thread{
        private DatagramSocket mSocket;
        private boolean isRunning = false;
        public UdpThread(int port) throws SocketException {
            mSocket = new DatagramSocket(port);
            mSocket.setSoTimeout(500);
            isRunning = true;
        }

        public void setStop(){
            isRunning = false;
        }

        @Override
        public void run() {
            while(isRunning){
                // 先发送，后接收
                if (!msgList.isEmpty()) {
                    MsgEntity entity = msgList.remove(0);
                    DatagramPacket pac = new DatagramPacket(entity.getMsg().getBytes(), entity.getMsg().getBytes().length);

                    try {
//                        pac.setAddress(InetAddress.getByName(entity.getDst_ip()));
//                        pac.setPort(entity.getDst_port());
                        pac.setAddress(InetAddress.getByName(dst_ip));
                        pac.setPort(port);
                        mSocket.send(pac);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                byte[] buf = new byte[2048];
                DatagramPacket recPac = new DatagramPacket(buf, buf.length);
                try {
                    mSocket.receive(recPac);
                    if (Tools.getHostIP().equals(recPac.getAddress().getHostAddress())) {
                        continue;
                    }
                    byte[] real_data = new byte[recPac.getLength()];
                    System.arraycopy(recPac.getData(),0,real_data,0,recPac.getLength());
                    MsgEntity recEntity = new MsgEntity(MsgEntity.TYPE_RECEIVE, recPac.getAddress().getHostAddress(),System.currentTimeMillis(),new String(real_data));
                    recEntity.setDst_port(recPac.getPort());
                    if (mCallback != null) {
                        mCallback.recMsg(recEntity);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mSocket.close();
            mSocket = null;
            System.gc();
        }
    }

    public interface RecMsgCallback {
        void recMsg(MsgEntity entity);
    }
}
