package DAO;
import Models.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private final Connection con;

    public StudentDAO() {
        this.con = DatabaseConnection.getConnection();
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(new Student(rs.getInt("student_id"), rs.getString("name"), rs.getString("email")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(rs.getInt("student_id"), rs.getString("name"), rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean gradeStudent(String email, int courseId, Double grade) {
        String sql = "INSERT INTO Enrollments (student_id, course_id, grade) " +
                "VALUES ((SELECT student_id from Students WHERE email = ?), ?, ?) " +
                "ON DUPLICATE KEY UPDATE grade = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, courseId);
            stmt.setDouble(3, grade);
            stmt.setDouble(4, grade);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String getGrades(String email) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String sql = "SELECT c.course_name, e.grade FROM Enrollments e " +
                    "JOIN Courses c ON e.course_id = c.course_id " +
                    "WHERE e.student_id = (SELECT student_id FROM Students WHERE email = ?)";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                stringBuilder.append("Course: ").append(rs.getString("course_name")).append(" | Grade: ").append(rs.getDouble("grade")).append("\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return stringBuilder.isEmpty() ? "No grades found" : stringBuilder.append("------------------------------------").toString();
    }
}
