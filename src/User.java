import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class User {
    public static String name;
    public static Client client;

    public static Thread inputMessageThread;
    public static Thread outputMessageThread;

    public static LinkedList<String> messages = new LinkedList<>();
    public static BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

    public static void clearConsole()
    {
        try
        {
            String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            ;
        }
    }

    public static void writeMessages() {
        for (String message: messages) {
            System.out.println(message);
        }
        System.out.println("\n");
        System.out.print(">> ");
    }
    public static String input(String placeholder) throws IOException {
        System.out.print(placeholder);
        return consoleReader.readLine();
    }

    public static Thread outputMessageThread() {
        Thread outputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String response;
                while (client.is_connected()) {
                    try {
                        response = client.receive();
                        messages.add(response);
                        clearConsole();
                        writeMessages();

                    } catch (IOException e) {
                        ;
                    }


                }
            }
        });
        return outputThread;
    }

    public static Thread inputMessageThread() {
        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (client.is_connected()) {
                    try {
                        String input = input("");
                        String message = "<" + name + "> " + input;
                        client.send(message);
                        messages.add(message);
                        clearConsole();
                        writeMessages();
                    } catch (IOException exception) {
                        ;
                    }
                }
            }
        });
        return inputThread;
    }


    public static void main(String[] args) {
        try {
            name = input("Имя -> ");
            if (name.equals("")) {
                throw new RuntimeException("Имя не может быть пустым");
            }

            String host = input("Хост -> ");
            client = new Client(host);
            client.connect();
            if (!client.is_connected()) {
                throw new IOException("Не удалось подключиться к серверу!");
            }
            System.out.println("[!] Вы успешно подключились к серверу!\nВводите свои сообщения в консоль:\n");
            writeMessages();
            inputMessageThread = inputMessageThread();
            outputMessageThread = outputMessageThread();
            inputMessageThread.start();
            outputMessageThread.start();


        } catch (IOException e) {
            try {
                client.disconnect();
            } catch (IOException ignored) {
                ;
            }

            System.out.print(e.toString());
        }

    }
}
