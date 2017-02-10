package enginek.dreamspace;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DreamListItem {

    String title;
    String time;
    String date;

    public DreamListItem(String title, String time, String date){
        this.title = title;
        this.time = time;
        this.date = date;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
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
}
