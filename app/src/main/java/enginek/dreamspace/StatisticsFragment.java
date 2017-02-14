package enginek.dreamspace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Joseph on 12/28/2016.
 */
public class StatisticsFragment extends Fragment {

    DatabaseHandler handler;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView numDreams, avgPerWeek, avgPerMonth, avgPerYear, mostInOneWeek, mostInOneMonth, mostInOneYear, currentWeek, currentMonth, currentYear;
    ImageButton connectionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stat_fragment, container, false);
        context = view.getContext();

        MainActivity.statisticsClicked();

        DatabaseHandler db = new DatabaseHandler(context);

        TextView test = (TextView) view.findViewById(R.id.test);


        numDreams = (TextView) view.findViewById(R.id.numberOfDreams);
        avgPerWeek = (TextView) view.findViewById(R.id.averagePerWeek);
        avgPerMonth = (TextView) view.findViewById(R.id.averagePerMonth);
        avgPerYear = (TextView) view.findViewById(R.id.averagePerYear);
        mostInOneWeek = (TextView) view.findViewById(R.id.mostPerWeek);
        mostInOneMonth = (TextView) view.findViewById(R.id.mostPerMonth);
        mostInOneYear = (TextView) view.findViewById(R.id.mostPerYear);
        currentWeek = (TextView) view.findViewById(R.id.currentThisWeek);
        currentMonth = (TextView) view.findViewById(R.id.currentThisMonth);
        currentYear = (TextView) view.findViewById(R.id.currentThisYear);

        if(db.getAverageData() != null && db.getCurrentData() != null && db.getRecordData() != null && db.getCounterData() != null){
            test.setText(String.valueOf(db.getCounterData().getDayCounter()));

            numDreams.setText(String.valueOf(db.getDreamCount()));
            avgPerWeek.setText(String.valueOf(db.getAverageData().getWeek()));
            avgPerMonth.setText(String.valueOf(db.getAverageData().getMonth()));
            avgPerYear.setText(String.valueOf(db.getAverageData().getYear()));
            mostInOneWeek.setText(String.valueOf(db.getRecordData().getWeekRecord()));
            mostInOneMonth.setText(String.valueOf(db.getRecordData().getMonthRecord()));
            mostInOneYear.setText(String.valueOf(db.getRecordData().getYearRecord()));
            currentWeek.setText(String.valueOf(db.getCurrentData().getWeek()));
            currentMonth.setText(String.valueOf(db.getCurrentData().getMonth()));
            currentYear.setText(String.valueOf(db.getCurrentData().getYear()));
        }



        connectionButton = (ImageButton) view.findViewById(R.id.connectionButton);
        connectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConFragment frag = new ConFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, R.anim.slide_in_top, R.anim.slide_out_bottom);
                transaction.replace(R.id.framelayout, frag, context.getString(R.string.con_fragment));
                transaction.addToBackStack(context.getString(R.string.con_fragment));
                transaction.commit();
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
