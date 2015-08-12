package com.bellantoni.chetta.lieme.listener;

import android.app.Activity;
import android.view.View;

/**
 * Created by Domenico on 12/08/2015.
 */
public class OnClickListenerFriendProfile implements View.OnClickListener {

    private String id;
    private Activity context;

    public OnClickListenerFriendProfile(String facebookid, Activity context){
        this.id = facebookid;
        this.context = context;

    }

    public interface OnClickFriendProfileInterface{
        void goFriendProfileFromFriendProfile(String facebookId);
    }

    private OnClickFriendProfileInterface mOnClickFriendProfileInterface;

    @Override
    public void onClick(View v) {
        if(this.context instanceof OnClickFriendProfileInterface){
            mOnClickFriendProfileInterface = (OnClickFriendProfileInterface)context;
            mOnClickFriendProfileInterface.goFriendProfileFromFriendProfile(this.id);
        }
    }


}
