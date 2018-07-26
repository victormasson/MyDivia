package masson.diiage.org.mydivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import masson.diiage.org.mydivia.Views.BookmarksActivity;
import masson.diiage.org.mydivia.Views.LineActivity;

public class MainActivity extends AppCompatActivity {
    static Button buttonLine;
    static Button buttonBookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLine = findViewById(R.id.buttonLine);
        buttonBookmarks = findViewById(R.id.buttonBookmarks);

        buttonLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LineActivity.class);
                startActivity(intent);
            }
        });

        buttonBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookmarksActivity.class);
                startActivity(intent);
            }
        });
    }
}
