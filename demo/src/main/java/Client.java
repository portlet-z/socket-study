import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhangxinzheng
 * @date 2018-12-25
 */
public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        //读取流的超时时间
        socket.setSoTimeout(3000);
        //连接本地，端口2000；超时时间3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        System.out.println("已发起服务器连接，并进入后续流程");
        System.out.println("客户端信息:" + socket.getLocalAddress() + " port: " + socket.getLocalPort());
        System.out.println("服务端信息:" + socket.getInetAddress() + " port: " + socket.getPort());

        try {
            // 发送数据
            todo(socket);
        } catch (Exception e) {
            System.err.println("异常关闭");
        }

        //资源释放
        socket.close();
        System.out.println("客户端已退出");
    }

    private static void todo(Socket client) throws Exception {
        //构建键盘输入流
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        //得到 Socket 输出流，并转为打印流
        OutputStream outputStream = client.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        //得到 Socket 输入流
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        do {
            //键盘读取一行
            String line = input.readLine();
            //发送数据到服务器
            printStream.println(line);

            String echo = socketBufferedReader.readLine();
            if ("exit".equals(echo)) {
                break;
            } else {
                System.out.println(echo);
            }
        } while (true);


        printStream.close();
        socketBufferedReader.close();
    }
}
