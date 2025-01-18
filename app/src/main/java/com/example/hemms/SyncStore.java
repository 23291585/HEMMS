package com.example.hemms;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SyncStore extends AppCompatActivity {

    private Spinner spinnerRoom;
    private Spinner spinnerItem;
    private EditText editTextStockUpdate;
    private TextView textViewCurrentStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_store);

        // UI element initialization
        spinnerRoom = findViewById(R.id.spinnerRoom);
        spinnerItem = findViewById(R.id.spinnerItem);
        editTextStockUpdate = findViewById(R.id.editTextStockUpdate);
        textViewCurrentStock = findViewById(R.id.textViewCurrentStock);

        // Oda numaralarını spinner'a bağla
        List<Room> roomList = MSSQLConnection.getRoomList();  // Veritabanından oda isimlerini ve ID'lerini al
        ArrayAdapter<Room> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roomList);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(roomAdapter);

        // Oda seçildiğinde ilaçları güncelle
        spinnerRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                Room selectedRoom = (Room) spinnerRoom.getSelectedItem(); // Oda nesnesini al
                int selectedRoomId = selectedRoom.getRoomId();  // Oda numarasını al
                updateItemList(selectedRoomId);  // Odaya ait ilaçları güncelle
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Hiçbir şey seçilmediğinde yapılacak işlem
            }
        });

        // İlaç seçildiğinde stok bilgisini güncelle
        spinnerItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                Item selectedItem = (Item) spinnerItem.getSelectedItem(); // Seçilen ilacı al
                updateCurrentStock(selectedItem);  // Stok bilgisini güncelle
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Hiçbir şey seçilmediğinde yapılacak işlem
            }
        });

        // Pencere kenarlarına uygunluk için
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Oda seçimine göre ilaçları güncelleyen metot
    private void updateItemList(int selectedRoomId) {
        List<Item> itemList = MSSQLConnection.getItemsForRoom(selectedRoomId); // Odaya özel ilaçları al
        ArrayAdapter<Item> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(itemAdapter);
    }

    // İlaç seçildiğinde mevcut stok bilgisini gösteren metot
    private void updateCurrentStock(Item selectedItem) {
        int currentStock = MSSQLConnection.getCurrentStock(selectedItem.getItemId());
        textViewCurrentStock.setText(String.valueOf(currentStock));
    }

    // Stok güncelleme işlemi
    public void updateStock(View view) {
        // Stok güncelleme işlemi burada yapılacak
        String stockInput = editTextStockUpdate.getText().toString();
        if (!stockInput.isEmpty()) {
            int newStock = Integer.parseInt(stockInput);  // Yeni stok miktarını al

            // Seçilen oda ve ilaç bilgilerini al
            Room selectedRoom = (Room) spinnerRoom.getSelectedItem();
            Item selectedItem = (Item) spinnerItem.getSelectedItem();

            // Stok bilgilerini Cabinets tablosunda güncelle
            updateStockInDatabase(selectedRoom.getRoomId(), selectedItem.getItemId(), newStock);
        } else {
            Toast.makeText(this, "Geçerli bir stok miktarı girin", Toast.LENGTH_SHORT).show();
        }
    }

    // MSSQL veritabanına bağlantı sağlama ve stok güncelleme işlemi
    private void updateStockInDatabase(int roomId, int itemId, int newStock) {
        try (Connection connection = MSSQLConnection.getConnection()) {
            if (connection != null) {
                String query = "UPDATE Cabinets SET quantity = ? WHERE room_id = ? AND item_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, newStock);  // Yeni stok miktarını ayarla
                preparedStatement.setInt(2, roomId);    // Seçilen oda id'si
                preparedStatement.setInt(3, itemId);    // Seçilen ilaç id'si

                int rowsAffected = preparedStatement.executeUpdate();  // Veritabanı güncelleme işlemi
                if (rowsAffected > 0) {
                    Log.d("updateStock", "Stok başarıyla güncellendi.");
                    Toast.makeText(this, "Stok başarıyla güncellendi", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("updateStock", "Stok güncellenirken hata oluştu.");
                    Toast.makeText(this, "Stok güncellenemedi", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Veritabanı hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
