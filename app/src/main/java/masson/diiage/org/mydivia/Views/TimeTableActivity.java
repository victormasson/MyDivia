package masson.diiage.org.mydivia.Views;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import masson.diiage.org.mydivia.Adapter.StopAdapter;
import masson.diiage.org.mydivia.Database.DatabaseHelper;
import masson.diiage.org.mydivia.Entities.Bookmarks;
import masson.diiage.org.mydivia.Entities.Line;
import masson.diiage.org.mydivia.Entities.Stop;
import masson.diiage.org.mydivia.Entities.TimeTable;
import masson.diiage.org.mydivia.R;

public class TimeTableActivity extends AppCompatActivity {
    static String extra_message = "session";
    static long lineId;
    static long stopId;
    static Context context;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();

    static TextView timeTableArret;
    static TextView timeTableDirection;
    static TextView timeTableTime;
    static TextView timeTableNext;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        setTitle("Horaires");

        timeTableArret = findViewById(R.id.timeTableArret);
        timeTableDirection = findViewById(R.id.timeTableDirection);
        timeTableTime = findViewById(R.id.timeTableTime);
        timeTableNext = findViewById(R.id.timeTableNext);
        progressBar = findViewById(R.id.timeTableProgressBar);

        Intent intent = getIntent();
        lineId = intent.getLongExtra(StopActivity.extra_lineid, 0);
        stopId = intent.getLongExtra(StopActivity.extra_stopid, 0);

        if (lineId == 0 && stopId == 0) {
            lineId = intent.getLongExtra(BookmarksActivity.extra_message_idline, 0);
            stopId = intent.getLongExtra(BookmarksActivity.extra_message_idstop, 0);
        }
        context = this;
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Line line = helper.getLine(db, lineId);
        setTitle("Horaires - " + line.getType() + " - " + line.getName());

        final Button timeTableBookmarks = findViewById(R.id.timeTableBookmarks);
        Bookmarks bookmarks = helper.getBookmarks(db, stopId, lineId);
        if (bookmarks.getLineId() != 0 && bookmarks.getStopId() != 0) {
            Drawable img = context.getResources().getDrawable(R.drawable.ic_star_black_24dp);
            timeTableBookmarks.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }

        timeTableBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper helper = new DatabaseHelper(context);
                SQLiteDatabase db = helper.getWritableDatabase();

                Bookmarks bookmarks = helper.getBookmarks(db, stopId, lineId);
                if (bookmarks.getLineId() == 0 && bookmarks.getStopId() == 0) {
                    Bookmarks b = new Bookmarks(0, lineId, stopId);
                    helper.addBookmarks(db, b);

                    Drawable img = context.getResources().getDrawable(R.drawable.ic_star_black_24dp);
                    timeTableBookmarks.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }
                else {
                    helper.deleteBookmarks(db, stopId, lineId);

                    Drawable img = context.getResources().getDrawable(R.drawable.ic_star_border_black_24dp);
                    timeTableBookmarks.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 20000);
    }

    public void stoptimertask(View v) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        loadApi(lineId, stopId);
                    }
                });
            }
        };
    }

    private void loadApi(long idLine, long idStop) {
        String baseUrlApi = String.format(getResources().getString(R.string.ApiTimeTable), idLine, idStop, idLine, idLine);

        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUrlApi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        AsyncTask<URL, Integer, TimeTable> task = new AsyncTask<URL, Integer, TimeTable>() {
            @Override
            protected TimeTable doInBackground(URL... urls) {
                TimeTable timeTable = new TimeTable();
                try {
                    InputStream inputStream = urls[0].openStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    StringBuilder stringBuilder = new StringBuilder();
                    String lineBuffer = null;

                    while ((lineBuffer = bufferedReader.readLine()) != null) {
                        stringBuilder.append(lineBuffer);
                    }

                    String data = stringBuilder.toString();

                    Pattern p = Pattern.compile("class=\"title\">([^<]*)</div>|class=\\\"v-align\\\">&gt;([^<]*)</span>|time1\\\" data-minut=\\\"([0-9]+)\\\"|time2\\\" data-minut=\\\"([0-9]+)\\\"|(picto-tram)");
                    Matcher m = p.matcher(data);
                    timeTable = new TimeTable();
                    while(m.find())
                    {
                        String str;
                        int a = m.groupCount();
                        for (int i=1; i < a + 1; i++) {
                            str = m.group(i);
                            if (str != null) {
                                switch (i) {
                                    case 1:
                                        timeTable.setArret(m.group(i));
                                        break;
                                    case 2:
                                        timeTable.setDirection(m.group(i));
                                        break;
                                    case 3:
                                        timeTable.setTime(m.group(i));
                                        break;
                                    case 4:
                                        timeTable.setNext(m.group(i));
                                        break;
                                    case 5:
                                        timeTable.setNext(m.group(i));
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return timeTable;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                visibleProgressBar();
            }

            @Override
            protected void onPostExecute(TimeTable timeTable) {
                super.onPostExecute(timeTable);
                invisibleProgressBar();
                displayLine(timeTable);
            }
        }.execute(baseUrl);
    }

    private void displayLine(TimeTable timeTable) {
//        StopAdapter adapter = new StopAdapter(listStop, this);
//        listViewStop.setAdapter(adapter);
        timeTableArret.setText(timeTable.getArret());
        timeTableDirection.setText(timeTable.getDirection());
        timeTableTime.setText(timeTable.getTime());
        timeTableNext.setText(timeTable.getNext());
    }

    private void visibleProgressBar() { progressBar.setVisibility(View.VISIBLE); }

    private void invisibleProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
