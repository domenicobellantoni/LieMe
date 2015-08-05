package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.bellantoni.chetta.lieme.adapter.NotificationListAdapter;
import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.generalclasses.Notification;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.facebook.FacebookSdk;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Domenico on 30/05/2015.
 */
public class NotificationFragment extends Fragment implements AbsListView.OnScrollListener {
    /**
     * Tag used on log messages.
     */
    static final String TAG = "Notification Fragment";

    private NotificationListAdapter adapter;
    private List<NotificationItem> rows;
    private FeedReaderDbHelperMessages mDbHelper;
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
        public void readQuestion(String questionId);
        public void readAnswer(String questionId );

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

            new RetrieveMessagesFromLocalDataBase().execute(null, null, null);



            for (int i = 0; i < 8; i++) {
                NotificationItem row = new NotificationItem(String.valueOf(idQuestions[i]), typeNotifications[i], stateNotification[i], new Timestamp(timeNotifications[i]));
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


            //rows.add(new NotificationItem(545154,0,1, new Date().getTime()));
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

    private void updateList(ArrayList<Question> notifications){
        this.rows.clear();

        for(Object o: notifications){
            Notification n = (Notification)o;
            // If notification is a Question
            if(n.getNotificationType() == 0)
            {
                Question q = (Question) n;
                NotificationItem row = new NotificationItem(q.getId(), q.getNotificationType(), q.getNotificationStatus(), q.getNotificationTimestamp());
                this.rows.add(row);
                Log.i(TAG, "Message retrieved from DB:" + q.getMessage());
            }

        }

        adapter.notifyDataSetChanged();
    }

    private class RetrieveMessagesFromLocalDataBase extends AsyncTask<Void, Void, Void> {
        private Cursor c;
        private ArrayList<Question> messages;

        @Override
        protected Void doInBackground(Void... params) {
            messages = new ArrayList<>();

            mDbHelper = new FeedReaderDbHelperMessages(getActivity().getApplicationContext());

            String[] projection = {
                    FeedReaderContractMessages.FeedEntry._ID,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP
            };

            SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();

            c = dbReader.query(
                    FeedReaderContractMessages.FeedEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                               // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP+" DESC"                                 // The sort order
            );

            if(c != null){
                if(c.moveToFirst()){
                    do{
                        String id = c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry._ID));
                        String sender_id = c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID));
                        String receiver_id = c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID));
                        String message_read = c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ));
                        String message = c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE));
                        Timestamp timestamp = Timestamp.valueOf(c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP)));
                        messages.add(new Question(id, sender_id, receiver_id, message_read, message, timestamp));
                    }while(c.moveToNext());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateList(messages);
        }
    }



}





