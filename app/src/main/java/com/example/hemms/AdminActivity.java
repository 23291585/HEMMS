package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void Back(View view){
        Intent intent = new Intent(AdminActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void StokGuncelle(View view){
        Intent intent = new Intent(AdminActivity.this,SyncStore.class);
        startActivity(intent);
    }
    public void listItems(View view){
        Intent intent = new Intent(AdminActivity.this,ItemsActivity.class);
        startActivity(intent);
    }
    public void listStaff(View view){
        Intent intent = new Intent(AdminActivity.this,ListStaff.class);
        startActivity(intent);
    }
    public void changePassword(View view){
        Intent intent = new Intent(AdminActivity.this,ChangePassword.class);
        startActivity(intent);
    }
    public void PersonelEkle(View view){
        Intent intent = new Intent(AdminActivity.this,PersonelEkle.class);
        startActivity(intent);
    }
    public void exitApp(View view){
        finishAffinity();
    }
}
