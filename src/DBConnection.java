import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    public Connection connection;
    public static String selectSQL = "SELECT * FROM pasien";

    public static String insertSQL = "INSERT INTO pasien (nama, nik, tgl_lahir, alamat) VALUES (?, ?, ?, ?)";

    public static String updateSQL = "UPDATE pasien SET nama = ?, tgl_lahir = ?, alamat = ? WHERE nik = ?";

    public static String deleteSQL = "DELETE FROM pasien WHERE nik = ?";
    String host;
    String user;
    String pass;

    public DBConnection(){
        host = "jdbc:mysql://localhost/db_klinik";
        user = "root";
        pass = "";
        try{
            if (cekDriver()){
                connection = DriverManager.getConnection(host, user, pass);
                //System.out.println("Connection successfully to database established");
            }
        } catch (SQLException e){
            System.out.println("Connection failed " + e.getMessage());
        }
    }

    public final boolean cekDriver(){
        boolean isJDBC = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //System.out.println("Driver oke");
            isJDBC = true;
        } catch (ClassNotFoundException c){
            System.out.println("Driver not found");
        }
        return isJDBC;
    }
}