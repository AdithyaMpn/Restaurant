package com.adithya.restaurant;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;;
import android.widget.TextView;


import java.util.List;

public class MessageAdapter extends ArrayAdapter<names> {
    public MessageAdapter(Context context, int resource, List<names> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.useritem, parent, false);
        }


        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        names message = getItem(position);


        authorTextView.setText(message.getName());

        return convertView;
    }
}
