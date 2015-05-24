package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private RoundImage roundFAB;
    private String id;
    private ImageButton FAB;

    ListView list;

    String[] itemname ={
            "Federico Badini",
            "Matteo Bana",
            "Alessandro Donini",
            "Nicora Elisa",
            "Massimo De Marchi",
            "Lorenzo Di tucci",
            "Davide Dipinto",
            "Leonardo Cavagnis"
    };

    Integer[] imgid={
            R.drawable.badini,
            R.drawable.bana,
            R.drawable.donini,
            R.drawable.elisa,
            R.drawable.demarchi,
            R.drawable.ditucci,
            R.drawable.dipinto,
            R.drawable.cavagnis,
    };
    String[] questions={
            "domand fhuhfskjdjksw",
            "domanda dkhsifgilfgilfguyeguyeg",
            "domanda sdgiyodgtwoedjwkldlywgdlwigdhlwkd",
            "domanda hdsdghs",
            "domanda sihdguydgweudgwjhgdjwhgdjshgdsgdhlsgdjs",
            "domanda iusgdilsyahgdlsjgdilys",
            "domanda sidhiofygejbdjkgdjskldgysjgdxb",
            "domanda lhdshdkjòsahdkasjdjqgdjshbjgd",
    };

    public interface ProfileFragmentInterface{
        public void goaskQuestionFragment();

    }

    private ProfileFragmentInterface mProfileFragmentInteface;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof ProfileFragmentInterface){
            mProfileFragmentInteface = (ProfileFragmentInterface)activity;

        }
    }

    public String getIdprofile() {
        return id;
    }

    public RoundImage getRoundedImage() {
        return roundedImage;
    }

    public String getNameSurnameString() {
        return nameSurnameString;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setRoundedImage(RoundImage roundedImage) {
        this.roundedImage = roundedImage;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setNameSurnameString(String nameSurnameString) {
        this.nameSurnameString = nameSurnameString;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    public ProfileFragmentInterface getmProfileFragmentInteface() {
        return mProfileFragmentInteface;
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
        final View firstAccessView = inflater.inflate(R.layout.fragment_profile, null);
        this.profileImage = (ImageView) firstAccessView.findViewById(R.id.imageProfile);
        this.nameSurname = (TextView) firstAccessView.findViewById(R.id.nameSurname);
        this.nameSurnameString = getArguments().getString("name") + " " + getArguments().getString("surname");
        this.nameSurname.setText(nameSurnameString);
        DownloaderProfileImage downloaderProfileImage = new DownloaderProfileImage();
        downloaderProfileImage.execute(getArguments().getString("photo1") + getArguments().get("id") + getArguments().getString("photo2"));
        FAB = (ImageButton) firstAccessView.findViewById(R.id.fab);
        Drawable d = getResources().getDrawable(R.drawable.ic_action);
        Bitmap bitmapAction = ((BitmapDrawable)d).getBitmap();
        FAB.setImageDrawable(new RoundImage(bitmapAction));
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileFragmentInteface.goaskQuestionFragment();

            }
        });


        CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid, questions);
        list=(ListView)firstAccessView.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });




        return firstAccessView;
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
