package com.sky.udp_tester;

public class MsgEntity {

    public static final String TYPE_SEND = "send";
    public static final String TYPE_RECEIVE = "receive";

    private String type;
    private String dst_ip;
    private int dst_port;
    private long date;
    private String msg;

    public MsgEntity(String type, String dst_ip,long date, String msg) {
        this.type = type;
        this.dst_ip = dst_ip;
        this.date = date;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDst_ip() {
        return dst_ip;
    }

    public void setDst_ip(String dst_ip) {
        this.dst_ip = dst_ip;
    }

    public int getDst_port() {
        return dst_port;
    }

    public void setDst_port(int dst_port) {
        this.dst_port = dst_port;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
