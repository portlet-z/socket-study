package net.portlet.constants;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 21:29
 */
public class UDPConstants {
    /**
     *公共头部
     */
    public static byte[] HEADER = new byte[]{7,7,7,7,7,7,7,7};
    /**
     * 服务器固化 UDP 接收端口
     */
    public static int PORT_SERVER = 30201;
    /**
     * 客户端回送端口
     */
    public static int PORT_CLIENT_RESPONSE = 30202;
}
