package com.example.eric.gamepalfinal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageAdapter2 extends BaseAdapter {
    private Context mContext;
    private int imageNum;
    String region2, genre2;

    public ImageAdapter2(Context c, int imgNum, String region, String genre) {
        mContext = c;
        imageNum = imgNum;
        region2 = region;
        genre2 = genre;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(470, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 0, 1, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);


        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                //Toast.makeText(mContext, "" + mThumbIds[position],
                      //  Toast.LENGTH_LONG).show();
                imageView.setImageResource(R.mipmap.selected);

                Bundle test = new Bundle();
                test.putInt("selectedImage", mThumbIds[position]);
                test.putInt("imageNumber", imageNum);
                test.putString("region", region2);
                test.putString("genre", genre2);


                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SettingsPage settingsPage = new SettingsPage();
                settingsPage.setArguments(test);
                fragmentTransaction.replace(R.id.content_frame, settingsPage);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();



            }
        });

        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {

            R.mipmap.fortnite,
            R.mipmap.minecraft,
            R.mipmap.gta5,
            R.mipmap.pubg,
            R.mipmap.r6,
            R.mipmap.overwatch,
            R.mipmap.rocket,
            R.mipmap.lol,
            R.mipmap.csgo,
            R.mipmap.battlefield,
            R.mipmap.cod,
            R.mipmap.fifa,
            R.mipmap.ssb,
            R.mipmap.rdr2,
            R.mipmap.skyrim,
            R.mipmap.fo4,
            R.mipmap.f76,
            R.mipmap.dest2,
            R.mipmap.wow,
            R.mipmap.ssbm,
            R.mipmap.nba,
            R.mipmap.forza4

    };
}