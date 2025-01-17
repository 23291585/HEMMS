package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // EditText'leri tanımla
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
    }

    // MainActivity'deki checkLogin metodunda, başarılı giriş sonrası işlemi aşağıdaki gibi yönlendirebilirsiniz:

    public void checkLogin(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Kullanıcı adı veya şifre boş olamaz!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            int isAdmin = MSSQLConnection.validateUser(username, password); // Admin kontrolü

            runOnUiThread(() -> {
                if (isAdmin != -1) { // Giriş başarılıysa
                    Toast.makeText(MainActivity.this, "Giriş başarılı!", Toast.LENGTH_SHORT).show();

                    if (isAdmin == 1) {
                        // Admin ana sayfasına yönlendir
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Normal kullanıcı ana sayfasına yönlendir
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Geçersiz kullanıcı adı veya şifre!", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
