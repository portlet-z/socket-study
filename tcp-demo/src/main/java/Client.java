import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

/**
 * @author zhangxinzheng
 * @date 2018-12-31 22:57
 */
public class Client {
    private static final int PORT = 20000;
    private static final int LOCAL_PORT = 20001;
    public static void main(String[] args) throws IOException {
        Socket socket = createSocket();
        initSocket(socket);
        //链接到本地20000端口，超时时间3秒，超过则抛出超时异常
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 3000);
        System.out.println("已发起服务器链接，并进入后续流程");
        System.out.println("客户端信息：" + socket.getLocalAddress() + " Port:" + socket.getLocalPort());
        System.out.println("服务器端信息：" + socket.getInetAddress() + " Port:" + socket.getPort());

        try {
            //发送接受数据
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        //释放资源
        socket.close();
        System.out.println("客户端已退出");
    }

    private static Socket createSocket() throws IOException{
        /*
        //无代理模式，等效于空构造函数
        Socket socket = new Socket(Proxy.NO_PROXY);
        //新建一份具有 HTTP 代理的套接字，传输数据将通过 www.baidu.com:8080端口转发
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Inet4Address.getByName("www.baidu.com"), 8080));
        socket = new Socket(proxy);
        //新建一个套接字，并且直接链接到本地20000的服务器上
        socket = new Socket("localhost", PORT);
        //新建一个套接字，并且直接链接到本地20000的服务器上
        socket = new Socket(Inet4Address.getLocalHost(), PORT);
        //新建一个套接字，并且直接链接到本地20000的服务器上，并且绑定到本地20001端口上
        socket = new Socket("localhost", PORT, Inet4Address.getLocalHost(), LOCAL_PORT);
        socket = new Socket(Inet4Address.getLocalHost(), PORT, Inet4Address.getLocalHost(), LOCAL_PORT);
        */
        Socket socket = new Socket();
        socket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), LOCAL_PORT));
        return socket;
    }

    private static void initSocket(Socket socket) throws SocketException {
        //设置读取超时时间为2秒
        socket.setSoTimeout(2000);
        //是否复用未完全关闭的 Socket 地址，对于指定 bind 操作后的套接字有效
        socket.setReuseAddress(true);
        //是否开启 Nagle 算法
        socket.setTcpNoDelay(true);
        //是否需要在长时无数据响应时发送确认数据（类似心跳包），时间大约为2小时
        socket.setKeepAlive(true);
        //对于 close 关闭操作行为进行怎样的处理；默认为 false, 0
        //false 0:默认情况，关闭立即返回，底层系统接管输出流，将缓冲区内的数据发送完成
        //true 0:关闭时立即返回，缓冲区数据抛弃，直接发送 RST 结束命令到对方，并无需经过2MSL 等待
        //true 200:关闭时最长阻塞200ms,随后按第二情况处理
        socket.setSoLinger(true, 200);
        //是否让紧急数据内敛，默认 false,紧急数据通过 socket.sendUrgentData(1);发送
        socket.setOOBInline(true);
        //设置接收发送缓冲器大小
        socket.setReceiveBufferSize(64 * 1024 * 1024);
        socket.setSendBufferSize(64 * 1024 * 1024);
        //设置性能参数：短链接，延迟，带宽的相对重要性
        socket.setPerformancePreferences(1,1,1);
    }

    private static void todo(Socket client) throws IOException {
        //得到 Socket 数据流，并转换为打印流
        OutputStream outputStream = client.getOutputStream();

        //得到 Socket 输入流
        InputStream inputStream = client.getInputStream();
        byte[] buffer = new byte[128];
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        //byte
        byteBuffer.put((byte) 126);
        //char
        char c = 'a';
        byteBuffer.putChar(c);
        //int
        int i = 2323123;
        byteBuffer.putInt(i);
        // bool
        boolean b = true;
        byteBuffer.put(b ? (byte) 1 : (byte) 0);
        // long
        long l = 3424325453432312321L;
        byteBuffer.putLong(l);
        //float
        float f = 12.1234f;
        byteBuffer.putFloat(f);
        //double
        double d = 432.134242345;
        byteBuffer.putDouble(d);
        //String
        String str = "hello world";
        byteBuffer.put(str.getBytes());

        //发送数据到服务器
        outputStream.write(buffer, 0, byteBuffer.position() + 1);
        int read = inputStream.read(buffer);
        System.out.println("收到数量：" + read);

        outputStream.close();
        inputStream.close();
    }
}
