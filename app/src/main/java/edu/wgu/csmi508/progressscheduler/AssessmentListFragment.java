package edu.wgu.csmi508.progressscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class AssessmentListFragment extends Fragment {
    private ListView mListView;
    private List<Assessment> mAssessmentList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAssessmentList = AssessmentList.getAssessmentList(getActivity()).getFullAssessmentList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        mListView = view.findViewById(R.id.list_view);

        ArrayAdapter<Assessment> adapter = new ArrayAdapter<>(getActivity(), R.layout.fragment_text_view, mAssessmentList);
        mListView.setAdapter(adapter);

        return view;
    }
}
