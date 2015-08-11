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
            "Filippo Zaffaroni",
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
            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

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

        fetchMovies();
    }


    private void fetchMovies() {
        swipeLayout.setRefreshing(true);

        new UpdateListTask().execute(null, null, null);
        this.adapter.notifyDataSetChanged();


    }


    public void onScroll(AbsListView view,
                         int firstVisible, int visibleCount, int totalCount) {


        boolean loadMore =
                firstVisible + visibleCount >= totalCount;

        if(loadMore) {
            //scaricare sempre in async task
            this.adapter.setCount(this.adapter.getCount()+1);

            rows.add(new ItemHome("question question question", "Pippofrom", "pippoTO","id fb form","id fb to", R.id.icon, true));
            //this.adapter.addAll(this.rows);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    mHomeFragmentInterface.goFriendProfileFromHome(adapter.getItem(position).getIdTo());

                }
            });



            adapter.notifyDataSetChanged();
        }
    }


    public void onScrollStateChanged(AbsListView v, int s) {

        adapter.notifyDataSetChanged();
    }

    private class UpdateListTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            /*for(int i=0; i<10000; i++){
                System.out.print("PROVA PER PERDER TEMPO");

            }*/
            ItemHome row = new ItemHome("domanda aggiunta eh ciao", "Ettusi Gianpaolo", "Babbo di Minchia", "54ds754ds","87987dfd", 547887, true);
            HomeFragment.this.rows.add(0,row);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    mHomeFragmentInterface.goFriendProfileFromHome(adapter.getItem(position).getIdTo());

                }
            });


            return null;
        }

        protected void onPostExecute(Void result){
            HomeFragment.this.swipeLayout.setRefreshing(false);

        }
    }


}
