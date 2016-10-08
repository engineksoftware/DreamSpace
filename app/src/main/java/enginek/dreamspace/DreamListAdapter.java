package enginek.dreamspace;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DreamListAdapter extends ArrayAdapter {

    private Context context;

    public DreamListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView dreamTitle, dreamTime, dreamDate;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        DreamListItem item = (DreamListItem) getItem(position);
        View view;

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            view = inflater.inflate(R.layout.dream_list_item, null);

            holder = new ViewHolder();
            holder.dreamTitle = (TextView) view.findViewById(R.id.dreamTitle);
            holder.dreamTime = (TextView) view.findViewById(R.id.dreamTime);
            holder.dreamDate = (TextView) view.findViewById(R.id.dreamDate);

            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        holder.dreamTitle.setText(item.getTitle());
        holder.dreamTime.setText(item.getTime());
        holder.dreamDate.setText(item.getDate());

        return view;


    }
}
