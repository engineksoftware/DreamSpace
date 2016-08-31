/*
    Dream Space
    Joseph Kessler
    Start: 31 August 2016
 */

package enginek.dreamspace;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity{

    /*
        Enum for the different messages the space thread could send to the UI thread.
     */
    private enum Messages {
        ADD_SMALL_YELLOW, ADD_SMALL_BLUE, ADD_SMALL_WHITE, ADD_SMALL_ORANGE,
        ADD_MED_YELLOW,   ADD_MED_BLUE,   ADD_MED_WHITE,   ADD_MED_ORANGE,
        ADD_LARGE_YELLOW, ADD_LARGE_BLUE, ADD_LARGE_WHITE, ADD_LARGE_ORANGE
    }

    final Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /*
        This method creates, and starts the space themed background using a thread. It sends messages to the
        uiHandler to add the different stars.
     */
    private void space(){

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                /*
                    Constant values for the different sizes, and colors. Only being used for better readability.
                    The numbers are added together to figure out which message to send.
                 */
                final int LARGE = 8;
                final int MED = 4;
                final int SMALL = 0;

                final int YELLOW = 0;
                final int BLUE = 1;
                final int WHITE = 2;
                final int ORANGE = 3;

                Messages messages[] = Messages.values();

                double sizeRandom  = Math.random();
                double colorRandom = Math.random();
                int size, color;

                /*
                    Randomly chooses a size.
                    Probabilities:
                        SMALL = 50%
                        MED = 30%
                        LARGE = 20%
                 */
                if(sizeRandom  > 0.5){
                    size = SMALL;
                }else
                if(sizeRandom <= 0.5 && sizeRandom > 0.2){
                    size = MED;
                }else{
                    size = LARGE;
                }

                /*
                    Randomly chooses a color.
                    Probabilities:
                        WHITE = 40%
                        BLUE = 30%
                        ORANGE = 15%
                        YELLOW = 15%
                 */
                if(colorRandom  > 0.6){
                    color = WHITE;
                }else
                if(colorRandom <= 0.6 && colorRandom > 0.3){
                    color = BLUE;
                }else
                if(colorRandom <= 0.3 && colorRandom > 0.15){
                    color = ORANGE;
                }else{
                    color = YELLOW;
                }

                Message msg = uiHandler.obtainMessage();
                msg.what = messages[size+color].ordinal();
                uiHandler.sendMessage(msg);


            }
        });

        thread.start();
    }

}
