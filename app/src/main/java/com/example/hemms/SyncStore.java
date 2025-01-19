package com.example.hemms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SyncStore extends AppCompatActivity {

    private Spinner spinnerRoom;
    private Spinner spinnerItem;
    private EditText editTextStockUpdate;
    private TextView textViewCurrentStock;
    private Item lastSelectedItem;  // Son seçilen ilaç
    private int lastSelectedItemPosition = -1;  // Son seçilen ilaç pozisyonu

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

                // Oda değiştiğinde, seçili ilaç varsa onun stok bilgisini güncelle
                if (lastSelectedItem != null && lastSelectedItem.getRoomId() == selectedRoomId) {
                    // Seçili ilaç aynı odada ise, stok bilgisini güncelle
                    updateCurrentStock(lastSelectedItem);

                    // Son seçili ilacı tekrar seç
                    if (lastSelectedItemPosition != -1) {
                        spinnerItem.setSelection(lastSelectedItemPosition, true);
                    }
                } else {
                    // Eğer ilaç seçili değilse veya oda farklıysa, stok bilgisini sıfırla
                    textViewCurrentStock.setText("Stok Miktarı : 0");
                }
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
                lastSelectedItem = selectedItem; // Son seçilen ilaç kaydediliyor
                lastSelectedItemPosition = position; // Seçili ilaç pozisyonunu kaydet
                updateCurrentStock(selectedItem);  // Stok bilgisini güncelle
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Hiçbir şey seçilmediğinde yapılacak işlem
            }
        });
    }

    // Oda seçimine göre ilaçları güncelleyen metot
    private void updateItemList(int selectedRoomId) {
        // Veritabanından seçilen odaya ait ilaçları al
        List<Item> itemList = MSSQLConnection.getItemsForRoom(selectedRoomId);

        if (itemList == null || itemList.isEmpty()) {
            // Eğer ilaç kaydı yoksa spinner'ı temizle ve kullanıcıya bilgi ver
            spinnerItem.setAdapter(null);
            Toast.makeText(this, "Seçilen oda için kayıtlı ilaç bulunamadı.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Eğer ilaç kaydı varsa spinner'ı doldur
        ArrayAdapter<Item> itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItem.setAdapter(itemAdapter);

        // Oda değiştiğinde, önceki seçili öğeyi tekrar seç
        if (lastSelectedItem != null && lastSelectedItemPosition != -1) {
            spinnerItem.setSelection(lastSelectedItemPosition, true);
        }
    }

    private void updateCurrentStock(Item selectedItem) {
        // Stok miktarını al
        int currentStock = MSSQLConnection.getCurrentStock(selectedItem.getItemId(), selectedItem.getRoomId());
        textViewCurrentStock.setText("Stok Miktarı : " + String.valueOf(currentStock));
    }

    // Stok güncelleme işlemi
    public void updateStock(View view) {
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
                    Toast.makeText(this, "Stok başarıyla güncellendi", Toast.LENGTH_SHORT).show();

                    // Mevcut stok miktarını güncelle
                    updateCurrentStock((Item) spinnerItem.getSelectedItem());
                } else {
                    Toast.makeText(this, "Stok güncellenemedi", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Veritabanı hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Geri gitme işlemi
    public void Back(View view) {
        Intent intent = new Intent(SyncStore.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
