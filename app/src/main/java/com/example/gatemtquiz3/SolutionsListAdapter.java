package com.example.gatemtquiz3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SolutionsListAdapter extends ArrayAdapter<SolutionListItem> {
    private Context mContext;
    private int mResource;

    public SolutionsListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<SolutionListItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Integer serialNo = getItem(position).getIdNo();
        String solutionImageName = getItem(position).getSolutionImageName();

        SolutionListItem solutionList = new SolutionListItem(serialNo, solutionImageName);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView serialNoTextView = (TextView) convertView.findViewById(R.id.textView_solutionNo);
        ImageView solutionImageView = (ImageView) convertView.findViewById(R.id.imageview_solution);

        serialNoTextView.setText("SOLUTION : " + (serialNo+1));
        solutionImageView.setImageResource(mContext.getResources().getIdentifier(solutionImageName, "drawable", mContext.getPackageName()));

        return convertView;
    }
}
