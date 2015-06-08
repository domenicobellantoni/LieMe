package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.bellantoni.chetta.lieme.db.FeedReaderContract;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelper;
import com.bellantoni.chetta.lieme.dummy.DummyContent;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ContactListFragment extends android.support.v4.app.Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "CONTACT_LIST_FRAGMENT";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public interface ContactListFragmentInterface{

    }

    private OnFragmentInteractionListener mListener;
    private ContactListFragmentInterface mContactListFragmentInterface;
    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter;
    /**
    * Db access object
    * */
    private FeedReaderDbHelper mDbHelper;
    /**
     * contact list
     * */
    private List<Contact> contacts;

    /**
     *
     * async class that retrieve data from DB
     */
    RetrieveContactsFromLocalDataBase retrieveContactsFromLocalDataBaseAsync;

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
    private String[] projection = {
            FeedReaderContract.FeedEntry._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
            FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP
    };

     // TODO: Rename and change types of parameters
    public static ContactListFragment newInstance(String param1, String param2) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mDbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        contacts = new ArrayList<Contact>();
        retrieveContactsFromLocalDataBaseAsync = new RetrieveContactsFromLocalDataBase();
        //retrieveContactsFromLocalDataBase();
        /* Add test
        contacts.add(new Contact("ok","ok","ok","ok","ok"));
        */

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAdapter = new ArrayAdapter<Contact>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, contacts);

        retrieveContactsFromLocalDataBaseAsync.execute(null, null, null);

        Log.i(TAG, "Retrieving facebook friends that use the application");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        updateContactList(graphResponse);
                    }
                }
        ).executeAsync();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    private void updateList(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if(activity instanceof ContactListFragmentInterface){
             mContactListFragmentInterface = (ContactListFragmentInterface)activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            // mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
            mListener.onFragmentInteraction(contacts.get(position).getId());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private void updateContactList(GraphResponse response){

        ArrayList<Contact> newContacts = new ArrayList<>();
        try {
            JSONObject jObject = response.getJSONObject();
            JSONArray users = jObject.getJSONArray("data");
            for(int i = 0; i< users.length(); i++)
            {
                JSONObject user = users.getJSONObject(i);
                String name = user.getString("name");
                String facebook_id = user.getString("id");
                int time = (int) (System.currentTimeMillis());
                Timestamp tsTemp = new Timestamp(time);
                String ts =  tsTemp.toString();
                newContacts.add(new Contact(null, name, facebook_id, ts));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();
        SQLiteDatabase dbWriter = mDbHelper.getWritableDatabase();

        for(Contact contact: newContacts){
            Cursor c = dbReader.query(
                    FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID + "=?",   // The columns for the WHERE clause
                    new String[]{contact.getFacebook_id()},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if(c.moveToNext()){
                Log.i(TAG, "user " + contact.getName() + " is already present in the local db");
            }
            else {
                Log.i(TAG, "user " + contact.getName() + " is not present in the local db");

                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, contact.getName());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID, contact.getFacebook_id());
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, contact.getTimestamp());
                long newRowId = dbWriter.insert(FeedReaderContract.FeedEntry.TABLE_NAME,null,values);
            }
        }

        new RetrieveContactsFromLocalDataBaseAfterUpdate().execute(null, null, null);

    }

    /* previous synchronous method*/
    private void retrieveContactsFromLocalDataBase(){
        /* Insert test
         *
         */
        // Gets the data repository in write mode
        SQLiteDatabase db1 = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, "mario rossi");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID, "00000");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, "00000");

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db1.insert(FeedReaderContract.FeedEntry.TABLE_NAME,
                null,
                values);

        /*
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME + " ASC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        fillTheContactListArray(c);
        */


    }

    private class RetrieveContactsFromLocalDataBase extends AsyncTask<Void, Void, Void>{
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

    private class RetrieveContactsFromLocalDataBaseAfterUpdate extends AsyncTask<Void, Void, Void>{
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

    private void fillTheContactListArray(Cursor c){
        // Clear the array
        contacts.clear();

        if(c != null){
            if(c.moveToFirst()){
                do{
                    String id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    String name = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME));
                    String facebook_id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID));
                    String timestamp = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP));
                    contacts.add(new Contact(id, name, facebook_id, timestamp));
                }while(c.moveToNext());
            }
        }

        updateList();

    }
}
