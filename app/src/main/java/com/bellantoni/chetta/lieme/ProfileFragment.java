package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.facebook.FacebookSdk;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileFragment extends Fragment {

    private TextView nameSurname;
    private String nameSurnameString;
    private ImageView profileImage;
    private Bitmap bitmap = null;
    private RoundImage roundedImage;
    private String id;
    private Bundle savedState;


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
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        if(savedBundle!=null){
            this.savedState=savedBundle;

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            System.out.println("USO IMMAGINE SALVATA, RIPRISTINO I SINGOLI CAMPI DEL FRAGMENT");
            this.nameSurname.setText(getArguments().getString("namesurname"));
            this.profileImage.setImageBitmap((Bitmap)getArguments().getParcelable("imgprofile"));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {
        final View firstAccessView = inflater.inflate(R.layout.fragment_profile, null);
        this.profileImage = (ImageView) firstAccessView.findViewById(R.id.imageProfile);
        this.nameSurname = (TextView) firstAccessView.findViewById(R.id.nameSurname);
        System.out.println("SCARICO IMMAGINE");
        this.nameSurnameString = getArguments().getString("name") + " " + getArguments().getString("surname");
        this.nameSurname.setText(nameSurnameString);
        DownloaderProfileImage downloaderProfileImage = new DownloaderProfileImage();
        downloaderProfileImage.execute(getArguments().getString("photo1") + getArguments().get("id") + getArguments().getString("photo2"));

        return firstAccessView;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("namesurname", nameSurnameString);
        bundle.putParcelable("imgprofile", bitmap);
        System.out.println("SALVO SINGOLI CAMPI FRAGMENT");
    }




    private class DownloaderProfileImage extends AsyncTask<String,String,Bitmap> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            roundedImage = new RoundImage(BitmapFactory.decodeResource(getResources(), R.mipmap.iconuseranonymous));
            profileImage.setImageDrawable(roundedImage);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                roundedImage = new RoundImage(bitmap);
                profileImage.setImageDrawable(roundedImage);
            }

        }


        @Override
        protected Bitmap doInBackground(String... linkphoto) {

            URL imageURL = null;

            try {
                imageURL = new URL(linkphoto[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }




        }
    }


}
