package enginek.dreamspace;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Joseph on 12/27/2016.
 */
public class ConViewFragment extends Fragment {

    TextView title, dreamAText, dreamBText;
    DatabaseHandler handler;
    Context context;
    Button accept, reject;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_view_fragment, container, false);
        context = view.getContext();

        MainActivity.statisticsClicked();

        handler = new DatabaseHandler(context);
        bundle = getArguments();
        Dream dreamA = handler.getDream(bundle.getInt("dreamA_id"));
        Dream dreamB = handler.getDream(bundle.getInt("dreamB_id"));

        title = (TextView) view.findViewById(R.id.connectionTitle);
        dreamAText = (TextView) view.findViewById(R.id.dreamA);
        dreamBText = (TextView) view.findViewById(R.id.dreamB);

        title.setText(dreamA.getTitle() + " and " + dreamB.getTitle());
        dreamAText.setText(dreamA.getDream());
        dreamBText.setText(dreamB.getDream());

        accept = (Button) view.findViewById(R.id.acceptConnection);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection();
                connection.setConnection_id(bundle.getInt("connection_id"));
                connection.setDreamA_id(bundle.getInt("dreamA_id"));
                connection.setDreamB_id(bundle.getInt("dreamB_id"));
                connection.setAccepted(1);

                handler.updateConnection(connection);
                getFragmentManager().popBackStack();
            }
        });

        reject = (Button) view.findViewById(R.id.rejectConnection);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection();
                connection.setConnection_id(bundle.getInt("connection_id"));
                handler.deleteConnection(connection);

                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
