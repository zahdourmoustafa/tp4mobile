package com.example.tp4;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener, NoteAdapter.OnNoteLongClickListener {

    private static final String KEY_NOTE_LIST = "note_list";

    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<String> notes = new ArrayList<>();
    private NotesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.note_list);
        adapter = new NoteAdapter(notes, this, this,this ); // Pass 'this' as the listener
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        dbHelper = new NotesDbHelper(this);

        if (savedInstanceState != null) {
            List<String> savedNotes = savedInstanceState.getStringArrayList(KEY_NOTE_LIST);
            if (savedNotes != null) {
                notes.addAll(savedNotes);
                adapter.notifyDataSetChanged();
            }
        }

        FloatingActionButton fab = findViewById(R.id.add_note_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNoteDialog();
            }
        });

        // Load notes from the database
        loadNotesFromDatabase();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(KEY_NOTE_LIST, new ArrayList<>(notes));
        super.onSaveInstanceState(outState);
    }

    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        final EditText editTextNote = dialogView.findViewById(R.id.edit_text_note);

        builder.setView(dialogView)
                .setTitle("Add Note")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String note = editTextNote.getText().toString();
                        addNoteToList(note);
                        saveNoteToDatabase(note);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addNoteToList(String note) {
        notes.add(note);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(String note) {
        // Handle the click event here
        // For example, you can open a detail activity or fragment to display the clicked note
        // You can also perform any other action based on the clicked note
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape mode, display note detail fragment next to note list
            NoteDetailFragment noteDetailFragment = NoteDetailFragment.newInstance(note);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.note_detail_container, noteDetailFragment)
                    .commit();
        } else {
            // In portrait mode, start NoteDetailActivity to display note detail
            Intent intent = new Intent(this, NoteDetailActivity.class);
            intent.putExtra("title", "Moustafa"); // Hardcoded title
            intent.putExtra("content", note);
            startActivity(intent);
        }
    }

    private void loadNotesFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                NotesContract.COLUMN_CONTENT
        };
        Cursor cursor = db.query(
                NotesContract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String note = cursor.getString(cursor.getColumnIndexOrThrow(NotesContract.COLUMN_CONTENT));
            notes.add(note);
        }
        cursor.close();
    }

    private void saveNoteToDatabase(String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NotesContract.COLUMN_CONTENT, note);
        long newRowId = db.insert(NotesContract.TABLE_NAME, null, values);
        if (newRowId != -1) {
            // Insertion was successful
        } else {
            // Insertion failed
        }
    }
    @Override
    public void onNoteLongClick(final String note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteFromDatabase(note);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNoteFromDatabase(String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = NotesContract.COLUMN_CONTENT + " LIKE ?";
        String[] selectionArgs = { note };
        int deletedRows = db.delete(NotesContract.TABLE_NAME, selection, selectionArgs);
        if (deletedRows > 0) {
            // Deletion was successful
            notes.remove(note);
            adapter.notifyDataSetChanged();
        } else {
            // Deletion failed
        }
    }
}
