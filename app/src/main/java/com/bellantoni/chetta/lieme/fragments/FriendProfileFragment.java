package com.bellantoni.chetta.lieme.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.adapter.ListInFriendFragmentAdapter;
import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.Notification;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.bellantoni.chetta.lieme.generalclasses.TimestampComparator;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.facebook.FacebookSdk;

import org.ocpsoft.pretty.time.PrettyTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by Domenico on 30/05/2015.
 */
public class FriendProfileFragment extends Fragment implements AbsListView.OnScrollListener ,SwipeRefreshLayout.OnRefreshListener {

    private String facebookId;
    private ImageButton imageButtonBack;
    private TextView nameSurnameFriend;
    private ImageView friendProfileImage;
    private ListInFriendFragmentAdapter adapter;
    private List<RowItemProfile> rows;
    private SwipeRefreshLayout swipeLayout;
    private PrettyTime p;
    private int maximumNumberOfQuestionShownFirstTime = 10;
    private ArrayList<Notification> messages = new ArrayList<>();
    private FeedReaderDbHelperMessages mDbHelperMessages;
    private String friendId;
    ListView list;

    String[] itemname ={
            "Federico Badini",
            "Matteo Bana",
            "Alessandro Donini",
            "Nicora Elisa",
            "Massimo De Marchi",
            "Lorenzo Di tucci",
            "Davide Dipinto",
            "Leonardo Cavagnis"
    };
    String[] idfb ={
            "Federico Badini",
            "id fb Matteo Bana",
            "id fb Alessandro Donini",
            "id fb fNicora Elisa",
            "id fb fMassimo De Marchi",
            "id fb Lorenzo Di tucci",
            "id fb Davide Dipinto",
            "id fb Leonardo Cavagnis"
    };

    Integer[] imgid={
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,

    };
    String[] questions={
            "Hai mai tradito la tua ragazzappp?",
            "Hai mai rubato in un supermercato?",
            "Ieri quando eri ubriaco, è vero che hai litigato con due ragazzi in discoteca?",
            "Hai copiato all'esame di ieri?",
            "Tua mamma sa che le rubi 10 euro dal portafogli ogni settimana?",
            "domanda iusgdilsyahgdlsjgdilys",
            "domanda sidhiofygejbdjkgdjskldgysjgdxb",
            "domanda lhdshdkjòsahdkasjdjqgdjshbjgd",
    };

    Boolean[] resultsQuestion ={
            true,
            false,
            true,
            false,
            false,
            false,
            true,
            false,
    };

    Date[] timeStamp ={

        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date()

    };




    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    public FriendProfileFragment(){

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedBundle){
        mDbHelperMessages = new FeedReaderDbHelperMessages(getActivity().getApplicationContext());

        super.onCreate(savedBundle);
        p = new PrettyTime(new Locale("en"));
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        this.rows = new ArrayList<RowItemProfile>();
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
            firstAccessView = inflater.inflate(R.layout.friend_profile_fragment_layout, null);
            swipeLayout = (SwipeRefreshLayout) firstAccessView.findViewById(R.id.swipe_refresh_layout);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            this.friendId = getArguments().getString("facebookIdFriend");

            Contact friendContact = ContactListFragment.findContactById(String.valueOf(this.friendId));

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(friendContact.getName());
            new RetrieveMessagesFromLocalDataBase().execute(this.friendId,null,null);


            for (int i = 0; i < 8; i++) {

                RowItemProfile row = new RowItemProfile(questions[i], itemname[i], idfb[i], imgid[i], resultsQuestion[i], p.format(timeStamp[i]));
                this.rows.add(row);

            }


            adapter = new ListInFriendFragmentAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listQuestionFriendProfile);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);

        }else{
            firstAccessView = getView();
        }
        return firstAccessView;
    }

    private void fetchMovies() {
        swipeLayout.setRefreshing(true);
        UpdateMessages updateMessages = new UpdateMessages(mDbHelperMessages);
        new RetrieveMessagesFromLocalDataBase().execute(this.friendId,null,null);
        this.adapter.notifyDataSetChanged();
    }

    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {

            int count = 0;

            for(int i=this.rows.size(); i<this.messages.size()-1; i++) {
                Question q = (Question)messages.get(i);
                if(!q.getAnswer().equals("undefined"))
                {
                    boolean res = true;
                    if(q.getAnswer().equals("no"))
                        res = false;
                    Contact senderContact = ContactListFragment.findContactById(q.getSender_id());
                    Contact receiverContact = ContactListFragment.findContactById(q.getReceiver_id());
                    if(receiverContact==null)
                        receiverContact = new Contact("","Name not found","","");
                    if(senderContact==null)
                        senderContact = new Contact("","Name not found","","");
                    RowItemProfile row = new RowItemProfile(q.getMessage(), senderContact.getName(), q.getSender_id(), R.drawable.ic_profile, res, p.format(q.getNotificationTimestamp()));
                    this.rows.add(row);
                    count++;
                }
                if(count>3)
                    break;
            }
            this.adapter.setCount(this.adapter.getCount()+count);

            adapter.notifyDataSetChanged();
        }
    }

    public void onScrollStateChanged(AbsListView v, int s) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        fetchMovies();
    }

    private class UpdateListTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //da scaricare
            Random random = new Random();
            int number = random.nextInt(3)+1;
            for(int i=0; i<number; i++){
                RowItemProfile row = new RowItemProfile("Pippo", "Pippo", "id fb Pippo", R.id.icon, true, p.format(new Date()));
                FriendProfileFragment.this.rows.add(0,row);
            }


            return null;
        }

        protected void onPostExecute(Void result){
           FriendProfileFragment.this.swipeLayout.setRefreshing(false);

        }
    }

    private class RetrieveMessagesFromLocalDataBase extends AsyncTask<String, Void, Void> {
        private Cursor c;
        private ArrayList<Notification> messages;

        @Override
        protected Void doInBackground(String... params) {
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
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID + "=?",                               // The columns for the WHERE clause
                    new String[]{String.valueOf(params[0])},                            // The values for the WHERE clause
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


    private void updateMessageArray(ArrayList<Notification> messages){
        Collections.sort(messages, new TimestampComparator());
        this.messages = messages;
        this.rows.clear();

        for(int i = 0; i < maximumNumberOfQuestionShownFirstTime && i < messages.size(); i++)
        {
            Question q = (Question)messages.get(i);
            if(!q.getAnswer().equals("undefined"))
            {
                boolean res = true;
                if(q.getAnswer().equals("no"))
                    res = false;
                Contact senderContact = ContactListFragment.findContactById(q.getSender_id());
                Contact receiverContact = ContactListFragment.findContactById(q.getReceiver_id());
                if(receiverContact==null)
                    receiverContact = new Contact("","Name not found","","");
                if(senderContact==null)
                    senderContact = new Contact("","Name not found","","");
                RowItemProfile row = new RowItemProfile(q.getMessage(), senderContact.getName(), q.getSender_id(), R.drawable.ic_profile, res, p.format(q.getNotificationTimestamp()));
                this.rows.add(row);
            }
        }

        this.adapter.setCount(this.rows.size());
        this.adapter.notifyDataSetChanged();
    }
}




