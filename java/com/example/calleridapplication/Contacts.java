package com.example.calleridapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.CallerIdApplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Contacts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Contacts extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
   ListView contactsListView;
   List contactsList;
   TextView pleasesignIn;
   DataBaseHelper db;
    DataBaseHelper3 db3;
   ImageView next;
   TextView contactlistSize;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Contacts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * // @param param1 Parameter 1.
     * // @param param2 Parameter 2.
     * // @return A new instance of fragment Contacts.
     */
    // TODO: Rename and change types and number of parameters
    public static Contacts newInstance() {
        Contacts fragment = new Contacts();
     //   Bundle args = new Bundle();
     //   args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
     //   fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    if (getArguments() != null) {
      ///      mParam1 = getArguments().getString(ARG_PARAM1);
     //       mParam2 = getArguments().getString(ARG_PARAM2);
      //  }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_contacts, container, false);
        contactsListView = (ListView) v.findViewById(R.id.contactsListView);
        contactlistSize = (TextView)v.findViewById(R.id.contactlistSize);
        pleasesignIn = (TextView)v.findViewById(R.id.PleaseSignIN);
        db = new DataBaseHelper(getActivity());
        db3 = new DataBaseHelper3(getActivity());
        contactsList = db.getEveryone();
        ArrayAdapter adapter = new ContactsAdapter(getActivity(), R.layout.contactadapter_layout,contactsList);
        contactsListView.setAdapter(adapter);
        if(db3.getCount()!=1){
            pleasesignIn.setVisibility(View.VISIBLE);
        }
        if(contactsList.size()==1){
            contactlistSize.setText(contactsList.size() + " contact");
        }else if(contactsList.size()==0){
            contactlistSize.setText("No contacts yet!");
        }else {
            contactlistSize.setText(contactsList.size() + " contacts");
        }
        return v;


        }
}