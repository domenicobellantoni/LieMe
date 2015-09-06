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
import com.bellantoni.chetta.lieme.generalclasses.DrawerElement;
import java.util.List;

/**
 * Created by Domenico on 28/05/2015.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerElement> {


    private Activity context;
    private List<DrawerElement> list;
    private View view;
    private TextView textView;
    private ImageView imageView;
    private ImageView numberNotifications;



    public DrawerAdapter(Activity context, List<DrawerElement> list) {
        super(context, R.layout.drawnerlistlayout, list);
        this.context=context;
        this.list = list;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        view = inflater.inflate(R.layout.drawnerlistlayout, null);
        view.setPadding(0,10,0,10);

        textView = (TextView) view.findViewById(R.id.sectionDrawer);
        textView.setText(this.list.get(position).getSection());
        textView.setTextColor(Color.BLACK);

        imageView = (ImageView) view.findViewById(R.id.imgDrawerSection);
        imageView.setImageResource(this.list.get(position).getIdImg());
        numberNotifications = (ImageView) view.findViewById(R.id.numberNotifications);


        //setNumberNotification(findUnreadNotification());
        return view;
    }


    /*private int findUnreadNotification(){
        List<Notification> notifications = NotificationFragment.getAllNotifications();
        int counter = 0;
        for(int i=0; i<notifications.size(); i++){
            if(notifications.get(i).getNotificationStatus()==1)
                counter++;
        }
        return counter;
    }*/

    /*private void setNumberNotification(int counter){
        switch (counter){
            case 1:
                numberNotifications.setImageResource(R.drawable.ic_icon_1);
            case 2:
                numberNotifications.setImageResource(R.drawable.ic_icon_2);
            case 3:
                numberNotifications.setImageResource(R.drawable.ic_icon_3);
            case 4:
                numberNotifications.setImageResource(R.drawable.ic_icon_4);
            case 5:
                numberNotifications.setImageResource(R.drawable.ic_icon_5);
            default:
                numberNotifications.setImageResource(R.drawable.ic_icon_6);
        }
    }*/
}
