package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;


/**
 * Created by Domenico on 30/05/2015.
 */
public class FriendProfileFragment extends Fragment {

    private String facebookId;
    private ImageButton imageButtonBack;
    private TextView nameSurnameFriend;

    public FriendProfileFragment(){

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

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
            firstAccessView = inflater.inflate(R.layout.friend_profile_fragment_layout, null);

                this.imageButtonBack = (ImageButton) firstAccessView.findViewById(R.id.backFromProfile);
                this.imageButtonBack.setImageResource(R.drawable.ic_back);
                this.nameSurnameFriend = (TextView) firstAccessView.findViewById(R.id.friendName);
                this.nameSurnameFriend.setText(getArguments().getString("facebookIdFriend"));



        }else{
            firstAccessView = getView();
        }



        return firstAccessView;
    }




}




