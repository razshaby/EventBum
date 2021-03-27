package com.raz.eventbum.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.raz.eventbum.R;
import com.raz.eventbum.objects.Demo_Data;

import java.util.ArrayList;

public class Adapter_DemoData extends ArrayAdapter<Demo_Data> {



    public Adapter_DemoData(Context context, ArrayList<Demo_Data> data) {
        super(context, 0, data);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Demo_Data demoData = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_demo, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView active = (TextView) convertView.findViewById(R.id.active);
        // Populate the data into the template view using the data object
        name.setText(demoData.getName());
        number.setText(demoData.getNumber()+"");
        active.setText(demoData.isActive()+"");
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), name.getText() ,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        // Return the completed view to render on screen
        return convertView;
    }


}
