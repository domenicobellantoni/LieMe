package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements AbsListView.OnScrollListener {

    private TextView nameSurname;
    private String nameSurnameString;
    private ImageView profileImage;
    private Bitmap bitmap = null;
    private RoundImage roundedImage;
    private RoundImage roundFAB;
    private String id;
    private ImageButton FAB;
    private CustomListAdapter adapter;




    private List<RowItemProfile> rows;



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
            "domand fhuhfskjdjksw",
            "domanda dkhsifgilfgilfguyeguyeg",
            "domanda sdgiyodgtwoedjwkldlywgdlwigdhlwkd",
            "domanda hdsdghs",
            "domanda sihdguydgweudgwjhgdjwhgdjshgdsgdhlsgdjs",
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

    // GCM
    String regid;
    GoogleCloudMessaging gcm;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String SERVER_URL = "http://computersecurityproject.altervista.org/gcm_server_php/register.php";
    String SENDER_ID = "706561393502";
    /**
     * Tag used on log messages.
     */
    static final String TAG = "LieMe";

    public interface ProfileFragmentInterface{
        public void goaskQuestionFragment();
        public void goFriendProfile(String facebookId);
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
        super.onCreate(savedBundle);
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
            this.profileImage = (ImageView) firstAccessView.findViewById(R.id.imageProfile);
            this.nameSurname = (TextView) firstAccessView.findViewById(R.id.nameSurname);
            //this.nameSurname.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/OpenSans-Light.ttf"));
            this.nameSurnameString = getArguments().getString("name") + " " + getArguments().getString("surname");
            this.nameSurname.setText(nameSurnameString);
            //DownloaderProfileImage downloaderProfileImage = new DownloaderProfileImage();
            //downloaderProfileImage.execute(getArguments().getString("photo1") + getArguments().get("id") + getArguments().getString("photo2"));
            RoundImage roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(), R.mipmap.iconuseranonymous));
            Picasso.with((Activity) getActivity()).load("https://graph.facebook.com/" + Profile.getCurrentProfile().getId() + "/picture?height=105&width=105")
                    .placeholder(roundedImage)
                    .transform(new CircleTransform()).fit().centerCrop().into(this.profileImage);
            FAB = (ImageButton) firstAccessView.findViewById(R.id.fab);
            Drawable d = getResources().getDrawable(R.drawable.ic_action);
            Bitmap bitmapAction = ((BitmapDrawable) d).getBitmap();
            FAB.setImageDrawable(new RoundImage(bitmapAction));
            FAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mProfileFragmentInteface.goaskQuestionFragment();
                    mProfileFragmentInteface.goContactListFragment();
                }
            });

            //la lista andrà creata in un async task e nel frattempo mostrato uno spin
            for (int i = 0; i < 8; i++) {

                RowItemProfile row = new RowItemProfile(questions[i], itemname[i], idfb[i], imgid[i], resultsQuestion[i]);
                this.rows.add(row);

            }


            //final CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid, questions,idfb);
            adapter = new CustomListAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = itemname[+position] + idfb[+position];
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mProfileFragmentInteface.goFriendProfile(adapter.getItem(position).getFacebookId());

                }
            });


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
        //System.out.println("QUESTO "+adapter.getItem(firstVisible).getId());


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {
            //scaricare sempre in async task  e far vedere spiner
            this.adapter.setCount(this.adapter.getCount()+1);

            //QUI DA FARE UNA QUERY ALLA VOLTA HO PROVATO A CARICARE TIPO 8 ELEMENTI ALLA VOLTA MA CRASHA, SPERO CHE LA QUERY SIA
            //VELOCE, AL MASSIMO POSSIAMO PROVARE 2/3 ALLA VOLTA
            rows.add(new RowItemProfile("Pippo", "Pippo", "id fb Pippo", R.id.icon, true));
            //this.adapter.addAll(this.rows);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = adapter.getItem(position).getFacebookId();
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mProfileFragmentInteface.goFriendProfile(adapter.getItem(position).getFacebookId());

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




    /*private class DownloaderProfileImage extends AsyncTask<String,String,Bitmap> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(), R.mipmap.iconuseranonymous));
            profileImage.setImageDrawable(roundedImage);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                roundedImage = new RoundImage(bitmap);
                profileImage.setImageDrawable(roundedImage);
            }

        }


        @Override
        protected Bitmap doInBackground(String... linkphoto) {

            URL imageURL = null;

            try {
                imageURL = new URL(linkphoto[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }*/

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
            HttpPost post = new HttpPost(SERVER_URL);
            try {
                List<NameValuePair> parameter = new ArrayList<>(1);
                parameter.add(new BasicNameValuePair("userId", params[1]));
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







}
