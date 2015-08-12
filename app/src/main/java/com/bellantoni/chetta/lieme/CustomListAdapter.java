package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.listener.OnClickListenerProfile;
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
        // Avoid unneccessary calls to findViewById() on each row, which is expensive!
        ViewHolder holder;

    /*
     * If convertView is not null, we can reuse it directly, no inflation required!
     * We only inflate a new View when the convertView is null.
     */
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.mylist, null);

            System.out.println("INFLATE VIEW");

            view.setPadding(0,10,0,10);
            // Create a ViewHolder and store references to the two children views
            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.nameList);
            holder.extratxt = (TextView) view.findViewById(R.id.question);
            holder.idfacebook = (TextView) view.findViewById(R.id.facebookId);
            holder. imageView = (ImageView) view.findViewById(R.id.imgList);
            holder.imgResponse = (ImageView) view.findViewById(R.id.imgResponse);
            holder.timestamp = (TextView) view.findViewById(R.id.dateQuestionProfile);


            // The tag can be any Object, this just happens to be the ViewHolder
            view.setTag(holder);



        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            System.out.println("NO INFLATE VIEW");
            view=convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind that data efficiently!
        //holder.txtTitle.setTypeface(tf);

        holder.txtTitle.setTextColor(Color.BLACK);
        holder.txtTitle.setText(this.rows.get(position).getNameSurname());
        //holder.imageView.setImageResource(R.mipmap.iconuseranonymous);
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).fit().centerCrop().into(holder.imageView);

        //holder.extratxt.setTypeface(tf);

        holder.extratxt.setText(this.rows.get(position).getQuestion());

        holder.idfacebook.setText(this.rows.get(position).getFacebookId());

        holder.timestamp.setText(this.rows.get(position).getTime());

        holder.txtTitle.setOnClickListener(new OnClickListenerProfile(holder.idfacebook.getText().toString(), this.context));
        holder.imageView.setOnClickListener(new OnClickListenerProfile(holder.idfacebook.getText().toString(), this.context));

        if(this.rows.get(position).getResultQuestion()==true){
            holder.imgResponse.setImageResource(R.drawable.heart_green);

            //holder.txtTitle.setCompoundDrawables(null,null,context.getResources().getDrawable(R.drawable.heart_green),null);

        }else{
            //holder.txtTitle.setCompoundDrawables(null,null,context.getResources().getDrawable(R.drawable.heart_red),null);

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