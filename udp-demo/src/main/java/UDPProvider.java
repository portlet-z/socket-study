import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * UDP 提供者，用于提供服务
 * @author zhangxinzheng
 * @date 2018-12-28
 */
public class UDPProvider {
    public static void main(String[] args) throws IOException {
        System.out.println("UDPProvider Started.");

        //作为接收者，指定一个端口用于数据接受
        DatagramSocket ds = new DatagramSocket(20000);

        //构建接收实体
        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

        //接收
        ds.receive(receivePack);

        //打印接收到的信息与发送者的信息
        //发送者的 IP 地址
        String ip = receivePack.getAddress().getHostAddress();
        int port = receivePack.getPort();
        int dataLength = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLength);
        System.out.println("UDPProvider receive from ip:" + ip
        +"\tport:" + port + "\tdata:" + data);

        //构建一份回送数据
        String responseData = "Receive data with length:" + dataLength;
        byte[] responseDataBytes = responseData.getBytes();
        // 直接根据发送者构建一份回送消息
        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes,
                responseDataBytes.length, receivePack.getAddress(), receivePack.getPort());
        ds.send(responsePacket);
        //完成
        System.out.println("UDPProvider Finished");
        ds.close();
    }
}
