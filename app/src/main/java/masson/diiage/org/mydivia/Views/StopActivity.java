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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import masson.diiage.org.mydivia.Adapter.LineAdapter;
import masson.diiage.org.mydivia.Adapter.StopAdapter;
import masson.diiage.org.mydivia.Database.DatabaseHelper;
import masson.diiage.org.mydivia.Entities.Line;
import masson.diiage.org.mydivia.Entities.Stop;
import masson.diiage.org.mydivia.MainActivity;
import masson.diiage.org.mydivia.R;

public class StopActivity extends AppCompatActivity {
    static String extra_stopid = "stopId";
    static String extra_lineid = "lineId";
    static ListView listViewStop;
    static ArrayList<Stop> listStop;
    public static ProgressBar progressBar;
    static String tagStop;
    private static Context context;
    static long lineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        tagStop = "stop";
        listStop = new ArrayList<Stop>();
        listViewStop = findViewById(R.id.listViewStop);
        progressBar = findViewById(R.id.stopProgressBar);
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        Intent intent = getIntent();
        lineId = intent.getLongExtra(LineActivity.extra_message, 0);

        Line line = helper.getLine(db, lineId);
        setTitle("Arrêt - " + line.getName() + " - " + line.getDirection());

        if (lineId != 0) {
            this.listStop = helper.getStop(db, lineId);
            if (this.listStop.size() == 0) {
                loadApi(lineId);
            }
            else {
                displayLine(listStop);
            }
        }
        else {
            loadApi(82);
        }

        this.listViewStop.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stop stop = (Stop)parent.getItemAtPosition(position);

                Intent intent = new Intent(StopActivity.this, TimeTableActivity.class);
                intent.putExtra(extra_stopid, stop.getId());
                intent.putExtra(extra_lineid, stop.getLineId());
                startActivity(intent);
            }
        });
    }

    private void loadApi(long lineId) {
        String baseUrlApi = String.format(getResources().getString(R.string.ApiStop), lineId);

        URL baseUrl = null;
        try {
            baseUrl = new URL(baseUrlApi); // Création de l'URL dans les ressources
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        AsyncTask<URL, Integer, ArrayList<Stop>> task = new AsyncTask<URL, Integer, ArrayList<Stop>>() {
            @Override
            protected ArrayList<Stop> doInBackground(URL... urls) {
                try {
                    InputStream inputStream = urls[0].openStream(); // Ouverture de la connexion avec l'URL
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  // Création d'un bufferReader pour faciliter la lecture

                    StringBuilder stringBuilder = new StringBuilder(); // Permet de concaténer plus rapidement les strings.
                    String lineBuffer = null;

                    while ((lineBuffer = bufferedReader.readLine()) != null){ // Tant qu'il y a des choses à lire.
                        stringBuilder.append(lineBuffer); // Ajout des lignes.
                    }

                    String data = stringBuilder.toString(); // le HTML

                    Pattern p = Pattern.compile("class=\"title\">([^<]*)</div>|class=\"code-totem\">([^<]*)</div>"); // Capture du contenu entre <b> et </b> (groupe 1)
                    Matcher m = p.matcher(data);
                    Stop stop = new Stop();
                    while(m.find())
                    {
                        String str;
                        int a = m.groupCount();

                        for (int i=1; i < a + 1; i++) {
                            str = m.group(i);
                            if (str != null) {
                                switch (i) {
                                    case 1:
                                        stop.setName(m.group(i));
                                        break;
                                    case 2:
                                        stop.setId(Long.parseLong(m.group(i)));
                                        addStop(stop);
                                        listStop.add(stop); // Ajoute la stop

                                        stop = new Stop();
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

                return listStop;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                visibleProgressBar();
            }

            @Override
            protected void onPostExecute(ArrayList<Stop> listStop) {
                super.onPostExecute(listStop);
                displayLine(listStop);
            }
        }.execute(baseUrl);
    }

    private void displayLine(ArrayList<Stop> listStop) {
        invisibleProgressBar();
        StopAdapter adapter = new StopAdapter(listStop, this);
        listViewStop.setAdapter(adapter);
    }

    private void visibleProgressBar() { progressBar.setVisibility(View.VISIBLE); }

    private void invisibleProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void addStop(Stop stop) {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        stop.setLineId(lineId);
        helper.addStop(db, stop);
    }
}
