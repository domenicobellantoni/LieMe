package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.bellantoni.chetta.lieme.adapter.NotificationListAdapter;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Domenico on 30/05/2015.
 */
public class NotificationFragment extends Fragment implements AbsListView.OnScrollListener {


    private NotificationListAdapter adapter;
    private List<NotificationItem> rows;

    ListView list;

    int[] idQuestions={
            465465135,
            687468787,
            687878,
            78747687,
            1587687,
            56487876,
            687687876,
            587687

    };

    int[] typeNotifications = {
            0,
            0,
            1,
            0,
            2,
            1,
            0,
            1
    };

    int[] stateNotification = {
            0,
            0,
            0,
            1,
            1,
            1,
            1,
            1

    };

    long[] timeNotifications = {
            new Date().getTime(),
            new Date().getTime(),
            new Date().getTime(),
            new Date().getTime(),
            new Date().getTime(),
            new Date().getTime(),
            new Date().getTime(),
            new Date().getTime(),
    };




    public interface NotificationInterface{
        // public void goaskQuestionFragment();
        public void readQuestion(int questionId);
        public void readAnswer(int questionId );

    }

    private NotificationInterface mNotificationInteface;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof NotificationInterface){
            mNotificationInteface = (NotificationInterface)activity;

        }
    }

    public NotificationFragment(){

    }

    public NotificationInterface mNotificationInteface() {
        return mNotificationInteface;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        this.rows = new ArrayList<NotificationItem>();
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {

        View firstAccessView;
        if(savedBundle==null) {
            firstAccessView = inflater.inflate(R.layout.notification_fragment_layout, null);

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Notifications");


            for (int i = 0; i < 8; i++) {

                NotificationItem row = new NotificationItem(idQuestions[i], typeNotifications[i], stateNotification[i], timeNotifications[i]);
                this.rows.add(row);

            }


            adapter = new NotificationListAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listNotification);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    if(adapter.getItem(position).getTypeNotification()==0) {
                        mNotificationInteface.readQuestion(adapter.getItem(position).getQuestionId());
                    }
                    if(adapter.getItem(position).getTypeNotification()==1){
                        mNotificationInteface.readAnswer(adapter.getItem(position).getQuestionId());

                    }

                }
            });



        }else{
            firstAccessView = getView();

        }



        return firstAccessView;
    }


    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {



        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {

            this.adapter.setCount(this.adapter.getCount()+1);


            rows.add(new NotificationItem(545154,0,1, new Date().getTime()));
            //this.adapter.addAll(this.rows);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    if(adapter.getItem(position).getTypeNotification()==0) {
                        //prima di leggere la domanda qui devo controllare se ho un dispositivo bluetooth attaccato
                        mNotificationInteface.readQuestion(adapter.getItem(position).getQuestionId());
                    }
                    if(adapter.getItem(position).getTypeNotification()==1){
                        mNotificationInteface.readAnswer(adapter.getItem(position).getQuestionId());

                    }

                }
            });


            //System.out.println("CONTATORE "+ this.adapter.getCount());
            adapter.notifyDataSetChanged();
        }
    }

    public void onScrollStateChanged(AbsListView v, int s) {

        //System.out.println("CONTATORE "+ this.adapter.getCount());
        adapter.notifyDataSetChanged();


    }




}





