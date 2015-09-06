package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.bellantoni.chetta.lieme.adapter.SearchListAdapter;
import com.bellantoni.chetta.lieme.db.FeedReaderContract;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelper;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.facebook.FacebookSdk;
import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;


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
            "1234",
            "1235",
            "12345",
            "12367",
            "12354",
            "12387",
            "123687"

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
        new RetrieveContactsFromLocalDataBase().execute(null, null, null);
        View firstAccessView;
        if(savedBundle==null) {
            firstAccessView = inflater.inflate(R.layout.search_fragment_layout, null);

            //Contact friendContact = ContactListFragment.findContactById(String.valueOf(this.friendId));

            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Search Friend");
            //AL POSTO DI QUESTO FOR DEVO PRENDERMI TUTTI I CONTATTI
            /*
            for(int i=0; i<8; i++){
                Contact c = new Contact(idContact[i], nameSurname[i], facebookId[i], timeStamp[i]);
                this.rows.add(c);

            }
*/
            adapter = new SearchListAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listSearch);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    //TOGLIERE IL TOAST E AGGIUNGERE LA RGA COMMENTATA DOPO
                    //Toast.makeText(getActivity().getApplicationContext(), "item selected", Toast.LENGTH_SHORT).show();
                    mSearchFragmentInterface.goFreindProfileFromSearch(rows.get(position).getFacebook_id());
                }
            });
            //list.setOnScrollListener(this);



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
                        list.setAdapter(new SearchListAdapter(getActivity(), rows));
                        adapter.notifyDataSetChanged();
                        System.out.println("size" + rows.size());
                    }
                    for(int i = 0; i<rows.size(); i++){
                        if(length < rows.get(i).getName().length()){
                            if((rows.get(i).getName()).contains(editableText.getText().toString())){
                                temp.add(rows.get(i));

                            }
                        }
                    }
                    if(temp.size()>0){
                        list.setAdapter(new SearchListAdapter(getActivity(), temp));
                        adapter.notifyDataSetChanged();
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


   /* @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        boolean loadMore =
                firstVisibleItem + visibleItemCount >= totalItemCount;
        int count = 0;
        if (loadMore) {

            /*for(int i=0; i<1; i++) {
                this.rows.add(new Contact("12", "pippo", "facebookId", "timestamp"));
            }
            count++;
            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {


                    Toast.makeText(getActivity().getApplicationContext(), "item selected", Toast.LENGTH_SHORT).show();


                }
            });*/
       /* }
        this.adapter.setCount(this.adapter.getCount()+count);

        adapter.notifyDataSetChanged();*/

    //}


    private class RetrieveContactsFromLocalDataBase extends AsyncTask<Void, Void, Void> {
        private Cursor c;

        @Override
        protected Void doInBackground(Void... params) {
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fillTheContactListArray(c);
        }
    }

    private  void fillTheContactListArray(Cursor c){
        // Clear the array
        contacts.clear();
        rows.clear();

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

        adapter.notifyDataSetChanged();

    }

}