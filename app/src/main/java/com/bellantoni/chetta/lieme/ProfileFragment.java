package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bellantoni.chetta.lieme.generalclasses.EndlessScrollListener;
import com.bellantoni.chetta.lieme.generalclasses.RoundImage;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment /*implements AbsListView.OnScrollListener*/ {

    private TextView nameSurname;
    private String nameSurnameString;
    private ImageView profileImage;
    private Bitmap bitmap = null;
    private RoundImage roundedImage;
    private RoundImage roundFAB;
    private String id;
    private ImageButton FAB;
    private CustomListAdapter adapter;
    private List<RowItemProfile> rows;

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

    String[] facebookids={
        "564874648477",
        "6264687618",
        "624167341781",
        "642787867354",
        "35416681717",
        "6541451738713",
        "3482678173",
        "56178531454187",
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

        RowItemProfile row =null;
        this.rows = new ArrayList<RowItemProfile>();
        for(int i=0; i<8; i++){
             row = new RowItemProfile(questions[i],itemname[i], facebookids[i], imgid[i]);
            rows.add(row);
        }
        this.adapter = new CustomListAdapter(getActivity(),rows);

        //this.adapter=new CustomListAdapter(getActivity(), itemname, imgid, questions,facebookids);
        list=(ListView)firstAccessView.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                String facebookid = facebookids[+position];
                Toast.makeText(getActivity().getApplicationContext(), Slecteditem+" "+facebookid, Toast.LENGTH_SHORT).show();

            }
        });

        /*list.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });*/

        /*list.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                while (true){
                    rows.add(new RowItemProfile("PIPPO", "PIPPO", "KDJWELIDJEKFJD", R.id.icon));

                }
            }
        });*/


       /* adapter.insert(row,8);
        adapter.insert(row,9);
        adapter.insert(row,9);
        adapter.notifyDataSetChanged();*/


        //
        return firstAccessView;
    }

    /*@Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        System.out.println("FINE LISTAAAAAAAAA");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        while(true){
            rows.add(new RowItemProfile("PIPPO", "PIPPO", "KLJHKLDHN", R.id.icon));
            this.adapter.notifyDataSetChanged();
        }

    }*/







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
