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
import com.bellantoni.chetta.lieme.adapter.ListInFriendFragmentAdapter;
import com.bellantoni.chetta.lieme.adapter.NotificationListAdapter;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.util.ArrayList;
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




    public interface NotificationInterface{
        // public void goaskQuestionFragment();
        public void readQuestion(int questionId);

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




            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());


            for (int i = 0; i < 8; i++) {

                NotificationItem row = new NotificationItem(idQuestions[i]);
                this.rows.add(row);

            }


            //final CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid, questions,idfb);
            adapter = new NotificationListAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listNotifiation);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub

                    mNotificationInteface.readQuestion(adapter.getItem(position).getQuestionId());

                }
            });



        }else{
            firstAccessView = getView();
        }



        return firstAccessView;
    }


    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {
        //System.out.println("QUESTO "+adapter.getItem(firstVisible).getId());


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {
            //scaricare sempre in async task  e far vedere spiner
            this.adapter.setCount(this.adapter.getCount()+1);

            //QUI DA FARE UNA QUERY ALLA VOLTA HO PROVATO A CARICARE TIPO 8 ELEMENTI ALLA VOLTA MA CRASHA, SPERO CHE LA QUERY SIA
            //VELOCE, AL MASSIMO POSSIAMO PROVARE 2/3 ALLA VOLTA
            rows.add(new NotificationItem(545154));
            //this.adapter.addAll(this.rows);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = adapter.getItem(position).getFacebookId();
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mNotificationInteface.readQuestion(adapter.getItem(position).getQuestionId());

                }
            });


            System.out.println("CONTATORE "+ this.adapter.getCount());
            adapter.notifyDataSetChanged();
        }
    }

    public void onScrollStateChanged(AbsListView v, int s) {

        //System.out.println("CONTATORE "+ this.adapter.getCount());
        adapter.notifyDataSetChanged();


    }




}





