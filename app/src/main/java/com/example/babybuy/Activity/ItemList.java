package com.example.babybuy.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.babybuy.Adapter.ItemListAdapter;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ItemList extends AppCompatActivity {
    ImageButton backtocategoryy;
    Button addnewitem;
    Integer itemcatid;
    TextView itemname, totalpurchasedprice, totaltobuyprice;
    ArrayList<ItemDataModel> alldata;
    RecyclerView item_recy;
    ItemDataModel itemDataModel;
    ItemListAdapter adapter;
    Database db = new Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        backtocategoryy = findViewById(R.id.backtocategory);
        addnewitem = findViewById(R.id.itemactivityaddbtn);
        itemname = findViewById(R.id.itemshowname);
        totalpurchasedprice = findViewById(R.id.totalpurchasedprice);
        totaltobuyprice = findViewById(R.id.totaltobuyprice);

        //getting value from Category Adapter using Intent
        itemcatid = getIntent().getExtras().getInt("idd");


        itemDataModel = new ItemDataModel();

        alldata = db.itemfetchdata(itemcatid);


        //ojbect created using recyclerview and connected to id
        item_recy = findViewById(R.id.item_recy_view);
        //Layoutmanager setup
        item_recy.setLayoutManager(new LinearLayoutManager(this));
        //swipe function
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(item_recy);
        //add Database data to adapter
        adapter = new ItemListAdapter(this, alldata);

        //add adapter to view
        item_recy.setAdapter(adapter);

        // Back to Category Fragment


        //Add new item (redirecting to Addnewitem activity)
        addnewitem.setOnClickListener(v -> {
            Intent intent = new Intent(ItemList.this, AddItem.class);
            intent.putExtra("pcid", itemcatid);
            startActivity(intent);
        });

        backtocategoryy.setOnClickListener(v -> {
            redirecttocategory();
        });


        //method to calcualte price
        priceresult();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }


        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Database db = new Database(ItemList.this);
            ArrayList<ItemDataModel> alldataswipe = db.itemfetchdata(itemcatid);
            int itemIdswipe = alldataswipe.get(position).getItemid();
            int itemstsswipe = alldataswipe.get(position).getItemstatus();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    db.deleteitem(itemIdswipe);
                    alldata.remove(position);
                    item_recy.getAdapter().notifyItemRemoved(position);
                    ArrayList<ItemDataModel> rrecentdata = db.itemfetchdata(itemcatid);
                    ItemListAdapter aadapter = new ItemListAdapter(ItemList.this, rrecentdata);
                    item_recy.setAdapter(aadapter);
                    priceresult();
                    Toast.makeText(ItemList.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    break;

                case ItemTouchHelper.RIGHT:
                    if (itemstsswipe == -1) {
                        db.itempurchased(1, itemIdswipe);
                        ArrayList<ItemDataModel> recentdata = db.itemfetchdata(itemcatid);
                        Toast.makeText(ItemList.this, "Item Purchased", Toast.LENGTH_SHORT).show();
                        Objects.requireNonNull(item_recy.getAdapter()).notifyDataSetChanged();
                        ItemListAdapter adapter = new ItemListAdapter(ItemList.this, recentdata);
                        item_recy.setAdapter(adapter);
                        alldata.get(position).setItemstatus(1);
                        priceresult();

                    } else if (itemstsswipe == 1) {
                        ArrayList<ItemDataModel> recentdata = db.itemfetchdata(itemcatid);
                        Toast.makeText(ItemList.this, "Already purchased", Toast.LENGTH_SHORT).show();
                        ItemListAdapter adapter = new ItemListAdapter(ItemList.this, recentdata);
                        item_recy.setAdapter(adapter);
                        Objects.requireNonNull(item_recy.getAdapter()).notifyDataSetChanged();

                    }
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(getResources().getColor(R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .setSwipeLeftActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeLeftBackgroundColor(getResources().getColor(R.color.colorRed))
                    .addSwipeRightLabel("purchased or tobuy")
                    .setSwipeRightLabelColor(getResources().getColor(R.color.white))
                    .addSwipeRightActionIcon(R.drawable.ic_purchase)
                    .setSwipeRightActionIconTint(getResources().getColor(R.color.white))
                    .addSwipeRightBackgroundColor(getResources().getColor(R.color.greencolor))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    //calculate price
    public void priceresult() {
        try {
            totalpurchasedprice = findViewById(R.id.totalpurchasedprice);
            totaltobuyprice = findViewById(R.id.totaltobuyprice);
            alldata = db.itemfetchdata(itemcatid);
            Double totalPurchasedPrice = 0.0;
            Double totaltoBuyPrice = 0.0;
            for (int i = 0; i < alldata.size(); i++) {
                if (alldata.get(i).getItemstatus() == -1) {
                    totaltoBuyPrice += alldata.get(i).getItemprice() * alldata.get(i).getItemquantity();
                } else {
                    totalPurchasedPrice += alldata.get(i).getItemprice() * alldata.get(i).getItemquantity();
                }
            }
            totalpurchasedprice.setText(String.valueOf(totalPurchasedPrice));
            totaltobuyprice.setText(String.valueOf(totaltoBuyPrice));
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void redirecttocategory() {
        startActivity(new Intent(ItemList.this, Home.class));
    }
}