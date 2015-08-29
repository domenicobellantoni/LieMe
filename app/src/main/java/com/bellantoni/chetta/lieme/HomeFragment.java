package com.bellantoni.chetta.lieme;


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
import android.widget.AdapterView;
import android.widget.ListView;
import com.bellantoni.chetta.lieme.adapter.ListInHomeAdapter;
import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.ItemHome;
import com.bellantoni.chetta.lieme.generalclasses.Notification;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.bellantoni.chetta.lieme.generalclasses.TimestampComparator;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import org.ocpsoft.pretty.time.PrettyTime;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Domenico on 28/07/2015.
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    private List<ItemHome> rows;
    private SwipeRefreshLayout swipeLayout;
    private ListInHomeAdapter adapter;
    private PrettyTime p ;
    private int maximumNumberOfQuestionShownFirstTime = 20;
    private ArrayList<Notification> messages = new ArrayList<>();
    private FeedReaderDbHelperMessages mDbHelperMessages;

    String[] itemnameTo ={
            "Federico Badini",
            "Matteo Bana",
            "Alessandro Donini",
            "Nicora Elisa",
            "Massimo De Marchi",
            "Lorenzo Di tucci",
            "Davide Dipinto",
            "Leonardo Cavagnis"
    };


    String[] itemnameFrom ={
            "Filippo Zaffaroni",
            "Claudio Giorgi",
            "Alessandra Giannini",
            "Teresa Amedeo",
            "Carola Bummo",
            "Giancarlo Fiumi",
            "Davide Fioggi",
            "Brioschi Giacomo Lizzardo"
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
            "Hai mai tradito la tua ragazza?",
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

    String[] idfbFrom ={
            "id fb Pippo Zaffaroni",
            "id fb Claudio Giorgi",
            "id fb Alessandra Giannini",
            "id fb Teresa Amedeo",
            "id fb Carola Bummo",
            "id fb Giancarlo Fiumi",
            "id fb Davide Fioggi",
            "id fb Brioschi Giacomo Lizzardo"
    };


    String[] idfbTo ={
            "id fb Federico Badini",
            "id fb Matteo Bana",
            "id fb Alessandro Donini",
            "id fb fNicora Elisa",
            "id fb fMassimo De Marchi",
            "id fb Lorenzo Di tucci",
            "id fb Davide Dipinto",
            "id fb Leonardo Cavagnis"
    };


    Date[] time = {
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),


    };



    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.p = new PrettyTime(new Locale("en"));
    }

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedBundle){
        mDbHelperMessages = new FeedReaderDbHelperMessages(getActivity().getApplicationContext());
        new RetrieveMessagesFromLocalDataBase().execute(null,null,null);
        super.onCreate(savedBundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        this.rows = new ArrayList<ItemHome>();
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {

        View firstAccessView;
        if(savedBundle==null) {
            firstAccessView = inflater.inflate(R.layout.home_fragment_layout, null);
            swipeLayout = (SwipeRefreshLayout) firstAccessView.findViewById(R.id.swipe_refresh_layout);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Home");


            for (int i = 0; i < 8; i++) {

                ItemHome row = new ItemHome(questions[i], itemnameFrom[i], itemnameTo[i], idfbFrom[i],idfbTo[i], imgid[i], resultsQuestion[i], p.format(time[i]));
                this.rows.add(row);

            }

            adapter = new ListInHomeAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listHome);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);

        }else{
            firstAccessView = getView();
        }

        return firstAccessView;

    }

    @Override
    public void onRefresh() {

        fetchMovies();
    }


    private void fetchMovies() {
        swipeLayout.setRefreshing(true);
        UpdateMessages updateMessages = new UpdateMessages(mDbHelperMessages);
        new UpdateListTask().execute(null, null, null);
        this.adapter.notifyDataSetChanged();


    }


    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {
            Random random = new Random();
            /*int number = random.nextInt(2)+1;
            //scaricare sempre in async task
            this.adapter.setCount(this.adapter.getCount()+number);

            for(int i=0; i<number; i++){
                rows.add(new ItemHome("Ieri abbiamo sentito arrivare la polizia in casa tua, è vero che hanno arrestato tuo figlio?", "Giancarlo Filippetti", "Giordano Romano","id fb form","id fb to", R.id.icon, true, p.format(new Date())));
            }*/

            int count = 0;
            //QUI DA FARE UNA QUERY
            for(int i=this.rows.size()-1; i<this.messages.size(); i++) {
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
                    //ImageView profileImage ;
                    //Picasso.with(getActivity().getApplicationContext()).load("https://graph.facebook.com/" + q.getSender_id() + "/picture?height=115&width=115").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(profileImage);
                    ItemHome row = new ItemHome(q.getMessage(), senderContact.getName(), receiverContact.getName(), q.getSender_id(), q.getReceiver_id(), R.drawable.ic_profile, res, p.format(q.getNotificationTimestamp()));
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

    private class UpdateListTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
/*
            //da scaricare
            Random random = new Random();
            int number = random.nextInt(3)+1;
            for(int i=0; i<number; i++){
                ItemHome row = new ItemHome("Ieri abbiamo sentito arrivare la polizia in casa tua, è vero che hanno arrestato tuo figlio?", "Rodolfo Giano", "Filippo Cavallotti", "54ds754ds","87987dfd", 547887, true, p.format(new Date()));
                HomeFragment.this.rows.add(0,row);
            }*/

            new RetrieveMessagesFromLocalDataBase().execute(null,null,null);
            return null;
        }

        protected void onPostExecute(Void result){
            HomeFragment.this.swipeLayout.setRefreshing(false);

        }
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


    private void updateMessageArray(ArrayList<Notification> messages){
        Collections.sort(messages, new TimestampComparator());
        // Question q = (Question)messages.get(0);
        // Log.i("MESSAGGIO PRIMO: ", q.getMessage() + " RISPOSTA: " + q.getAnswer());
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
                //ImageView profileImage ;
                //Picasso.with(getActivity().getApplicationContext()).load("https://graph.facebook.com/" + q.getSender_id() + "/picture?height=115&width=115").placeholder(R.mipmap.iconuseranonymous).transform(new CircleTransform()).fit().centerCrop().into(profileImage);
                //ItemHome("Ieri abbiamo sentito arrivare la polizia in casa tua, è vero che hanno arrestato tuo figlio?", "Rodolfo Giano", "Filippo Cavallotti", "54ds754ds","87987dfd", 547887, true, p.format(new Date()));
                ItemHome row = new ItemHome(q.getMessage(), senderContact.getName(), receiverContact.getName(), q.getSender_id(), q.getReceiver_id(), R.drawable.ic_profile, res, p.format(q.getNotificationTimestamp()));
                this.rows.add(row);
            }
        }

        this.adapter.setCount(this.rows.size());
        this.adapter.notifyDataSetChanged();
    }
}
