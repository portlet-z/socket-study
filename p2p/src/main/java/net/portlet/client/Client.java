package net.portlet.client;

import net.portlet.client.bean.ServerInfo;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 22:12
 */
public class Client {
    public static void main(String[] args) {
        ServerInfo info = ClientSearcher.searchServer(10000);
        System.out.println("Server:" + info);
        if (info != null) {
            try {
                TCPClient.linkWith(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
