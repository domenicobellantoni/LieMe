package com.bellantoni.chetta.lieme.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.adapter.CustomListAdapter;
import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.db.FeedReaderContractMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.drawnerActivity;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.Notification;
import com.bellantoni.chetta.lieme.generalclasses.Question;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.bellantoni.chetta.lieme.generalclasses.TimestampComparator;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.picasso.Picasso;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.ocpsoft.pretty.time.PrettyTime;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProfileFragment extends Fragment implements AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener {

    private TextView nameSurname;
    private String nameSurnameString;
    private ImageView profileImage;
    private Bitmap bitmap = null;
    private RoundImage roundedImage;
    private RoundImage roundFAB;
    private String id;
    private ImageButton FAB;
    private CustomListAdapter adapter;
    private PrettyTime p ;
    private List<RowItemProfile> rows;
    private SwipeRefreshLayout swipeLayout;
    private int maximumNumberOfQuestionShownFirstTime = 10;
    private ArrayList<Notification> messages = new ArrayList<>();

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
            "id fb Federico Badini",
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

    Date[] timeStamp = {
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date(),
            new Date()
    } ;

    // GCM
    String regid;
    GoogleCloudMessaging gcm;
    private FeedReaderDbHelperMessages mDbHelperMessages;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String SERVER_URL_REGISTRATION = "http://computersecurityproject.altervista.org/gcm_server_php/register.php";
    public static final String SERVER_URL_SEND_MESSAGE = "http://computersecurityproject.altervista.org/gcm_server_php/send_message.php";
    String SENDER_ID = "706561393502";
    /**
     * Tag used on log messages.
     */
    static final String TAG = "LieMe";

    public interface ProfileFragmentInterface{
        //public void goFriendProfile(String facebookId);
        public void goContactListFragment();

    }

    private ProfileFragmentInterface mProfileFragmentInteface;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof ProfileFragmentInterface){
            mProfileFragmentInteface = (ProfileFragmentInterface)activity;

        }
    }

    public String getIdprofile() {
        return id;
    }

    public RoundImage getRoundedImage() {
        return roundedImage;
    }

    public String getNameSurnameString() {
        return nameSurnameString;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setRoundedImage(RoundImage roundedImage) {
        this.roundedImage = roundedImage;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setNameSurnameString(String nameSurnameString) {
        this.nameSurnameString = nameSurnameString;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    public ProfileFragmentInterface getmProfileFragmentInteface() {
        return mProfileFragmentInteface;
    }

    @Override
    public void onCreate(Bundle savedBundle){

        mDbHelperMessages = new FeedReaderDbHelperMessages(getActivity().getApplicationContext());
        new RetrieveMessagesFromLocalDataBase().execute(null,null,null);
        super.onCreate(savedBundle);

        this.p = new PrettyTime(new Locale("en"));

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
            firstAccessView = inflater.inflate(R.layout.fragment_profile, null);
            swipeLayout = (SwipeRefreshLayout) firstAccessView.findViewById(R.id.swipe_refresh_layout);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            this.profileImage = (ImageView) firstAccessView.findViewById(R.id.imageProfile);

            RoundImage roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(), R.mipmap.iconuseranonymous));
            Picasso.with((Activity) getActivity()).load("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?height=115&width=115")
                    .placeholder(roundedImage)
                    .transform(new CircleTransform()).fit().centerCrop().into(this.profileImage);
            FAB = (ImageButton) firstAccessView.findViewById(R.id.fab);
            Drawable d = getResources().getDrawable(R.drawable.ic_action);
            Bitmap bitmapAction = ((BitmapDrawable) d).getBitmap();
            FAB.setImageDrawable(new RoundImage(bitmapAction));
            FAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mProfileFragmentInteface.goContactListFragment();
                }
            });


            for (int i = 0; i < 8; i++) {

                RowItemProfile row = new RowItemProfile(questions[i], itemname[i], idfb[i], imgid[i], resultsQuestion[i], p.format(timeStamp[i]));
                this.rows.add(row);
            }

            adapter = new CustomListAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);

        }else{
            firstAccessView = getView();
        }

        // GCM
        // Check device for Play Services APK.
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
            regid = getRegistrationId(getActivity().getApplicationContext());

            if (regid.isEmpty()) {
                registerInBackground();
                Log.i(TAG, " not connected");
            }
            else {
                // Here the regid is sent to the server
                // this call could be put only in the registerInBackground()
                // because if the regid is already present then the application
                // was already registered in the server
                //sendRegistrationIdToBackend();
                LoginUserToServer loginUserToServer = new LoginUserToServer();
                loginUserToServer.execute(getArguments().get("id").toString(), regid);
                //mDisplay.setText("already connected with id: " + regid);
                Log.i(TAG, "already connected with id: " + regid);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        return firstAccessView;
    }

    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {
            Random random = new Random();
            //scaricare sempre in async task  e far vedere spiner

            int count = 0;
            //QUI DA FARE UNA QUERY
            for(int i=this.rows.size()-1; i<this.messages.size()-1; i++) {
                Question q = (Question)messages.get(i);
                if(!q.getAnswer().equals("undefined"))
                {
                    boolean res = true;
                    if(q.getAnswer().equals("no"))
                        res = false;
                    Contact senderContact = ContactListFragment.findContactById(q.getSender_id());
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

    // GCM
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getActivity().getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(TAG, msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // this method persists the registration ID in shared preferences, but
        return context.getSharedPreferences(drawnerActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void sendRegistrationIdToBackend() {

    }

    private class LoginUserToServer extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "Processing server login with user id:  " + params[0] + " regid: " + params[1]);
            String msg = "";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(SERVER_URL_REGISTRATION);
            try {
                List<NameValuePair> parameter = new ArrayList<>(1);
                parameter.add(new BasicNameValuePair("userId", params[0]));
                parameter.add(new BasicNameValuePair("regId", regid));

                post.setEntity(new UrlEncodedFormEntity(parameter));

                HttpResponse resp = client.execute(post);

            } catch (IOException e) {
                msg = "Error :" + e.getMessage();
            }
            Log.i(TAG, msg);
            return null;
        }
    }

    @Override
    public void onRefresh() {

        fetchMovies();
    }

    private void fetchMovies() {
        swipeLayout.setRefreshing(true);
        UpdateMessages updateMessages = new UpdateMessages(mDbHelperMessages);
        updateMessages.update(Profile.getCurrentProfile().getId());
        new UpdateListTask().execute(null, null, null);

        this.adapter.notifyDataSetChanged();


    }

    private class UpdateListTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            new RetrieveMessagesFromLocalDataBase().execute(null,null,null);
            return null;
        }

        protected void onPostExecute(Void result){
           ProfileFragment.this.swipeLayout.setRefreshing(false);
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
                    FeedReaderContractMessages.FeedEntry.COLUMN_NAME_RECEIVER_ID + "=?",                               // The columns for the WHERE clause
                    new String[]{String.valueOf(Profile.getCurrentProfile().getId())},                            // The values for the WHERE clause
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
                RowItemProfile row = new RowItemProfile(q.getMessage(), senderContact.getName(), q.getSender_id(), R.drawable.ic_profile, res, p.format(q.getNotificationTimestamp()));
                this.rows.add(row);
            }
        }
        this.adapter.setCount(this.rows.size());
        this.adapter.notifyDataSetChanged();
    }
}
