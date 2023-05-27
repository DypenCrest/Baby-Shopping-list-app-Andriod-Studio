package com.example.babybuy.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;

public class ManageItemAdapter extends RecyclerView.Adapter<ManageItemAdapter.ViewHolder> {

    Context context;
    ArrayList<ItemDataModel> arrayList;

    public ManageItemAdapter(Context context, ArrayList<ItemDataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemtitle, itemdes, itemprice, itemquantity;
        CheckBox itemstatuscheckbox;
        ImageView pedit, pdelete, itemimage;
        CardView itemcardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemtitle = itemView.findViewById(R.id.item_title_id);
            itemdes = itemView.findViewById(R.id.item_des_id);
            itemprice = itemView.findViewById(R.id.item_price_id);
            itemquantity = itemView.findViewById(R.id.item_quantity_id);
            itemstatuscheckbox = itemView.findViewById(R.id.itempurchased);
            itemimage = itemView.findViewById(R.id.item_img_id);
            itemcardview = itemView.findViewById(R.id.cardview_id);

        }
    }

    @NonNull
    @Override
    public ManageItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.design_item_recyclerview, parent, false);
        ManageItemAdapter.ViewHolder viewHolder = new ManageItemAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDataModel pdm = arrayList.get(position);

        holder.itemtitle.setText(pdm.getItemname());
        holder.itemdes.setText(pdm.getItemdescription());
        holder.itemprice.setText(String.valueOf(pdm.getItemprice()));
        holder.itemquantity.setText(String.valueOf(pdm.getItemquantity()));
        Bitmap ImageDataInBitmap = BitmapFactory.decodeByteArray(pdm.getItemimage(), 0, pdm.getItemimage().length);
        holder.itemimage.setImageBitmap(ImageDataInBitmap);

        if (pdm.getItemstatus() > 0) {
            holder.itemstatuscheckbox.setChecked(true);
        }

        holder.itemstatuscheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int position = holder.getAdapterPosition();
                if (holder.itemstatuscheckbox.isChecked()) {
                    int itemstsvalue = 1;
                    Database db = new Database(context);
                    db.itempurchased(itemstsvalue, pdm.getItemid());
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    int itemstsvalue = -1;
                    Database db = new Database(context);
                    db.itempurchased(itemstsvalue, pdm.getItemid());
                    arrayList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
