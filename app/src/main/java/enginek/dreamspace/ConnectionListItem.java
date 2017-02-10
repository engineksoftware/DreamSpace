package enginek.dreamspace;

/**
 * Created by Joseph on 12/11/2016.
 */
    public class ConnectionListItem {

    String dreamOneTitle, dreamTwoTitle;
    int accepted;

    public ConnectionListItem(String dreamOneTitle, String dreamTwoTitle, int accepted){
        this.dreamOneTitle = dreamOneTitle;
        this.dreamTwoTitle = dreamTwoTitle;
        this.accepted = accepted;
    }

    public void setDreamOneTitle(String dreamOneTitle){
        this.dreamOneTitle = dreamOneTitle;
    }

    public void setDreamTwoTitle(String dreamTwoTitle){
        this.dreamTwoTitle = dreamTwoTitle;
    }

    public void setAccepted(int accepted){
        this.accepted = accepted;
    }

    public String getDreamOneTitle(){
        return this.dreamOneTitle;
    }

    public String getDreamTwoTitle(){
        return this.dreamTwoTitle;
    }

    public int getAccepted(){
        return this.accepted;
    }
}
