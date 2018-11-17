package com.example.eric.gamepalfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int resource, List<cards> items) {
        super(context, resource, items);


    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            //here
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView pic = (ImageView) convertView.findViewById(R.id.bgImage);

        name.setText(card.getName());
        //pic.setImageResource(R.mipmap.fortnite); //user first image
        Glide.with(getContext()).load(card.getImageUrl1()).into(pic);

        return convertView;

    }
}
