package edu.wgu.csmi508.progressscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.wgu.csmi508.progressscheduler.database.DbBaseHelper;
import edu.wgu.csmi508.progressscheduler.database.DbSchema;
import edu.wgu.csmi508.progressscheduler.database.DbSchema.CourseMentorTable;

public class MentorSelectorFragment extends Fragment {

    private static final String ARG_COURSE_ID = "course_id";

    private Course mCourse;
    private ListView mListView;
    private List<Mentor> mFullMentorList = new ArrayList<>();
    private List<Mentor> mCourseMentorList = new ArrayList<Mentor>();

    public static MentorSelectorFragment newInstance(UUID courseId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_COURSE_ID, courseId);

        MentorSelectorFragment fragment = new MentorSelectorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID courseId = (UUID) getArguments().getSerializable(ARG_COURSE_ID);
        mCourse = CourseList.getCourseList(getActivity()).getCourse(courseId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = view.findViewById(R.id.list_view);

        mFullMentorList = MentorList.getMentorList(getActivity()).getMentorList();
        mCourseMentorList = mCourse.getMentorList();
        ArrayAdapter<Mentor> adapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_checked_text_view, mFullMentorList);
        mListView.setAdapter(adapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        checkThisCoursesMentors(mCourseMentorList, mFullMentorList);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView selectedItem = (CheckedTextView) view;
                boolean isChecked = selectedItem.isChecked();
                Mentor toggledMentor = mFullMentorList.get(position);
                if (isChecked) {
                    SQLiteDatabase db = new DbBaseHelper(getActivity()).getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(CourseMentorTable.Cols.COURSE, mCourse.getCourseId().toString());
                    values.put(CourseMentorTable.Cols.MENTOR, toggledMentor.getMentorId().toString());
                    db.insert(CourseMentorTable.NAME, null, values);
                    db.close();
                } else {
                    SQLiteDatabase db = new DbBaseHelper(getActivity()).getWritableDatabase();
                    String mentorId = toggledMentor.getMentorId().toString();
                    String courseId = mCourse.getCourseId().toString();
                    db.delete(CourseMentorTable.NAME, CourseMentorTable.Cols.COURSE + "=? and " + CourseMentorTable.Cols.MENTOR + "=?", new String[]{courseId, mentorId});
                    db.close();
                }
            }
        });


        return view;
    }

    private void checkThisCoursesMentors(List<Mentor> thisCoursesMentors, List<Mentor> fullMentorList) {
        for (Mentor mentor : thisCoursesMentors) {
            UUID mentorId = mentor.getMentorId();
            for (int i = 0; i < fullMentorList.size(); i++) {
                if (fullMentorList.get(i).getMentorId().equals(mentorId)) {
                    mListView.setItemChecked(i, true);
                }
            }
        }
    }

}
