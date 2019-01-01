package net.portlet.client.bean;

import lombok.Data;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 22:12
 */
@Data
public class ServerInfo {
    private int port;
    private String address;
    private String sn;

    public ServerInfo(int port, String address, String sn) {
        this.port = port;
        this.address = address;
        this.sn =sn;
    }

    @Override
    public String toString() {
        return "port:" + port + "\n" +
                "address:" + address + "\n" +
                "sn:" + sn;
    }
}
