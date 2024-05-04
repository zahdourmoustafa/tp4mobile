package com.example.tp4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.content.res.Configuration;

public class NoteDetailActivity extends AppCompatActivity {

    private TextView timestampTextView;
    private MediaPlayer mediaPlayer;
    private boolean isPaused = false;
    private NotesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        timestampTextView = findViewById(R.id.text_view_timestamp);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        dbHelper = new NotesDbHelper(this);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");

            TextView titleTextView = findViewById(R.id.text_view_title_detail);
            TextView contentTextView = findViewById(R.id.text_view_content_detail);

            titleTextView.setText(title);
            contentTextView.setText(content);

            displayCurrentTime();
        }

        ImageView musicImageView = findViewById(R.id.image_view_music);
        musicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    if (!mediaPlayer.isPlaying()) {
                        if (isPaused) {
                            mediaPlayer.start();
                            isPaused = false;
                        } else {
                            mediaPlayer.start();
                        }
                    } else {
                        mediaPlayer.pause();
                        isPaused = true;
                    }
                }
            }
        });
    }

    private void displayCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        timestampTextView.setText(sdf.format(new Date(currentTimeMillis)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && isPaused) {
            mediaPlayer.start();
            isPaused = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void saveNoteToDatabase(String title, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesContract.COLUMN_TITLE, title);
        values.put(NotesContract.COLUMN_CONTENT, content);
        long newRowId = db.insert(NotesContract.TABLE_NAME, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save note", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNoteDetail(String note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            NoteDetailFragment noteDetailFragment = NoteDetailFragment.newInstance(note);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.note_detail_container, noteDetailFragment);
            transaction.commit();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("title", "Moustafa");
            intent.putExtra("content", note);
            startActivity(intent);
        }
    }
}
