package enginek.dreamspace;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 10/7/2016.
 */
public class DreamsFragment extends ListFragment {

    DatabaseHandler db;
    List<Dream> dbList;
    List dreamList;
    Context context;
    TextView noDreams;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dreams_fragment, container, false);

        //Gets dreams from db
        db = new DatabaseHandler(view.getContext());
        dbList = db.getDreams();
        dreamList = new ArrayList();

        noDreams = (TextView) view.findViewById(R.id.nodreamstext);
        context = view.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(dbList.size() != 0){
            noDreams.setVisibility(View.GONE);
            for(int x = 0; x < dbList.size(); x++){

                //Creates new list items using the dreams from the db
                dreamList.add(new DreamListItem(dbList.get(x).getTitle(), dbList.get(x).getTime(),dbList.get(x).getDate()));

            }

            DreamListAdapter adapter = new DreamListAdapter(context, dreamList);
            setListAdapter(adapter);
        }else{
            noDreams.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Dream dream = db.getDream(dbList.get(position).getId());
        DreamFragment frag = new DreamFragment();

        Bundle args = new Bundle();
        args.putInt("id", dream.getId());
        args.putString("title", dream.getTitle());
        args.putString("dream", dream.getDream());
        args.putString("time", dream.getTime());
        args.putString("date", dream.getDate());
        frag.setArguments(args);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, frag);
        transaction.addToBackStack(null);
        MainActivity.hideButtons();
        transaction.commit();


    }
}
