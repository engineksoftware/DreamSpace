package enginek.dreamspace;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFragment extends Fragment {

    private static final String ADD_TAG = "ADD FRAGMENT";
    private static final String DREAM_TAG = "DREAM FRAGMENT";
    private static final String STAT_TAG = "STAT FRAGMENT";
    private static final String CON_FRAG = "CON FRAGMENT";

    View view;
    String title;
    EditText text;
    DatabaseHandler db;
    AdView adView;
    AdRequest request;
    boolean adLoaded;
    ImageButton back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_fragment, container, false);

        MainActivity.addDreamClicked();

        adView = (AdView) view.findViewById(R.id.adView);
        request = new AdRequest.Builder().build();
        adLoaded = false;

        ImageButton button = (ImageButton) view.findViewById(R.id.savedream);
        text = (EditText) view.findViewById(R.id.dreamtext);
        db = new DatabaseHandler(view.getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler db = new DatabaseHandler(view.getContext());

                if(text.getText().toString().equals("")){
                    Toast.makeText(getActivity(),"I can't save an empty dream!", Toast.LENGTH_SHORT).show();
                }else{
                    createAlert();
                }
            }
        });

        back = (ImageButton) view.findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        return view;
    }

    @Override
    public void onPause(){
        super.onPause();

        adView.pause();
    }

    @Override
    public void onResume(){
        super.onResume();

        if(adLoaded == false) {
            adView.loadAd(request);
            adLoaded = true;
        }else{
            adView.resume();
        }


    }

    public void createAlert(){

        //Creates dialog using custom layout
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.title_dialog);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.setCancelable(true);

        final EditText input = (EditText) dialog.findViewById(R.id.dreamTitle);
        Button save = (Button) dialog.findViewById(R.id.saveDialog);
        Button close = (Button) dialog.findViewById(R.id.closeDialog);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(input.getText().toString().equals("")){
                    Toast.makeText(view.getContext(), "Please enter a title for the dream. ", Toast.LENGTH_SHORT).show();
                }else{
                    title =  input.getText().toString();

                    Calendar calendar = Calendar.getInstance();

                    //Formats time and date
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat tf = new SimpleDateFormat("hh:mm");
                    String time = tf.format(calendar.getTime());
                    String date = df.format(calendar.getTime());

                    //Adds am or pm to time
                    if(calendar.get(Calendar.HOUR_OF_DAY) == 12 || calendar.get(Calendar.HOUR_OF_DAY) > 12){
                        time += "pm";
                    }else{
                        time += "am";
                    }

                    //Saves dream to db
                    String dreamText = text.getText().toString();
                    Dream dream = new Dream(title, dreamText, time, date);
                    db.addDream(dream);

                    //Clears EditText
                    text.setText("");

                    //Closes the keyboard when you press save
                    InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                    dialog.dismiss();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
