package com.nrda.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nrda.R;
import com.nrda.database.DatabaseRoutemodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ShelterAdapter extends BaseAdapter {

    private Activity mActivity;
    private Context mContext ;
    private ArrayList<String> data;
    private ArrayList<String> idData;
    private static LayoutInflater inflater=null;

    //private int m_nlayoutID = 0;

    public ShelterAdapter(Context a, ArrayList<String> d, ArrayList<String> ids) {
        mContext = a;
       // mActivity = (Activity)a;
        data=d;
        idData=ids;
        inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //m_nlayoutID = listRow;
    }
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.spinner_item, null);

        TextView sr_source       = (TextView)vi.findViewById(R.id.sr_source);


        sr_source.setText(data.get(position));
        sr_source.setTag(idData.get(position));
        return vi;
    }
}