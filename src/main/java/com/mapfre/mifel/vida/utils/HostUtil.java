package com.mapfre.mifel.vida.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

public class HostUtil {
    private InetAddress ip;
    private String hostname;
    private String address;

    public HostUtil() {
        Map<String,String> data = this.getAddressData();
        this.hostname= data.get("HOSTNAME");
        this.address= data.get("ADDRESS");
    }

    public String getAddress() {
        return address;
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, String> getAddressData(){
        Map<String,String> data= new LinkedHashMap<>();
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            address=ip.getHostAddress();
            data.put("HOSTNAME", hostname);
            data.put("ADDRESS", address);

            /*System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            System.out.println("Your current Addres : " + address);*/

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return data;
    }
}
