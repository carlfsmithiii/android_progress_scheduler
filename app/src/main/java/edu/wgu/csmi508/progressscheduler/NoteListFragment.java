package edu.wgu.csmi508.progressscheduler;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.NoteTable;
import edu.wgu.csmi508.progressscheduler.database.NoteCursorWrapper;

public class NoteListFragment extends Fragment {

    private static final int REQUEST_EDIT_NOTE = 0;
    private static final int REQUEST_NEW_NOTE = 1;

    private static final String ARG_COURSE_ID = "course_id";


    private UUID mCourseId;
    private List<String> noteList = new ArrayList<>();
    private ListView mListView;
    private List<Note> mNoteList = new ArrayList<Note>();
    private ArrayAdapter mNoteAdapter;

    public static NoteListFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        NoteListFragment fragment = new NoteListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mNoteList = new NoteList(getActivity()).getNoteList(mCourseId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = view.findViewById(R.id.list_view);

        mNoteAdapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_text_view, mNoteList);
        mListView.setAdapter(mNoteAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = mNoteList.get(position);
                Intent intent = NoteEditActivity.newIntent(getActivity(), note.getNoteId());
                startActivityForResult(intent, REQUEST_EDIT_NOTE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EDIT_NOTE || requestCode == REQUEST_NEW_NOTE) {
            List<Note> noteList = new NoteList(getActivity()).getNoteList(mCourseId);
            mNoteList.clear();
            mNoteList.addAll(noteList);
            mNoteAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_note:
                Note note = new Note(mCourseId);
                new NoteList(getActivity()).addNote(note);
                Intent intent = NoteEditActivity.newIntent(getActivity(), note.getNoteId());
                startActivityForResult(intent, REQUEST_NEW_NOTE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
