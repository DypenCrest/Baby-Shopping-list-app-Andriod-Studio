package com.example.babybuy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.babybuy.Model.CatDataModel;
import com.example.babybuy.Model.ItemDataModel;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {


    //final variable declaration for Database
    public static final String DB_NAME = "babybuy.db";


    //final variable declaration for Registration and Login Table
    public static final String USER_TABLE = "user";
    public static final String USER_ID = "id";
    public static final String USER_COL_1 = "firstname";
    public static final String USER_COL_2 = "lastname";
    public static final String USER_COL_3 = "email";
    public static final String USER_COL_4 = "password";
    public static final String USER_COL_5 = "image";
    public static final String USER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE +
            "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_COL_1 + " varchar(150), "
            + USER_COL_2 + " varchar(150), "
            + USER_COL_3 + " varchar(500), "
            + USER_COL_4 + " varchar(18), "
            + USER_COL_5 + " BLOB)";


    //final variable declaration for Category Table
    public static final String CATEGORY_TABLE = "category";
    public static final String CATEGORY_ID = "categoryid";
    public static final String CATEGORY_COL_1 = "categoryname";
    public static final String CATEGORY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE +
            "(" + CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CATEGORY_COL_1 + " varchar(50))";


    //final variable declaration for Item Table
    public static final String ITEM_TABLE = "item";
    public static final String ITEM_ID = "itemid";
    public static final String ITEM_COL_1 = "itemname";
    public static final String ITEM_COL_2 = "itemquantity";
    public static final String ITEM_COL_3 = "itemprice";
    public static final String ITEM_COL_4 = "itemdescription";
    public static final String ITEM_COL_5 = "itemstatus";
    public static final String ITEM_COL_6 = "itemimage";
    public static final String ITEM_COL_7 = "itemcategoryid";
    public static final String ITEM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + ITEM_TABLE +
            "(" + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ITEM_COL_1 + " varchar(100), "
            + ITEM_COL_2 + " INTEGER, "
            + ITEM_COL_3 + " real, "
            + ITEM_COL_4 + " varchar(100), "
            + ITEM_COL_5 + " INTEGER, "
            + ITEM_COL_6 + " BLOB, "
            + ITEM_COL_7 + " INTEGER REFERENCES " + CATEGORY_TABLE + "(" + CATEGORY_COL_1 + " ))";


    //Constructor for Database class
    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }


    //Method for table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        //execute above query
        db.execSQL(USER_TABLE_CREATE);
        db.execSQL(CATEGORY_TABLE_CREATE);
        db.execSQL(ITEM_TABLE_CREATE);
    }


    //Method for table upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        onCreate(db);
    }


    //Insert query for Registration
    public boolean insert(String firstname, String lastname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(USER_COL_1, firstname);
        v.put(USER_COL_2, lastname);
        v.put(USER_COL_3, email);
        v.put(USER_COL_4, password);
        long res = db.insert(USER_TABLE, null, v);
        if (res == -1)
            return false;
        else
            return true;
    }

    //Method for preventing multiple registration using same email address
    public Boolean checkemail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getlistemail = db.rawQuery("select * from " + USER_TABLE + " where " +
                "email = ?", new String[]{email});
        if (getlistemail.getCount() > 0)
            return false;
        else
            return true;
    }


    //Method for login validation
    public Boolean checkemailandpassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + USER_TABLE + " where" +
                        " email = ? and password = ?",
                new String[]{email, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    public String getfullname(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getdata = db.rawQuery("select * from " + USER_TABLE + " where " + USER_COL_3 + " = ?", new String[]{email});
        String fullname = "";
        while (getdata.moveToNext()) {
            String firstname = getdata.getString(1);
            String lastname = getdata.getString(2);
            fullname = firstname + " " + lastname;
        }

        return fullname;
    }


    //insert method for category
    public long categoryadd(String catname) {
        SQLiteDatabase catdb = getWritableDatabase();
        ContentValues catv = new ContentValues();
        catv.put(CATEGORY_COL_1, catname);
        return catdb.insert(CATEGORY_TABLE, null, catv);
    }


    //select method for category
    public ArrayList<CatDataModel> categoryfetchdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getdata = db.rawQuery("select * from " + CATEGORY_TABLE, null);
        ArrayList<CatDataModel> catarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            CatDataModel cat = new CatDataModel();
            cat.catid = getdata.getInt(0);
            cat.catname = getdata.getString(1);
            catarray.add(cat);
        }
        return catarray;
    }


    //update method for category
    public int updatecategoryname(String catname, int catid) {
        SQLiteDatabase udb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CATEGORY_COL_1, catname);
        return udb.update(CATEGORY_TABLE, cv, "categoryid = ?", new String[]{String.valueOf(catid)});
    }


    // delete method for category
    public int deletecategory(int catid) {
        SQLiteDatabase ddb = getWritableDatabase();
        return ddb.delete(CATEGORY_TABLE, CATEGORY_ID + " = ?", new String[]{String.valueOf(catid)});
    }


    //insert method for item
    public long itemadd(ItemDataModel itemDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(ITEM_COL_1, itemDataModel.getItemname());
        v.put(ITEM_COL_2, itemDataModel.getItemquantity());
        v.put(ITEM_COL_3, itemDataModel.getItemprice());
        v.put(ITEM_COL_4, itemDataModel.getItemdescription());
        v.put(ITEM_COL_5, itemDataModel.getItemstatus());
        v.put(ITEM_COL_6, itemDataModel.getItemimage());
        v.put(ITEM_COL_7, itemDataModel.getItemcategoryid());
        return db.insert(ITEM_TABLE, null, v);
    }


    //change status of item purchased or not
    public int itempurchased(int itemstsid, int itemid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(ITEM_COL_5, itemstsid);
        return db.update(ITEM_TABLE, v, ITEM_ID + " = ?", new String[]{String.valueOf(itemid)});
    }


    //select method for itemlist
    public ArrayList<ItemDataModel> itemfetchdata(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getdata = db.rawQuery("select * from " + ITEM_TABLE + " where itemcategoryid = ?", new String[]{String.valueOf(id)});
        ArrayList<ItemDataModel> itemarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            ItemDataModel prod = new ItemDataModel();
            prod.setItemid(getdata.getInt(0));
            prod.setItemname(getdata.getString(1));
            prod.setItemquantity(getdata.getInt(2));
            prod.setItemprice(getdata.getDouble(3));
            prod.setItemdescription(getdata.getString(4));
            prod.setItemstatus(getdata.getInt(5));
            prod.setItemimage(getdata.getBlob(6));

            itemarray.add(prod);
        }
        return itemarray;
    }


    //purchased item list for manageitem fragment
    public ArrayList<ItemDataModel> itemfetchdataforpurchased(int itemsts) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getdata = db.rawQuery("select * from " + ITEM_TABLE + " where " + ITEM_COL_5 + " = ?", new String[]{String.valueOf(itemsts)});
        ArrayList<ItemDataModel> itemarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            ItemDataModel prod = new ItemDataModel();
            prod.setItemid(getdata.getInt(0));
            prod.setItemname(getdata.getString(1));
            prod.setItemquantity(getdata.getInt(2));
            prod.setItemprice(getdata.getDouble(3));
            prod.setItemdescription(getdata.getString(4));
            prod.setItemstatus(getdata.getInt(5));
            prod.setItemimage(getdata.getBlob(6));
            itemarray.add(prod);
        }
        return itemarray;
    }


    //delete item item when item delete
    public int deleteitem(int id) {
        SQLiteDatabase ddb = getWritableDatabase();
        return ddb.delete(ITEM_TABLE, ITEM_ID + " = ?",
                new String[]{String.valueOf(id)});
    }


    //delete item items when category delete
    public int deleteitembycatid(int procatid) {
        SQLiteDatabase ddb = getWritableDatabase();
        return ddb.delete(ITEM_TABLE, ITEM_COL_7 + " = ?",
                new String[]{String.valueOf(procatid)});
    }
}
