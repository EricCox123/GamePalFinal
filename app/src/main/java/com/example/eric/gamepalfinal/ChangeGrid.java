package com.example.eric.gamepalfinal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eric.gamepalfinal.ImageAdapter;
import com.example.eric.gamepalfinal.R;

public class ChangeGrid extends Fragment {


    Context context;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        View v = inflater.inflate(R.layout.grid_change_layout, container, false);
        context = v.getContext();

        int imgNum = getArguments().getInt("imageNum");
        String region = getArguments().getString("region");
        String genre = getArguments().getString("genre");
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter2(context,imgNum,region,genre));



        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
