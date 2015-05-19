package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bellantoni.chetta.lieme.generalclasses.RoundImage;

public class ProfileFragment extends Fragment {

    private TextView nameSurname;
    private ImageView profileImage;
    private Bitmap bitmap = null;
    private RoundImage roundedImage;
    private String id;

    public interface ProfileFragmentInterface{

    }

    private ProfileFragmentInterface mProfileFragmentInteface;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof ProfileFragmentInterface){
            mProfileFragmentInteface = (ProfileFragmentInterface)activity;

        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {
        final View firstAccessView = inflater.inflate(R.layout.fragment_profile, null);


        this.profileImage = (ImageView) firstAccessView.findViewById(R.id.imageProfile);
        this.nameSurname = (TextView) firstAccessView.findViewById(R.id.nameSurname);
        this.nameSurname.setText(getArguments().getString("name") + " " + getArguments().getString("surname"));




        return firstAccessView;
    }

}
