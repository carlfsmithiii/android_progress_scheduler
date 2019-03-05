package edu.wgu.csmi508.progressscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MentorListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MentorAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.titled_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mentor_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_mentor:
                Mentor mentor = new Mentor();
                MentorList.getMentorList(getActivity()).addMentor(mentor);
                Intent intent = MentorPagerActivity.newIntent(getActivity(), mentor.getMentorId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        MentorList mentorList = MentorList.getMentorList(getActivity());
        List<Mentor> mentors = mentorList.getMentorList();

        mAdapter = new MentorAdapter(mentors);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class MentorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Mentor mMentor;
        private TextView mMentorNameTextView;

        public MentorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_mentor, parent, false));
            itemView.setOnClickListener(this);

            mMentorNameTextView = itemView.findViewById(R.id.mentor_name_list_textview);
        }

        public void bind(Mentor mentor) {
            mMentor = mentor;
            mMentorNameTextView.setText(mMentor.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = MentorPagerActivity.newIntent(getActivity(), mMentor.getMentorId());
            startActivity(intent);
        }
    }

    private class MentorAdapter extends RecyclerView.Adapter<MentorHolder> {
        private List<Mentor> mMentors;

        public MentorAdapter(List<Mentor> mentors) {
            mMentors = mentors;
        }

        @NonNull
        @Override
        public MentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MentorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull MentorHolder holder, int position) {
            Mentor mentor = mMentors.get(position);
            holder.bind(mentor);
        }

        @Override
        public int getItemCount() {
            return mMentors.size();
        }
    }

}
