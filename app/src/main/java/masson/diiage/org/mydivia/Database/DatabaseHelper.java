package masson.diiage.org.mydivia.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import masson.diiage.org.mydivia.Entities.Bookmarks;
import masson.diiage.org.mydivia.Entities.Line;
import masson.diiage.org.mydivia.Entities.Stop;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "divia2018.db";

    public static final String TABLE_LINE = "line";
    public static final String TABLE_LINE_ID = "id";
    public static final String TABLE_LINE_TYPE = "type";
    public static final String TABLE_LINE_DIRECTION = "direction";
    public static final String TABLE_LINE_NAME = "name";

    public static final String CREATE_TABLE_LINE = "CREATE TABLE " + TABLE_LINE + "("
        + TABLE_LINE_ID + " INTEGER NOT NULL PRIMARY KEY,"
        + TABLE_LINE_TYPE + " TEXT,"
        + TABLE_LINE_NAME + " TEXT,"
        + TABLE_LINE_DIRECTION + " TEXT);";

    public static final String TABLE_STOP = "stop";
    public static final String TABLE_STOP_ID = "id";
    public static final String TABLE_STOP_ID_LINE = "lineId";
    public static final String TABLE_STOP_NAME = "name";

    public static final String CREATE_TABLE_STOP = "CREATE TABLE " + TABLE_STOP + "("
            + TABLE_STOP_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TABLE_STOP_NAME + " TEXT,"
            + TABLE_STOP_ID_LINE + " INTEGER);";

    public static final String TABLE_BOOKMARKS = "bookmarks";
    public static final String TABLE_BOOKMARKS_ID = "id";
    public static final String TABLE_BOOKMARKS_ID_LINE = "lineId";
    public static final String TABLE_BOOKMARKS_ID_STOP = "stopId";

    public static final String CREATE_TABLE_BOOKMARKS = "CREATE TABLE " + TABLE_BOOKMARKS + "("
            + TABLE_BOOKMARKS_ID + " INTEGER PRIMARY KEY,"
            + TABLE_BOOKMARKS_ID_LINE + " INTEGER,"
            + TABLE_BOOKMARKS_ID_STOP + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { baseUpdateTo(db, 1); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = DATABASE_VERSION; i <= newVersion; i++) {
            baseUpdateTo(db, i);
        }
        Log.d("db", "Base mise à jour");
    }

    public void baseUpdateTo(SQLiteDatabase db, int version) {
        switch (version) {
            case 1:
                db.execSQL(CREATE_TABLE_LINE);
                db.execSQL(CREATE_TABLE_STOP);
                db.execSQL(CREATE_TABLE_BOOKMARKS);
                break;
            default:
                break;
        }
    }

    public void addLine(SQLiteDatabase db, Line line) {
        db.insert(TABLE_LINE, null, line.toContentValues());
    }

    public void addStop(SQLiteDatabase db, Stop stop) {
        db.insert(TABLE_STOP, null, stop.toContentValues());
    }

    public void addBookmarks(SQLiteDatabase db, Bookmarks bookmarks) {
        long a = db.insert(TABLE_BOOKMARKS, null, bookmarks.toContentValues());
    }

    public void deleteBookmarks(SQLiteDatabase db, long stopId, long lineId) {
        Bookmarks bookmarks = getBookmarks(db, stopId, lineId);
        if (bookmarks != null) {
            db.delete(TABLE_BOOKMARKS,
                    TABLE_BOOKMARKS_ID_STOP + " = ? and " + TABLE_BOOKMARKS_ID_LINE + " = ?",
                    new String[] { String.valueOf(stopId), String.valueOf(lineId) });
        }
    }

    public ArrayList<Line> getLine(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE_LINE,
                new String[] {TABLE_LINE_ID, TABLE_LINE_TYPE, TABLE_LINE_NAME, TABLE_LINE_DIRECTION},
                null,
                null,
                null,
                null,
                null);

        ArrayList<Line> listLine = new ArrayList<Line>();
        while(cursor.moveToNext()){
            final long lineId = cursor.getLong(0);
            final String type = cursor.getString(1);
            final String name = cursor.getString(2);
            final String direction = cursor.getString(3);

            Line line = new Line();
            line.setId(lineId);
            line.setType(type);
            line.setName(name);
            line.setDirection(direction);
            listLine.add(line);
        }
        return listLine;
    }

    public Line getLine(SQLiteDatabase db, long lineId) {
        Cursor cursor = db.query(TABLE_LINE,
                new String[] {TABLE_LINE_ID, TABLE_LINE_TYPE, TABLE_LINE_NAME, TABLE_LINE_DIRECTION},
                TABLE_LINE_ID + " = ?",
                new String[] { String.valueOf(lineId) },
                null,
                null,
                null);

        Line line = new Line();
        while(cursor.moveToNext()){
            final long id = cursor.getLong(0);
            final String type = cursor.getString(1);
            final String name = cursor.getString(2);
            final String direction = cursor.getString(3);

            line.setId(id);
            line.setType(type);
            line.setName(name);
            line.setDirection(direction);
        }
        return line;
    }

    public ArrayList<Stop> getStop(SQLiteDatabase db, long lineId) {
        Cursor cursor = db.query(TABLE_STOP,
                new String[] {TABLE_STOP_ID, TABLE_STOP_ID_LINE, TABLE_STOP_NAME,},
                TABLE_STOP_ID_LINE + " = ?",
                new String[] { String.valueOf(lineId) },
                null,
                null,
                null);

        ArrayList<Stop> listStop = new ArrayList<Stop>();
        while(cursor.moveToNext()){
            final long stopId = cursor.getLong(0);
            final long stoplineId = cursor.getLong(1);
            final String name = cursor.getString(2);

            final Stop stop = new Stop() {{
                setId(stopId);
                setLineId(stoplineId);
                setName(name);
            }};
            listStop.add(stop);
        }
        return listStop;
    }

    public Bookmarks getBookmarks(SQLiteDatabase db, long stopId, long lineId) {
        Cursor cursor = db.rawQuery("", null);
        if (stopId != 0 && lineId != 0) {
            cursor = db.query(TABLE_BOOKMARKS,
                    new String[] { TABLE_BOOKMARKS_ID, TABLE_BOOKMARKS_ID_LINE, TABLE_BOOKMARKS_ID_STOP },
                    TABLE_BOOKMARKS_ID_STOP + " = ? and " + TABLE_BOOKMARKS_ID_LINE + " = ?",
                    new String[] { String.valueOf(stopId), String.valueOf(lineId) },
                    null,null,null);
        }
        Bookmarks bookmarks = new Bookmarks();
        while(cursor.moveToNext()){
            final long bookmarksId = cursor.getLong(0);
            final long bookmarksIdLine = cursor.getLong(1);
            final long bookmarksIdStop = cursor.getLong(2);

            bookmarks = new Bookmarks() {{
                setId(bookmarksId);
                setLineId(bookmarksIdLine);
                setStopId(bookmarksIdStop);
            }};
        }

        return bookmarks;
    }

    public boolean existBookmarks(SQLiteDatabase db, long stopId, long lineId) {
        Bookmarks bookmarks = getBookmarks(db, stopId, lineId);
        if (bookmarks != null || bookmarks.getId() != 0) {
            return true;
        }
        return false;
    }

    public ArrayList<Bookmarks> getBookmarks(SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("SELECT "
                    + " bm." + TABLE_BOOKMARKS_ID + ", bm." + TABLE_BOOKMARKS_ID_LINE + ", bm." + TABLE_BOOKMARKS_ID_STOP
                    + ", line." + TABLE_LINE_NAME + ", line." + TABLE_LINE_TYPE + ", line." + TABLE_LINE_DIRECTION + ", stop." + TABLE_STOP_NAME
                    + " FROM " + TABLE_BOOKMARKS + " bm "
                    + " JOIN " + TABLE_LINE + " line ON " + "bm." + TABLE_BOOKMARKS_ID_LINE + " = line." + TABLE_LINE_ID
                    + " JOIN " + TABLE_STOP + " stop ON " + "bm." + TABLE_BOOKMARKS_ID_STOP + " = stop." + TABLE_STOP_ID
                , null);

        ArrayList<Bookmarks> listBookmarks = new ArrayList<Bookmarks>();
        while(cursor.moveToNext()){
            final long bookmarksId = cursor.getLong(0);
            final long bookmarksLineId = cursor.getLong(1);
            final long bookmarksStopId = cursor.getLong(2);

            final String lineName = cursor.getString(3);
            final String lineType = cursor.getString(4);
            final String lineDirection = cursor.getString(5);
            final String stopName = cursor.getString(6);

            final Line line = new Line() {{
                setId(bookmarksLineId);
                setName(lineName);
                setType(lineType);
                setDirection(lineDirection);
            }};

            final Stop stop = new Stop() {{
                setId(bookmarksStopId);
                setName(stopName);
            }};

            Bookmarks bookmarks = new Bookmarks() {{
                setId(bookmarksId);
                setLineId(bookmarksLineId);
                setStopId(bookmarksStopId);
                setLine(line);
                setStop(stop);
            }};

            listBookmarks.add(bookmarks);
        }
        return listBookmarks;
    }
}
