package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;

import com.bellantoni.chetta.lieme.dialog.DialogQuestionAnswered;
import com.bellantoni.chetta.lieme.dialog.LogoutDialog;
import com.bellantoni.chetta.lieme.dialog.QuestionDialog;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class drawnerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ProfileFragment.ProfileFragmentInterface, LogoutDialog.LogoutInterface, QuestionDialog.QuestionInterface, FriendProfileFragment.FriendProfileFragmentInterface, ContactListFragment.ContactListFragmentInterface, ContactListFragment.OnFragmentInteractionListener, NotificationFragment.NotificationInterface, HomeFragment.HomeFragmentInterface {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String surname;
    private String name;
    private String id;
    private Intent intent;
    private String photo1, photo2;
    private ProfileFragment profileFragment;
    private Intent serviceIntent;
    private FriendProfileFragment friendProfileFragment;
    private NotificationFragment notificationFragment;
    private QuestionDialog questionDialog;
    private DialogQuestionAnswered dialogQuestionAnswered;
    private HomeFragment homefragment;
    private List<String> titlesActionbar = new ArrayList<String>();

    private final String TAG = "DrawnerActivity";
    private final String ANSWER_MANAGER_URL = "http://computersecurityproject.altervista.org/gcm_server_php/answerToQuestion.php?";


    private AskFragment askFragment;
    private ContactListFragment contactListFragment;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawner);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        this.actionBar = getSupportActionBar();
        this.actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f59200")));
        this.actionBar.setTitle(Profile.getCurrentProfile().getFirstName() + " " + Profile.getCurrentProfile().getLastName());

        this.intent = getIntent();
        if(intent.getStringExtra("idfromlogin")!=null) {
            this.surname = intent.getStringExtra("surnamefromlogin");
            this.name = intent.getStringExtra("namefromlogin");
            this.id = intent.getStringExtra("idfromlogin");
        }

        if(intent.getStringExtra("idfromsplash")!=null) {
            this.surname = intent.getStringExtra("surnamefromsplash");
            this.name = intent.getStringExtra("namefromsplash");
            this.id = intent.getStringExtra("idfromsplash");
        }

        this.photo1 = "https://graph.facebook.com/";
        this.photo2 = "/picture?height=105&width=105";

        ContactListFragment.downloadFriends(this);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position){
            case 0:
                goProfile();
                break;
            case 1:
                goHome();
                break;
            case 2:
                goContactListFragment();
                // goAskQuestion(null);
                break;
            case 3:
                goNotifications();
                break;
            case 4:
                logout();
                break;

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.drawner, menu);

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){

        LogoutDialog logoutDialog = new LogoutDialog();
        logoutDialog.show(getSupportFragmentManager(), "PROGRESS_DIALOG");
    }

    @Override
    public void yesPressed(){
        LoginManager.getInstance().logOut();
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void noPressed(){
        //nothing to do
    }



      ///se un istanza del profilo è già stata creata imposto e basta
      //ho creato tutti i metodi necessari di set e get nel profile fragment
        //per la rotazione guadrare il manufest tag conf in questa attività
    private void goProfile(){



        this.actionBar = getSupportActionBar();
        actionBar.setTitle(Profile.getCurrentProfile().getFirstName()+" "+Profile.getCurrentProfile().getLastName());
        //this.actionBar.hide();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        if(this.profileFragment==null) {
            Profile profile = Profile.getCurrentProfile();
            this.name = profile.getFirstName();
            this.surname = profile.getLastName();
            this.id = profile.getId();
            this.photo1 = "https://graph.facebook.com/";
            this.photo2 = "/picture?height=105&width=105";

            Bundle bundle = new Bundle();
            bundle.putString("name", this.name);
            bundle.putString("surname", this.surname);
            bundle.putString("photo1", this.photo1);
            bundle.putString("photo2", this.photo2);
            bundle.putString("id", this.id);
            this.profileFragment = new ProfileFragment();
            this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            profileFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, profileFragment, "ProfileFragment").addToBackStack("ProfileFragment")
                    .commit();
        }else{
            this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            this.profileFragment.setNameSurnameString(this.profileFragment.getNameSurnameString());
            this.profileFragment.setBitmap(this.profileFragment.getBitmap());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, profileFragment, "ProfileFragment").addToBackStack("ProfileFragmemt")
                    .commit();
        }


    }
/*
    @Override
    public void goaskQuestionFragment(){
        goAskQuestion();
    }

    private void goAskQuestion(){
        // The ask fragment will be called always after the selection of the receiver
        Bundle bundle = new Bundle();
        bundle.putString("edttext", "From Activity");

        this.actionBar.show();

        if(this.askFragment==null) {
            this.askFragment = new AskFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, this.askFragment,"AskFragment")
                    .commit();

        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, this.askFragment, "AskFragment")
                    .commit();

        }
    }
*/
    private void goAskQuestion( String receiver_id){
        // The ask fragment will be called always after the selection of the receiver
        Bundle bundle = new Bundle();
        bundle.putString("receiver", receiver_id);
        bundle.putString("facebook_id", this.id);

        this.actionBar.show();
        this.actionBar.setTitle("Ask a Question");


        if(this.askFragment==null) {
            this.askFragment = new AskFragment();
            this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            Log.i(TAG, bundle.toString());
            this.askFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.container, this.askFragment, "AskFragment").addToBackStack("AskFragment")
                    .commit();

        }else{
            this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            this.askFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, this.askFragment, "AskFragment").addToBackStack("AskFragment")
                    .commit();

        }
    }

    //se c'è solo un fragment chiudo l'attività
    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            this.actionBar.setTitle(titlesActionbar.get(titlesActionbar.size() - 1));
            this.titlesActionbar.remove(titlesActionbar.size() - 1);
            super.onBackPressed();

        }
    }


    private void goHome(){

       if(this.homefragment==null){
           this.homefragment = new HomeFragment();
           this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));

           FragmentManager fragmentManager = getSupportFragmentManager();

           fragmentManager.beginTransaction()
                   .add(R.id.container, this.homefragment, "HomeFragment").addToBackStack("HomeFragment")
                   .commit();


       }else{
           this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));

           FragmentManager fragmentManager = getSupportFragmentManager();

           fragmentManager.beginTransaction()
                   .add(R.id.container, this.homefragment, "HomeFragment").addToBackStack("HomeFragment")
                   .commit();

       }

    }

    @Override
    public void goFriendProfile(String facebookId){



            Bundle bundle=new Bundle();
            bundle.putString("facebookIdFriend", facebookId);

            this.friendProfileFragment = new FriendProfileFragment();
        this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            this.friendProfileFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container, friendProfileFragment, "FriendProfileFragment").addToBackStack("FriendProfileFragment")
                    .commit();

    }

    @Override
    public void goFriendProfileFromFriend(String facebookId){



        Bundle bundle=new Bundle();
        bundle.putString("facebookIdFriend", facebookId);

        this.friendProfileFragment = new FriendProfileFragment();
        this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
        this.friendProfileFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, friendProfileFragment, "FriendProfileFragment").addToBackStack("FriendProfileFragment")
                .commit();

    }

    @Override
    public void onFragmentInteraction(String id) {
        goAskQuestion(id);
    }

    @Override
    public void goContactListFragment() {
        contactListFragment();
    }

    private void contactListFragment(){
        this.actionBar.show();

        if(this.contactListFragment==null) {
            this.contactListFragment = new ContactListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.container, this.contactListFragment,"ContactListFragment").addToBackStack("ContactListFragment")
                    .commit();

        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, this.contactListFragment, "ContactListFragment").addToBackStack("ContactListFragment")
                    .commit();

        }
    }

    private void goNotifications(){



        if(this.notificationFragment==null){
            this.notificationFragment = new NotificationFragment();
            this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, this.notificationFragment,"NotificationFragment").addToBackStack("NotificationFragment")
                    .commit();

        }else{
            this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, this.notificationFragment, "NotificationFragment").addToBackStack("NotificationFragment")
                    .commit();
        }

    }

    @Override
    public void readQuestion(String questionId){

        System.out.println("DOMANDA CLICCATA");
        this.questionDialog = new QuestionDialog();

        Bundle args = new Bundle();
        args.putInt("questionId", Integer.valueOf(questionId));
        questionDialog.setArguments(args);
        questionDialog.show(getSupportFragmentManager(), "QUESTION_DIALOG");
    }

    @Override
    public void readAnswer(String questionId){

        this.dialogQuestionAnswered = new DialogQuestionAnswered();
        Bundle args = new Bundle();
        args.putInt("questionId", Integer.valueOf(questionId));
        this.dialogQuestionAnswered.setArguments(args);
        dialogQuestionAnswered.show(getSupportFragmentManager(), "READ_ANSWER_DIALOG");

    }

    @Override
    public void yesQuestionPressed(int idQuestion){
        new SendAnswerToServer().execute(Profile.getCurrentProfile().getId(), String.valueOf(idQuestion), "yes");
    }

    @Override
    public void noQuestionPressed(int idQuestion){
        //STORE THE ANSWER
    }


    @Override
    public void goFriendProfileFromHome(String facebookId){

        Bundle bundle=new Bundle();
        bundle.putString("facebookIdFriend", facebookId);

        this.friendProfileFragment = new FriendProfileFragment();
        this.titlesActionbar.add(String.valueOf(actionBar.getTitle()));
        this.friendProfileFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, friendProfileFragment, "FriendProfileFragment").addToBackStack("FriendProfileFragment")
                .commit();

    }



    private class SendAnswerToServer extends AsyncTask<String,String,String> {

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
            String userId = params[0];
            String questionId = params[1];
            String answer = params[2];

            Log.i(TAG, "Answering: usr:" + userId + " qId:" + questionId + " ans:" + answer);
            String msg = "";

            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet();
            try {
                get.setURI(new URI(ANSWER_MANAGER_URL+"user_id="+userId+"&question_id="+questionId+"&answer="+answer));
                HttpResponse resp = client.execute(get);

            } catch (IOException | URISyntaxException e) {
                msg = "Error :" + e.getMessage();
                Log.i(TAG, "Error: " + msg);
            }



            return null;
        }
    }

}
