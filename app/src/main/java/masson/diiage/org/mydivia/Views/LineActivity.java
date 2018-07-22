package masson.diiage.org.mydivia.Views;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import masson.diiage.org.mydivia.Adapter.LineAdapter;
import masson.diiage.org.mydivia.Database.DatabaseHelper;
import masson.diiage.org.mydivia.Entities.Line;
import masson.diiage.org.mydivia.MainActivity;
import masson.diiage.org.mydivia.R;

public class LineActivity extends AppCompatActivity {
    static String extra_message = "session";
    static ListView listViewLine;
    static ArrayList<Line> listLine;
    static String tagLine;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        setTitle("Les lignes disponibles");
        extra_message = "session";
        tagLine = "line";
        listLine = new ArrayList<Line>();
        listViewLine = findViewById(R.id.listViewLine);
        progressBar = findViewById(R.id.lineProgressBar);

        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        this.listLine = helper.getLine(db);
        if (this.listLine.size() == 0) {
            loadApi();
        }
        else {
            displayLine(listLine);
        }

        this.listViewLine.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Line line = (Line)parent.getItemAtPosition(position);

                Intent intent = new Intent(LineActivity.this, StopActivity.class);
                intent.putExtra(extra_message, line.getId());
                startActivity(intent);
            }
        });
    }

    private void loadApi() {
        String baseUrlApi = getResources().getString(R.string.ApiLine);

        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUrlApi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        AsyncTask<URL, Integer, ArrayList<Line>> task = new AsyncTask<URL, Integer, ArrayList<Line>>() {
            @Override
            protected ArrayList<Line> doInBackground(URL... urls) {
                try {
                    InputStream inputStream = urls[0].openStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                    StringBuilder stringBuilder = new StringBuilder();
                    String lineBuffer = null;

                    while ((lineBuffer = bufferedReader.readLine()) != null) {
                        stringBuilder.append(lineBuffer);
                    }

                    String data = stringBuilder.toString();

                    Pattern p = Pattern.compile("<option value=\"([0-9]+)\"|data-class=\"(.+?)\"|data-type=\"(.+?)\"|> > (.+?)<");
                    Matcher m = p.matcher(data);
                    Line line = new Line();
                    while(m.find())
                    {
                        String str;
                        int a = m.groupCount();

                        for (int i=1; i < a + 1; i++) {
                            str = m.group(i);
                            if (str != null) {
                                switch (i) {
                                    case 1:
                                        line.setId(Long.parseLong(m.group(i)));
                                        break;
                                    case 2:
                                        line.setName(m.group(i));
                                        break;
                                    case 3:
                                        line.setType(m.group(i));
                                        break;
                                    case 4:
                                        line.setDirection(m.group(i));
                                        listLine.add(line);
                                        addLine(line);
                                        line = new Line();
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

                return listLine;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                visibleProgressBar();
            }

            @Override
            protected void onPostExecute(ArrayList<Line> listLine) {
                super.onPostExecute(listLine);
                displayLine(listLine);
            }
        }.execute(baseUrl);
    }

    private void displayLine(ArrayList<Line> listLine) {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        invisibleProgressBar();
        this.listLine = helper.getLine(db);
        LineAdapter adapter = new LineAdapter(this.listLine, this);
        listViewLine.setAdapter(adapter);
    }

    private void visibleProgressBar() { progressBar.setVisibility(View.VISIBLE); }

    private void invisibleProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void addLine(Line line) {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.addLine(db, line);
    }
}
