package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Veritabanından ilaçları al ve RecyclerView ile bağla
        itemList = MSSQLConnection.getItemsList(); // getItemsList methodu veritabanından verileri alacak

        // Adapter'ı bağla, Context'i geçiyoruz
        itemAdapter = new ItemAdapter(ItemsActivity.this, itemList);
        recyclerView.setAdapter(itemAdapter);
    }
    public void Back(View view){
        Intent intent = new Intent(ItemsActivity.this,AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
