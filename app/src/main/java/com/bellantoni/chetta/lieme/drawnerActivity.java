package com.bellantoni.chetta.lieme;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.WindowManager;

import com.bellantoni.chetta.lieme.dialog.LogoutDialog;
import com.bellantoni.chetta.lieme.network.NetworkController;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;


public class drawnerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ProfileFragment.ProfileFragmentInterface, LogoutDialog.LogoutInterface {


    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String surname;
    private String name;
    private String id;
    private Intent intent;
    private String photo1, photo2;
    private ProfileFragment profileFragment;
    private Intent serviceIntent;

    private AskFragment askFragment;
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
        this.actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9200")));
        this.actionBar.setTitle(R.string.app_name);

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

        if(NetworkController.isOnline(this.getApplicationContext())==true){
            System.out.print("SONO ONLINE");
        }else{
            System.out.print("SONO OFFLINE");
        }



    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position){
            case 0:
                goProfile();

                break;
            case 1:
                goAskQuestion();

                break;
            case 2:
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
        this.actionBar.hide();
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
            profileFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, profileFragment, "ProfileFragment")
                    .commit();
        }else{
            this.profileFragment.setNameSurnameString(this.profileFragment.getNameSurnameString());
            this.profileFragment.setBitmap(this.profileFragment.getBitmap());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, profileFragment, "ProfileFragment")
                    .commit();
        }


    }

    @Override
    public void goaskQuestionFragment(){
        goAskQuestion();

    }

    private void goAskQuestion(){


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

    //se c'è solo un fragment chiudo l'attività
    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }


}
