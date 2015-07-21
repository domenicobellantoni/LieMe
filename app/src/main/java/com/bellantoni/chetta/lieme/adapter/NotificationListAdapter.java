package com.bellantoni.chetta.lieme.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;

import java.util.List;


/**
 * Created by Domenico on 21/07/2015.
 */
public class NotificationListAdapter extends ArrayAdapter<NotificationItem> {

    private View view;

    private final Activity context;
    private List<NotificationItem> rows;
    private int count = 1;

    public NotificationListAdapter(Activity context, List<NotificationItem> firstRows ) {

        super(context, R.layout.mylist, firstRows);
        this.context = context;
        this.rows = firstRows;


    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        return view;
    }

    @Override
    public NotificationItem getItem(int position){
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

        ImageView imageView;
        TextView standardText;

        int position;
    }
}
