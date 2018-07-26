package masson.diiage.org.mydivia.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import masson.diiage.org.mydivia.Entities.Bookmarks;
import masson.diiage.org.mydivia.Entities.Stop;
import masson.diiage.org.mydivia.R;

public class BookmarksAdapter extends BaseAdapter {
    private ArrayList<Bookmarks> listBookmarks;
    private Activity context;
    private LayoutInflater layoutInflater;

    public BookmarksAdapter(ArrayList<Bookmarks> listBookmarks, Activity context) {
        this.listBookmarks = listBookmarks;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return listBookmarks.size(); }

    @Override
    public Bookmarks getItem(int i) { return listBookmarks.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        BookmarksViewHolder bookmarksViewHolder = new BookmarksViewHolder();

        try {
            if (convertView != null) {
                bookmarksViewHolder = (BookmarksViewHolder)view.getTag();
            } else {
                view = layoutInflater.inflate(R.layout.item_bookmarks, null);
                bookmarksViewHolder = new BookmarksViewHolder(
                        (TextView) view.findViewById(R.id.bookmarksLineTitle),
                        (TextView) view.findViewById(R.id.bookmarksLineId),
                        (TextView) view.findViewById(R.id.bookmarksStopName),
                        (TextView) view.findViewById(R.id.bookmarksLineDirection));
                view.setTag(bookmarksViewHolder);
            }

            Bookmarks bookmarks = getItem(i);
            bookmarksViewHolder.labelLineTitle.setText(bookmarks.getLine().getName());
            bookmarksViewHolder.labelLineId.setText(String.valueOf(bookmarks.getLine().getId()));
            bookmarksViewHolder.labelStopName.setText(bookmarks.getStop().getName());
            bookmarksViewHolder.labelLineDirection.setText(bookmarks.getLine().getDirection());
        }
        catch (Exception e) {
            e.getStackTrace();
        }

        return view;
    }

    private class BookmarksViewHolder {
        public TextView labelLineTitle;
        public TextView labelLineId;
        public TextView labelStopName;
        public TextView labelLineDirection;

        public BookmarksViewHolder () {
        }

        public BookmarksViewHolder(TextView labelLineTitle, TextView labelLineId, TextView labelStopName, TextView labelLineDirection) {
            this.labelLineTitle = labelLineTitle;
            this.labelLineId = labelLineId;
            this.labelStopName = labelStopName;
            this.labelLineDirection = labelLineDirection;
        }
    }
}
