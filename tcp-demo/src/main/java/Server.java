import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangxinzheng
 * @date 2019-01-01 18:05
 */
public class Server {
    private static final int PORT = 20000;

    public static void main(String[] args) throws Exception{
        ServerSocket server = createServerSocket();
        initServerSocket(server);
        server.bind(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 50);
        System.out.println("服务器准本就绪");
        System.out.println("服务器信息：" + server.getInetAddress() + " Port:" + server.getLocalPort());

        //等待客户端连接
        for (;;) {
            //得到客户端
            Socket client = server.accept();
            //客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            //启动线程
            clientHandler.start();
        }
    }

    private static ServerSocket createServerSocket() throws IOException {
        //创建基础的 ServerSocket
        ServerSocket serverSocket = new ServerSocket();
        //绑定到本地端口上
        //serverSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 50);
        //绑定到本地端口20000上，并且设置当前可允许等待链接的队列为50
        //serverSocket = new ServerSocket(PORT);
        //等效于上面的方案，队列设置为50个
        //serverSocket = new ServerSocket(PORT, 50);
        //与上面相同
        //serverSocket = new ServerSocket(PORT, 50, Inet4Address.getLocalHost());
        return serverSocket;

    }

    private static void initServerSocket(ServerSocket serverSocket) throws IOException {
        //是否复用未完全关闭的地址端口
        serverSocket.setReuseAddress(true);
        //等效 Socket#setReceiveBufferSize
        serverSocket.setReceiveBufferSize(64 * 1024 * 1024);
        //设置 serverSocket#accept 超时时间
        //serverSocket.setSoTimeout(2000);
        //设置性能参数：短链接，延迟，带宽的相对重要性
        serverSocket.setPerformancePreferences(1, 1, 1);
    }

    /**
     * 客户端消息处理
     */
    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("新客户端连接：" + socket.getInetAddress() + " Port:" + socket.getPort());
            try {
                //得到打印流，用于数据输出；服务器回送数据使用
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                //得到输入流，用于接受数据
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (true) {
                    //客户端拿到一条数据
                    String line = socketInput.readLine();
                    if ("exit".equals(line)) {
                        socketOutput.println("exit");
                        break;
                    } else {
                        //打印到屏幕，并回送数据长度
                        System.out.println(line);
                        socketOutput.println("回送：" + line.length());
                    }
                }
            } catch (Exception e) {
                System.err.println("链接异常断开");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("客户端已关闭");
        }
    }
}
