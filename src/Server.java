import java.net.*;
import java.io.*;
import java.util.LinkedList;

public class Server {

    public static final String HOST = "0.0.0.0";
    public static final int PORT = 8080;
    public static final int MAX_CLIENTS = 5;
    public static LinkedList<ConnectionEntity> serverList = new LinkedList<>();

    public static LinkedList<String> messages = new LinkedList<>();

    // Вот я не знаю почему, но не работает
    public static void disconnectListener() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (ConnectionEntity ce: serverList) {
                        if (!ce.is_connected()) {
                            System.out.println("[!] Клиент отключен: " + ce.getHost() + ":" + ce.getPort());
                        }
                    }
                }
            }
        });
        thread.start();
    }
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        // SocketAddress addr = new InetSocketAddress(HOST, PORT);
        //server.bind(addr);

        System.out.println("[!] Сервер запущен: " + HOST + ":" + PORT);
        disconnectListener();
        while ((MAX_CLIENTS == 0) || (serverList.toArray().length <= MAX_CLIENTS)) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ConnectionEntity(socket));
                    System.out.println("[+] Новое подключение: " + socket);
                } catch (IOException e) {
                    System.out.println("[!] Ошибка сервера: " + e);
                    socket.close();
                }
            }
    }
}