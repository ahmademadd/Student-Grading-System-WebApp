package Controllers.Observer;
import Controllers.DAO.MySQLDAO;
import Models.StudentClient;
import java.util.HashMap;
import java.util.Map;

public class DatabaseWatcher extends Thread {
    private volatile GradeNotifier notifier;
    private final MySQLDAO mySQLDAO;
    private final Map<String, String> lastGrades = new HashMap<>();

    public DatabaseWatcher() {
        mySQLDAO = new MySQLDAO();
        notifier = GradeNotifier.getInstance();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000); // Check for updates every 5 seconds
                checkForGradeUpdates();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void checkForGradeUpdates() {
        for (StudentClient studentClient : notifier.getObservers()) {
            String grades = mySQLDAO.getGrades(studentClient.getEmail());
            if (!grades.equals(lastGrades.get(studentClient.getEmail()))){
                notifier.notifyObservers(studentClient.getEmail(), grades);
                lastGrades.put(studentClient.getEmail(),grades);
            }
        }
    }
}
