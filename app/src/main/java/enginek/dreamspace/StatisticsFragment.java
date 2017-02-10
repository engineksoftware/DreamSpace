package enginek.dreamspace;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Joseph on 12/28/2016.
 */
public class StatisticsFragment extends Fragment {

    DatabaseHandler handler;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView numDreams, avgPerWeek, avgPerMonth, avgPerYear, mostInOneWeek, mostInOneMonth, mostInOneYear, currentWeek, currentMonth, currentYear, test1, test2;
    ImageButton connectionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stat_fragment, container, false);
        context = view.getContext();

        MainActivity.statisticsClicked();



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
        test1 = (TextView) view.findViewById(R.id.test1);
        test2 = (TextView) view.findViewById(R.id.test2);

        pref = context.getSharedPreferences(context.getString(R.string.statistics_data),Context.MODE_PRIVATE);
        editor = pref.edit();

        numDreams.setText(String.valueOf(pref.getInt(context.getString(R.string.total_amount_of_dreams), 0)));
        avgPerWeek.setText(String.valueOf(pref.getInt(context.getString(R.string.week_average), 0)));
        avgPerMonth.setText(String.valueOf(pref.getInt(context.getString(R.string.month_average), 0)));
        avgPerYear.setText(String.valueOf(pref.getInt(context.getString(R.string.year_average), 0)));
        mostInOneWeek.setText(String.valueOf(pref.getInt(context.getString(R.string.most_in_a_week), 0)));
        mostInOneMonth.setText(String.valueOf(pref.getInt(context.getString(R.string.most_in_a_month), 0)));
        mostInOneYear.setText(String.valueOf(pref.getInt(context.getString(R.string.most_in_a_year), 0)));
        currentWeek.setText(String.valueOf(pref.getInt(context.getString(R.string.week_dream_counter), 0)));
        currentMonth.setText(String.valueOf(pref.getInt(context.getString(R.string.month_dream_counter), 0)));
        currentYear.setText(String.valueOf(pref.getInt(context.getString(R.string.year_dream_counter), 0)));
        test1.setText(String.valueOf(pref.getInt(context.getString(R.string.current_day_counter), 0)));
        test2.setText(String.valueOf(pref.getInt(context.getString(R.string.week_day_counter), 0)));

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
