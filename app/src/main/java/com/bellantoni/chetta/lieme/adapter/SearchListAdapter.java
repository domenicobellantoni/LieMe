package com.bellantoni.chetta.lieme.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Domenico on 03/09/2015.
 */
public class SearchListAdapter extends ArrayAdapter<Contact>{

    private View view;
    private final Activity context;
    private List<Contact> rows;
    private int count = 1;


    public SearchListAdapter(Activity context, List<Contact> rows){

        super(context, R.layout.list_contacts, rows);
        this.context = context;
        this.rows = rows;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.list_contacts, null);
            view.setPadding(0,10,0,10);

            holder = new ViewHolder();
            holder.imageContact = (ImageView)view.findViewById(R.id.imageContact);
            holder.nameSurnameContact = (TextView) view.findViewById(R.id.nameSurnameContact);
            holder.idContact = (TextView) view.findViewById(R.id.idContact);

            view.setTag(holder);
        }else{
            view=convertView;
            holder = (ViewHolder) convertView.getTag();

            holder.nameSurnameContact.setTextColor(Color.BLACK);
            holder.idContact.setText(this.rows.get(position).getFacebook_id());
            Picasso.with(context).load("https://graph.facebook.com/" + this.rows.get(position).getFacebook_id() + "/picture?height=115&width=115").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(holder.imageContact);

        }

        return view;
    }


    @Override
    public Contact getItem(int position){
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

        ImageView imageContact;
        TextView nameSurnameContact;
        TextView idContact;
        int position;

    }


}
