package com.bellantoni.chetta.lieme.listener;

import android.app.Activity;
import android.view.View;

/**
 * Created by Domenico on 12/08/2015.
 */
public class OnClickListenerProfile implements View.OnClickListener {

    private String id;
    private Activity context;

    public OnClickListenerProfile(String facebookid, Activity context){
        this.id = facebookid;
        this.context = context;

    }

    public interface OnClickProfileInterface{
        void goFriendProfileFromProfile(String facebookId);
    }

    private OnClickProfileInterface mClickProfileInterface;

    @Override
    public void onClick(View v) {

        if(context instanceof OnClickProfileInterface){
            mClickProfileInterface = (OnClickProfileInterface)context;
            mClickProfileInterface.goFriendProfileFromProfile(this.id);


        }
    }



}
