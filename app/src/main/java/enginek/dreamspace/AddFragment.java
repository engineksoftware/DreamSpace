package enginek.dreamspace;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFragment extends Fragment {

    View view;
    String title;
    EditText text;
    DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_fragment, container, false);

        MainActivity.addDreamClicked();

        Button button = (Button) view.findViewById(R.id.savedream);
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

        return view;
    }

    public void createAlert(){

        //Creates dialog using custom layout
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.title_dialog);
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
                    if(calendar.get(Calendar.AM_PM) == Calendar.AM){
                        time += "am";
                    }else{
                        time += "pm";
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
