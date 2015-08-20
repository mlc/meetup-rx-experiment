package com.meetup.rxexperiments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.meetup.rxexperiments.model.Group;

public class GroupsAdapter extends ArrayAdapter<Group> {
    final LayoutInflater inflater;

    public GroupsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = (convertView == null ? inflater.inflate(android.R.layout.simple_list_item_2, parent, false) : convertView);
        Group item = getItem(position);
        ((TextView)v.findViewById(android.R.id.text1)).setText(item.name);
        ((TextView)v.findViewById(android.R.id.text2)).setText(getContext().getResources().getQuantityString(R.plurals.group_details_membercount, item.members, item.members, item.who));
        return v;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }
}
