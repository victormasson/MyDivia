package masson.diiage.org.mydivia.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import masson.diiage.org.mydivia.Entities.Line;
import masson.diiage.org.mydivia.R;

public class LineAdapter extends BaseAdapter {
    private ArrayList<Line> listLine;
    private Activity context;
    private LayoutInflater layoutInflater;

    public LineAdapter(ArrayList<Line> listLine, Activity context) {
        this.listLine = listLine;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return listLine.size(); }

    @Override
    public Line getItem(int i) { return listLine.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // La vue qui est retournée
        View view = convertView;
        // Permet de mémoriser les calculs de findByIds
        LineViewHolder lineViewHolder;

        try {
            // La vue est recyclée si le convertView est null
            if (convertView != null) {
                // On récupère la vue
                lineViewHolder = (LineViewHolder)view.getTag();
            } else {
                view = layoutInflater.inflate(R.layout.item_line, null);
                lineViewHolder = new LineViewHolder((TextView) view.findViewById(R.id.lineId), (TextView) view.findViewById(R.id.lineTitle), (TextView) view.findViewById(R.id.lineDirection));
                view.setTag(lineViewHolder);
            }

            Line line = getItem(i);
            lineViewHolder.labelId.setText(String.valueOf(line.getId()));
            lineViewHolder.labelTitle.setText(line.getName().toString());
            lineViewHolder.labelDirection.setText(line.getDirection().toString());
        }
        catch (Exception e) {
            e.getStackTrace();
        }

        return view;
    }

    private class LineViewHolder {
        public TextView labelId;
        public TextView labelTitle;
        public TextView labelDirection;

        public LineViewHolder () {
        }

        public LineViewHolder(TextView labelId, TextView labelTitle, TextView labelDirection) {
            this.labelId = labelId;
            this.labelTitle = labelTitle;
            this.labelDirection = labelDirection;
        }
    }
}
