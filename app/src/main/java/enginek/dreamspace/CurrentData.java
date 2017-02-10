package enginek.dreamspace;

/**
 * Created by Joseph Kessler on 2/10/2017.
 */

public class CurrentData {

    int week;
    int month;
    int year;
    int id;

    public CurrentData(){
        this.week = 0;
        this.month = 0;
        this.year = 0;
        this.id = 0;

    }

    public CurrentData(int id, int dayCounter, int weekCounter, int monthCounter, int yearCounter, int started){
        this.week = dayCounter;
        this.month = weekCounter;
        this.year = monthCounter;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeek(int week){
        this.week = week;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public int getWeek() {
        return week;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

}
