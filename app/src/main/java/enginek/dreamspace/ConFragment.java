package enginek.dreamspace;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_fragment, container, false);
        context = view.getContext();

        MainActivity.connectionsClicked();

        db = new DatabaseHandler(view.getContext());
        connections = db.getConnections();
        connectionList = new ArrayList();

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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Connection connection = db.getConnection(connections.get(position).getConnection_id());
        ConViewFragment frag = new ConViewFragment();

        Bundle args = new Bundle();
        args.putInt("dreamA_id", connection.getDreamA_id());
        args.putInt("dreamB_id", connection.getDreamB_id());
        frag.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, frag);
        transaction.addToBackStack(null);
        transaction.commit();


    }



}
