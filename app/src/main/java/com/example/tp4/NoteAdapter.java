package com.example.tp4;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<String> notes;
    private Context context;
    private OnNoteClickListener clickListener;
    private OnNoteLongClickListener longClickListener;

    public NoteAdapter(List<String> notes, Context context, OnNoteClickListener clickListener, OnNoteLongClickListener longClickListener) {
        this.notes = notes;
        this.context = context;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String note = notes.get(position);
        int orientation = holder.itemView.getContext().getResources().getConfiguration().orientation;
        holder.bind(note, orientation);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView titleTextView;
        private TextView contentTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.text_view_title);
            contentTextView = itemView.findViewById(R.id.text_view_content);

            // Set OnClickListener to handle item clicks
            itemView.setOnClickListener(this);
            // Set OnLongClickListener to handle long clicks
            itemView.setOnLongClickListener(this);
        }

        public void bind(String note, int orientation) {
            // Display "Moustafa" as the title
            titleTextView.setText("Moustafa");

            // Determine the maximum number of characters to display based on the orientation
            int maxChars = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 20 : 10;

            // Display the first 'maxChars' characters of the note followed by "..." if it exceeds 'maxChars'
            String displayedText;
            if (note.length() > maxChars) {
                displayedText = note.substring(0, maxChars) + "..."; // Display first 'maxChars' characters with ellipsis
            } else {
                displayedText = note; // If the note is shorter than 'maxChars' characters, display the whole note
            }
            contentTextView.setText(displayedText);
        }

        @Override
        public void onClick(View v) {
            // Get the note at this position
            String selectedNote = notes.get(getAdapterPosition());

            // Notify the click listener of the click event
            clickListener.onNoteClick(selectedNote);
        }

        @Override
        public boolean onLongClick(View v) {
            // Get the note at this position
            String selectedNote = notes.get(getAdapterPosition());

            // Notify the long click listener of the long click event
            longClickListener.onNoteLongClick(selectedNote);

            // Return true to indicate that the long click event is consumed
            return true;
        }
    }

    // Interface for handling click events
    public interface OnNoteClickListener {
        void onNoteClick(String note);
    }

    // Interface for handling long click events
    public interface OnNoteLongClickListener {
        void onNoteLongClick(String note);
    }
}
