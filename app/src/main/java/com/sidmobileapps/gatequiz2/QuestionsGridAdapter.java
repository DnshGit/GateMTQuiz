package com.sidmobileapps.gatequiz2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class QuestionsGridAdapter extends ArrayAdapter<QuestionGridItem> {

    private Context mContext;
    private int mResource;


    public QuestionsGridAdapter(@NonNull Context context, int resource, @NonNull ArrayList<QuestionGridItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int bgColor = getItem(position).getAnswerStatusColor();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.questions_grid_item, null);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_grid_item);
        tv.setText(Integer.toString(position+1));
        tv.setBackground(ContextCompat.getDrawable(getContext(), bgColor));
        return convertView;
    }
}
