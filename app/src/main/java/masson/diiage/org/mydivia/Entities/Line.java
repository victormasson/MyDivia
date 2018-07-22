package masson.diiage.org.mydivia.Entities;

import android.content.ContentValues;

import masson.diiage.org.mydivia.Database.DatabaseHelper;

public class Line {
    long id;
    String Type; //Tram
    String Name; //T1
    String Direction; //Dijon

    public Line(long id, String type, String name, String direction) {
        this.id = id;
        Type = type;
        Name = name;
        Direction = direction;
    }

    public Line() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        if (name.contains("perturb")) {
            name = name.replace("perturb", "").trim();
        }
        Name = name;
    }

    public String getDirection() {
        return Direction;
    }

    public void setDirection(String direction) {
        if (direction.contains("&#039;")) {
            direction = direction.replace("&#039;", "'").trim();
        }
        Direction = direction;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_LINE_ID, this.getId());
        contentValues.put(DatabaseHelper.TABLE_LINE_TYPE, this.getType());
        contentValues.put(DatabaseHelper.TABLE_LINE_NAME, this.getName());
        contentValues.put(DatabaseHelper.TABLE_LINE_DIRECTION, this.getDirection());
        return contentValues;
    }

    public static ContentValues toContentValues(long id, String type, String name, String direction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TABLE_LINE_ID, id);
        contentValues.put(DatabaseHelper.TABLE_LINE_TYPE, type);
        contentValues.put(DatabaseHelper.TABLE_LINE_NAME, name);
        contentValues.put(DatabaseHelper.TABLE_LINE_DIRECTION, direction);
        return contentValues;
    }
}
