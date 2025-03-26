package View.ClientSocket;
import java.io.*;
import java.net.*;

public class SocketClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient();
        socketClient.login();
        socketClient.listenForUpdates();
    }

    public SocketClient() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void login() {
        BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the Student Portal!");
        while (true) {
            try {
                // Get user email input
                System.out.print("Enter your student email: ");
                String email = consoleInput.readLine();
                // get user password
                System.out.print("Enter your password: ");
                String password = consoleInput.readLine();

                out.println(email);  // Send email
                out.println(password);  // Send password

                String response = in.readLine();
                if (response.equals("Successful Login!")) {
                    System.out.println(response);
                    break;
                } else
                    System.out.println(response);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void listenForUpdates() {
        try {
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("🔔 Grade notification: " + response);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}