package net.portlet.server;

import net.portlet.constants.TCPConstants;

import java.io.IOException;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 21:32
 */
public class Server {
    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
        boolean isSucceed = tcpServer.start();
        if (!isSucceed) {
            System.out.println("Start TCP server failed!");
            return;
        }

        ServerProvider.start(TCPConstants.PORT_SERVER);
        try {
            //noinspection ResultOfMethodCallIgnore
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UDPProvider.stop();
        tcpServer.stop();
    }
}
