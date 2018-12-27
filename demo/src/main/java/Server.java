import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangxinzheng
 * @date 2018-12-25
 */
public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(2000);

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息：" + serverSocket.getInetAddress() + " port: " + serverSocket.getLocalPort());

        //等待客户端链接
        for (;;) {
            //得到客户端
            Socket client = serverSocket.accept();
            //客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            //启动线程
            clientHandler.start();
        }
    }

    /**
     *
     */
    private static class ClientHandler extends Thread {
        private Socket socket;
        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("新客户端链接：" + socket.getInetAddress() + " port:" + socket.getPort());
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
