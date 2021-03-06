package com.bellantoni.chetta.lieme.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperNotification;
import com.bellantoni.chetta.lieme.dialog.NetworkDialog;
import com.bellantoni.chetta.lieme.drawnerActivity;
import com.bellantoni.chetta.lieme.network.NetworkController;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.bellantoni.chetta.lieme.network.UpdateNotifications;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import java.lang.ref.WeakReference;

public class SplashActivity extends ActionBarActivity implements NetworkDialog.NetworkInfoInteface {

    private static final long MIN_WAIT_INTERVAL = 1500L;

    private static final long MAX_WAIT_INTERVAL = 3000L;

    private static final int GO_AHEAD_WHAT = 1;

    private long mStartTime = -1L;

    private boolean mIsDone;

    private Handler mHandler;

    private static final String IS_DONE_KEY = "com.bellantoni.chetta.lieme.key.IS_DONE_KEY";

    private static final String START_TIME_KEY ="com.bellantoni.chetta.lieme.key.START_TIME_KEY";

    private static class UiHandler extends Handler{

        private WeakReference<SplashActivity> mActivityRef;

        public UiHandler(final SplashActivity srcActivity){
            this.mActivityRef = new WeakReference<SplashActivity>(srcActivity);
        }

        @Override
        public void handleMessage(Message msg){
            final SplashActivity srcActivity = this.mActivityRef.get();
            if(srcActivity == null){
                return;

            }
            switch(msg.what) {
                case GO_AHEAD_WHAT:
                    long elapsedTime = SystemClock.uptimeMillis();
                    if (elapsedTime >= MIN_WAIT_INTERVAL) {
                        srcActivity.mIsDone = true;
                        srcActivity.goAhead();
                    }

                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        if(savedInstanceState!=null){
            this.mStartTime = savedInstanceState.getLong(START_TIME_KEY);

        }
        mHandler = new UiHandler(this);
        //FacebookSdk.sdkInitialize(getApplicationContext());

        //LoginManager.getInstance().logOut();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_splash, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(mStartTime==-1L) {
            mStartTime = SystemClock.uptimeMillis();
        }
        final Message goAheadMessage = mHandler.obtainMessage(GO_AHEAD_WHAT);
        mHandler.sendMessageAtTime(goAheadMessage, mStartTime+MAX_WAIT_INTERVAL);
    }

    private void goAhead(){

        if(NetworkController.isOnline(getApplicationContext())==true){

            AccessToken accessToken = checkSession();
            if(accessToken==null) {
                final Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                final Intent intent = new Intent(this, drawnerActivity.class);

                Profile profile = Profile.getCurrentProfile();
                intent.putExtra("namefromsplash", profile.getFirstName());
                intent.putExtra("surnamefromsplash", profile.getLastName());
                intent.putExtra("idfromsplash", profile.getId());
                intent.putExtra("photo1", "https://graph.facebook.com/" );
                intent.putExtra("photo2","/picture?height=105&width=105");


                FeedReaderDbHelperMessages mDbHelper = new FeedReaderDbHelperMessages(getApplicationContext());

                UpdateMessages updateMessages = new UpdateMessages(mDbHelper);
                updateMessages.update(Profile.getCurrentProfile().getId());

                FeedReaderDbHelperNotification mDbHelperNotification = new FeedReaderDbHelperNotification(getApplicationContext());

                UpdateNotifications updateNotifications = new UpdateNotifications(mDbHelperNotification, mDbHelper);
                updateNotifications.update(Profile.getCurrentProfile().getId());

                startActivity(intent);
                finish();
            }

        }else{
            NetworkDialog networkDialog = new NetworkDialog();
            networkDialog.show(getSupportFragmentManager(),"NETWORK_DIALOG");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DONE_KEY, mIsDone);
        outState.putLong(START_TIME_KEY, mStartTime);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
        this.mIsDone=savedInstance.getBoolean(IS_DONE_KEY);

    }

    private AccessToken checkSession() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken;
    }

    @Override
    public void okInfoInterface() {
        goAhead();
    }

}
