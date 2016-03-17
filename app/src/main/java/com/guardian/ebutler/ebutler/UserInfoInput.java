package com.guardian.ebutler.ebutler;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guardian.ebutler.fragments.*;

public class UserInfoInput extends Activity {

    private ScrollView priScrollViewConversation;
    private LinearLayout priLinearLayoutConversation;
    private RelativeLayout priRelativeLayoutTaskbar;
    private AnswerFragmentInterface priAnwserFragmentInterface;
    private ImageButton priButtonDecline;
    private ImageButton priButtonOk;
    private ScrollView priScrollViewAnswer;
    private LinearLayout priLinearLayoutAnswer;
    private RelativeLayout priRelativeLayoutForEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_input);
        this.findViewsByIds();
        this.showQuestion();
    }

    private void findViewsByIds() {
        this.priScrollViewConversation = (ScrollView) findViewById(R.id.user_info_input_scrollViewConversation);
        this.priLinearLayoutConversation = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutConversation);
        this.priRelativeLayoutTaskbar = (RelativeLayout) findViewById(R.id.user_info_input_taskbar);
        this.priButtonDecline = (ImageButton) findViewById(R.id.user_info_input_buttonDecline);
        this.priButtonOk = (ImageButton) findViewById(R.id.user_info_input_buttonOk);
        this.priScrollViewAnswer = (ScrollView) findViewById(R.id.user_info_input_scrollViewAnswer);
        this.priLinearLayoutAnswer = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutAnswer);
        this.priRelativeLayoutForEditText = (RelativeLayout) findViewById(R.id.user_info_input_relativeLayoutForEditText);
    }

    public void buttonOk_onClick(View view) {
        showQuestion();
        this.scrollScrollViewConversation(View.FOCUS_DOWN);
    }

    private void scrollScrollViewConversation(final int iDirection) {
        priScrollViewConversation.post(new Runnable() {
            @Override
            public void run() {
                priScrollViewConversation.fullScroll(iDirection);
            }
        });
    }

    private RelativeLayout createConversationStatement(String iStatement, boolean iIsButler){
        RelativeLayout lResult = new RelativeLayout(this);
        lResult.setLayoutParams(this.createStatementLayoutParam(iIsButler));
        lResult.setBackgroundResource(R.drawable.out_message_bg);
        lResult.addView(this.createStatementTextView(iStatement));
        return lResult;
    }

    private LinearLayout.LayoutParams createStatementLayoutParam(boolean iIsButler) {
        LinearLayout.LayoutParams lResult = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (iIsButler == true)
            lResult.gravity = Gravity.LEFT;
        else
            lResult.gravity = Gravity.RIGHT;
        return lResult;
    }


    private TextView createStatementTextView(String iStatement) {
        TextView lResult = new TextView(this);
        lResult.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        lResult.setMaxWidth((int) (R.dimen.conversation_statement_maxWidth * this.getResources().getDisplayMetrics().density + 0.5f));
        lResult.setPadding(0, 5, 0, 5);
        lResult.setText(iStatement);
        return lResult;
    }

    private void showQuestion()
    {
//        getquestion
//        if there's no question -> show something nice -> return


//        swtich case of UI TYPE
//        show answer and question
//        this.priLinearLayoutConversation.addView(createConversationStatement("Ghê qué", false));
//        this.priAnwserFragmentInterface = YesNoFragment.newInstance("wel");
//        getFragmentManager().beginTransaction().add(this.priLinearLayoutAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();

        switchTaskbarToLightTheme(false);
        this.adjustScrollViewAnswer();
        this.scrollScrollViewConversation(View.FOCUS_DOWN);
    }

    private void adjustScrollViewAnswer() {
        this.priScrollViewAnswer.post(new Runnable() {
            public void run() {
                int lAnswerMaxHeight = converToPixel(getResources().getDimension(R.dimen.answer_maxHeight));
                if (priScrollViewAnswer.getHeight() > lAnswerMaxHeight)
                    priScrollViewAnswer.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, lAnswerMaxHeight));
                else
                    priScrollViewAnswer.setLayoutParams(
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }
        });

    }


    private void switchTaskbarToLightTheme(boolean iIsLightTheme) {
        if (iIsLightTheme == true) {
            this.priRelativeLayoutTaskbar.setBackgroundColor(getResources().getColor(R.color.background));
            this.priButtonOk.setBackground(getResources().getDrawable(R.drawable.white_button));
            this.priButtonDecline.setBackground(getResources().getDrawable(R.drawable.white_button));
            this.priButtonOk.setImageResource(R.mipmap.ic_done_blue);
            this.priButtonDecline.setImageResource(R.mipmap.ic_clear_blue);
        }
        else {
            this.priRelativeLayoutTaskbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.priButtonOk.setBackground(getResources().getDrawable(R.drawable.blue_button));
            this.priButtonDecline.setBackground(getResources().getDrawable(R.drawable.blue_button));
            this.priButtonOk.setImageResource(R.mipmap.ic_done);
            this.priButtonDecline.setImageResource(R.mipmap.ic_clear);
        }

    }

    public int converToPixel(float idp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(idp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public void buttonClear_onClick(View view) {
        this.priLinearLayoutAnswer.removeAllViews();
    }
}
