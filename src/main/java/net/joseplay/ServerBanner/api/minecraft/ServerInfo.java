package net.joseplay.ServerBanner.api.minecraft;

import com.google.gson.JsonArray;

import java.awt.image.BufferedImage;
import java.lang.reflect.Array;

public class ServerInfo {
    private String ip;
    private JsonArray motd;
    private int onlinePlayers;
    private int maxPlayers;
    private long latency;
    private byte[] icon;
    private JsonArray playerList;

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public JsonArray getMotd() { return motd; }
    public void setMotd(JsonArray motd) { this.motd = motd; }

    public int getOnlinePlayers() { return onlinePlayers; }
    public void setOnlinePlayers(int onlinePlayers) { this.onlinePlayers = onlinePlayers; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public long getLatency() { return latency; }
    public void setLatency(long latency) { this.latency = latency; }

    public byte[] getIcon() { return icon; }
    public void setIcon(byte[] icon) { this.icon = icon; }

    public JsonArray getPlayerList() { return playerList; }
    public void setPlayerList(JsonArray playerList) { this.playerList = playerList; }
}
