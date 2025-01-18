package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserActivity extends AppCompatActivity {

    private TextView welcomeTextView; // Hoşgeldiniz mesajını gösterecek TextView
    private String userId; // Giriş yapan kullanıcının ID'si, UserSession'dan alınacak

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // Hoşgeldiniz mesajını gösterecek TextView'i buluyoruz
        welcomeTextView = findViewById(R.id.welcomeTextView);

        // UserSession'dan kullanıcı ID'sini alıyoruz
        userId = UserSession.getInstance(this).getUserId();

        // Eğer userId null değilse, veritabanından kullanıcı bilgilerini alıp ekranda gösteriyoruz
        if (userId != null) {
            getUserInfoAndDisplay(userId); // Veritabanından kullanıcı bilgilerini al
        } else {
            welcomeTextView.setText("Kullanıcı bilgileri bulunamadı.");
            Toast.makeText(this, "Kullanıcı oturumu hatalı", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Veritabanından kullanıcı bilgilerini alıp ekranda gösteren metod
    private void getUserInfoAndDisplay(String userId) {
        Connection connection = MSSQLConnection.getConnection();
        if (connection != null) {
            try {
                // Kullanıcı bilgilerini sorgula
                String query = "SELECT staff_title, staff_first_name, staff_last_name FROM Staff WHERE staff_id = '" + userId + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    String title = resultSet.getString("staff_title");
                    String firstName = resultSet.getString("staff_first_name");
                    String lastName = resultSet.getString("staff_last_name");

                    // Hoşgeldiniz mesajını oluşturuyoruz ve TextView'da gösteriyoruz
                    String welcomeMessage = "Hoşgeldiniz " + title + " " + firstName + " " + lastName;
                    welcomeTextView.setText(welcomeMessage);
                } else {
                    welcomeTextView.setText("Kullanıcı bilgileri bulunamadı.");
                    Toast.makeText(this, "Kullanıcı bilgileri bulunamadı", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                welcomeTextView.setText("Veritabanı hatası.");
                Toast.makeText(this, "Veritabanı hatası", Toast.LENGTH_SHORT).show();
            }
        } else {
            welcomeTextView.setText("Veritabanı bağlantısı sağlanamadı.");
            Toast.makeText(this, "Veritabanı bağlantısı sağlanamadı", Toast.LENGTH_SHORT).show();
        }
    }

    // Stok Güncelleme butonuna tıklanıldığında
    public void StokGuncelle(View view) {
        Intent intent = new Intent(UserActivity.this, SyncStore.class);
        startActivity(intent);
    }

    // Şifre Değiştir butonuna tıklanıldığında
    public void SifreDegistir(View view) {
        Intent intent = new Intent(UserActivity.this, ChangeSelfPassword.class);
        startActivity(intent);
    }
    public void exitApp(View view){
        finishAffinity();
    }
}
