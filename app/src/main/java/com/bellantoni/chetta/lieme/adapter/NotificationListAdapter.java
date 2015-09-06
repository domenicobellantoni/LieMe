package com.bellantoni.chetta.lieme.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bellantoni.chetta.lieme.NotificationFragment;
import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.squareup.picasso.Picasso;
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
        super(context, R.layout.list_notifications, firstRows);
        this.context = context;
        this.rows = firstRows;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.list_notifications, null);

            view.setPadding(0,10,0,10);

            holder = new ViewHolder();
            holder.textNotification = (TextView) view.findViewById(R.id.textNotification);
            holder.idQuestion = (TextView) view.findViewById(R.id.idQuestion);
            holder.typeNotification = (TextView) view.findViewById(R.id.typeNotification);
            holder.imageNotification = (ImageView) view.findViewById(R.id.imageNotification);
            holder.timeNotification = (TextView) view.findViewById(R.id.timeNotification);
            holder.layout = (LinearLayout) view.findViewById(R.id.itemNotification);

            view.setTag(holder);

        } else {

            view=convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        //if(this.rows.size()==0)
        //    return view;

        int typeNotification = this.rows.get(position).getTypeNotification();

        holder.textNotification.setTextColor(Color.BLACK);
        holder.idQuestion.setText(String.valueOf(this.rows.get(position).getQuestionId()));
        holder.typeNotification.setText(String.valueOf(this.rows.get(position).getTypeNotification()));
        holder.timeNotification.setText(String.valueOf(this.rows.get(position).getTimeNotification()));
        if(typeNotification==0){
            holder.textNotification.setText(R.string.askQuestion);
            holder.imageNotification.setImageResource(R.mipmap.iconuseranonymous);
       }
        if(typeNotification==1){
            //nome da recuperare da con id notifica, quindi id utente quindi dome
            Question questionObj = NotificationFragment.findQuestionById(String.valueOf(this.rows.get(position).getAnsweredQuestionId()));
            //Contact user = ContactListFragment.findContactById(questionObj.getReceiver_id());
            holder.textNotification.setText("Answered your question");
            /*if(questionObj.getMessage_read().equalsIgnoreCase("0")){
                holder.layout.setBackgroundColor(Color.parseColor("#000000"));
            }
            System.out.println("STATO MESSAGGIO"+questionObj.getMessage_read());*/
            Picasso.with(context).load("https://graph.facebook.com/" + questionObj.getReceiver_id() + "/picture?height=115&width=115").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(holder.imageNotification);
        }
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

        ImageView imageNotification;
        TextView textNotification;
        TextView idQuestion;
        TextView typeNotification;
        TextView timeNotification;
        LinearLayout layout;
        int position;
    }
}
