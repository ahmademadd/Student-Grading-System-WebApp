package Socket;
import DAO.MySQLDAO;
import Observer.DatabaseWatcher;
import Observer.GradeNotifier;
import java.io.*;
import java.net.*;

public class SocketServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            DatabaseWatcher databaseWatcher = new DatabaseWatcher();
            databaseWatcher.start();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                // Handle client in a new thread
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Thread to handle each client separately
    static class ClientHandler extends Thread {
        private final GradeNotifier gradeNotifier = GradeNotifier.getInstance();
        private MySQLDAO sqldao = new MySQLDAO();
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String email = in.readLine();
                    String password = in.readLine();
                    if (sqldao.validateLogin(email, password)) {
                        out.println("Successful Login!");
                        gradeNotifier.addObserver(new StudentClient(email, in, out));
                        break;
                    } else
                        out.println("Wrong email or password");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}