package com.ssh.sm2_update.bean;

public class DBTable {

    private String name;

    private long autoIncrement = 1;

    public DBTable() {
    }

    public DBTable(String name, long autoIncrement) {
        this.name = name;
        if (autoIncrement > 1) {
            this.autoIncrement = autoIncrement;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }
}
