package com.example.calleridapplication;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.CallerIdApplication.R;

import org.apache.commons.math3.geometry.euclidean.twod.Line;

import java.util.List;

public class EmailsAdapter2 extends ArrayAdapter<EmailModel> {
    private Context mContext;
    private int mResource;

    // Used for the ViewHolder pattern
    // https://developer.android.com/training/improving-layouts/smooth-scrolling
    static class ViewHolder {
        TextView subject;
        TextView time;
        ImageView read;
        TextView body;
    }

    public EmailsAdapter2(Context context, int resource, List<EmailModel> messages) {
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
            holder.subject = convertView.findViewById(R.id.textView7);
            holder.time = convertView.findViewById(R.id.textView6);
            holder.read = convertView.findViewById(R.id.checkBox);
            holder.body = convertView.findViewById(R.id.textView8);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.subject.setText(" "+message.getSubject());
        holder.time.setText(" "+message.getTime());
        if(!message.isRead()){
       holder.read.setImageResource(R.drawable.ic_baseline_done_one_tick);
        }


        System.out.println("bodies before : "+message.getBodyPreview());
       //String body2 = message.getBodyPreview().replaceAll("\\r","<html><br></html>");
       //String bodies = message.getBodyPreview().replaceAll("(\r\n\r\n|\n|\r)", "");
        String bodies = message.getBodyPreview().replaceAll("\\\\r", "");
       bodies = bodies.replaceAll("\\\\n", "");
       System.out.println("bodies : "+bodies);
       holder.body.setText(bodies);
        return convertView;
    }

    // Convert Graph's DateTimeTimeZone format to
    // a LocalDateTime, then return a formatted string

}

