package com.example.hemms;

public class Staff {
    private String staffFirstName;
    private String staffLastName;
    private String staffTitle;

    // Constructor, getter ve setter'lar
    public Staff(String staffFirstName, String staffLastName, String staffTitle) {
        this.staffFirstName = staffFirstName;
        this.staffLastName = staffLastName;
        this.staffTitle = staffTitle;
    }

    public String getStaffFirstName() {
        return staffFirstName;
    }

    public void setStaffFirstName(String staffFirstName) {
        this.staffFirstName = staffFirstName;
    }

    public String getStaffLastName() {
        return staffLastName;
    }

    public void setStaffLastName(String staffLastName) {
        this.staffLastName = staffLastName;
    }

    public String getStaffTitle() {
        return staffTitle;
    }

    public void setStaffTitle(String staffTitle) {
        this.staffTitle = staffTitle;
    }
}

