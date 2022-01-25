package br.com.leodevelb.crawlerts.utils;

import java.net.InetAddress;

public class Ping {

    public static boolean ping(String host) {
        try {
            InetAddress inet = InetAddress.getByName(host);
            return inet.isReachable(5000);
        } catch (Exception ex) {
            return false;
        }
    }

}
