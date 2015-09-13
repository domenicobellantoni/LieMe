package com.bellantoni.chetta.lieme.listener;

import android.app.Activity;
import android.view.View;


/**
 * Created by Domenico on 11/08/2015.
 */
public class OnClickListenerHomeTo implements View.OnClickListener {

    private String id;
    private Activity context;

    public OnClickListenerHomeTo(String id, Activity context){
        this.context = context;
        this.id = id;

    }

    public interface OnClickHomeInterface{
        void goFriendProfileFromHome(String facebookId);
    }

    private OnClickHomeInterface mOnClickHomeInterface;



    @Override
    public void onClick(View v) {
        if(context instanceof OnClickHomeInterface){
            mOnClickHomeInterface = (OnClickHomeInterface)context;
            mOnClickHomeInterface.goFriendProfileFromHome(this.id);
        }
    }
}
