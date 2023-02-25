import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class ConnectionEntity extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ConnectionEntity(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    public boolean equals(Object obj) {
        return (this == obj);
    }

    @Override
    public void run() {
        String word;


        try {
            for (String message: Server.messages) {
                this.send(message);
            }

            while (this.socket.isConnected()) {
                word = in.readLine();

                for (ConnectionEntity ce: Server.serverList) {
                    if (!ce.equals(this)) {
                        ce.send(word);
                    }
                }
                Server.messages.add(word);
            }
        } catch (IOException exception) {
            String error = "[!] Ошибка в " +
                    this.getClass().getName() +
                    "(" +
                    this.getHost() +
                    ":" +
                    this.getPort() +
                    ")" +
                    ": " +
                    exception;
            System.out.println(error);

        } finally {
            try {
                this.socket.close();
                Server.serverList.remove(this);
            } catch (Exception ignored) {
                ;
            }
        }
    }

    public String getHost() {
        return this.socket.getLocalAddress().toString();
    }

    public int getPort() {
        return this.socket.getPort();
    }

    public boolean is_connected() {
        return this.socket.isConnected();
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}