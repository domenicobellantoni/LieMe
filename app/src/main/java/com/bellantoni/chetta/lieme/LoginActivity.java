package com.bellantoni.chetta.lieme;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bellantoni.chetta.lieme.db.FeedReaderDbHelperMessages;
import com.bellantoni.chetta.lieme.dialog.NetworkDialog;
import com.bellantoni.chetta.lieme.network.UpdateMessages;
import com.facebook.Profile;


public class LoginActivity extends ActionBarActivity implements LoginFragment.ListenerInterface, NetworkDialog.NetworkInfoInteface{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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

    @Override
    public void goProfile(Profile profile){
        final Intent intent = new Intent(this, drawnerActivity.class);
        //final Intent intent = new Intent(this, New_Drawner.class);
        intent.putExtra("namefromlogin", profile.getFirstName());
        intent.putExtra("surnamefromlogin", profile.getLastName());
        intent.putExtra("idfromlogin", profile.getId());
        //intent.putExtra("IMAGE", profile.getProfilePictureUri(40,40).toString());

        FeedReaderDbHelperMessages mDbHelper = new FeedReaderDbHelperMessages(getApplicationContext());

        UpdateMessages updateMessages = new UpdateMessages(mDbHelper);
        updateMessages.update(Profile.getCurrentProfile().getId());

        startActivity(intent);
        finish();

    }

    @Override
    public void errorConnection(){

            NetworkDialog networkDialog = new NetworkDialog();
            networkDialog.show(getSupportFragmentManager(), "NETWORK_DIALOG");

    }

    @Override
    public void okInfoInterface(){

        //nothing to do

    }




}
