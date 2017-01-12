/*
    Dream Space
    Joseph Kessler
    Start: 31 August 2016
 */

package enginek.dreamspace;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends FragmentActivity{

    private static final String ADD_TAG = "ADD FRAGMENT";
    private static final String DREAM_TAG = "DREAM FRAGMENT";
    private static final String STAT_TAG = "STAT FRAGMENT";

    static ImageButton add, dreams,statistics;
    AddFragment addFrag;
    DreamsFragment dreFrag;
    StatisticsFragment statFrag;
    static LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFrag = new AddFragment();
        dreFrag = new DreamsFragment();
        statFrag = new StatisticsFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, addFrag).commit();

        add = (ImageButton) findViewById(R.id.addButton);
        dreams = (ImageButton) findViewById(R.id.dreamsButton);
        statistics = (ImageButton) findViewById(R.id.statisticsButton);
        layout = (LinearLayout) findViewById(R.id.mainbuttons);

        addDream(add);

    }

    public void addDream(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.framelayout, addFrag, ADD_TAG);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public static void addDreamClicked(){
        add.setBackgroundResource(R.drawable.purple_button_pressed);
        dreams.setBackgroundResource(R.drawable.purple_button);
        statistics. setBackgroundResource(R.drawable.purple_button);
    }

    public void dreams(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //Checks to see which frag is open before choosing the animation.
        //Used to make sure this frag looks like its sliding in from the correct side.
        if(getSupportFragmentManager().findFragmentByTag(ADD_TAG) != null && getSupportFragmentManager().findFragmentByTag(ADD_TAG).isVisible()){
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
        if(getSupportFragmentManager().findFragmentByTag(STAT_TAG) != null && getSupportFragmentManager().findFragmentByTag(STAT_TAG).isVisible()){
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        }

        transaction.replace(R.id.framelayout, dreFrag, DREAM_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void dreamsClicked(){
        dreams.setBackgroundResource(R.drawable.purple_button_pressed);
        add.setBackgroundResource(R.drawable.purple_button);
        statistics. setBackgroundResource(R.drawable.purple_button);
    }

    public void statistics(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.framelayout, statFrag, STAT_TAG);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void statisticsClicked(){
        statistics.setBackgroundResource(R.drawable.purple_button_pressed);
        dreams.setBackgroundResource(R.drawable.purple_button);
        add. setBackgroundResource(R.drawable.purple_button);
    }
}
