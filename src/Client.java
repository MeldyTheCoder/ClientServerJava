import java.io.*;
import java.net.Socket;

public class Client {

    private String HOST;
    private int PORT;

    private final String DEFAULT_HOST = "0.0.0.0";

    private final int DEFAULT_PORT = 8080;

    private Socket clientSocket;

    private BufferedReader socketIn;
    private BufferedWriter socketOut;

    public Client(String host, int port) {
        this.HOST = host;
        this.PORT = port;
    }

    public Client(int port) {
        this.PORT = port;
        this.HOST = DEFAULT_HOST;
    }

    public Client(String host) {
        this.HOST = host;
        this.PORT = DEFAULT_PORT;
    }
    public Client() {
        this.HOST = DEFAULT_HOST;
        this.PORT = DEFAULT_PORT;
    }

    public void connect() throws IOException {
        if (!this.is_connected()) {
            this.clientSocket = new Socket(this.HOST, this.PORT);
            this.socketIn = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            this.socketOut = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
    }

    public boolean is_connected() {
        if (this.clientSocket == null) {
            return false;
        }

        return this.clientSocket.isConnected();

    }

    public void send(String message) throws IOException {
        if (!(message == null || message.equals(""))) {
            this.socketOut.write(message + "\n");
            this.socketOut.flush();
        }
    }

    public String receive() throws IOException {
        return this.socketIn.readLine();
    }

    public void disconnect() throws IOException {
        this.clientSocket.close();
        this.socketIn.close();
        this.socketOut.close();

    }


}