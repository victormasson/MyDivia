package masson.diiage.org.mydivia.Entities;

import android.content.ContentValues;

import masson.diiage.org.mydivia.Database.DatabaseHelper;

public class Bookmarks {
    long id;
    long lineId;
    long stopId;
    Line line;
    Stop stop;

    public Bookmarks() {
    }

    public Bookmarks(long id, long lineId, long stopId) {
        this.id = id;
        this.lineId = lineId;
        this.stopId = stopId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public long getStopId() {
        return stopId;
    }

    public void setStopId(long stopId) {
        this.stopId = stopId;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_BOOKMARKS_ID_LINE, this.getLineId());
        contentValues.put(DatabaseHelper.TABLE_BOOKMARKS_ID_STOP, this.getStopId());
        return contentValues;
    }

    public static ContentValues toContentValues(long id, long lineId, long stopId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_BOOKMARKS_ID_LINE, lineId);
        contentValues.put(DatabaseHelper.TABLE_BOOKMARKS_ID_STOP, stopId);
        return contentValues;
    }
}
