package com.example.gatemtquiz3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ResultListAdapter extends ArrayAdapter<ResultListItem> {
    private Context mContext;
    private int mResource;


    public ResultListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ResultListItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Integer serialNo = getItem(position).getSerialNo();
        String answeredValue = getItem(position).getAnswerValue();
        Double currentScore = getItem(position).getCurrentScore();

        ResultListItem resultList = new ResultListItem(serialNo, answeredValue, currentScore);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView serialNoTextView = (TextView) convertView.findViewById(R.id.textview_serial);
        TextView answerValueTextView = (TextView) convertView.findViewById(R.id.textview_answers);
        TextView currentScoreTextView = (TextView) convertView.findViewById(R.id.textview_scores);

        serialNoTextView.setText(Integer.toString(serialNo));
        answerValueTextView.setText(answeredValue);
        currentScoreTextView.setText(Double.toString(currentScore));

        if (serialNo%2 == 0) {
            serialNoTextView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            answerValueTextView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            currentScoreTextView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
        }

        return convertView;
    }
}
