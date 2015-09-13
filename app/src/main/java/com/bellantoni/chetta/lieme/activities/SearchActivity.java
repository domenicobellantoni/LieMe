package com.bellantoni.chetta.lieme.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bellantoni.chetta.lieme.R;
import com.bellantoni.chetta.lieme.adapter.SearchListAdapter;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 15/08/2015.
 */
public class SearchActivity extends ActionBarActivity implements AbsListView.OnScrollListener {


    private String query;
    private SearchListAdapter adapter;
    private List<Contact> rows;
    private ListView list;



    String idContact[] ={
        "123",
        "123",
        "123",
        "123",
        "123",
        "123",
        "123",
        "123"

    };

    String nameSurname[] = {
            "Luca Biaggi",
            "Giovanni Raimondi",
            "Filippo Pulpo",
            "Carlotta Gianni",
            "Salvatore Rimmolo",
            "Gianni Costanzo",
            "Carlo Bummo",
            "Serena Aivieri"

    };

    String facebookId[] ={
            "123",
            "123",
            "123",
            "123",
            "123",
            "123",
            "123",
            "123"

    };

    String timeStamp[] ={
            "123",
            "123",
            "123",
            "123",
            "123",
            "123",
            "123",
            "123"

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F59200")));

        this.rows = new ArrayList<Contact>();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            this.query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this,query, Toast.LENGTH_LONG);
        }


        setUpList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            Log.e("query", intent.getStringExtra(SearchManager.QUERY));
        }
    }

    private void setUpList(){

        for(int i=0; i<8; i++){
            Contact c = new Contact(idContact[i], nameSurname[i], facebookId[i], timeStamp[i]);
            this.rows.add(c);

        }

        adapter = new SearchListAdapter(this, this.rows);
        list = (ListView) findViewById(R.id.listSearch);

        list.setAdapter(adapter);

        list.setOnScrollListener(this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String facebookId = adapter.getItem(position).getFacebook_id();
                Intent intent = new Intent();
                intent.putExtra("facebookId", Profile.getCurrentProfile().getId());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        adapter.notifyDataSetChanged();
    }

    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;
        int count = 0;
        if (loadMore) {

            for(int i=0; i<10; i++) {
                this.rows.add(new Contact("12", "pippo", "facebookId", "timestamp"));
            }
            count++;
        }
        this.adapter.setCount(this.adapter.getCount()+count);

        adapter.notifyDataSetChanged();
    }

}
