package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;


public class drawnerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ProfileFragment.ProfileFragmentInterface {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String surname;
    private String name;
    private String id;
    private Intent intent;
    private String photo1, photo2;

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //qui qualsiasi sia position creo un fragmnet devo modificarlo

        switch (position){
            case 1:
                goProfile();
                break;
            case 0:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drawner, container, false);

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

        }
    }

    private void logout(){
        LoginManager.getInstance().logOut();
        final Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goProfile(){

        Bundle bundle = new Bundle();
        bundle.putString("name", this.name);
        bundle.putString("surname", this.surname);
        System.out.println("CAZZO DI COGNOME "+ this.surname);
        bundle.putString("photo1", this.photo1);
        bundle.putString("photo2", this.photo2);
        bundle.putString("id", this.id);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, profileFragment)
                .commit();
    }

}
