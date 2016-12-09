/*
    Dream Space
    Joseph Kessler
    Start: 31 August 2016
 */

package enginek.dreamspace;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity{

    static Button add, dreams,connections;
    AddFragment addFrag;
    DreamsFragment dreFrag;
    ConFragment conFrag;
    static LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFrag = new AddFragment();
        dreFrag = new DreamsFragment();
        conFrag = new ConFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, addFrag).commit();

        add = (Button) findViewById(R.id.addButton);
        dreams = (Button) findViewById(R.id.dreamsButton);
        connections = (Button) findViewById(R.id.connectionsButton);
        layout = (LinearLayout) findViewById(R.id.mainbuttons);

        addDream(add);

    }

    public void addDream(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, addFrag);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public static void addDreamClicked(){
        add.setBackgroundResource(R.drawable.purple_button_pressed);
        dreams.setBackgroundResource(R.drawable.purple_button);
        connections. setBackgroundResource(R.drawable.purple_button);
    }

    public void dreams(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, dreFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void dreamsClicked(){
        dreams.setBackgroundResource(R.drawable.purple_button_pressed);
        add.setBackgroundResource(R.drawable.purple_button);
        connections. setBackgroundResource(R.drawable.purple_button);
    }

    public void connections(View view){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, conFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void connectionsClicked(){
        connections.setBackgroundResource(R.drawable.purple_button_pressed);
        dreams.setBackgroundResource(R.drawable.purple_button);
        add. setBackgroundResource(R.drawable.purple_button);
    }
}
