package com.example.calleridapplication;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.CallerIdApplication.R;

import java.util.List;

public class MailsAdapter extends ArrayAdapter<EmailModel> {
    private Context mContext;
    private int mResource;

    // Used for the ViewHolder pattern
    // https://developer.android.com/training/improving-layouts/smooth-scrolling
    static class ViewHolder {
        TextView subject;
        TextView time;
    }

    public MailsAdapter(Context context, int resource, List<EmailModel> messages) {
        super(context, resource, messages);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmailModel message = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.subject = convertView.findViewById(R.id.subject);
            holder.time = convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.subject.setText(message.getSubject());
        holder.time.setText(message.getTime());

        return convertView;
    }

    // Convert Graph's DateTimeTimeZone format to
    // a LocalDateTime, then return a formatted string

}