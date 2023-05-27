package com.example.babybuy.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.ItemDataModel;
import com.example.babybuy.R;

import java.io.ByteArrayOutputStream;

public class AddItem extends AppCompatActivity {

    TextView itemaddcamera, itemaddgallery;
    Button itemaddbtn;
    ImageView itemaddimage, backicon;
    EditText itemaddname, itemadddes, itemaddquantity, itemaddprice;
    final int CAMERA_CODE = 100;
    final int GALLERY_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        //Database connection
        Database db = new Database(this);

        itemaddbtn = findViewById(R.id.itemaddbtnid);
        itemaddname = findViewById(R.id.itemaddtitleid);
        itemaddquantity = findViewById(R.id.itemaddquantityid);
        itemaddprice = findViewById(R.id.itemaddpriceid);
        itemadddes = findViewById(R.id.itemadddesid);
        itemaddimage = findViewById(R.id.itemaddimageid);
        itemaddgallery = findViewById(R.id.itemaddfromgallery);
        itemaddcamera = findViewById(R.id.itemaddfromcamera);
        backicon = findViewById(R.id.backimgf);

        //category id value from Intent data pass
        int itemcatidlist = getIntent().getExtras().getInt("pcid");


        //add item image from gallery
        itemaddgallery.setOnClickListener(v -> {
            Intent igallery = new Intent(Intent.ACTION_PICK);
            igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(igallery, GALLERY_CODE);
        });

        //add item image from camera
        itemaddcamera.setOnClickListener(v -> {
            Intent icamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(icamera, CAMERA_CODE);
        });

        //add item data to item table of database
        itemaddbtn.setOnClickListener(v -> {

            Integer prdstatus = -1;
            ItemDataModel itemDataModel = new ItemDataModel();
            itemDataModel.setItemimage(convertImageViewToByteArray(itemaddimage));
            itemDataModel.setItemname(itemaddname.getText().toString());
            itemDataModel.setItemquantity(Integer.parseInt(itemaddquantity.getText().toString()));
            itemDataModel.setItemprice(Double.parseDouble(itemaddprice.getText().toString()));
            itemDataModel.setItemdescription(itemadddes.getText().toString());
            itemDataModel.setItemstatus(prdstatus);
            itemDataModel.setItemcategoryid(itemcatidlist);

            long result = db.itemadd(itemDataModel);
            if (result == -1) {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Succcess", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddItem.this, ItemList.class);
                intent.putExtra("idd", itemcatidlist);
                startActivity(intent);
            }
        });

        //back to itemlistactivity
        backicon.setOnClickListener(v -> {
            Intent intent = new Intent(AddItem.this, ItemList.class);
            intent.putExtra("idd", itemcatidlist);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                //for camera
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                itemaddimage.setImageBitmap(img);
            } else if (requestCode == GALLERY_CODE) {
                //for gallery
                itemaddimage.setImageURI(data.getData());
            }
        }
    }

    private byte[] convertImageViewToByteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}