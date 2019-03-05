package edu.wgu.csmi508.progressscheduler;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TermListActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_TERM = 0;

    private ListView mListView;
    private List<Term> mTermList = new ArrayList<Term>();
    private ArrayAdapter mTermAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);

        ActionBar actionBar = getSupportActionBar();


        TermList mTerms = TermList.getTermList(this);
        mTermList.addAll(mTerms.getList());

        mTermAdapter = new ArrayAdapter<Term>(this, android.R.layout.simple_list_item_1, mTermList);
        mListView = new ListView(this);
        mListView.setAdapter(mTermAdapter);
        linearLayout.addView(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = TermPagerActivity.newIntent(TermListActivity.this, mTermList.get(position).getTermId());
                startActivityForResult(intent, REQUEST_EDIT_TERM);
            }
        });

        setContentView(linearLayout);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_EDIT_TERM) {
            List<Term> termList = TermList.getTermList(this).getList();
            mTermList.clear();
            mTermList.addAll(termList);
            mTermAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_term_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.new_term:
                Term term = new Term();
                TermList.getTermList(this).addTerm(term);
                Intent intent = TermEditActivity.newIntent(this, term.getTermId());
                startActivityForResult(intent, REQUEST_EDIT_TERM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
