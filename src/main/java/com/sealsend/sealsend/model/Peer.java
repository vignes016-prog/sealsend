package com.sealsend.sealsend.model;

import java.util.UUID;

public class Peer {

    private final String id;
    private final String name;
    private final String ip;
    private final int port;

    public Peer(String name, String ip, int port) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getIp() { return ip; }
    public int getPort() { return port; }
}