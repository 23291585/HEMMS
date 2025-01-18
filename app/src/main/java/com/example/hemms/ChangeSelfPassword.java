package com.example.hemms;
import static com.example.hemms.MSSQLConnection.isOldPasswordCorrect;
import static com.example.hemms.MSSQLConnection.updatePassword;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class ChangeSelfPassword extends AppCompatActivity {

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;

    private String userId; // Dinamik olarak belirlenecek

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_self_password);

        // UI element initialization
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        // Kullanıcının ID'sini alıyoruz
        userId = getCurrentUserId();
    }

    // Giriş yapan kullanıcının ID'sini döndüren metod
    private String getCurrentUserId() {
        // Bu metodu, giriş yapan kullanıcıyı almak için uygun bir şekilde yazmalısınız.
        // Örneğin, bir kullanıcı yönetim sisteminiz varsa:
        // return FirebaseAuth.getInstance().getCurrentUser().getUid();
        return "currentUserId";  // Test için sabit bir değer
    }

    // Şifreyi değiştir butonuna tıklanıldığında
    public void changePassword(View view) {
        // Şifre değiştirme işlemleri burada yapılacak
        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Eski şifreyi doğrula
        if (isOldPasswordCorrect(userId, oldPassword)) {
            // Yeni şifreleri karşılaştır
            if (newPassword.equals(confirmPassword)) {
                // Şifreyi güncelle
                boolean isUpdated = updatePassword(userId, newPassword);
                if (isUpdated) {
                    Toast.makeText(this, "Şifre başarıyla değiştirildi.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Şifre güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Yeni şifreler eşleşmiyor.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Eski şifre yanlış.", Toast.LENGTH_SHORT).show();
        }
    }
}

