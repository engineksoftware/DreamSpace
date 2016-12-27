package enginek.dreamspace;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by Joseph on 12/27/2016.
 */
public class ConViewFragment extends Fragment {

    TextView title, dreamAText, dreamBText;
    DatabaseHandler handler;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_view_fragment, container, false);
        context = view.getContext();

        MainActivity.connectionsClicked();

        handler = new DatabaseHandler(context);
        Bundle bundle = getArguments();
        Dream dreamA = handler.getDream(bundle.getInt("dreamA_id"));
        Dream dreamB = handler.getDream(bundle.getInt("dreamB_id"));

        title = (TextView) view.findViewById(R.id.connectionTitle);
        dreamAText = (TextView) view.findViewById(R.id.dreamA);
        dreamBText = (TextView) view.findViewById(R.id.dreamB);

        title.setText(dreamA.getTitle() + " and " + dreamB.getTitle());
        dreamAText.setText(dreamA.getDream());
        dreamBText.setText(dreamB.getDream());

        return view;
    }
}
