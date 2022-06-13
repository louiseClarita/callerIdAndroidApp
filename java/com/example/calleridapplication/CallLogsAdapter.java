package com.example.calleridapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.CallerIdApplication.R;

import java.util.List;

import okhttp3.Call;

public class CallLogsAdapter extends ArrayAdapter<CallLogs> {
    private Context mContext;
    private int mResource;
    List<CallLogs> callLogs;
    public static Boolean openedfromfrag = false;
    static Boolean openCreate = false;
    DataBaseHelper2 dt2=null;
    DataBaseHelper dt1=null;
    // Used for the ViewHolder pattern
    // https://developer.android.com/training/improving-layouts/smooth-scrolling
    static class ViewHolder {
        TextView duration;
        TextView time;
        TextView contactname;
        Button isSaved;
        ImageView direction;
    }

    public CallLogsAdapter(Context context, int resource, List<CallLogs> calllogs) {
        super(context, resource, calllogs);
        mContext = context;
        mResource = resource;
        this.callLogs = calllogs;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CallLogs calllog = getItem(position);

        CallLogsAdapter.ViewHolder holder;
        dt2 = new DataBaseHelper2(parent.getContext());
        dt1 = new DataBaseHelper(parent.getContext());
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new CallLogsAdapter.ViewHolder();
            holder.duration = convertView.findViewById(R.id.textView5);
            holder.time = convertView.findViewById(R.id.textView4);
            holder.contactname = convertView.findViewById(R.id.textView3);
            holder.isSaved = convertView.findViewById(R.id.button2);
            holder.direction = convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (CallLogsAdapter.ViewHolder) convertView.getTag();
        }


        if(calllog.getDuration().equals("0")) {
            holder.duration.setText("MISSED CALL");
        }else {
            holder.duration.setText(calllog.getDuration() +" minutes");
        }
        holder.time.setText(calllog.getDate());
        //if(dt1.getContactName(calllog.getCallerid()).equals("null")){
      //      holder.contactname.setText("N/A");
     //   }else{
        holder.contactname.setText(calllog.getPhoneNbre());
        if(!calllog.getCalllogid().equals(null)){

            holder.contactname.setText(dt1.getContactName(calllog.getCallerid()));
            if(dt1.getContactName(calllog.getCallerid()).equals("")){
               if(!(dt1.fetchcontact(calllog.getPhoneNbre()).getContact_id()=="")){
                   dt2.modifyContactid(calllog.getDate(),dt1.fetchcontact(calllog.getPhoneNbre()));
                   holder.contactname.setText(dt1.getContactName(calllog.getCallerid()));
               }else{
               holder.contactname.setText(calllog.getPhoneNbre());
               }
            }

        }
//        if(dt1.getContactName(calllog.getCallerid()).equals("")){
      //    holder.contactname.setText(calllog.getPhoneNbre());
     //  }
        if(calllog.getSaved().equals("true")){

          //  holder.isSaved.setBackgroundColor(0x808080);
           // holder.isSaved.setTextColor(getContext().getResources().getColor(R.color.white));
          //he meshye holder.isSaved.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
         //   holder.isSaved.setTextColor(Color.parseColor("#000000"));
         //   holder.isSaved.setTextColor(Integer.parseInt("0x000000"));
            holder.isSaved.setClickable(false);
            holder.isSaved.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            holder.isSaved.setText("saved!");
            holder.isSaved.setEnabled(false);
          //  holder.isSaved.setTextColor(0x00000);
        }else {
            holder.isSaved.setText("not saved in CRM");
            holder.isSaved.setEnabled(true);
            holder.isSaved.setBackgroundColor(getContext().getResources().getColor(R.color.grey));
         //   holder.isSaved.setBackgroundColor(0x008000);
            if(dt1.getContactName(calllog.getCallerid()).equals("")){
               // holder.contactname.setText(calllog.getPhoneNbre());
                //eza huwe empty yaane msh b crm.. so we should create the contact in crm then save the logg

            holder.isSaved.setEnabled(true);
            holder.isSaved.setClickable(true);
            holder.isSaved.setOnClickListener(new View.OnClickListener() {
                    @Override
            public void onClick(View v) {
                        openCreateApp(getItem(position),mContext);
                    }
                });




        }

    else{
            holder.isSaved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openedfromfrag = true;

                    openSaveLogPage(getItem(position).getCalllogid());
                }
            });

        }
        }
if(calllog.getDirection().equals("true")){
    //true means outgoing

    holder.direction.setImageResource(R.drawable.ic_baseline_call_missed_outgoing_24);
}else {

    //false means incoming

    holder.direction.setImageResource(R.drawable.ic_baseline_call_received_24);

}
        return convertView;
    }

    private void openCreateApp(@NonNull CallLogs c, Context mContext) {
        Intent i = new Intent(mContext,first.class);
        openCreate = true;
        Log.d("idinadapter",c.getCalllogid());
        i.putExtra("id",c.getCalllogid());
        getContext().startActivity(i);
    }

    private void openSaveLogPage(String id) {

        Intent i = new Intent(getContext(),SaveLogsPage.class);
        openedfromfrag=true;
        i.putExtra("id",id);
        getContext().startActivity(i);
    }

    // Convert Graph's DateTimeTimeZone format to
    // a LocalDateTime, then return a formatted string


}


