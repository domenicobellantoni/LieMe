package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.bellantoni.chetta.lieme.db.FeedReaderContractNotification;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperNotification;
import com.bellantoni.chetta.lieme.generalclasses.Notification;
import com.bellantoni.chetta.lieme.generalclasses.NotificationImpl;
import com.bellantoni.chetta.lieme.generalclasses.NotificationItem;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.bellantoni.chetta.lieme.generalclasses.TimestampComparator;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.bellantoni.chetta.lieme.network.UpdateNotifications;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.pretty.time.PrettyTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Domenico on 30/05/2015.
 */
public class NotificationFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener  {
    /**
     * Tag used on log messages.
     */
    static final String TAG = "Notification Fragment";

    private NotificationListAdapter adapter;
    private List<NotificationItem> rows;
    private FeedReaderDbHelperMessages mDbHelperMessages;
    private PrettyTime p;
    private static FeedReaderDbHelperNotification mDbHelperNotifications;
    public static ArrayList<Notification> allMessages;
    public static ArrayList<Notification> allNotifications;
    private UpdateNotifications updateNotifications;
    private SwipeRefreshLayout swipeLayout;
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

    Date[] timeNotifications = {
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date()

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
        p =  new PrettyTime(new Locale("en"));
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mDbHelperMessages = new FeedReaderDbHelperMessages(getActivity().getApplicationContext());
        mDbHelperNotifications = new FeedReaderDbHelperNotification(getActivity().getApplicationContext());

        updateNotifications = new UpdateNotifications(mDbHelperNotifications, mDbHelperMessages);


        this.rows = new ArrayList<NotificationItem>();
        for (int i = 0; i < 8; i++) {
            NotificationItem row = new NotificationItem(String.valueOf(idQuestions[i]), typeNotifications[i], stateNotification[i], p.format(timeNotifications[i]));
            this.rows.add(row);
        }
        this.allMessages = new ArrayList<>();
        this.allNotifications = new ArrayList<>();
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void update(){

        new RetrieveMessagesFromLocalDataBase().execute(null, null, null);
        new RetrieveNotificationsFromLocalDataBase().execute(null,null,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {

        View firstAccessView;
        if(savedBundle==null) {
            firstAccessView = inflater.inflate(R.layout.notification_fragment_layout, null);
            swipeLayout = (SwipeRefreshLayout) firstAccessView.findViewById(R.id.swipe_refresh_layout);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Notifications");

            update();

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
                        mNotificationInteface.readAnswer(adapter.getItem(position).getAnsweredQuestionId());

                    }

                }
            });

        }else{
            firstAccessView = getView();

        }
        return firstAccessView;
    }

    private void fetchMovies() {
        swipeLayout.setRefreshing(true);
        //QUI DA FARE LA QUERY
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        fetchMovies();
    }


    public void onScroll(AbsListView view,int firstVisible, int visibleCount, int totalCount) {

        if(this.adapter.getCount() >= this.rows.size())
            return;


        boolean loadMore = firstVisible + visibleCount >= totalCount;

        if(loadMore) {

            this.adapter.setCount(this.adapter.getCount()+1);
            Log.i(TAG, "COUNT" +this.adapter.getCount() + " list " + this.rows.size());


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
                        mNotificationInteface.readAnswer(adapter.getItem(position).getAnsweredQuestionId());

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

    private void updateList(ArrayList<Notification> notifications){
        this.rows.clear();

        for(Object o: notifications){
            Notification n = (Notification)o;
            // If notification is a Question
            if(n.getNotificationType() == 0)
            {
                Question q = (Question) n;
                if(!q.getReceiver_id().equals(Profile.getCurrentProfile().getId()))
                    continue;

                if(!questionPresent(q))
                    this.allMessages.add(q);
                else
                    Log.i(TAG, q.getMessage() + " already in the question array");
                if(!notificationPresent(n))
                    this.allNotifications.add(n);
                else
                    Log.i(TAG, q.getMessage() + " already in the notification array");

                NotificationItem row = new NotificationItem(q.getId(), q.getNotificationType(), q.getNotificationStatus(), p.format(new Date(q.getNotificationTimestamp().getTime())));
                this.rows.add(row);
                Log.i(TAG, "Message retrieved from DB:" + q.getMessage());
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateList(){
        this.rows.clear();
        Collections.sort(allNotifications, new TimestampComparator());

        for(Object o: allNotifications){
            Notification n = (Notification)o;
            // If notification is a Question
            if(n.getNotificationType() == 0)
            {
                Question q = (Question) n;
                if(!q.getReceiver_id().equals(Profile.getCurrentProfile().getId()))
                    continue;

                NotificationItem row = new NotificationItem(q.getId(), q.getNotificationType(), q.getNotificationStatus(), p.format(new Date(q.getNotificationTimestamp().getTime())));
                this.rows.add(row);
                Log.i(TAG, "Message retrieved from DB:" + q.getMessage());
            }

            // If notification is an answer
            if(n.getNotificationType() == 1)
            {
                NotificationImpl nImpl = (NotificationImpl) n;

                NotificationItem row = new NotificationItem(nImpl.getId(), nImpl.getNotificationType(), nImpl.getNotificationStatus(), p.format(new Date(nImpl.getNotificationTimestamp().getTime())));

                String content = nImpl.getContent();
                JSONObject json = null;
                try {
                    json = new JSONObject(content);
                    row.setAnsweredQuestionId(json.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                this.rows.add(row);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateNotificationArray(ArrayList<Notification> notifications){
        for(Notification n: notifications){
            if(!notificationPresent(n))
            {
                allNotifications.add(n);
                Log.i(TAG, "Notification " + n.getId() + " added in the notification array");
            }
            else
                Log.i(TAG, "Notification " + n.getId() + " already in the notification array");
        }
        updateList();
    }

    private void updateMessageArray(ArrayList<Notification> notifications){
        for(Notification n: notifications){
            Question q = (Question)n;

            if(!notificationPresent(n)){
                allNotifications.add(n);
                Log.i(TAG, "Question " + q.getMessage() + " added in the notification array");
            }
            else{
                Log.i(TAG, "Question " + q.getMessage() + " already in the notification array");
            }


            if(!questionPresent(q))
            {
                allMessages.add(q);
                Log.i(TAG, "Question " + q.getMessage() + " added in the question array");
            }
            else{
                Log.i(TAG, "Question " + q.getMessage() + " already in the question array");
            }
        }

        updateList();
    }


    private class RetrieveMessagesFromLocalDataBase extends AsyncTask<Void, Void, Void> {
        private Cursor c;
        private ArrayList<Notification> messages;

        @Override
        protected Void doInBackground(Void... params) {
            messages = new ArrayList<>();



            String[] projection = {
                    FeedReaderContractMessages.FeedEntry._ID,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_MESSAGE_READ,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_SENDER_ID,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_ANSWER,
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP
            };

            SQLiteDatabase dbReader = mDbHelperMessages.getReadableDatabase();

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
                        String answer = c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_ANSWER));
                        Timestamp timestamp = Timestamp.valueOf(c.getString(c.getColumnIndexOrThrow(FeedReaderContractMessages.FeedEntry.COLUMN_NAME_TIMESTAMP)));
                        messages.add(new Question(id, sender_id, receiver_id, message_read, message, timestamp, answer));
                    }while(c.moveToNext());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateMessageArray(messages);
        }
    }

    private class RetrieveNotificationsFromLocalDataBase extends AsyncTask<Void, Void, Void> {
        private Cursor c;
        private ArrayList<Notification> notifications;

        @Override
        protected Void doInBackground(Void... params) {
            notifications = new ArrayList<>();

            String[] projection = {
                    FeedReaderContractNotification.FeedEntry._ID,
                    FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TYPE,
                    FeedReaderContractNotification.FeedEntry.COLUMN_NAME_STATUS,
                    FeedReaderContractNotification.FeedEntry.COLUMN_NAME_CONTENT,
                    FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TIMESTAMP
            };

            SQLiteDatabase dbReader = mDbHelperNotifications.getReadableDatabase();

            c = dbReader.query(
                    FeedReaderContractNotification.FeedEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                               // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TIMESTAMP+" DESC"                                 // The sort order
            );

            if(c != null){
                if(c.moveToFirst()){
                    do{
                        String id = c.getString(c.getColumnIndexOrThrow(FeedReaderContractNotification.FeedEntry._ID));
                        String type = c.getString(c.getColumnIndexOrThrow(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TYPE));
                        String status = c.getString(c.getColumnIndexOrThrow(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_STATUS));
                        String content = c.getString(c.getColumnIndexOrThrow(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_CONTENT));
                        Timestamp timestamp = Timestamp.valueOf(c.getString(c.getColumnIndexOrThrow(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TIMESTAMP)));
                        notifications.add(new NotificationImpl(timestamp, Integer.valueOf(type), Integer.valueOf(status), content, id));
                    }while(c.moveToNext());
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateNotificationArray(notifications);
        }
    }

    public static Question findQuestionById(String id){

        for(Object n: allMessages){
            Question q = (Question)n;
            Log.i(TAG, "YYY" + q.getId());
            if(q.getId().equals(id)){
                Log.i(TAG, String.valueOf(q.getMessage()));
                return q;
            }
        }
        return new Question("nessuno", "nessuno","nessuno","nessuno","nessuno",null,"nessuno");
    }

    public static void addNotification(NotificationImpl n, Context c){
        if(mDbHelperNotifications==null)
            mDbHelperNotifications = new FeedReaderDbHelperNotification(c);
        new InsertNotification().execute(String.valueOf(n.getNotificationType()), String.valueOf(n.getNotificationStatus()), n.getContent(), String.valueOf(n.getNotificationTimestamp()));
    }

    private boolean questionPresent(Question question){
        for(Object n: allMessages){
            Question q = (Question)n;
            if(q.getId().equals(question.getId())){
                return true;
            }
        }
        return false;
    }

    private boolean notificationPresent(Notification notification){

        for(Object n: allNotifications){
            Notification nImpl = (Notification)n;

            if(nImpl.getId().equals(notification.getId()) && nImpl.getNotificationType()==notification.getNotificationType()){
                return true;
            }
        }
        return false;
    }

    private static class InsertNotification extends AsyncTask<String,String,String> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String ... params) {
            String type = params[0];
            String status = params[1];
            String content = params[2];
            String timestamp = params[3];

            SQLiteDatabase dbWriter = mDbHelperNotifications.getWritableDatabase();

            Log.i(TAG, "Inserting new notification to local DB");
            ContentValues values = new ContentValues();
            values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TYPE, type);
            values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_STATUS, status);
            values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_CONTENT, content);
            values.put(FeedReaderContractNotification.FeedEntry.COLUMN_NAME_TIMESTAMP, timestamp);

            long newRowId = dbWriter.insert(FeedReaderContractNotification.FeedEntry.TABLE_NAME,null,values);

            return null;
        }
    }
}





