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

        TextView bio = (TextView) convertView.findViewById(R.id.bio);
        ImageView pic = (ImageView) convertView.findViewById(R.id.bgImage);
        ImageView pic2 = (ImageView) convertView.findViewById(R.id.bgImage2);
        ImageView pic3= (ImageView) convertView.findViewById(R.id.bgImage3);
        ImageView pic4 = (ImageView) convertView.findViewById(R.id.bgImage4);

        name.setText(card.getName());
        bio.setText(card.getBio());

        Glide.with(getContext()).load(card.getImageUrl1()).into(pic);
        Glide.with(getContext()).load(card.getImageUrl2()).into(pic2);
        Glide.with(getContext()).load(card.getImageUrl3()).into(pic3);
        Glide.with(getContext()).load(card.getImageUrl4()).into(pic4);
        return convertView;

    }
}
