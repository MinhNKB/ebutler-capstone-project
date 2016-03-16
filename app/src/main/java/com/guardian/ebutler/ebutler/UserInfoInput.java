package com.guardian.ebutler.ebutler;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserInfoInput extends Activity {

    private ScrollView priScrollViewConversation;
    private LinearLayout priLinearLayoutConversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_input);
        this.findViewsByIds();
    }

    private void findViewsByIds() {
        this.priScrollViewConversation = (ScrollView) findViewById(R.id.user_info_input_scrollViewConversation);
        this.priLinearLayoutConversation = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutConversation);
    }

    public void buttonOk_onClick(View view) {
        RelativeLayout lNewConversationStatement = createConversationStatement(((EditText) findViewById(R.id.wel)).getText().toString(), false);
        this.priLinearLayoutConversation.addView(lNewConversationStatement);
        priScrollViewConversation.post(new Runnable() {
            @Override
            public void run() {
                priScrollViewConversation.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private RelativeLayout createConversationStatement(String iStatement, boolean iIsButler){
        RelativeLayout lResult = new RelativeLayout(this);
        lResult.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        if (iIsButler == true)
            lResult.setGravity(Gravity.START);
        else
            lResult.setGravity(Gravity.END);
        lResult.setBackgroundResource(R.drawable.out_message_bg);
        TextView lStatement = new TextView(this);
        lStatement.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        lStatement.setMaxWidth((int) (R.dimen.conversation_statement_maxWidth * this.getResources().getDisplayMetrics().density + 0.5f));
        lStatement.setPadding(0, 5, 0, 5);
        lStatement.setText(iStatement);
        lResult.addView(lStatement);
        return lResult;
    }
}
