package com.yuuko.commands.audio.handlers.lavalink.entity;

import java.net.URI;

public class LavalinkNode {
    private String address;
    private String password;

    public String getAddress() {
        return address;
    }

    public URI getURI() {
        return URI.create(address);
    }

    public String getPassword() {
        return password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
