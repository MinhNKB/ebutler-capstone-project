package com.guardian.ebutler.ebutler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Duy on 1/27/2016.
 */
public class DashboardAdapter extends BaseAdapter {
    private List<DashboardTask> tasks = null;
    private Context context;
    private static LayoutInflater inflater = null;
    public DashboardAdapter(Dashboard dashboard, List<DashboardTask> tasks) {
        this.tasks = tasks;
        context = dashboard;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.tasks.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView firstLine;
        TextView secondLine;
        TextView thirdLine;
        View flag;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.dashboard_item, null);

        holder.firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        holder.secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        holder.thirdLine = (TextView) rowView.findViewById(R.id.thirdLine);
        holder.flag = (View) rowView.findViewById(R.id.flag);

        holder.firstLine.setText(this.tasks.get(position).FirstLine);
        holder.secondLine.setText(this.tasks.get(position).SecondLine);
        holder.thirdLine.setText(this.tasks.get(position).ThirdLine);
        holder.flag.setBackgroundColor(this.tasks.get(position).Flag);


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + position, Toast.LENGTH_LONG).show();
            }
        });


        return rowView;
    }

}
