package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.UUID;

public class NoteEditFragment extends Fragment {

    private static final String ARG_NOTE_ID = "note_id";

    private UUID mNoteId;
    private Note mNote;
    private EditText mEditText;

    public static NoteEditFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE_ID, noteId);

        NoteEditFragment fragment = new NoteEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNoteId = (UUID) getArguments().getSerializable(ARG_NOTE_ID);
        mNote = new NoteList(getActivity()).getNote(mNoteId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        new NoteList(getActivity()).updateNote(mNote);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_edit, container, false);
        mEditText = view.findViewById(R.id.note_edit_text);
        if (mNote.getNote() != null) {
            mEditText.setText(mNote.getNote());
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.delete_note:
                new NoteList(getActivity()).removeNote(mNote);
                getActivity().onBackPressed();
                return true;
            case R.id.email_note:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mEditText.getText().toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, "Course Note");
                try {
                    startActivity(Intent.createChooser(intent, "Send Email Using: "));
                } catch (android.content.ActivityNotFoundException ex) {}
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
