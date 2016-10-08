package enginek.dreamspace;

import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DreamFragment extends Fragment {

    TextView title, time, date, dream;
    Button edit, delete;
    DatabaseHandler db;
    View view;
    Bundle args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dream_fragment, container, false);

        args = getArguments();
        db = new DatabaseHandler(view.getContext());

        title = (TextView) view.findViewById(R.id.dreamtitle);
        time = (TextView) view.findViewById(R.id.dreamtime);
        date = (TextView) view.findViewById(R.id.dreamdate);
        dream = (TextView) view.findViewById(R.id.yourdream);

        title.setText(args.getString("title"));
        time.setText(args.getString("time"));
        date.setText(args.getString("date"));
        dream.setText(args.getString("dream"));

        delete = (Button) view.findViewById(R.id.deletedream);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlert();
            }
        });

        return view;
    }

    public void createAlert(){

        //Creates dialog using custom layout
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.delete_dialog);
        dialog.setCancelable(true);

        Button delete = (Button) dialog.findViewById(R.id.deletedream);
        Button close = (Button) dialog.findViewById(R.id.closedialog);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creates a dream object so the db can delete it
                Dream dream = new Dream();
                dream.setId(args.getInt("id"));
                db.deleteDream(dream);

                //Closes the dialog, pops back to the last fragment, and unhides the buttons
                dialog.dismiss();
                MainActivity.unhideButtons();
                getFragmentManager().popBackStack();

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
