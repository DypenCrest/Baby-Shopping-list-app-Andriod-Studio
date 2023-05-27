package com.example.babybuy.Model;

import android.graphics.Bitmap;

public class ItemDataModel {
    Bitmap ImageDataInBitmap;
    Integer itemid;
    String itemname;
    Integer itemquantity;
    Double itemprice;
    String itemdescription;
    Integer itemstatus;
    byte[] itemimage;
    Integer itemcategoryid;

    public Bitmap getImageDataInBitmap() {
        return ImageDataInBitmap;
    }

    public void setImageDataInBitmap(Bitmap imageDataInBitmap) {
        ImageDataInBitmap = imageDataInBitmap;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Integer getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(Integer itemquantity) {
        this.itemquantity = itemquantity;
    }

    public Double getItemprice() {
        return itemprice;
    }

    public void setItemprice(Double itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public Integer getItemstatus() {
        return itemstatus;
    }

    public void setItemstatus(Integer itemstatus) {
        this.itemstatus = itemstatus;
    }

    public byte[] getItemimage() {
        return itemimage;
    }

    public void setItemimage(byte[] itemimage) {
        this.itemimage = itemimage;
    }

    public Integer getItemcategoryid() {
        return itemcategoryid;
    }

    public void setItemcategoryid(Integer itemcategoryid) {
        this.itemcategoryid = itemcategoryid;
    }
}