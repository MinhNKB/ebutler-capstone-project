package com.guardian.ebutler.ebutler.custom;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.ebutler.dataclasses.TaskType;

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
        public ImageButton pubImageButton;
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
        lHolder.pubImageButton = (ImageButton) lRowView.findViewById(R.id.list_item_multiline_imageButton);

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
        if (this.priFilteredItems.get(iPosition).pubImageButton != -1)
            lHolder.pubImageButton.setImageResource(this.priFilteredItems.get(iPosition).pubImageButton);
        else
            lHolder.pubImageButton.setVisibility(View.GONE);



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
    public Filter getFilter() {
        return null;
    }

    public Filter getFilter(boolean[] iFilters) {
        return new CustomFilter(iFilters);
    }
    class CustomFilter extends Filter{
        private boolean[] priFilters;

        public CustomFilter(boolean[] iFilters) {
            this.priFilters = iFilters;
        }
        @Override
        protected FilterResults performFiltering(CharSequence iConstraint) {
            iConstraint = normalizeVietnameseString(normalizeVietnameseString(iConstraint.toString()).toLowerCase());
            FilterResults lResult = new FilterResults();

            if (iConstraint != null && iConstraint.toString().length() > 0) {
                List<CustomListItem> lFounded = new ArrayList<CustomListItem>();
                for(CustomListItem item: priOriginalItems){
                    if((item.pubFirstLine != null && normalizeVietnameseString(item.pubFirstLine).toLowerCase().contains(iConstraint))
                            || (item.pubSecondLine != null && normalizeVietnameseString(item.pubSecondLine).toLowerCase().contains(iConstraint))
                            || (item.pubThirdLine != null && normalizeVietnameseString(item.pubThirdLine).toLowerCase().contains(iConstraint))){
                        lFounded.add(item);
                    }
                }
                lResult.values = lFounded;
                lResult.count = lFounded.size();
            }else {
                lResult.values = priOriginalItems;
                lResult.count = priOriginalItems.size();
            }

            if (this.priFilters != null){
                List<CustomListItem> lFounded = new ArrayList<CustomListItem>();
                for (CustomListItem item: (List<CustomListItem>) lResult.values
                     ) {
                    if (this.priFilters[0] == true && item.pubTaskType != null && item.pubTaskType == TaskType.OneTimeReminder){
                        lFounded.add(item);
                        continue;
                    }
                    if (this.priFilters[1] == true && item.pubTaskType == TaskType.CheckList){
                        lFounded.add(item);
                        continue;
                    }
                    if (this.priFilters[2] == true && item.pubTaskType == TaskType.Note){
                        lFounded.add(item);
                        continue;
                    }
                }
                lResult.values = lFounded;
                lResult.count = lFounded.size();
            }

            return lResult;
        }
        @Override
        protected void publishResults(CharSequence iConstraint, FilterResults iResults) {
            priFilteredItems = (List<CustomListItem>) iResults.values;
            notifyDataSetChanged();
        }
    }

    static public String normalizeVietnameseString(String iString){

        String lResult = iString;
        lResult = lResult.replace('ả', 'a');
        lResult = lResult.replace('à', 'a');
        lResult = lResult.replace('á', 'a');
        lResult = lResult.replace('ã', 'a');
        lResult = lResult.replace('ạ', 'a');

        lResult = lResult.replace('ẳ', 'a');
        lResult = lResult.replace('ằ', 'a');
        lResult = lResult.replace('ắ', 'a');
        lResult = lResult.replace('ẵ', 'a');
        lResult = lResult.replace('ặ', 'a');

        lResult = lResult.replace('ẩ', 'a');
        lResult = lResult.replace('ầ', 'a');
        lResult = lResult.replace('ấ', 'a');
        lResult = lResult.replace('ẫ', 'a');
        lResult = lResult.replace('ậ', 'a');

        lResult = lResult.replace('ẻ', 'e');
        lResult = lResult.replace('è', 'e');
        lResult = lResult.replace('é', 'e');
        lResult = lResult.replace('ẽ', 'e');
        lResult = lResult.replace('ẹ', 'e');

        lResult = lResult.replace('ể', 'e');
        lResult = lResult.replace('ề', 'e');
        lResult = lResult.replace('ế', 'e');
        lResult = lResult.replace('ễ', 'e');
        lResult = lResult.replace('ệ', 'e');

        lResult = lResult.replace('ỉ', 'i');
        lResult = lResult.replace('ì', 'i');
        lResult = lResult.replace('í', 'i');
        lResult = lResult.replace('ĩ', 'i');
        lResult = lResult.replace('ị', 'i');

        lResult = lResult.replace('ỏ', 'o');
        lResult = lResult.replace('ò', 'o');
        lResult = lResult.replace('ó', 'o');
        lResult = lResult.replace('õ', 'o');
        lResult = lResult.replace('ọ', 'o');

        lResult = lResult.replace('ở', 'o');
        lResult = lResult.replace('ờ', 'o');
        lResult = lResult.replace('ớ', 'o');
        lResult = lResult.replace('ỡ', 'o');
        lResult = lResult.replace('ợ', 'o');

        lResult = lResult.replace('ổ', 'o');
        lResult = lResult.replace('ồ', 'o');
        lResult = lResult.replace('ồ', 'o');
        lResult = lResult.replace('ỗ', 'o');
        lResult = lResult.replace('ộ', 'o');

        lResult = lResult.replace('ủ', 'u');
        lResult = lResult.replace('ù', 'u');
        lResult = lResult.replace('ú', 'u');
        lResult = lResult.replace('ũ', 'u');
        lResult = lResult.replace('ụ', 'u');

        lResult = lResult.replace('ử', 'u');
        lResult = lResult.replace('ừ', 'u');
        lResult = lResult.replace('ứ', 'u');
        lResult = lResult.replace('ữ', 'u');
        lResult = lResult.replace('ự', 'u');

        lResult = lResult.replace('Ả', 'a');
        lResult = lResult.replace('À', 'a');
        lResult = lResult.replace('Á', 'a');
        lResult = lResult.replace('Ã', 'a');
        lResult = lResult.replace('Ạ', 'a');

        lResult = lResult.replace('Ẳ', 'a');
        lResult = lResult.replace('Ằ', 'a');
        lResult = lResult.replace('Ắ', 'a');
        lResult = lResult.replace('Ẵ', 'a');
        lResult = lResult.replace('Ặ', 'a');

        lResult = lResult.replace('Ẩ', 'a');
        lResult = lResult.replace('Ầ', 'a');
        lResult = lResult.replace('Ấ', 'a');
        lResult = lResult.replace('Ẫ', 'a');
        lResult = lResult.replace('Ậ', 'a');

        lResult = lResult.replace('Ẻ', 'e');
        lResult = lResult.replace('È', 'e');
        lResult = lResult.replace('É', 'e');
        lResult = lResult.replace('Ẽ', 'e');
        lResult = lResult.replace('Ẹ', 'e');

        lResult = lResult.replace('Ể', 'e');
        lResult = lResult.replace('Ề', 'e');
        lResult = lResult.replace('Ế', 'e');
        lResult = lResult.replace('Ễ', 'e');
        lResult = lResult.replace('Ệ', 'e');

        lResult = lResult.replace('Ỉ', 'i');
        lResult = lResult.replace('Ì', 'i');
        lResult = lResult.replace('Í', 'i');
        lResult = lResult.replace('Ĩ', 'i');
        lResult = lResult.replace('Ị', 'i');

        lResult = lResult.replace('Ỏ', 'o');
        lResult = lResult.replace('Ò', 'o');
        lResult = lResult.replace('Ó', 'o');
        lResult = lResult.replace('Õ', 'o');
        lResult = lResult.replace('Ọ', 'o');

        lResult = lResult.replace('Ở', 'o');
        lResult = lResult.replace('Ờ', 'o');
        lResult = lResult.replace('Ớ', 'o');
        lResult = lResult.replace('Ỡ', 'o');
        lResult = lResult.replace('Ợ', 'o');

        lResult = lResult.replace('Ổ', 'o');
        lResult = lResult.replace('Ồ', 'o');
        lResult = lResult.replace('Ố', 'o');
        lResult = lResult.replace('Ỗ', 'o');
        lResult = lResult.replace('Ộ', 'o');

        lResult = lResult.replace('Ủ', 'u');
        lResult = lResult.replace('Ù', 'u');
        lResult = lResult.replace('Ú', 'u');
        lResult = lResult.replace('Ũ', 'u');
        lResult = lResult.replace('Ụ', 'u');

        lResult = lResult.replace('Ử', 'u');
        lResult = lResult.replace('Ừ', 'u');
        lResult = lResult.replace('Ứ', 'u');
        lResult = lResult.replace('Ữ', 'u');
        lResult = lResult.replace('Ự', 'u');

        lResult = lResult.replace('đ', 'd');
        lResult = lResult.replace('Đ', 'd');

        return  lResult;
    }




}
