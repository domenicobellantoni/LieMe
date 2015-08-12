package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bellantoni.chetta.lieme.dialog.LogoutDialog;
import com.bellantoni.chetta.lieme.network.NetworkController;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;



public class LoginFragment extends Fragment {

    private CallbackManager mCallbackManager;

    private Profile facebookProfile;

    public interface ListenerInterface{
       void goProfile(Profile profile);
       void errorConnection();
    }

    private ListenerInterface mListener;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            facebookProfile=profile;
            mListener.goProfile(facebookProfile);

        }

        @Override
        public void onCancel() {

            if(NetworkController.isOnline(getActivity().getApplicationContext())==false) {
                mListener.errorConnection();
            }
        }

        @Override
        public void onError(FacebookException e) {

            if(NetworkController.isOnline(getActivity().getApplicationContext())==false) {
                mListener.errorConnection();

            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    public  LoginFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager=CallbackManager.Factory.create();


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager,mCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        if(facebookProfile!=null){
            mListener.goProfile(facebookProfile);
        }

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof ListenerInterface){
            mListener =(ListenerInterface)activity;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        this.mListener=null;
    }


}
