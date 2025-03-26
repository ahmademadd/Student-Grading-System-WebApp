package DAO;
import Observer.GradeNotifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class MySQLDAO {
    private final Connection con;
    private final StudentDAO studentDAO;

    public MySQLDAO() {
        this.con = DatabaseConnection.getConnection();
        this.studentDAO = new StudentDAO();
    }

    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }

    public boolean registerStudent(String name, String email, String password) {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO Students (name, email, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean validateLogin(String email, String password) {
        String sql = "SELECT password_hash FROM Students WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password_hash");
                return checkPassword(password, hashedPassword);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getGrades(String email) {
        return studentDAO.getGrades(email);
    }

    public boolean gradeStudent(String email, int courseID, Double grade) {
        boolean isGraded = studentDAO.gradeStudent(email, courseID, grade);
        GradeNotifier.getInstance().notifyObservers(email, getGrades(email));
        return isGraded;
    }
}