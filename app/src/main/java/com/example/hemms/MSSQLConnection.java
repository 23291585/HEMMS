package com.example.hemms;

import android.os.StrictMode;
import android.util.Log;  // Log sınıfını ekleyin
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MSSQLConnection {

    // MSSQL bağlantı bilgileri
    private static final String IP = "94.54.32.242"; // Statik IP adresi
    private static final String PORT = "1433";      // MSSQL varsayılan portu
    private static final String DATABASE = "hemms_db"; // Veritabanı adı
    private static final String USERNAME = "sa";    // MSSQL kullanıcı adı
    private static final String PASSWORD = "Gulsah1m!"; // MSSQL şifresi

    // MSSQL bağlantısını başlatan metod
    public static Connection getConnection() {
        Connection connection = null;
        String connectionString;

        try {
            // Ağ işlemlerine izin ver (sadece test için)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Bağlantı dizesi
            connectionString = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + "/" + DATABASE + ";"
                    + "user=" + USERNAME + ";"
                    + "password=" + PASSWORD + ";"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;"
                    + "loginTimeout=30;";


            // Bağlantıyı başlat
            connection = DriverManager.getConnection(connectionString);

        } catch (Exception e) {
            // Hata durumunda istisna yazdır
            Log.e("MSSQLConnection", "Bağlantı hatası: ", e);  // Hata mesajını logla
        }

        return connection;
    }

    // Veritabanından veri çekmek için örnek bir metod
    public static void fetchData() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                String query = "SELECT * FROM Staff";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    // Tablo sütunlarından birini yazdır
                    Log.d("MSSQLConnection", "Veri: " + rs.getString("staff_first_name"));
                }
            }
        } catch (Exception e) {
            Log.e("MSSQLConnection", "Veri çekme hatası: ", e);  // Hata mesajını logla
        }
    }

    // Kullanıcı adı ve şifre doğrulama metodu
    public static int validateUser(String username, String password) {
        int isAdmin = -1; // -1, hatalı giriş durumunu temsil eder

        try (Connection connection = getConnection()) {
            if (connection != null) {
                // SQL sorgusu: Kullanıcı adı ve şifreye göre kullanıcıyı kontrol et
                String query = "SELECT is_admin FROM Staff WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username); // Kullanıcı adı parametresi
                preparedStatement.setString(2, password); // Şifre parametresi

                ResultSet resultSet = preparedStatement.executeQuery();

                // Eğer sonuç dönerse
                if (resultSet.next()) {
                    isAdmin = resultSet.getInt("is_admin"); // Admin bilgisini al
                }
            }
        } catch (Exception e) {
            Log.e("MSSQLConnection", "Kullanıcı doğrulama hatası: ", e);  // Hata mesajını logla
        }

        return isAdmin; // -1 hata, 0 normal kullanıcı, 1 admin
    }
    // Personel ekleme metodu
    public static boolean addEmployee(String firstName, String lastName, String userTitle,
                                      String userName, String userPW, String userMail,
                                      String userPhone, boolean isAdmin) {
        boolean isSuccess = false;

        try (Connection connection = getConnection()) {
            if (connection != null) {
                // SQL sorgusu: Personel bilgilerini ekle
                String query = "INSERT INTO Staff (username, password, staff_first_name, staff_last_name, " +
                        "staff_email, staff_tel_no, staff_title, is_admin) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Parametreleri ayarla
                preparedStatement.setString(1, userName);  // Kullanıcı adı
                preparedStatement.setString(2, userPW);    // Şifre
                preparedStatement.setString(3, firstName); // Personel adı
                preparedStatement.setString(4, lastName);  // Personel soyadı
                preparedStatement.setString(5, userMail);  // E-posta
                preparedStatement.setString(6, userPhone); // Telefon numarası
                preparedStatement.setString(7, userTitle); // Personel unvanı
                preparedStatement.setBoolean(8, isAdmin);  // Admin durumu (true/false)

                // Sorguyu çalıştır
                int rowsAffected = preparedStatement.executeUpdate();

                // Eğer veri başarıyla eklendiyse, işlem başarılı oldu
                isSuccess = rowsAffected > 0;
            }
        } catch (Exception e) {
            Log.e("MSSQLConnection", "Personel ekleme hatası: ", e);  // Hata mesajını logla
        }

        return isSuccess;  // true veya false döndür
    }
    public static boolean updatePassword(String firstName, String lastName, String newPassword) {
        boolean isSuccess = false;

        try (Connection connection = getConnection()) {
            if (connection != null) {
                // SQL sorgusu: Kullanıcı adı ve soyadı ile şifreyi güncelle
                String query = "UPDATE Staff SET password = ? WHERE staff_first_name = ? AND staff_last_name = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Parametreleri ayarla
                preparedStatement.setString(1, newPassword);  // Yeni şifre
                preparedStatement.setString(2, firstName);    // Personel adı
                preparedStatement.setString(3, lastName);     // Personel soyadı

                // Sorguyu çalıştır
                int rowsAffected = preparedStatement.executeUpdate();

                // Eğer veri başarıyla güncellendiyse, işlem başarılı oldu
                isSuccess = rowsAffected > 0;
            }
        } catch (Exception e) {
            Log.e("MSSQLConnection", "Şifre güncelleme hatası: ", e);  // Hata mesajını logla
        }

        return isSuccess;  // true veya false döndür
    }
    public static List<Staff> getStaffList() {
        List<Staff> staffList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            if (connection != null) {
                // SQL sorgusu: Personel bilgilerini al
                String query = "SELECT staff_first_name, staff_last_name, staff_title FROM Staff";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Sonuçları listeye ekle
                while (resultSet.next()) {
                    String firstName = resultSet.getString("staff_first_name");
                    String lastName = resultSet.getString("staff_last_name");
                    String title = resultSet.getString("staff_title");

                    // Yeni Staff nesnesi oluştur ve listeye ekle
                    Staff staff = new Staff(firstName, lastName, title);
                    staffList.add(staff);
                }
            }
        } catch (Exception e) {
            Log.e("MSSQLConnection", "Personel listesi alma hatası: ", e);  // Hata mesajını logla
        }

        return staffList;
    }
    public static List<Item> getItemsList() {
        List<Item> itemList = new ArrayList<>();

        try (Connection connection = getConnection()) {
            if (connection != null) {
                String query = "SELECT c.room_id, c.item_id, c.quantity, c.expiration_date, " +
                        "i.item_name, i.item_category, i.item_required_quantity " +
                        "FROM Cabinets c JOIN Items i ON c.item_id = i.item_id";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int itemId = resultSet.getInt("item_id");
                    String itemName = resultSet.getString("item_name");
                    String itemCategory = resultSet.getString("item_category");
                    int itemRequiredQuantity = resultSet.getInt("item_required_quantity");
                    int quantity = resultSet.getInt("quantity");
                    String expirationDate = resultSet.getString("expiration_date");
                    int roomId = resultSet.getInt("room_id");

                    // Item nesnesini oluştur
                    Item item = new Item(itemId, itemName, itemCategory, itemRequiredQuantity, quantity, expirationDate, roomId);
                    itemList.add(item);
                }
            }
        } catch (Exception e) {
            Log.e("MSSQLConnection", "İlaç listesi alma hatası: ", e);
        }

        return itemList;
    }


}
