package com.bellantoni.chetta.lieme;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
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
import com.bellantoni.chetta.lieme.generalclasses.ItemHome;
import com.bellantoni.chetta.lieme.generalclasses.RowItemProfile;
import com.facebook.FacebookSdk;

import org.ocpsoft.pretty.time.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;


/**
 * Created by Domenico on 30/05/2015.
 */
public class FriendProfileFragment extends Fragment implements AbsListView.OnScrollListener ,SwipeRefreshLayout.OnRefreshListener {

    private String facebookId;
    private ImageButton imageButtonBack;
    private TextView nameSurnameFriend;
    private ImageView friendProfileImage;
    private ListInFriendFragmentAdapter adapter;
    private List<RowItemProfile> rows;
    private SwipeRefreshLayout swipeLayout;
    private PrettyTime p;

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
            "Federico Badini",
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
            "Hai mai tradito la tua ragazza?",
            "Hai mai rubato in un supermercato?",
            "Ieri quando eri ubriaco, è vero che hai litigato con due ragazzi in discoteca?",
            "Hai copiato all'esame di ieri?",
            "Tua mamma sa che le rubi 10 euro dal portafogli ogni settimana?",
            "domanda iusgdilsyahgdlsjgdilys",
            "domanda sidhiofygejbdjkgdjskldgysjgdxb",
            "domanda lhdshdkjòsahdkasjdjqgdjshbjgd",
    };

    Boolean[] resultsQuestion ={
            true,
            false,
            true,
            false,
            false,
            false,
            true,
            false,
    };

    Date[] timeStamp ={

        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date(),
        new Date()

    };


   /* public interface FriendProfileFragmentInterface{

        public void goFriendProfileFromFriend(String facebookId);

    }

    private FriendProfileFragmentInterface mFriendProfileFragmentInteface;*/

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
       /* if(activity instanceof FriendProfileFragmentInterface){
            mFriendProfileFragmentInteface = (FriendProfileFragmentInterface)activity;

        }*/
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
        p = new PrettyTime(new Locale("en"));
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
            swipeLayout = (SwipeRefreshLayout) firstAccessView.findViewById(R.id.swipe_refresh_layout);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);


            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("facebookIdFriend"));



            for (int i = 0; i < 8; i++) {

                RowItemProfile row = new RowItemProfile(questions[i], itemname[i], idfb[i], imgid[i], resultsQuestion[i], p.format(timeStamp[i]));
                this.rows.add(row);

            }


            //final CustomListAdapter adapter=new CustomListAdapter(getActivity(), itemname, imgid, questions,idfb);
            adapter = new ListInFriendFragmentAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listQuestionFriendProfile);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);
            /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = itemname[+position] + idfb[+position];
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mFriendProfileFragmentInteface.goFriendProfileFromFriend(adapter.getItem(position).getFacebookId());

                }
            });*/



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

            Random random = new Random();
            int number = random.nextInt(2)+1;
            this.adapter.setCount(this.adapter.getCount()+number);


            for(int i=0; i<number; i++) {
                rows.add(new RowItemProfile("Pippo", "Pippo", "id fb Pippo", R.id.icon, true, p.format(new Date())));
            }


            //System.out.println("CONTATORE "+ this.adapter.getCount());
            adapter.notifyDataSetChanged();
        }
    }

    public void onScrollStateChanged(AbsListView v, int s) {

        //System.out.println("CONTATORE "+ this.adapter.getCount());
        adapter.notifyDataSetChanged();


    }


    @Override
    public void onRefresh() {
        fetchMovies();
    }

    private void fetchMovies() {
        swipeLayout.setRefreshing(true);

        new UpdateListTask().execute(null, null, null);
        this.adapter.notifyDataSetChanged();


    }

    private class UpdateListTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //da scaricare
            Random random = new Random();
            int number = random.nextInt(3)+1;
            for(int i=0; i<number; i++){
                RowItemProfile row = new RowItemProfile("Pippo", "Pippo", "id fb Pippo", R.id.icon, true, p.format(new Date()));
                FriendProfileFragment.this.rows.add(0,row);
            }


            return null;
        }

        protected void onPostExecute(Void result){
           FriendProfileFragment.this.swipeLayout.setRefreshing(false);

        }
    }

}




