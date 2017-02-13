package enginek.dreamspace;

/**
 * Created by Joseph Kessler on 2/10/2017.
 */

public class RecordData {

    int id;
    int weekRecord;
    int monthRecord;
    int yearRecord;


    public RecordData(){
        this.weekRecord = 0;
        this.monthRecord = 0;
        this.yearRecord = 0;
        this.id = 0;

    }

    public RecordData(int id, int dayCounter, int weekCounter, int monthCounter){
        this.weekRecord = dayCounter;
        this.monthRecord = weekCounter;
        this.yearRecord = monthCounter;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeekRecord(int weekRecord){
        this.weekRecord = weekRecord;
    }

    public void setMonthRecord(int monthRecord){
        this.monthRecord = monthRecord;
    }

    public void setYearRecord(int yearRecord){
        this.yearRecord = yearRecord;
    }

    public int getId() {
        return id;
    }

    public int getWeekRecord() {
        return weekRecord;
    }

    public int getMonthRecord() {
        return monthRecord;
    }

    public int getYearRecord() {
        return yearRecord;
    }

}
