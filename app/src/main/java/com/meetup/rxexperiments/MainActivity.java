package com.meetup.rxexperiments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.meetup.rxexperiments.model.Group;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    @InjectView(android.R.id.list) ListView list;
    @InjectView(R.id.spinner) ProgressBar spinner;
    @InjectView(android.R.id.empty) TextView empty;
    GroupsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        adapter = new GroupsAdapter(this);
        list.setAdapter(adapter);
        show(spinner);
        Http.getInstance(this).get(API.recommendedGroups(this), Group.class)
                .subscribeOn(Schedulers.io())
                .buffer(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(grs -> {
                            adapter.addAll(grs);
                            show(list);
                        },
                        ex -> {
                            empty.setText(ex.toString());
                            show(empty);
                        },
                        () -> {
                            if (adapter.getCount() == 0) {
                                show(empty);
                            }
                        });
    }

    private void show(View target) {
        for (View v : new View[]{list, spinner, empty}) {
            v.setVisibility(target == v ? View.VISIBLE : View.GONE);
        }
    }
}
