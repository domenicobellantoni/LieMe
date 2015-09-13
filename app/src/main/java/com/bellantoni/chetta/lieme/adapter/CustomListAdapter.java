package com.bellantoni.chetta.lieme.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.listener.OnClickListenerProfile;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<RowItemProfile> {

    private final Activity context;
    private List<RowItemProfile> rows;
    private int count = 1;

    private View view;


    public CustomListAdapter(Activity context, List<RowItemProfile> firstRows ) {

        super(context, R.layout.mylist, firstRows);
        this.context = context;
        this.rows = firstRows;


    }



    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.mylist, null);

            view.setPadding(0,10,0,10);
            // Create a ViewHolder and store references to the two children views
            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.nameList);
            holder.extratxt = (TextView) view.findViewById(R.id.question);
            holder.idfacebook = (TextView) view.findViewById(R.id.facebookId);
            holder. imageView = (ImageView) view.findViewById(R.id.imgList);
            holder.imgResponse = (ImageView) view.findViewById(R.id.imgResponse);
            holder.timestamp = (TextView) view.findViewById(R.id.dateQuestionProfile);



            view.setTag(holder);



        } else {
            view=convertView;
            holder = (ViewHolder) convertView.getTag();
        }



        holder.txtTitle.setTextColor(Color.BLACK);
        holder.txtTitle.setText(this.rows.get(position).getNameSurname());

        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).fit().centerCrop().into(holder.imageView);
        RoundImage roundedImage = new RoundImage(BitmapFactory.decodeResource(context.getResources(), R.mipmap.iconuseranonymous));
        Picasso.with(context).load("https://graph.facebook.com/" + this.rows.get(position).getFacebookId() + "/picture?height=115&width=115")
                .placeholder(roundedImage)
                .transform(new CircleTransform()).fit().centerCrop().into(holder.imageView);


        holder.extratxt.setText(this.rows.get(position).getQuestion());

        holder.idfacebook.setText(this.rows.get(position).getFacebookId());

        holder.timestamp.setText(this.rows.get(position).getTime());

        holder.txtTitle.setOnClickListener(new OnClickListenerProfile(holder.idfacebook.getText().toString(), this.context));
        holder.imageView.setOnClickListener(new OnClickListenerProfile(holder.idfacebook.getText().toString(), this.context));

        if(this.rows.get(position).getResultQuestion()==true){
            holder.imgResponse.setImageResource(R.drawable.heart_green);
        }else{

            holder.imgResponse.setImageResource(R.drawable.heart_red);
        }

        return view;
    }


    @Override
    public RowItemProfile getItem(int position){
        return this.rows.get(position);

    }





    @Override
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    static class ViewHolder {
        TextView txtTitle;
        TextView extratxt;
        ImageView imageView;
        TextView idfacebook;
        ImageView imgResponse;
        TextView timestamp;
        int position;
    }
}