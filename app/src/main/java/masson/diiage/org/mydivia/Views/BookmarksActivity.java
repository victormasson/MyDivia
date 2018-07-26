package masson.diiage.org.mydivia.Views;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

import masson.diiage.org.mydivia.Adapter.BookmarksAdapter;
import masson.diiage.org.mydivia.Adapter.LineAdapter;
import masson.diiage.org.mydivia.Database.DatabaseHelper;
import masson.diiage.org.mydivia.Entities.Bookmarks;
import masson.diiage.org.mydivia.Entities.Line;
import masson.diiage.org.mydivia.Entities.TimeTable;
import masson.diiage.org.mydivia.R;

public class BookmarksActivity extends AppCompatActivity {
    static String extra_message_idline = "extra_message_idline";
    static String extra_message_idstop = "extra_message_idstop";

    static ListView listViewBookmarks;
    static ArrayList<Bookmarks> listBookmarks;
    static String tagBookmarks;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        setTitle("Mes favoris");
        tagBookmarks = "bookmarks";
        listBookmarks = new ArrayList<Bookmarks>();
        listViewBookmarks = findViewById(R.id.listViewBookmarks);
//        visibleProgressBar();
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        this.listBookmarks = helper.getBookmarks(db);
        displayLine(listBookmarks);

        this.listViewBookmarks.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bookmarks bookmarks = (Bookmarks)parent.getItemAtPosition(position);

                Intent intent = new Intent(BookmarksActivity.this, TimeTableActivity.class);
                intent.putExtra(extra_message_idline, bookmarks.getLineId());
                intent.putExtra(extra_message_idstop, bookmarks.getStopId());
                startActivity(intent);
            }
        });
    }

    private void displayLine(ArrayList<Bookmarks> listBookmarks) {
//        invisibleProgressBar();
        BookmarksAdapter adapter = new BookmarksAdapter(listBookmarks, this);
        listViewBookmarks.setAdapter(adapter);
    }

    private void visibleProgressBar() { progressBar.setVisibility(View.VISIBLE); }

    private void invisibleProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
