package com.sky.udp_tester;

import androidx.annotation.NonNull;

import java.net.NetworkInterface;
import java.util.Arrays;

public class InterfaceEntity {

    private int index;
    private String name;
    private String ip_addr;
    private String ip_v6_addr;
    private byte[] hardwareAddress;
    private int mtu;
    private NetworkInterface parentInterface;
    private boolean isloopback;
    private boolean isPointToPoint;
    private boolean isUp;
    private boolean isVirtual;
    private boolean supportMulticast;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp_addr() {
        return ip_addr;
    }

    public void setIp_addr(String ip_addr) {
        this.ip_addr = ip_addr;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIp_v6_addr() {
        return ip_v6_addr;
    }

    public void setIp_v6_addr(String ip_v6_addr) {
        this.ip_v6_addr = ip_v6_addr;
    }

    public byte[] getHardwareAddress() {
        return hardwareAddress;
    }

    public void setHardwareAddress(byte[] hardwareAddress) {
        this.hardwareAddress = hardwareAddress;
    }

    public int getMtu() {
        return mtu;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }

    public NetworkInterface getParentInterface() {
        return parentInterface;
    }

    public void setParentInterface(NetworkInterface parentInterface) {
        this.parentInterface = parentInterface;
    }

    public boolean isIsloopback() {
        return isloopback;
    }

    public void setIsloopback(boolean isloopback) {
        this.isloopback = isloopback;
    }

    public boolean isPointToPoint() {
        return isPointToPoint;
    }

    public void setPointToPoint(boolean pointToPoint) {
        isPointToPoint = pointToPoint;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(boolean virtual) {
        isVirtual = virtual;
    }

    public boolean isSupportMulticast() {
        return supportMulticast;
    }

    public void setSupportMulticast(boolean supportMulticast) {
        this.supportMulticast = supportMulticast;
    }

    @NonNull
    @Override
    public String toString() {
        String str = "IP V4 : " + ip_addr + "\n" +
                "IP V6 : " + ip_v6_addr + "\n" +
                "Hardware : " + (hardwareAddress == null ? "" : Tools.getBytesMac(hardwareAddress)) + "\n" +
                "MTU : " + mtu + "\n" +
                "isLoopback : " + isloopback + "\n" +
                "isPorintToPoint : " + isPointToPoint + "\n" +
                "isUp : " + isUp + "\n" +
                "isVirtual : " + isVirtual + "\n" +
                "supportMulticast : " + supportMulticast;
        return str;
    }
}
