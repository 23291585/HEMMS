package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChangePassword extends AppCompatActivity {
    EditText firstNameText, lastNameText, userPWText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        // findViewById() işlemi, setContentView() çağrısından sonra yapılmalı
        firstNameText = findViewById(R.id.firstNameText);
        lastNameText = findViewById(R.id.lastNameText);
        userPWText = findViewById(R.id.userPWText);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Back(View view) {
        Intent intent = new Intent(ChangePassword.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    public void clearTable(View view) {
        firstNameText.setText("");
        lastNameText.setText("");
        userPWText.setText("");
    }
    public void clearTable() {
        firstNameText.setText("");
        lastNameText.setText("");
        userPWText.setText("");
    }

    public void updatePasswordInDatabase(View view) {
        // Formdaki verileri al
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String newPassword = userPWText.getText().toString();  // Yeni şifre

        // Şifreyi güncelle
        boolean success = MSSQLConnection.updatePassword(firstName, lastName, newPassword);

        // Sonuç hakkında kullanıcıya bilgi ver
        if (success) {
            Toast.makeText(this, "Şifre başarıyla güncellendi.", Toast.LENGTH_SHORT).show();
            clearTable();
        } else {
            Toast.makeText(this, "Şifre güncelleme hatası.", Toast.LENGTH_SHORT).show();
        }
    }
}
