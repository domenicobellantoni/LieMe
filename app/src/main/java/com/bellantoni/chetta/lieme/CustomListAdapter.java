package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.generalclasses.RoundImage;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;
    private final String[] questions;

    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid, String[] questions) {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;
        this.questions=questions;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);
        rowView.setPadding(0,10,0,10);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.nameList);
        txtTitle.setTextColor(Color.BLACK);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgList);
        TextView extratxt = (TextView) rowView.findViewById(R.id.question);

        txtTitle.setText(itemname[position]);
        imageView.setImageDrawable(new RoundImage(BitmapFactory.decodeResource(context.getResources(), imgid[position])));
        //imageView.setImageResource(imgid[position]);
        extratxt.setText(questions[position]);
        return rowView;

    }

    ;
}