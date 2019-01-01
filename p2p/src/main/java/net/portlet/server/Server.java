package net.portlet.server;

import net.portlet.constants.TCPConstants;

import java.io.IOException;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 21:32
 */
public class Server {
    public static void main(String[] args) {
        ServerProvider.start(TCPConstants.PORT_SERVER);
        try {
            //noinspection ResultOfMethodCallIgnore
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerProvider.stop();
    }
}
