package com.example.tp4;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailFragment extends Fragment {

    private static final String ARG_NOTE = "note";

    private String note;

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    public static NoteDetailFragment newInstance(String note) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getString(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);
        TextView noteTextView = view.findViewById(R.id.note_text_view);
        TextView timestampTextView = view.findViewById(R.id.text_view_timestamp);
        noteTextView.setText(note);

        // Set the current timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String currentTimestamp = sdf.format(new Date());
        timestampTextView.setText(currentTimestamp);

        return view;
    }
}
