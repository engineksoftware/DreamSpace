package enginek.dreamspace;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Joseph on 10/8/2016.
 */
public class DreamListAdapter extends ArrayAdapter implements Filterable{

    private Context context;
    List<DreamListItem> dreams;
    List<DreamListItem> originalDreams;
    DreamFilter filter = new DreamFilter();

    public DreamListAdapter(Context context, List<DreamListItem> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        dreams = items;
        originalDreams = items;
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

        //Uses the filtered results because this list is searchable.
        holder.dreamTitle.setText(dreams.get(position).getTitle());
        holder.dreamTime.setText(dreams.get(position).getTime());
        holder.dreamDate.setText(dreams.get(position).getDate());

        return view;


    }

    public int getCount(){
        return dreams.size();
    }

    public Object getItem(int position){
        return dreams.get(position);
    }

    public Filter getFilter(){
        return filter;
    }

    private class DreamFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //If nothing is typed into the search box, it sets the list back to what it was originally.
            if(constraint == null || constraint.length() == 0){
                results.values = originalDreams;
                results.count = originalDreams.size();
            }else{
                List filteredDreams = new ArrayList<DreamListItem>();

                //Loops through the list to see if any of the titles contain what the user is typing.
                for(int x = 0; x < dreams.size(); x++){
                    if(dreams.get(x).getTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredDreams.add(dreams.get(x));
                    }
                }

                results.values = filteredDreams;
                results.count = filteredDreams.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dreams = (List<DreamListItem>) results.values;
            notifyDataSetChanged();
        }

    }
}
