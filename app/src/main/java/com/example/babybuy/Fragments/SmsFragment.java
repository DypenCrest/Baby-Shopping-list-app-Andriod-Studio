package com.example.babybuy.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.babybuy.R;

public class SmsFragment extends Fragment {

    EditText smobile, stitle, sdes, sprice;
    Button btnsendsms;

    public SmsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms, container, false);

        //bind variable to layout tag
        smobile = view.findViewById(R.id.smobile);
        stitle = view.findViewById(R.id.stitle);
        sdes = view.findViewById(R.id.sdescription);
        sprice = view.findViewById(R.id.sprice);
        btnsendsms = view.findViewById(R.id.btnsendsms);

        //call SendMessage method
        btnsendsms.setOnClickListener(v -> {
            SendMessage();
        });
        return view;
    }


    //SMS send method
    public void SendMessage() {
        String stringPhone = smobile.getText().toString().trim();
        String stringtitle = stitle.getText().toString().trim();
        String stringdes = sdes.getText().toString().trim();
        String stringprice = sprice.getText().toString().trim();
        String fullmessage = "title: " + stringtitle + " Description: " + stringdes + " quantity: " +
                " price: " + stringprice;


        if (stringPhone.equals("") || stringtitle.equals("") || stringprice.equals("")) {
            Toast.makeText(getActivity(), "enter all field", Toast.LENGTH_SHORT).show();
        } else {
            SmsManager smsman = SmsManager.getDefault();
            smsman.sendTextMessage(stringPhone, null, fullmessage, null, null);
            Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
            smobile.getText().clear();
            stitle.getText().clear();
            sdes.getText().clear();
            sprice.getText().clear();
        }
    }
    }