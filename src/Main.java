import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        Library lib = new Library();
        lib.createTable();
        lib.available();
        lib.add("Unexpected");
        lib.add("Rich Dad Poor Dad");
        lib.add("Atomic Habits");
        lib.available();
        System.out.println(lib.search("Unexpected"));
    }
}
class DB{
    static Connection connect() throws Exception{
        return DriverManager.getConnection("jdbc:sqlite:books.db");
    }
}
class Library{
    void createTable() {
        try (Connection conn = DB.connect();
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT,
                    status INTEGER
                )
                """;
            stmt.execute(sql);
            System.out.println("Table Created/Ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void add(String name) {
        String sql = "INSERT INTO books(name, status) VALUES (?, ?)";
        try (Connection conn = DB.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, 1);
            ps.executeUpdate();
            System.out.println(name + " Added Successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void available() {
        String sql = "SELECT * FROM books WHERE status = 1";
        try (Connection conn = DB.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Available books:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    int search(String search) {
        String sql = "SELECT * FROM books WHERE name = ?";
        try (Connection conn = DB.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, search);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Available books:");
            if(rs.next()){
                
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}