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
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.bellantoni.chetta.lieme.listener.OnClickListenerFriendProfile;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListInFriendFragmentAdapter extends ArrayAdapter<RowItemProfile> {


    private View view;

    private final Activity context;
    private List<RowItemProfile> rows;
    private int count = 3;

    public ListInFriendFragmentAdapter(Activity context, List<RowItemProfile> firstRows ) {

        super(context, R.layout.mylist, firstRows);
        this.context = context;
        this.rows = firstRows;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.list_question_profile_friend, null);

            view.setPadding(0,10,0,10);

            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.nameListFriend);
            holder.extratxt = (TextView) view.findViewById(R.id.questionFriend);
            holder.idfacebook = (TextView) view.findViewById(R.id.facebookIdFriend);
            holder. imageView = (ImageView) view.findViewById(R.id.imgListFriend);
            holder.imgResponse = (ImageView) view.findViewById(R.id.imgResponseFriend);
            holder.timeStamp = (TextView) view.findViewById(R.id.dateQuestionProfileFriend);

            view.setTag(holder);



        } else {

            view=convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setTextColor(Color.BLACK);
        holder.txtTitle.setText(this.rows.get(position).getNameSurname());

        holder.timeStamp.setText(this.rows.get(position).getTime());

        RoundImage roundedImage = new RoundImage(BitmapFactory.decodeResource(context.getResources(), R.mipmap.iconuseranonymous));
        Picasso.with(context).load("https://graph.facebook.com/" + this.rows.get(position).getFacebookId() + "/picture?height=115&width=115")
                .placeholder(roundedImage)
                .transform(new CircleTransform()).fit().centerCrop().into(holder.imageView);
        //holder.extratxt.setTypeface(tf);
        holder.extratxt.setText(this.rows.get(position).getQuestion());

        holder.idfacebook.setText(this.rows.get(position).getFacebookId());

        holder.txtTitle.setOnClickListener(new OnClickListenerFriendProfile(holder.idfacebook.getText().toString(), this.context));
        holder.imageView.setOnClickListener(new OnClickListenerFriendProfile(holder.idfacebook.getText().toString(), this.context));

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
        TextView timeStamp;
        int position;
    }
}
