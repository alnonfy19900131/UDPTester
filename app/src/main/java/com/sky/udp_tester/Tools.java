package com.sky.udp_tester;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    /**
     * 数组ID转换为MAC地址字符串
     * @param macBytes
     * @return
     */
    public static String getBytesMac(byte[] macBytes){
        String value = "";
        for(int i = 0;i < macBytes.length; i++){
            String sTemp = Integer.toHexString(0xFF &  macBytes[i]);
            if(macBytes[i] <= 0x0F && macBytes[i] >= 0){
                value = value+"0"+sTemp+":";
            }else{
                value = value+sTemp+":";
            }
        }
        value = value.substring(0,value.lastIndexOf(":"));
        value = value.replace(":", "");
        return value;
    }

    /**
     * 检测是否是组播地址
     * @param address
     * @return
     */
    public static boolean isMulcastAddress (String address) {
        if (address == null) {
            return false;
        }
//        return address.matches("2((2[4-9])|(3\\d))(\\.(([01]\\d{2})|(2(([0-4]\\d)|(5[0-5]))))){3}");
        return address.matches("^(?:22[4-9]|23[0-9])" +
                "\\." +
                "(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])" +
                "\\." +
                "(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])" +
                "\\." +
                "(?:\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$");
    }

    /**
     * 获取本地IP地址
     * @return
     */
    public static String  getHostIP() {
        String hostIp = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }

    /**
     * 检测IP地址合法性
     * @param addr 输入IP地址
     * @return
     */
    public static boolean isIP(String addr) {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr))
        {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        boolean ipAddress = mat.find();
        //============对之前的ip判断的bug在进行判断
        if (ipAddress==true){
            String ips[] = addr.split("\\.");
            if(ips.length==4){
                try{
                    for(String ip : ips){
                        if(Integer.parseInt(ip)<0||Integer.parseInt(ip)>255){
                            return false;
                        }
                    }
                }catch (Exception e){
                    return false;
                }
                return true;
            }else{
                return false;
            }
        }
        return ipAddress;
    }
}
