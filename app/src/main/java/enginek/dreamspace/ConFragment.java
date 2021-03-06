package enginek.dreamspace;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 10/7/2016.
 */
public class ConFragment extends ListFragment {

    DatabaseHandler db;
    List<Connection> connections;
    List connectionList;
    static ConnectionListAdapter adapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_fragment, container, false);
        context = view.getContext();

        MainActivity.statisticsClicked();

        db = new DatabaseHandler(view.getContext());
        connections = db.getConnections();
        connectionList = new ArrayList();

        ImageButton back = (ImageButton) view.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ImageButton info = (ImageButton) view.findViewById(R.id.infoButton);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoFragment frag = new InfoFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.framelayout, frag, context.getString(R.string.info_fragment));
                transaction.addToBackStack(context.getString(R.string.info_fragment));
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(connections.size() > 0){
            for(int x = 0; x < connections.size(); x++){

                String dreamOneTitle = db.getDream(connections.get(x).getDreamA_id()).getTitle();
                String dreamTwoTitle = db.getDream(connections.get(x).getDreamB_id()).getTitle();

                //Creates new list items using the dreams from the db
                connectionList.add(new ConnectionListItem(dreamOneTitle, dreamTwoTitle,connections.get(x).getAccepted()));

            }

            adapter = new ConnectionListAdapter(context, connectionList);
            setListAdapter(adapter);
        }


    }

    @Override
    public void onResume(){
        super.onResume();

        if(db.getConnectionsCount() == 0){
            if(adapter!=null) {
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Connection connection = db.getConnection(connections.get(position).getConnection_id());
        ConViewFragment frag = new ConViewFragment();

        Bundle args = new Bundle();
        args.putInt("connection_id", connection.getConnection_id());
        args.putInt("dreamA_id", connection.getDreamA_id());
        args.putInt("dreamB_id", connection.getDreamB_id());
        args.putInt("accepted", connection.getAccepted());
        frag.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom);
        transaction.replace(R.id.framelayout, frag, context.getString(R.string.con_view_fragment));
        transaction.addToBackStack(context.getString(R.string.con_view_fragment));
        transaction.commit();


    }



}
