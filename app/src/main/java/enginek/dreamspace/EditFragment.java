package enginek.dreamspace;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Joseph on 10/9/2016.
 */
public class EditFragment extends Fragment {

    TextView time, date;
    String originalDream, originalTitle;
    EditText dream, title;
    Button save, close;

    View view;
    Bundle args;
    DatabaseHandler db;

    Dream d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_fragment, container, false);

        MainActivity.dreamsClicked();

        args = getArguments();
        db = new DatabaseHandler(view.getContext());

        title = (EditText) view.findViewById(R.id.edittitle);
        time = (TextView) view.findViewById(R.id.dreamtime);
        date = (TextView) view.findViewById(R.id.dreamdate);
        dream = (EditText) view.findViewById(R.id.editdream);

        title.setText(args.getString("title"));
        time.setText(args.getString("time"));
        date.setText(args.getString("date"));
        dream.setText(args.getString("dream"));

        originalDream = dream.getText().toString();
        originalTitle = title.getText().toString();

        save = (Button) view.findViewById(R.id.savedit);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Does nothing if nothing was changed. Updates db if any edits were made.
                if(originalDream.equals(dream.getText().toString()) && originalTitle.equals(title.getText().toString())){
                    getFragmentManager().popBackStack();
                }else
                    //Pops a toast if the dream is empty.
                    if(dream.getText().toString().equals("")){
                        Toast.makeText(view.getContext(), "I can't save an empty dream! Press delete on the page before this if you want to remove it.", Toast.LENGTH_LONG).show();
                }else
                     //Pops a toast if the title is empty.
                    if(title.getText().toString().equals("")) {
                        Toast.makeText(view.getContext(), "I can't save it with no title! Please enter one.", Toast.LENGTH_SHORT).show();
                    }else{
                            d = new Dream();

                            d.setId(args.getInt("id"));
                            d.setTitle(title.getText().toString());
                            d.setTime(args.getString("time"));
                            d.setDate(args.getString("date"));
                            d.setDream(dream.getText().toString());

                            db.updateDream(d);

                            getFragmentManager().popBackStack();

                    }
            }
        });

        close = (Button) view.findViewById(R.id.closeedit);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                getFragmentManager().popBackStack();
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
}
