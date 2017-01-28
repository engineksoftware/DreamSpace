package enginek.dreamspace;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Created by Joseph on 12/27/2016.
 */
public class ConViewFragment extends Fragment {

    TextView title, dreamAText, dreamBText;
    DatabaseHandler handler;
    Context context;
    Button accept, reject, delete;
    Bundle bundle;
    LinearLayout buttonsLayout;
    int accepted;
    ViewSwitcher viewSwitcher;
    Dream dreamA, dreamB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_view_fragment, container, false);
        context = view.getContext();

        MainActivity.statisticsClicked();

        handler = new DatabaseHandler(context);
        bundle = getArguments();

        if(handler.getDream(bundle.getInt("dreamA_id")) == null || handler.getDream(bundle.getInt("dreamB_id")) == null){
            getFragmentManager().popBackStack();
        }else{
            dreamA = handler.getDream(bundle.getInt("dreamA_id"));
            dreamB = handler.getDream(bundle.getInt("dreamB_id"));
        }

        accepted = bundle.getInt("accepted");

        title = (TextView) view.findViewById(R.id.connectionTitle);
        dreamAText = (TextView) view.findViewById(R.id.dreamA);
        dreamBText = (TextView) view.findViewById(R.id.dreamB);
        buttonsLayout = (LinearLayout) view.findViewById(R.id.buttons);
        viewSwitcher = (ViewSwitcher) view.findViewById(R.id.buttonSwitcher);

        if(accepted > 0){
            viewSwitcher.showNext();
        }

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

        delete = (Button) view.findViewById(R.id.deleteConnection);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connection = new Connection();
                connection.setConnection_id(bundle.getInt("connection_id"));
                connection.setDreamA_id(bundle.getInt("dreamA_id"));
                connection.setDreamB_id(bundle.getInt("dreamB_id"));
                connection.setAccepted(1);

                handler.deleteConnection(connection);
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
