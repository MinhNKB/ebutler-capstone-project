package com.guardian.ebutler.ebutler.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.guardian.ebutler.ebutler.R;
import com.guardian.ebutler.world.Global;

/**
 * Created by Duy on 3/17/2016.
 */
public class CustomScrollView extends ScrollView {

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int lMaxHeight = Global.getInstance().dpToPx(getContext(),(int)(getResources().getDimension(R.dimen.answer_maxHeight)));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(lMaxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
