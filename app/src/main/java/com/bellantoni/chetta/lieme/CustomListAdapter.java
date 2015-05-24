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
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<RowItemProfile> {

    private final Activity context;
    /*private final String[] itemname;
    private final Integer[] imgid;
    private final String[] questions;
    private final String[] ids;*/
    List<RowItemProfile> rows;

    public CustomListAdapter(Activity context, List<RowItemProfile> rows) {
        super(context, R.layout.mylist, rows);
        // TODO Auto-generated constructor stub

        this.context = context;
        /*this.itemname = itemname;
        this.imgid = imgid;
        this.questions=questions;
        this.ids=ids;*/
        this.rows=rows;
    }



    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null, true);
        rowView.setPadding(0,10,0,10);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.nameList);
        txtTitle.setTextColor(Color.BLACK);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgList);
        TextView extratxt = (TextView) rowView.findViewById(R.id.question);
        TextView idfacebook = (TextView) rowView.findViewById(R.id.facebookId);
        txtTitle.setText(this.rows.get(position).getNameSurname());
        imageView.setImageDrawable(new RoundImage(BitmapFactory.decodeResource(context.getResources(), this.rows.get(position).getIdImg())));
        //imageView.setImageResource(imgid[position]);
        extratxt.setText(this.rows.get(position).getQuestion());
        idfacebook.setText(this.rows.get(position).getId());

        return rowView;

    }

    ;


}