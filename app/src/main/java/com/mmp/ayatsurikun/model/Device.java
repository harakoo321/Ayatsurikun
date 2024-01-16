package com.mmp.ayatsurikun.model;

public class Device {
    private final String id;
    private final String name;
    private final int port;
    private final ConnectionMethod connectionMethod;

    public Device(String id, String name, int port, ConnectionMethod connectionMethod) {
        this.id = id;
        this.name = name;
        this.port = port;
        this.connectionMethod = connectionMethod;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public ConnectionMethod getConnectionMethod() {
        return connectionMethod;
    }
}
