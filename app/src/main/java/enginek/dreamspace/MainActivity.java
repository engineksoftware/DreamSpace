/*
    Dream Space
    Joseph Kessler
    Start: 31 August 2016
 */

package enginek.dreamspace;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends Activity{

    /*
        Enum for the different messages the space thread could send to the UI thread.
     */
    private enum Messages {
        ADD_SMALL_YELLOW, ADD_SMALL_BLUE, ADD_SMALL_WHITE, ADD_SMALL_ORANGE,
        ADD_MED_YELLOW,   ADD_MED_BLUE,   ADD_MED_WHITE,   ADD_MED_ORANGE,
        ADD_LARGE_YELLOW, ADD_LARGE_BLUE, ADD_LARGE_WHITE, ADD_LARGE_ORANGE
    }

    int screenWidth;
    int screenHeight;

    static RelativeLayout background;
    static RelativeLayout.LayoutParams params;

    /*
        Handler to manipulate the UI.
     */
    final Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg){

            ImageView star = new ImageView(MainActivity.this);

            if(msg.obj.equals(Messages.ADD_SMALL_YELLOW)){
                star.setBackgroundResource(R.drawable.small_star_yellow);
            }else
                if(msg.obj.equals(Messages.ADD_SMALL_BLUE)){
                    star.setBackgroundResource(R.drawable.small_star_blue);
                }else
                    if(msg.obj.equals(Messages.ADD_SMALL_WHITE)){
                        star.setBackgroundResource(R.drawable.small_star_white);
                    }else
                        if (msg.obj.equals(Messages.ADD_SMALL_ORANGE)){
                            star.setBackgroundResource(R.drawable.small_star_orange);
                        }

            int starPlacement = randomizePlacement();
            params = new RelativeLayout.LayoutParams(star.getWidth(), star.getHeight());
            params.leftMargin = starPlacement;
            params.topMargin = 40;
            star.setLayoutParams(params);
            background.addView(star);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        screenWidth  = display.getWidth();
        screenHeight = display.getHeight();

        space();


    }

    @Override
    protected void onStart(){
        super.onStart();

        background = (RelativeLayout) findViewById(R.id.starBackground);
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

                int size = randomizeSize();
                int color = randomizeColor();
                Messages messages = chooseMessage(size, color);

                Message msg = uiHandler.obtainMessage();
                msg.obj = messages;
                uiHandler.sendMessage(msg);


            }
        });

        thread.start();
    }

    /*
        Randomly picks a size for the start based on a probability.
     */
    private int randomizeSize(){
        double sizeRandom  = Math.random();
        int size;

        /*
                    Randomly chooses a size.
                    Probabilities:
                        SMALL = 50%
                        MED = 30%
                        LARGE = 20%
        */
        if(sizeRandom  > 0.5){
            size = 0;
        }else
        if(sizeRandom <= 0.5 && sizeRandom > 0.2){
            size = 1;
        }else{
            size = 2;
        }

        return size;
    }

    /*
        Randomly picks a color for the start based on a probability.
     */
    private int randomizeColor(){
        double colorRandom  = Math.random();
        int color;

        /*
                    Randomly chooses a color.
                    Probabilities:
                        WHITE = 40%
                        BLUE = 30%
                        ORANGE = 15%
                        YELLOW = 15%
                 */
        if(colorRandom  > 0.6){
            color = 0;
        }else
        if(colorRandom <= 0.6 && colorRandom > 0.3){
            color = 1;
        }else
        if(colorRandom <= 0.3 && colorRandom > 0.15){
            color = 2;
        }else{
            color = 3;
        }

        return color;
    }

    /*
        Randomly chooses the placement for the star based on the width of the screen.
     */
    private int randomizePlacement(){
        Random random = new Random();
        return random.nextInt(screenWidth);
    }
    /*
        Chooses which message to send based on the size and color that were randomly chosen.
        Could possibly be re-written to choose the message by adding the color and size together,
        but I'm going to leave it like this for now.
     */
    private Messages chooseMessage(int size, int color){

        Messages messages;

        if(size == 0){
            if(color == 0)
                messages = Messages.ADD_SMALL_WHITE;
            else
            if(color == 1)
                messages = Messages.ADD_SMALL_BLUE;
            else
            if(color == 2)
                messages = Messages.ADD_SMALL_ORANGE;
            else
                messages = Messages.ADD_SMALL_YELLOW;
        }else
            if(size == 1){
                if(color == 0)
                    messages = Messages.ADD_MED_WHITE;
                else
                if(color == 1)
                    messages = Messages.ADD_MED_BLUE;
                else
                if(color == 2)
                    messages = Messages.ADD_MED_ORANGE;
                else
                    messages = Messages.ADD_MED_YELLOW;
            }else{
                if(color == 0)
                    messages = Messages.ADD_LARGE_WHITE;
                else
                if(color == 1)
                    messages = Messages.ADD_LARGE_BLUE;
                else
                if(color == 2)
                    messages = Messages.ADD_LARGE_ORANGE;
                else
                    messages = Messages.ADD_LARGE_YELLOW;
            }

        return messages;

    }
}
