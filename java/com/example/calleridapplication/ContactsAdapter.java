package com.example.calleridapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.CallerIdApplication.R;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactModel> {
    private Context mContext;
    private int mResource;
    List<ContactModel> contacts;
    static ContactModel contact = null;
    static Boolean openContactFrag = false;
    ProgressBar progressBar ;
    // Used for the ViewHolder pattern
    // https://developer.android.com/training/improving-layouts/smooth-scrolling
    static class ViewHolder {
        TextView fullname;
        TextView company;
        TextView job;
        TextView phonenbre;
        ImageView next;
        ImageButton delete;
        TextView email;
    }

    public ContactsAdapter(Context context, int resource, List<ContactModel> contacts) {
        super(context, resource, contacts);
        mContext = context;
        mResource = resource;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        contact = null;
         contact = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.fullname = convertView.findViewById(R.id.fullname);
            holder.company = convertView.findViewById(R.id.company);
            holder.job = convertView.findViewById(R.id.job);
            holder.phonenbre= convertView.findViewById(R.id.phonenbre);
            holder.next = convertView.findViewById(R.id.next);
            holder.delete =convertView.findViewById(R.id.delete);
            holder.email = convertView.findViewById(R.id.email);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.phonenbre.setVisibility(View.VISIBLE);
        holder.job.setVisibility(View.VISIBLE);
        holder.company.setVisibility(View.VISIBLE);
        if(getItem(position).getContact_mobilephone().equals("null") || contact.getContact_mobilephone().isEmpty() || contact.getContact_mobilephone().equals("")){
            holder.phonenbre.setVisibility(View.GONE);
        }
        if(getItem(position).getContact_job().equals("null") || contact.getContact_job().equals("") || contact.getContact_job().isEmpty()){
            holder.job.setVisibility(View.GONE);
        }
        if(getItem(position).getContact_company().equals("null")){
            holder.company.setVisibility(View.GONE);
        }
if(!getItem(position).getContact_email().equals("") &&  !getItem(position).getContact_email().equals("null"))
           holder.email.setText(getItem(position).getContact_email());
       holder.next.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               openCalllogsRelated(mContext,getItem(position));
           }
       });

        holder.fullname.setText(contact.getContact_fname() + " " +contact.getContact_lname());
        holder.company.setText(contact.getContact_company());
        holder.job.setText(contact.getContact_job());
        holder.phonenbre.setText(contact.getContact_mobilephone());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     openPopUp(getItem(position),mContext,v);
            }
        });

        return convertView;
    }

    private void openPopUp(ContactModel item, Context context,View v) {

        AlertDialog diaBox = deleteOption(context,item);
        diaBox.show();

    }
        private AlertDialog deleteOption(Context context,ContactModel c)
        {
            AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)
                    // set message, title, and icon
                    .setTitle("Delete Contact")
                    .setMessage("are you sure you want to delete this contact from Dynamics 365?\n" +
                            " Name:"+c.getContact_fname()+""+c.getContact_fname()+
                            "\n ")
                    .setIcon(R.drawable.ic_baseline_delete_sweep_24)

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int whichButton) {

                            //your deleting code
                           DataBaseHelper dt = new DataBaseHelper(context);
                          if(dt.deleteContact(c) == -1){
                              Toast.makeText(context,"error deleting this contact",Toast.LENGTH_LONG );


                          }else{
                              Toast.makeText(context,"deleted",Toast.LENGTH_LONG );
                              openContactFrag = true;
                              Intent i = new Intent(context,first.class);

                              context.startActivity(i);

                          }

                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    })
                    .create();

            return myQuittingDialogBox;
        }



    public void openCalllogsRelated(Context ctx,ContactModel c){
        Intent i = new Intent(ctx,relatedCallLogs.class);
        Log.d("opening",c.getContact_fname()+""+c.getContact_lname());
       Log.d("idinadapter",c.getContact_id());
        i.putExtra("id",c.getContact_id());
        i.putExtra("token","");
        ctx.startActivity(i);
   }
    // Convert Graph's DateTimeTimeZone format to
    // a LocalDateTime, then return a formatted string




}