package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;

import java.sql.RowIdLifetime;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<RowItemProfile> {

    private final Activity context;
    /*private final String[] itemname;
    private final Integer[] imgid;
    private final String[] questions;
    private final String[] ids;*/
    private List<RowItemProfile> rows;
    private int count = 1;
    private TextView txtTitle;
    private ImageView imageView;
    private TextView extratxt;
    private TextView idfacebook;
    private View view;

    public CustomListAdapter(Activity context, List<RowItemProfile> rows /*String[] itemname, Integer[] imgid, String[] questions, String[] ids*/) {
        super(context, R.layout.mylist, rows);
        // TODO Auto-generated constructor stub

        this.context = context;
        /*this.itemname = itemname;
        this.imgid = imgid;
        this.questions=questions;
        this.ids=ids;*/
        this.rows = rows;
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

            // Create a ViewHolder and store references to the two children views
            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.nameList);
            holder.extratxt = (TextView) view.findViewById(R.id.question);
            holder.idfacebook = (TextView) view.findViewById(R.id.facebookId);
            holder. imageView = (ImageView) view.findViewById(R.id.imgList);

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

        holder.txtTitle.setTextColor(Color.BLACK);
        holder.txtTitle.setText(this.rows.get(position).getNameSurname());
        //holder.imageView.setImageResource(R.mipmap.iconuseranonymous);
        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").fit().centerCrop().into(holder.imageView);
        holder.extratxt.setText(this.rows.get(position).getQuestion());

        holder.idfacebook.setText(this.rows.get(position).getId());

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
        int position;
    }
}