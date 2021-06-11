package com.ziyear.rpc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zrpc")
public class ZRpcProperties {
    private String zookeepeer = "127.0.0.1:2181";
    private int port = 10345;
    private String ip = "127.0.0.1";

    public String getZookeepeer() {
        return zookeepeer;
    }

    public void setZookeepeer(String zookeepeer) {
        this.zookeepeer = zookeepeer;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
