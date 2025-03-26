package Socket;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class StudentClient {
    private final BufferedReader in;
    private final PrintWriter out;
    private final String email;

    StudentClient(String email, BufferedReader in, PrintWriter out) {
        this.email = email;
        this.in = in;
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public String getEmail() {
        return email;
    }
}
