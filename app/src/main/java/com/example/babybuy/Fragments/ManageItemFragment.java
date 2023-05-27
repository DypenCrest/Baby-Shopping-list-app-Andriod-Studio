package com.example.babybuy.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.babybuy.Adapter.ManageItemAdapter;
import com.example.babybuy.Database.Database;
import com.example.babybuy.Model.ItemDataModel;
import com.example.babybuy.R;

import java.util.ArrayList;


public class ManageItemFragment extends Fragment {

    public ManageItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        Button purchasedbtn = view.findViewById(R.id.purchasedprodfrag);
        Button tobuybtn = view.findViewById(R.id.tobuyprod);

        // Load purchased item when fragment is called
       try{
           int i = 1;
           if (i == 1) {
               int itemsts = 1;
               Database db = new Database(getActivity());
               ItemDataModel itemDataModel = new ItemDataModel();
               ArrayList<ItemDataModel> alldata = db.itemfetchdataforpurchased(itemsts);
               RecyclerView item_recy = view.findViewById(R.id.pruductmanagerecy);
               item_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
               ManageItemAdapter adapter = new ManageItemAdapter(getActivity(), alldata);
               item_recy.setAdapter(adapter);
           }
       }catch (Exception exp){
           Toast.makeText(getActivity(), "No item found", Toast.LENGTH_SHORT).show();
       }



        // Load purchased item when purchased button is clicked
        purchasedbtn.setOnClickListener(v -> {
            int itemsts = 1;
            Database db = new Database(getActivity());
            ItemDataModel itemDataModel = new ItemDataModel();
            ArrayList<ItemDataModel> alldata = db.itemfetchdataforpurchased(itemsts);
            RecyclerView item_recy = view.findViewById(R.id.pruductmanagerecy);
            item_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
            ManageItemAdapter adapter = new ManageItemAdapter(getActivity(), alldata);
            item_recy.setAdapter(adapter);
        });


        //Load tobuy item when tobuy is clicked
        try {
            tobuybtn.setOnClickListener(v -> {
                int itemsts = -1;
                Database db = new Database(getActivity());
                ItemDataModel itemDataModel = new ItemDataModel();
                ArrayList<ItemDataModel> alldata = db.itemfetchdataforpurchased(itemsts);
                RecyclerView item_recy = view.findViewById(R.id.pruductmanagerecy);
                item_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
                ManageItemAdapter adapter = new ManageItemAdapter(getActivity(), alldata);
                item_recy.setAdapter(adapter);
            });
        }
        catch (Exception exp){
            Toast.makeText(getActivity(), "No item found", Toast.LENGTH_SHORT).show();
        }


        return view;

    }
}