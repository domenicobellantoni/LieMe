package com.bellantoni.chetta.lieme.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.ItemHome;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.listener.OnClickListenerHomeTo;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by Domenico on 28/07/2015.
 */
public class ListInHomeAdapter extends ArrayAdapter<ItemHome> {

    private View view;

    private final Activity context;
    private List<ItemHome> rows;
    private int count = 1;

    public ListInHomeAdapter(Activity context, List<ItemHome> firstRows ) {

        super(context, R.layout.list_question_home, firstRows);
        this.context = context;
        this.rows = firstRows;


    }




    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.list_question_home, null);



            view.setPadding(0,10,0,10);

            holder = new ViewHolder();
            holder.nameFrom = (TextView) view.findViewById(R.id.nameFrom);



            holder.nameTo = (TextView) view.findViewById(R.id.nameTo);


            holder.question = (TextView) view.findViewById(R.id.questionFriendHome);
            holder.idfacebookTo = (TextView) view.findViewById(R.id.facebookIdFriendHome);
            holder. imageView = (ImageView) view.findViewById(R.id.imageFbHome);
            holder.imgResponse = (ImageView) view.findViewById(R.id.imgResponseFriendHome);
            holder.timeStamp = (TextView) view.findViewById(R.id.dateQuestionProfileHome);


            // The tag can be any Object, this just happens to be the ViewHolder
            view.setTag(holder);



        } else {

            //System.out.println("NO INFLATE VIEW");
            view=convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameTo.setTextColor(Color.BLACK);


        holder.nameFrom.setText(this.rows.get(position).getNameFrom());


        holder.nameTo.setText(this.rows.get(position).getNameTo());

        holder.timeStamp.setText(this.rows.get(position).getTime());

        //l'immagine ovviamente la scarico dall'id
        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).fit().centerCrop().into(holder.imageView);
        RoundImage roundedImage = new RoundImage(BitmapFactory.decodeResource(context.getResources(), R.mipmap.iconuseranonymous));
        Picasso.with(context).load("https://graph.facebook.com/" + this.rows.get(position).getIdTo() + "/picture?height=115&width=115")
                .placeholder(roundedImage)
                .transform(new CircleTransform()).fit().centerCrop().into(holder.imageView);
        //holder.extratxt.setTypeface(tf);
        holder.question.setText(this.rows.get(position).getQuestion());

        holder.idfacebookTo.setText(this.rows.get(position).getIdTo());

        holder.nameTo.setOnClickListener(new OnClickListenerHomeTo(holder.idfacebookTo.getText().toString(), this.context));
        holder.imageView.setOnClickListener(new OnClickListenerHomeTo(holder.idfacebookTo.getText().toString(),this.context));

        if(this.rows.get(position).isResultQuestion()==true){
            holder.imgResponse.setImageResource(R.drawable.heart_green);

        }else{

            holder.imgResponse.setImageResource(R.drawable.heart_red);
        }
        return view;
    }


    @Override
    public ItemHome getItem(int position){
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
        TextView nameFrom;
        TextView nameTo;
        TextView question;
        ImageView imageView;
        TextView idfacebookTo;
        ImageView imgResponse;
        TextView timeStamp;
        int position;
    }
}
