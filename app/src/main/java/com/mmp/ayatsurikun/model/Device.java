package com.mmp.ayatsurikun.model;

public class Device {
    private final String id;
    private final String name;
    private final int port;
    private final ConnectionType connectionType;

    public Device(String id, String name, int port, ConnectionType connectionType) {
        this.id = id;
        this.name = name;
        this.port = port;
        this.connectionType = connectionType;
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

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device that = (Device) o;
        return id.equals(that.id) &&
                name.equals(that.name) &&
                port == that.port &&
                connectionType == that.connectionType;
    }
}
