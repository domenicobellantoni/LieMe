package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.facebook.FacebookSdk;

/**
 * Created by Domenico on 21/05/2015.
 */
public class AskFragment extends android.support.v4.app.Fragment {

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
        setRetainInstance(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {
        final View firstAccessView = inflater.inflate(R.layout.ask_question, null);

        return firstAccessView;
    }
}
