package Main;
import DAO.MySQLDAO;

public class Main {

    public static void main(String[] args) {
        MySQLDAO mySQLDAO = new MySQLDAO();
        mySQLDAO.gradeStudent("ahmad@gmail.com", 1,64.3);
        mySQLDAO.gradeStudent("ahmad@gmail.com", 2,47.5);
    }
}
