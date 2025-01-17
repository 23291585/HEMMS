package com.example.hemms;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListStaff extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffAdapter staffAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_staff);

        // RecyclerView'in başlangıç ayarlarını yap
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Personel listesini veritabanından al
        List<Staff> staffList = MSSQLConnection.getStaffList();

        // Eğer personel listesi alınamazsa bir uyarı göster
        if (staffList == null || staffList.isEmpty()) {
            Toast.makeText(this, "Personel listesi alınamadı", Toast.LENGTH_SHORT).show();
        } else {
            // Personel verilerini Adapter ile RecyclerView'a bağla
            staffAdapter = new StaffAdapter(staffList);
            recyclerView.setAdapter(staffAdapter);
        }

        // Edge-to-edge padding ayarı
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
