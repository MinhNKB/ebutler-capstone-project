package com.guardian.ebutler.ebutler.custom;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.ebutler.ebutler.Dashboard;
import com.guardian.ebutler.ebutler.R;


/**
 * Created by Duy on 4/11/2016.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context priContext;
    private List<String> priFilteredHeaders;
    private List<String> priOriginalHeaders;
    private HashMap<String, List<CustomListItem>> priFilteredItems = null;
    private HashMap<String, List<CustomListItem>> priOriginalItems = null;

    public CustomExpandableListAdapter(Context context, List<String> lHeaders,
                                       HashMap<String, List<CustomListItem>> iItems) {
        this.priContext = context;
        this.priOriginalHeaders = lHeaders;
        this.priOriginalItems = iItems;
        this.priFilteredItems = iItems;
//        for (int i = priOriginalHeaders.size(); i > -1; --i)
//            if (priOriginalItems.get(this.priOriginalHeaders.get(i)).size() == 0)
//                priOriginalHeaders.remove(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.priFilteredItems.get(this.priOriginalHeaders.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public class Holder
    {
        public TextView pubFirstLine;
        public TextView pubSecondLine;
        public TextView pubThirdLine;
        public ImageView pubImageButton;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final CustomListItem lItem = (CustomListItem) getChild(groupPosition, childPosition);
        Holder lHolder = new Holder();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.priContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_item_multiline, null);
        }

        lHolder.pubFirstLine = (TextView) convertView.findViewById(R.id.list_item_multiline_firstLine);
        lHolder.pubSecondLine = (TextView) convertView.findViewById(R.id.list_item_multiline_secondLine);
        lHolder.pubThirdLine = (TextView) convertView.findViewById(R.id.list_item_multiline_thirdLine);
        lHolder.pubImageButton = (ImageView) convertView.findViewById(R.id.list_item_multiline_imageButton);

        if (lItem.pubFirstLine != null)
            lHolder.pubFirstLine.setText(lItem.pubFirstLine);
        else
            lHolder.pubFirstLine.setVisibility(View.GONE);
        if (lItem.pubSecondLine != null)
            lHolder.pubSecondLine.setText(lItem.pubSecondLine);
        else
            lHolder.pubSecondLine.setVisibility(View.GONE);
        if (lItem.pubThirdLine != null)
            lHolder.pubThirdLine.setText(lItem.pubThirdLine);
        else
            lHolder.pubThirdLine.setVisibility(View.GONE);
        if (lItem.pubImageButton != -1)
            lHolder.pubImageButton.setImageResource(lItem.pubImageButton);
        else
            lHolder.pubImageButton.setVisibility(View.GONE);

        if (lItem.pubImageButtonSize != 0) {
            ViewGroup.LayoutParams lParams = lHolder.pubImageButton.getLayoutParams();
            lParams.height = lItem.pubImageButtonSize;
            lParams.width = lItem.pubImageButtonSize;
            lHolder.pubImageButton.setLayoutParams(lParams);
        }

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(priContext, "Item Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.priFilteredItems.get(this.priOriginalHeaders.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.priOriginalHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.priOriginalHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        //By default the group is hidden
        View lView = new FrameLayout(this.priContext);

        String lGroupTitle = getGroup(groupPosition).toString();

        //in case there's more than one group and the group title is not empty then show a TextView within the group bar
        if(getGroupCount() > 1 && this.priOriginalItems.get(this.priOriginalHeaders.get(groupPosition)).size() > 0) {

            LayoutInflater infalInflater = (LayoutInflater) this.priContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            lView = infalInflater.inflate(R.layout.expandable_list_group, null);
            TextView lblListHeader = (TextView) lView
                    .findViewById(R.id.expandable_list_group_textView);
            lblListHeader.setText(lGroupTitle);
        }

        return lView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}