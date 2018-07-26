package masson.diiage.org.mydivia.Adapter;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import masson.diiage.org.mydivia.Entities.Stop;
import masson.diiage.org.mydivia.R;

public class StopAdapter extends BaseAdapter {
    private ArrayList<Stop> listStop;
    private Activity context;
    private LayoutInflater layoutInflater;

    public StopAdapter(ArrayList<Stop> listStop, Activity context) {
        this.listStop = listStop;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return listStop.size(); }

    @Override
    public Stop getItem(int i) { return listStop.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // La vue qui est retournée
        View view = convertView;
        // Permet de mémoriser les calculs de findByIds
        StopViewHolder stopViewHolder;

        try {
            // La vue est recyclée si le convertView est null
            if (convertView != null) {
                // On récupère la vue
                stopViewHolder = (StopAdapter.StopViewHolder)view.getTag();
            } else {
                view = layoutInflater.inflate(R.layout.item_stop, null);
                stopViewHolder = new StopAdapter.StopViewHolder((TextView) view.findViewById(R.id.stopName), (TextView) view.findViewById(R.id.stopId), (TextView) view.findViewById(R.id.stoplineId));
                view.setTag(stopViewHolder);
            }

            Stop stop = getItem(i);
            stopViewHolder.labelTitle.setText(stop.getName());
            stopViewHolder.labelId.setText(String.valueOf(stop.getId()));
            stopViewHolder.labelLineId.setText(String.valueOf(stop.getLineId()));
        }
        catch (Exception e) {
            e.getStackTrace();
        }

        return view;
    }

    private class StopViewHolder {
        public TextView labelTitle;
        public TextView labelId;
        public TextView labelLineId;

        public StopViewHolder () {
        }

        public StopViewHolder(TextView labelTitle, TextView labelId, TextView labelLineId) {
            this.labelTitle = labelTitle;
            this.labelId = labelId;
            this.labelLineId = labelLineId;
        }
    }
}
