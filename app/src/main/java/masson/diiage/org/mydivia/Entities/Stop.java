package masson.diiage.org.mydivia.Entities;

import android.content.ContentValues;

import masson.diiage.org.mydivia.Database.DatabaseHelper;

public class Stop {
    long id;
    long lineId;
    String Name;

    public Stop() {
    }

    public Stop(long id, long lineId, String name) {
        this.id = id;
        this.lineId = lineId;
        Name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.contains("&#039;")) {
            name = name.replace("&#039;", "'").trim();
        }
        Name = name;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_STOP_ID, this.getId());
        contentValues.put(DatabaseHelper.TABLE_STOP_ID_LINE, this.getLineId());
        contentValues.put(DatabaseHelper.TABLE_STOP_NAME, this.getName());
        return contentValues;
    }

    public static ContentValues toContentValues(long id, long lineId, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_STOP_ID, id);
        contentValues.put(DatabaseHelper.TABLE_STOP_ID_LINE, lineId);
        contentValues.put(DatabaseHelper.TABLE_STOP_NAME, name);
        return contentValues;
    }
}
