package com.bellantoni.chetta.lieme;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import com.bellantoni.chetta.lieme.adapter.ListInHomeAdapter;
import com.bellantoni.chetta.lieme.generalclasses.ItemHome;
import com.facebook.FacebookSdk;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 28/07/2015.
 */
public class HomeFragment extends Fragment implements AbsListView.OnScrollListener,SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    private List<ItemHome> rows;
    private SwipeRefreshLayout swipeLayout;
    private ListInHomeAdapter adapter;

    String[] itemnameTo ={
            "Federico Badini",
            "Matteo Bana",
            "Alessandro Donini",
            "Nicora Elisa",
            "Massimo De Marchi",
            "Lorenzo Di tucci",
            "Davide Dipinto",
            "Leonardo Cavagnis"
    };


    String[] itemnameFrom ={
            "Pippo Zaffaroni",
            "Claudio Giorgi",
            "Alessandra Giannini",
            "Teresa Amedeo",
            "Carola Bummo",
            "Giancarlo Fiumi",
            "Davide Fioggi",
            "Brioschi Giacomo Lizzardo"
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

    String[] idfbFrom ={
            "id fb Pippo Zaffaroni",
            "id fb Claudio Giorgi",
            "id fb Alessandra Giannini",
            "id fb Teresa Amedeo",
            "id fb Carola Bummo",
            "id fb Giancarlo Fiumi",
            "id fb Davide Fioggi",
            "id fb Brioschi Giacomo Lizzardo"
    };


    String[] idfbTo ={
            "id fb Federico Badini",
            "id fb Matteo Bana",
            "id fb Alessandro Donini",
            "id fb fNicora Elisa",
            "id fb fMassimo De Marchi",
            "id fb Lorenzo Di tucci",
            "id fb Davide Dipinto",
            "id fb Leonardo Cavagnis"
    };



    public interface HomeFragmentInterface{
        public void goFriendProfileFromHome(String facebookId);
    }

    private HomeFragmentInterface mHomeFragmentInterface;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof HomeFragmentInterface){
            mHomeFragmentInterface = (HomeFragmentInterface)activity;

        }

    }

    public HomeFragment(){

    }

    @Override
    public void onCreate(Bundle savedBundle){
        super.onCreate(savedBundle);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        this.rows = new ArrayList<ItemHome>();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedBundle) {

        View firstAccessView;
        if(savedBundle==null) {
            firstAccessView = inflater.inflate(R.layout.home_fragment_layout, null);
            swipeLayout = (SwipeRefreshLayout) firstAccessView.findViewById(R.id.swipe_refresh_layout);
            swipeLayout.setOnRefreshListener(this);
            swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);



            /*swipeLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeLayout.setRefreshing(true);

                                            fetchMovies();
                                        }
                                    }
            );*/
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Home");


            for (int i = 0; i < 8; i++) {

                ItemHome row = new ItemHome(questions[i], itemnameFrom[i], itemnameTo[i], idfbFrom[i],idfbTo[i], imgid[i], resultsQuestion[i]);
                this.rows.add(row);

            }

            adapter = new ListInHomeAdapter(getActivity(), this.rows);
            list = (ListView) firstAccessView.findViewById(R.id.listHome);
            list.setAdapter(adapter);

            list.setOnScrollListener(this);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = itemname[+position] + idfb[+position];
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mHomeFragmentInterface.goFriendProfileFromHome(adapter.getItem(position).getIdTo());

                }
            });
        }else{
            firstAccessView = getView();
        }

        return firstAccessView;

    }

    @Override
    public void onRefresh() {
        /*for (int i = 7; i >= 0; i--) {

            ItemHome row = new ItemHome(questions[i], itemnameFrom[i], itemnameTo[i], idfbFrom[i],idfbTo[i], imgid[i], resultsQuestion[i]);
            this.rows.add(0,row);
            adapter.notifyDataSetChanged();

        }*/
        fetchMovies();
    }


    private void fetchMovies() {
        swipeLayout.setRefreshing(true);


        //QUI RICOSTRUISCO LA LISTA
       //MI SA CHE è DA FARE IN UN ASYNC TASK O UN NUOVO THREAD, DA PROVARE ALTRIMENTI è UN DRAMMA PER LA GRAFICA

        //swipeLayout.setRefreshing(false);

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
            rows.add(new ItemHome("question question question", "Pippofrom", "pippoTO","id fb form","id fb to", R.id.icon, true));
            //this.adapter.addAll(this.rows);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    //String Slecteditem = adapter.getItem(position).getFacebookId();
                    //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    mHomeFragmentInterface.goFriendProfileFromHome(adapter.getItem(position).getIdTo());

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
