package net.portlet.server;

import net.portlet.constants.TCPConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 21:32
 */
public class Server {
    public static void main(String[] args) throws IOException {
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
        boolean isSucceed = tcpServer.start();
        if (!isSucceed) {
            System.out.println("Start TCP server failed!");
            return;
        }

        ServerProvider.start(TCPConstants.PORT_SERVER);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do {
            str = bufferedReader.readLine();
            tcpServer.broadcast(str);
        } while ("00bye00".equalsIgnoreCase(str));

        UDPProvider.stop();
        tcpServer.stop();
    }
}
