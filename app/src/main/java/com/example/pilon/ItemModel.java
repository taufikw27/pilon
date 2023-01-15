package com.example.pilon;

public class ItemModel {
    String namacalon,jumlahsuara;
    int imgcalon;

    public ItemModel(String namacalon, String jumlahsuara, int imgcalon) {
        this.namacalon = namacalon;
        this.jumlahsuara = jumlahsuara;
        this.imgcalon = imgcalon;
    }

    public String getNamacalon() {
        return namacalon;
    }

    public String getJumlahsuara() {
        return jumlahsuara;
    }

    public int getImgcalon() {
        return imgcalon;
    }
}
