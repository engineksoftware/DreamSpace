package enginek.dreamspace;

/**
 * Created by Joseph on 12/11/2016.
 */
public class Connection {

    int connection_id, dreamA_id, dreamB_id, accepted;

    public Connection(int connection_id, int dreamA_id, int dreamB_id, int accepted){
        this.connection_id = connection_id;
        this.dreamA_id = dreamA_id;
        this.dreamB_id = dreamB_id;
        this.accepted = accepted;

    }

    public Connection( int dreamA_id, int dreamB_id, int accepted){
        this.dreamA_id = dreamA_id;
        this.dreamB_id = dreamB_id;
        this.accepted = accepted;

    }

    public Connection(){

    }

    public void setConnection_id(int connection_id){
        this.connection_id = connection_id;
    }

    public void setDreamA_id(int dreamA_id){
        this.dreamA_id = dreamA_id;
    }

    public void setDreamB_id(int dreamB_id){
        this.dreamB_id = dreamB_id;
    }

    public void setAccepted(int accepted){
        this.accepted = accepted;
    }

    public int getConnection_id(){
        return this.connection_id;
    }

    public int getDreamA_id(){
        return this.dreamA_id;
    }

    public int getDreamB_id(){
        return this.dreamB_id;
    }

    public int getAccepted(){
        return this.accepted;
    }
}
