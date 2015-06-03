package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.graphics.Color;
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

import com.bellantoni.chetta.lieme.adapter.ListInFriendFragmentAdapter;
import com.bellantoni.chetta.lieme.generalclasses.CircleTransform;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Domenico on 30/05/2015.
 */
public class FriendProfileFragment extends Fragment implements AbsListView.OnScrollListener {

    private String facebookId;
    private ImageButton imageButtonBack;
    private TextView nameSurnameFriend;
    private ImageView friendProfileImage;
    private ListInFriendFragmentAdapter adapter;
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
    String[] idfb ={
            "id fb Federico Badini",
            "id fb Matteo Bana",
            "id fb Alessandro Donini",
            "id fb fNicora Elisa",
            "id fb fMassimo De Marchi",
            "id fb Lorenzo Di tucci",
            "id fb Davide Dipinto",
            "id fb Leonardo Cavagnis"
    };

    Integer[] imgid={
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,
            R.drawable.ic_profile,

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


    public interface FriendProfileFragmentInterface{
       // public void goaskQuestionFragment();
        public void goFriendProfileFromFriend(String facebookId);

    }

    private FriendProfileFragmentInterface mFriendProfileFragmentInteface;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof FriendProfileFragmentInterface){
            mFriendProfileFragmentInteface = (FriendProfileFragmentInterface)activity;

        }
    }

    public FriendProfileFragment(){

    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        this.rows = new ArrayList<RowItemProfile>();
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

                this.friendProfileImage = (ImageView) firstAccessView.findViewById(R.id.friendImgProfile);
                //l'immagine poi è da scaricare sabendo l'id di facebook dell'amico
                 Picasso.with(getActivity().getApplicationContext()).load("http://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).fit().centerCrop().into(this.friendProfileImage);



            for (int i = 0; i < 8; i++) {

                RowItemProfile row = new RowItemProfile(questions[i], itemname[i], idfb[i], imgid[i]);
                this.rows.add(row);

            }


            //final CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid, questions,idfb);
            adapter = new ListInFriendFragmentAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listQuestionFriendProfile);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = itemname[+position] + idfb[+position];
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mFriendProfileFragmentInteface.goFriendProfileFromFriend(adapter.getItem(position).getFacebookId());

                }
            });



        }else{
            firstAccessView = getView();
        }



        return firstAccessView;
    }


    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {
        //System.out.println("QUESTO "+adapter.getItem(firstVisible).getId());


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {
            //scaricare sempre in async task  e far vedere spiner
            this.adapter.setCount(this.adapter.getCount()+1);

            //QUI DA FARE UNA QUERY ALLA VOLTA HO PROVATO A CARICARE TIPO 8 ELEMENTI ALLA VOLTA MA CRASHA, SPERO CHE LA QUERY SIA
            //VELOCE, AL MASSIMO POSSIAMO PROVARE 2/3 ALLA VOLTA
            rows.add(new RowItemProfile("Pippo", "Pippo", "id fb Pippo", R.id.icon));
            //this.adapter.addAll(this.rows);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = adapter.getItem(position).getFacebookId();
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mFriendProfileFragmentInteface.goFriendProfileFromFriend(adapter.getItem(position).getFacebookId());

                }
            });


            System.out.println("CONTATORE "+ this.adapter.getCount());
            adapter.notifyDataSetChanged();
        }
    }

    public void onScrollStateChanged(AbsListView v, int s) {

        //System.out.println("CONTATORE "+ this.adapter.getCount());
        adapter.notifyDataSetChanged();


    }




}



