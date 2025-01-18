package com.example.hemms;

public class Item {
    private int itemId;
    private String itemName;
    private String itemCategory;
    private int itemRequiredQuantity;
    private int quantity;
    private String expirationDate;
    private int roomId;

    // Constructor
    public Item(int itemId, String itemName, String itemCategory, int itemRequiredQuantity,
                int quantity, String expirationDate, int roomId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.itemRequiredQuantity = itemRequiredQuantity;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.roomId = roomId;
    }

    // Getter ve Setter metodları
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getItemRequiredQuantity() {
        return itemRequiredQuantity;
    }

    public void setItemRequiredQuantity(int itemRequiredQuantity) {
        this.itemRequiredQuantity = itemRequiredQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    @Override
    public String toString() {
        return this.itemName;  // İlaç adını döndür
    }

}
