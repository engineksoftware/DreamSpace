package enginek.dreamspace;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Joseph on 10/7/2016.
 */
public class ConFragment extends Fragment {

    JSONObject obj;
    String[] words;
    int[] vectorA, vectorB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_fragment, container, false);

        MainActivity.connectionsClicked();

        TextView test = (TextView) view.findViewById(R.id.test);
        TextView test2 = (TextView) view.findViewById(R.id.test2);
        TextView test3 = (TextView) view.findViewById(R.id.test3);
        TextView test4 = (TextView) view.findViewById(R.id.test4);
        TextView test5 = (TextView) view.findViewById(R.id.test5);
        DatabaseHandler handler = new DatabaseHandler(view.getContext());
        List<Dream> dream = handler.getDreams();

        test.setText(dream.get(0).getVector());
        test2.setText(dream.get(1).getVector());
        test3.setText(dream.get(2).getVector());
        test4.setText(dream.get(3).getVector());

        try{
            words = createWordList(new JSONObject(dream.get(0).getVector()), new JSONObject(dream.get(2).getVector()));
            vectorA = createVector(words, new JSONObject(dream.get(0).getVector()));
            vectorB = createVector(words, new JSONObject(dream.get(2).getVector()));
        }catch (JSONException e){
            System.out.println(e);
        }

        test5.setText("\n" +Arrays.toString(words) + "\n" + Arrays.toString(vectorA)
                + "\n" + Arrays.toString(vectorB)
                + "\n" + String.valueOf(calculateSimilarity(vectorA,vectorB)));
        //test5.setText("\n" + Arrays.toString(vectorA) + "\n" + Arrays.toString(vectorB));


        return view;
    }

    public String[] createWordList(JSONObject v1, JSONObject v2){

        Iterator<?> v1Keys = v1.keys();
        Iterator<?> v2Keys = v2.keys();
        Set<String> words = new HashSet<>();

        while(v1Keys.hasNext()){
            words.add((String)v1Keys.next());
        }

        while(v2Keys.hasNext()){
            words.add((String)v2Keys.next());
        }

        return words.toArray(new String[words.size()]);

    }

    public int[] createVector(String[] words, JSONObject obj){
        int[] occurences = new int[words.length];
        for(int x = 0; x < words.length;x++){
            if(!obj.has(words[x])){
                occurences[x] = 0;
            }else{

                try{
                    occurences[x] = obj.getInt(words[x]);
                }catch (JSONException e){
                    System.out.println(e);
                }

            }

        }

        return occurences;
    }

    public double calculateSimilarity(int[] vectorA, int[] vectorB){
        int sumA = 0;
        int sumB = 0;
        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;


        for(int x = 0; x < vectorA.length; x++){
            dotProduct = dotProduct + (vectorA[x] * vectorB[x]);
        }

        for(int x = 0; x < vectorA.length; x++){
            sumA += (vectorA[x] * vectorA[x]);
        }

        for(int x = 0; x < vectorA.length; x++){
            sumB += (vectorB[x] * vectorB[x]);
        }

        magnitudeA = Math.sqrt(sumA);
        magnitudeB = Math.sqrt(sumB);

        return dotProduct / (magnitudeA * magnitudeB);

    }



}
