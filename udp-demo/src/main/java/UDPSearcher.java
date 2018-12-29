import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * UDP 搜索者，用于搜索服务支持方
 * @author zhangxinzheng
 * @date 2018-12-28
 */
public class UDPSearcher {

    private static final int LISTEN_PORT = 30000;

    public static void main(String[] args) throws Exception {
        System.out.println("UDPSearcher Started.");
        Listener listener = listen();
        sendBroadcast();
        System.in.read();
        List<Device> devices = listener.getDevicesAndClose();
        for (Device device : devices) {
            System.out.println("Device:" + device.toString());
        }
        System.out.println("UDPSearcher Finished.");
    }

    private static Listener listen() throws InterruptedException {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();
        countDownLatch.await();
        return listener;
    }

    /**
     * 发送广播
     */
    private static void sendBroadcast() throws IOException{
        System.out.println("UDPSearcher sendBroadcast Started.");

        //作为搜索方，让系统自动分配端口
        DatagramSocket ds = new DatagramSocket();

        //构建一份回送数据
        String requestData = MessageCreator.buildWithPort(LISTEN_PORT);
        byte[] requestDataBytes = requestData.getBytes();
        // 直接根据发送者构建一份回送消息
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes,
                requestDataBytes.length);
        requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        requestPacket.setPort(20000);
        ds.send(requestPacket);
        ds.close();

        //完成
        System.out.println("UDPSearcher Finished");
    }

    private static class Device{
        final int port;
        final String ip;
        final String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    private static class Listener extends Thread{
        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> devices = new ArrayList<>();
        private boolean done = false;
        private DatagramSocket ds = null;

        public Listener(int listenPort, CountDownLatch countDownLatch) {
            super();
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            //通知已启动
            countDownLatch.countDown();
            try {
                //监听端口
                ds = new DatagramSocket(listenPort);
                while (!done) {
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
                    System.out.println("UDPSearcher receive from ip:" + ip
                    +"\tport:" + port + "\tdata:" + data);
                    String sn = MessageCreator.parseSn(data);
                    if (null != sn) {
                        Device device = new Device(port, ip, sn);
                        devices.add(device);
                    }
                }
            } catch (Exception e) {

            } finally {
                close();
            }
            System.out.println("UDPSearcher listener finished");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        List<Device> getDevicesAndClose() {
            done = true;
            close();
            return devices;
        }
    }
}
