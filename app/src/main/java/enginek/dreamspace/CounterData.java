package enginek.dreamspace;

/**
 * Created by Joseph Kessler on 2/10/2017.
 */

public class CounterData {
    int dayCounter;
    int weekCounter;
    int monthCounter;
    int yearCounter;
    int id;

    public CounterData(){
        this.dayCounter = 0;
        this.weekCounter = 0;
        this.monthCounter = 0;
        this.yearCounter = 0;
        this.id = 0;

    }

    public CounterData(int id, int dayCounter, int weekCounter, int monthCounter, int yearCounter, int started){
        this.dayCounter = dayCounter;
        this.weekCounter = weekCounter;
        this.monthCounter = monthCounter;
        this.yearCounter = yearCounter;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDayCounter(int dayCounter){
        this.dayCounter = dayCounter;
    }

    public void setWeekCounter(int weekCounter){
        this.weekCounter = weekCounter;
    }

    public void setMonthCounter(int monthCounter){
        this.monthCounter = monthCounter;
    }

    public void setYearCounter(int yearCounter){
        this.yearCounter = yearCounter;
    }

    public int getId() {
        return id;
    }

    public int getDayCounter() {
        return dayCounter;
    }

    public int getWeekCounter() {
        return weekCounter;
    }

    public int getMonthCounter() {
        return monthCounter;
    }

    public int getYearCounter() {
        return yearCounter;
    }

}
