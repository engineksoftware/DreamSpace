package enginek.dreamspace;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConnectionListAdapter extends ArrayAdapter{

    private Context context;
    List<ConnectionListItem> connections;

    public ConnectionListAdapter(Context context, List<ConnectionListItem> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        connections = items;
        this.context = context;
    }

    private class ViewHolder{
        TextView dreamOne, dreamTwo;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        ConnectionListItem item = (ConnectionListItem) getItem(position);
        View view;

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            if(item.getAccepted() > 0)
                view = inflater.inflate(R.layout.accepted_connection_list_item, null);
            else
                view = inflater.inflate(R.layout.possible_connection_list_item, null);

            holder = new ViewHolder();
            holder.dreamOne = (TextView) view.findViewById(R.id.dreamOne);
            holder.dreamTwo = (TextView) view.findViewById(R.id.dreamTwo);

            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.dreamOne.setText(connections.get(position).getDreamOneTitle());
        holder.dreamTwo.setText(connections.get(position).getDreamTwoTitle());

        return view;


    }

    public int getCount(){
        return connections.size();
    }

    public Object getItem(int position){
        return connections.get(position);
    }
}