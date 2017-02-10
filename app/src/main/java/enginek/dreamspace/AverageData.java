package enginek.dreamspace;

/**
 * Created by Joseph Kessler on 2/10/2017.
 */

public class AverageData {

    int week;
    int month;
    int year;
    int id;

    public AverageData(){
        this.week = 0;
        this.month = 0;
        this.year = 0;
        this.id = 0;

    }

    public AverageData(int id, int dayCounter, int weekCounter, int monthCounter, int yearCounter, int started){
        this.id = id;
        this.week = dayCounter;
        this.month = weekCounter;
        this.year = monthCounter;
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
