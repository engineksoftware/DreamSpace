package enginek.dreamspace;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    ImageButton search, delete;
    ViewSwitcher viewSwitcher;
    EditText searchParams;
    static DreamListAdapter adapter;
    AdView adView;
    AdRequest request;
    boolean deleteSelected;
    List<Integer> deleteDreamList;
    LinearLayout buttonLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dreams_fragment, container, false);

        MainActivity.dreamsClicked();
        deleteSelected = false;
        deleteDreamList = new ArrayList();

        //Gets dreams from db
        db = new DatabaseHandler(view.getContext());
        dbList = db.getDreams();
        dreamList = new ArrayList();

        noDreams = (TextView) view.findViewById(R.id.nodreamstext);
        context = view.getContext();

        viewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher);
        searchParams = (EditText) view.findViewById(R.id.searchParams);

        searchParams.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter != null)
                    adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search = (ImageButton) view.findViewById(R.id.searchbutton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dbList.size() == 0){
                    Toast.makeText(context, "No dreams to search for. ", Toast.LENGTH_SHORT).show();
                }else{
                    viewSwitcher.showNext();
                }
            }
        });

        buttonLayout = (LinearLayout) view.findViewById(R.id.buttonLayout);
        Button deleteAll = (Button) view.findViewById(R.id.deleteAll);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDeleteAlert();
            }
        });

        Button deleteS = (Button) view.findViewById(R.id.deleteSelected);
        deleteS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteDreamList.size() > 0){
                    for (int x = 0; x < deleteDreamList.size(); x++){
                        db.deleteDream(dbList.get(deleteDreamList.get(x)));
                        adapter.remove(adapter.getItem(deleteDreamList.get(x)));
                        buttonLayout.setVisibility(View.GONE);

                    }
                }else{
                    Toast.makeText(context, "Please select a dream first.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        delete = (ImageButton) view.findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(deleteSelected){
                    buttonLayout.setVisibility(View.GONE);
                    deleteSelected = false;

                    if(deleteDreamList.size() > 0){
                        for(int x=0; x < deleteDreamList.size(); x++){
                            getListView().getChildAt(deleteDreamList.get(x)).setBackground(context.getDrawable(R.drawable.round_purple_list_item));
                        }
                    }
                }else
                if(db.getDreamCount() > 0){
                    deleteSelected = true;
                    buttonLayout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(context, "No dreams to delete.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageButton back = (ImageButton) view.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(dbList.size() > 0){
            noDreams.setVisibility(View.GONE);
            for(int x = 0; x < dbList.size(); x++){

                //Creates new list items using the dreams from the db
                dreamList.add(new DreamListItem(dbList.get(x).getTitle(), dbList.get(x).getTime(),dbList.get(x).getDate()));

            }

            adapter = new DreamListAdapter(context, dreamList);
            setListAdapter(adapter);
        }else{
            noDreams.setVisibility(View.VISIBLE);
        }

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);


    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        //Clears the adapter when there aren't any dreams left.
        if(db.getDreamCount() == 0){
            if(adapter!=null) {
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(deleteSelected == false){
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

            transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom);
            transaction.replace(R.id.framelayout, frag);
            transaction.addToBackStack(context.getString(R.string.dreams_fragment));
            transaction.commit();
        }else{



            if(deleteDreamList.contains(position)){
                v.setBackground(context.getDrawable(R.drawable.round_purple_list_item));
                deleteDreamList.remove((Object) position);
            }else{
                v.setBackground(context.getDrawable(R.drawable.delete_list_item));
                deleteDreamList.add(position);
            }


        }




    }

    public void createDeleteAlert(){
        //Creates dialog using custom layout
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.delete_all_dialog);
        dialog.setCancelable(true);

        Button delete = (Button) dialog.findViewById(R.id.deletedream);
        Button close = (Button) dialog.findViewById(R.id.closedialog);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAllDreams();
                buttonLayout.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //Clears the adapter when there aren't any dreams left.
                if(db.getDreamCount() == 0){
                    if(adapter!=null) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        dialog.show();
    }
}
