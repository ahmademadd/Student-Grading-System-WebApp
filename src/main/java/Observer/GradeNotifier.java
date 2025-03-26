package Observer;
import Socket.StudentClient;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

public class GradeNotifier {
    private final List<StudentClient> observers;
    private static GradeNotifier instance;

    private GradeNotifier() {
        observers = new Vector<>();
    }

    public List<StudentClient> getObservers() {
        return observers;
    }

    public static GradeNotifier getInstance() {
        if (instance == null) {
            instance = new GradeNotifier();
        }
        return instance;
    }

    public void addObserver(StudentClient observer) {
        observers.add(observer);
    }

    public void removeObserver(StudentClient observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String email, String updatedGrades) {
        for (StudentClient observer : observers) {
            if (observer.getEmail().equals(email)) {
                PrintWriter out = observer.getOut();
                if (out != null) {
                    out.println(updatedGrades);
                }
            }
        }

    }

}
