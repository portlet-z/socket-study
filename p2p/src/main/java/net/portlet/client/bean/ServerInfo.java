package net.portlet.client.bean;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 22:12
 */
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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
