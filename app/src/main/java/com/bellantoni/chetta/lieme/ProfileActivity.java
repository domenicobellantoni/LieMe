package com.bellantoni.chetta.lieme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ProfileActivity extends ActionBarActivity {

    private TextView nameSurname;
    private ImageView profileImage;
    private Bitmap bitmap = null;
    private RoundImage roundedImage;
    private String id;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        this.nameSurname = (TextView) findViewById(R.id.nameSurname);
        this.intent = getIntent();
        this.nameSurname.setText(this.intent.getStringExtra("NAME")+" "+this.intent.getStringExtra("SURNAME"));
        this.id = this.intent.getStringExtra("ID");

        setImage(id);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private void setImage(String id){

        this.profileImage = (ImageView) findViewById(R.id.imageProfile);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL imageURL = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + id + "/picture?height=105&width=105");
        } catch (MalformedURLException e) {
            //qui dovrei impostare una immagine di default vuota se va male
            e.printStackTrace();
        }

        try {
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            //qui dovrei impostare una immagine di default vuota se va male
            e.printStackTrace();
        }

        this.roundedImage = new RoundImage(bitmap);

        this.profileImage.setImageDrawable(roundedImage);

    }



}
