package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.db.FeedReaderContract;
import com.bellantoni.chetta.lieme.db.FeedReaderDbHelper;
import com.bellantoni.chetta.lieme.generalclasses.Contact;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.facebook.FacebookSdk;

/**
 * Created by Domenico on 21/05/2015.
 */
public class AskFragment extends android.support.v4.app.Fragment {

    private final String TAG = "AskFragment";
    /**
     * Db access object
     * */
    private FeedReaderDbHelper mDbHelper;
    private Contact receiverContact;
    private View firstAccessView;
    public interface AskFragmentInterface{


    }

    private AskFragmentInterface mAskFragmentInterface;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof AskFragmentInterface){
            mAskFragmentInterface = (AskFragmentInterface)activity;

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mDbHelper = new FeedReaderDbHelper(getActivity().getApplicationContext());
        setRetainInstance(true);
        String receiverId = getArguments().getString("receiver");
        retrieveReceiverInfo(receiverId);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {
        firstAccessView = inflater.inflate(R.layout.ask_question, null);

        final Button button = (Button) firstAccessView.findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage();
            }
        });

        setReceiverName();
        return firstAccessView;
    }

    private void retrieveReceiverInfo(String receiverId){
        Log.i(TAG, receiverId);
        SQLiteDatabase dbReader = mDbHelper.getReadableDatabase();

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP
        };

        Cursor c = dbReader.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                FeedReaderContract.FeedEntry._ID + "=?",  // The columns for the WHERE clause
                new String[]{receiverId},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        if(c.moveToNext()){
            String id_r = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
            String name = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME));
            String facebook_id = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_FACEBOOK_ID));
            String timestamp = c.getString(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP));
            receiverContact = new Contact(id_r, name, facebook_id, timestamp);
            Log.i(TAG, "Message receiver: " + receiverContact.getName());
        }
        else {
            Log.i(TAG, "ERROR: user id " + receiverId + " not found");
        }
    }

    private void  setReceiverName(){
        TextView receiverTextView = (TextView)firstAccessView.findViewById(R.id.who);
        receiverTextView.setText("To: " + receiverContact.getName());
    }

    public void sendMessage(){
        MessageHandler messageHandler = new MessageHandler(receiverContact,null, null);
        messageHandler.send();
    }
}
