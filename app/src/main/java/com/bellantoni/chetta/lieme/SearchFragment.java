package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import com.bellantoni.chetta.lieme.adapter.SearchListAdapter;
import com.bellantoni.chetta.lieme.db.FeedReaderContract;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelper;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Domenico on 05/09/2015.
 */
public class SearchFragment extends Fragment /*implements AbsListView.OnScrollListener*/{


    private SearchListAdapter adapter;
    private List<Contact> rows;
    private ListView list;
    private EditText editableText;
    private SearchView searchView;
    /**
     * Db access object
     * */
    private static FeedReaderDbHelper mDbHelper;
    /**
     * contact list
     * */
    public static List<Contact> contacts;
    private static String[] projection = {
            FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
            FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP
    };

    public interface SearchFragmentInterface{
        void goFreindProfileFromSearch(String facebookId);
    }

    private SearchFragmentInterface mSearchFragmentInterface;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof SearchFragmentInterface){
            mSearchFragmentInterface = (SearchFragmentInterface)activity;

        }
    }

    public SearchFragment(){

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        mDbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        contacts = new ArrayList<>();
        this.rows = new ArrayList<Contact>();

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
            firstAccessView = inflater.inflate(R.layout.search_fragment_layout, null);

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Your Friends");

            list = (ListView) firstAccessView.findViewById(R.id.listSearch);
            downloadData();

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    mSearchFragmentInterface.goFreindProfileFromSearch(adapter.getItem(position).getFacebook_id());
                }
            });



            this.editableText = (EditText) firstAccessView.findViewById(R.id.editableText);
            this.editableText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<Contact> temp = new ArrayList<Contact>();
                    int length = editableText.getText().length();
                    temp.clear();
                    if(editableText.getText().toString().matches("")){
                        adapter = new SearchListAdapter(getActivity(), contacts);
                        list.setAdapter(adapter);
                    }else {
                        for (int i = 0; i < rows.size(); i++) {
                            if (length < rows.get(i).getName().length()) {
                                if ((rows.get(i).getName().toLowerCase()).contains(editableText.getText().toString().toLowerCase())) {
                                    temp.add(rows.get(i));
                                }
                            }
                        }
                        if(temp.size()>0){
                            adapter = new SearchListAdapter(getActivity(), temp);
                            list.setAdapter(adapter);
                        }else{
                            temp.clear();
                            temp.add(new Contact("anonymous", "No results", "anonymous", "anonymous"));
                            adapter = new SearchListAdapter(getActivity(),temp);
                            list.setAdapter(adapter);
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }else{
            firstAccessView = getView();
        }
        return firstAccessView;
    }

    private  void fillTheContactListArray(Cursor c){
        // Clear the array
        contacts.clear();
        rows.clear();
        int counter = 0;

        if(c != null){
            if(c.moveToFirst()){
                do{
                    String id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    String name = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME));
                    String facebook_id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID));
                    String timestamp = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP));
                    Contact tmp = new Contact(id, name, facebook_id, timestamp);
                    contacts.add(tmp);
                    rows.add(tmp);
                }while(c.moveToNext());
            }
        }
        adapter = new SearchListAdapter(getActivity(), this.rows);
        adapter.setCount(adapter.getCount()+counter);
        list.setAdapter(adapter);
    }


    private void downloadData(){


        Cursor c;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME + " ASC";

        c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        fillTheContactListArray(c);

    }

}