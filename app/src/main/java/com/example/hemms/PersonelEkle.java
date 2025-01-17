package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PersonelEkle extends AppCompatActivity {
    EditText firstName, lastName, userTitle, userName, userPW, userMail, userPhone;
    CheckBox isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_ekle); // setContentView önce çağrılmalı

        // UI öğelerini bağla
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        userTitle = findViewById(R.id.userTitle);
        userName = findViewById(R.id.userName);
        userPW = findViewById(R.id.userPW);
        userMail = findViewById(R.id.userMail);
        userPhone = findViewById(R.id.userPhone);
        isAdmin = findViewById(R.id.isAdmin); // CheckBox

        // EdgeToEdge ayarlarını koruyarak
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Back(View view) {
        Intent intent = new Intent(PersonelEkle.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    public void exitApp(View view) {
        finishAffinity();
    }

    // Personel ekleme fonksiyonu
    public void addEmployeeToDatabase(View view) {
        // Formdaki verileri al
        String firstNameText = firstName.getText().toString();
        String lastNameText = lastName.getText().toString();
        String userTitleText = userTitle.getText().toString();
        String userNameText = userName.getText().toString();
        String userPWText = userPW.getText().toString();
        String userMailText = userMail.getText().toString();
        String userPhoneText = userPhone.getText().toString();

        // Admin olup olmadığını kontrol et
        boolean isAdminValue = isAdmin.isChecked();  // CheckBox'ın işaretli olup olmadığını kontrol et

        // Personeli veritabanına ekle
        boolean success = MSSQLConnection.addEmployee(firstNameText, lastNameText, userTitleText,
                userNameText, userPWText, userMailText,
                userPhoneText, isAdminValue);

        // Sonuç hakkında kullanıcıya bilgi ver
        if (success) {
            Toast.makeText(this, "Personel başarıyla eklendi.", Toast.LENGTH_SHORT).show();
            clearTable();
        } else {
            Toast.makeText(this, "Personel ekleme hatası.", Toast.LENGTH_SHORT).show();
        }
    }
    public void clearTable(){
        firstName.setText("");
        lastName.setText("");
        userTitle.setText("");
        userName.setText("");
        userPW.setText("");
        userMail.setText("");
        userPhone.setText("");
        if (isAdmin.isChecked()) {
            isAdmin.setChecked(false);
        }
    }
    public void clearTable(View view){
        firstName.setText("");
        lastName.setText("");
        userTitle.setText("");
        userName.setText("");
        userPW.setText("");
        userMail.setText("");
        userPhone.setText("");
        if (isAdmin.isChecked()) {
            isAdmin.setChecked(false);
        }
    }
}