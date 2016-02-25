package com.guardian.ebutler.ebutler;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 1/27/2016.
 */
public class CustomListAdapter extends BaseAdapter implements Filterable{
    private List<CustomListItem> priFilteredItems = null;
    private List<CustomListItem> priOriginalItems = null;
    private Context priContext;
    private static LayoutInflater inflater = null;
    public CustomListAdapter(Activity iContext, List<CustomListItem> iItem) {
        this.priFilteredItems = new ArrayList<>(iItem);
        this.priOriginalItems = new ArrayList<>(iItem);
        this.priContext = iContext;
        inflater = (LayoutInflater) this.priContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return this.priFilteredItems.size();
    }

    @Override
    public Object getItem(int iPosition) {
        // TODO Auto-generated method stub
        return iPosition;
    }

    @Override
    public long getItemId(int iPosition) {
        // TODO Auto-generated method stub
        return iPosition;
    }

    public class Holder
    {
        public TextView pubFirstLine;
        public TextView pubSecondLine;
        public TextView pubThirdLine;
        public View flag;
    }
    @Override
    public View getView(final int iPosition, View iConvertView, ViewGroup iParent) {
        // TODO Auto-generated method stub
        Holder lHolder = new Holder();
        View lRowView;
        lRowView = inflater.inflate(R.layout.list_item_multiline, null);

        lHolder.pubFirstLine = (TextView) lRowView.findViewById(R.id.list_item_multiline_firstLine);
        lHolder.pubSecondLine = (TextView) lRowView.findViewById(R.id.list_item_multiline_secondLine);
        lHolder.pubThirdLine = (TextView) lRowView.findViewById(R.id.list_item_multiline_thirdLine);
        lHolder.flag = (View) lRowView.findViewById(R.id.flag);

        if (this.priFilteredItems.get(iPosition).pubFirstLine != null)
            lHolder.pubFirstLine.setText(this.priFilteredItems.get(iPosition).pubFirstLine);
        if (this.priFilteredItems.get(iPosition).pubSecondLine != null)
            lHolder.pubSecondLine.setText(this.priFilteredItems.get(iPosition).pubSecondLine);
        else
            lHolder.pubSecondLine.setVisibility(View.GONE);
        if (this.priFilteredItems.get(iPosition).pubThirdLine != null)
            lHolder.pubThirdLine.setText(this.priFilteredItems.get(iPosition).pubThirdLine);
        else
            lHolder.pubThirdLine.setVisibility(View.GONE);
        if (this.priFilteredItems.get(iPosition).pubFlag != R.color.transparent)
            lHolder.flag.setBackgroundColor(this.priFilteredItems.get(iPosition).pubFlag);
        else lHolder.flag.setVisibility(View.GONE);

//        lRowView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(priContext, "You Clicked " + iPosition, Toast.LENGTH_LONG).show();
//            }
//        });

        return lRowView;
    }

    @Override
    public Filter getFilter(){
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence iConstraint) {
                iConstraint = iConstraint.toString().toLowerCase();
                FilterResults lResult = new FilterResults();

                if (iConstraint != null && iConstraint.toString().length() > 0) {
                    List<CustomListItem> lFounded = new ArrayList<CustomListItem>();
                    for(CustomListItem item: priOriginalItems){
                        if((item.pubFirstLine != null &&item.pubFirstLine.toLowerCase().contains(iConstraint))
                                || (item.pubSecondLine != null && item.pubSecondLine.toLowerCase().contains(iConstraint))
                                || (item.pubThirdLine != null && item.pubThirdLine.toLowerCase().contains(iConstraint))){
                            lFounded.add(item);
                        }
                    }
                    lResult.values = lFounded;
                    lResult.count = lFounded.size();
                }else {
                    lResult.values = priOriginalItems;
                    lResult.count = priOriginalItems.size();
                }
                return lResult;
            }
            @Override
            protected void publishResults(CharSequence iConstraint, FilterResults iResults) {
                priFilteredItems = (List<CustomListItem>) iResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
