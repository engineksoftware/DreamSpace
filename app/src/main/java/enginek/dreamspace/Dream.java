package enginek.dreamspace;

/**
 * Created by Joseph on 10/8/2016.
 */
public class Dream {

    int id;
    String title, dream, time, date, vector;

    public Dream(){

    }

    public Dream(int id, String title, String dream, String time, String date, String vector){
        this.id = id;
        this.title = title;
        this.dream = dream;
        this.time = time;
        this.date = date;
        this.vector = vector;
    }

    public Dream(String title,String dream, String time, String date, String vector){
        this.title = title;
        this.dream = dream;
        this.time = time;
        this.date = date;
        this.vector = vector;
    }

    public Dream(String title,String dream, String time, String date){
        this.title = title;
        this.dream = dream;
        this.time = time;
        this.date = date;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {return this.title;}

    public void setTitle(String title) {this.title = title;}

    public String getDream(){
        return this.dream;
    }

    public void setDream(String dream){
        this.dream = dream;
    }

    public String getTime(){
        return this.time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getVector(){
        return this.vector;
    }

    public void setVector(String vector){
        this.vector = vector;
    }

}
