package com.guardian.ebutler.ebutler;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guardian.ebutler.fragments.AnswerFragmentInterface;
import com.guardian.ebutler.fragments.TextboxFragment;
import com.guardian.ebutler.fragments.YesNoFragment;

public class UserInfoInput extends Activity {

    private ScrollView priScrollViewConversation;
    private LinearLayout priLinearLayoutConversation;
    private RelativeLayout priRelativeLayoutTaskbar;
    private AnswerFragmentInterface priAnwserFragmentInterface;
    private ImageButton priButtonDecline;
    private ImageButton priButtonOk;
    private ScrollView priScrollViewAnswer;
    private LinearLayout priLinearLayoutAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_input);
        this.findViewsByIds();
        showQuestion();
    }

    private void findViewsByIds() {
        this.priScrollViewConversation = (ScrollView) findViewById(R.id.user_info_input_scrollViewConversation);
        this.priLinearLayoutConversation = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutConversation);
        this.priRelativeLayoutTaskbar = (RelativeLayout) findViewById(R.id.user_info_input_taskbar);
        this.priButtonDecline = (ImageButton) findViewById(R.id.user_info_input_buttonDecline);
        this.priButtonOk = (ImageButton) findViewById(R.id.user_info_input_buttonOk);
        this.priScrollViewAnswer = (ScrollView) findViewById(R.id.user_info_input_scrollViewAnswer);
        this.priLinearLayoutAnswer = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutAnswer);
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

    private void showQuestion()
    {
        //getquestion
        //if there's no question -> show something nice -> return
        //swtich case of UI TYPE
        //show question
        //show answer
        this.priLinearLayoutConversation.addView(createConversationStatement("Ghê qué", true));

        YesNoFragment lFragment = YesNoFragment.newInstance("wel");
        this.priAnwserFragmentInterface = lFragment;
        getFragmentManager().beginTransaction().add(this.priLinearLayoutAnswer.getId(), lFragment).commit();

        this.adjustScrollViewAnswer();

        //if it is edit text -> switch dark theme
        switchTaskbar(true);
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


    private void switchTaskbar(boolean iIsLightTheme) {
        if (iIsLightTheme == true) {
            this.priRelativeLayoutTaskbar.setBackgroundColor(getResources().getColor(R.color.background));
            this.priButtonOk.setBackground(getResources().getDrawable(R.drawable.white_button));
            this.priButtonDecline.setBackground(getResources().getDrawable(R.drawable.white_button));
            this.priButtonOk.setImageResource(R.mipmap.ic_done_blue);
            this.priButtonDecline.setImageResource(R.mipmap.ic_clear_blue);
        }
        else {
            int iDarkColor = getResources().getColor(R.color.colorPrimary);
            this.priRelativeLayoutTaskbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.priButtonOk.setBackgroundColor(iDarkColor);
            this.priButtonDecline.setBackgroundColor(iDarkColor);
            this.priButtonOk.setBackgroundResource(R.mipmap.ic_done);
            this.priButtonDecline.setBackgroundResource(R.mipmap.ic_clear);
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
