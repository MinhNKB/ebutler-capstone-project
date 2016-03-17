package com.guardian.ebutler.ebutler;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.guardian.ebutler.fragments.*;

import java.util.ArrayList;

public class UserInfoInput extends Activity {

    private ScrollView priScrollViewConversation;
    private LinearLayout priLinearLayoutConversation;
    private RelativeLayout priRelativeLayoutTaskbar;
    private AnswerFragmentInterface priAnwserFragmentInterface;
    private ImageButton priButtonDecline;
    private ImageButton priButtonOk;
    private ScrollView priScrollViewAnswer;
    private LinearLayout priLinearLayoutAnswer;
    private RelativeLayout priRelativeLayoutForSimpleAnswer;

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
        this.priScrollViewAnswer = (ScrollView) findViewById(R.id.user_info_input_customScrollViewAnswer);
        this.priLinearLayoutAnswer = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutAnswer);
        this.priRelativeLayoutForSimpleAnswer = (RelativeLayout) findViewById(R.id.user_info_input_relativeLayoutForEditText);
    }

    public void buttonOk_onClick(View view) {
        this.createConversationStatement(this.priAnwserFragmentInterface.getChatStatement(), false);
        clearQuestion();
        showQuestion();
    }

    private void scrollScrollViewConversation(final int iDirection) {
        priScrollViewConversation.post(new Runnable() {
            @Override
            public void run() {
                priScrollViewConversation.fullScroll(iDirection);
            }
        });
    }

    private void createConversationStatement(String iStatement, boolean iIsButler){
        RelativeLayout lResult = new RelativeLayout(this);
        lResult.setLayoutParams(this.createStatementLayoutParam(iIsButler));
        lResult.setBackgroundResource(R.drawable.out_message_bg);
        lResult.addView(this.createStatementTextView(iStatement));
        this.priLinearLayoutConversation.addView(lResult);
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

        this.priAnwserFragmentInterface = this.getQuestionFragment();
        if (this.priAnwserFragmentInterface == null)
            return;

        if (this.priFakeEnum == 3 || this.priFakeEnum == 6)
            getFragmentManager().beginTransaction().add(this.priRelativeLayoutForSimpleAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();
        else
            getFragmentManager().beginTransaction().add(this.priLinearLayoutAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();
        this.createConversationStatement("Đây là câu hỏi", true);

        this.switchTaskbarToLightTheme(true);
        this.scrollScrollViewConversation(View.FOCUS_DOWN);
        ++this.priFakeEnum;
    }

    private int priFakeEnum = 2;

    private AnswerFragmentInterface getQuestionFragment() {
        ArrayList<String> lStringList = new ArrayList<String>();
        lStringList.add("Random1");lStringList.add("Random1");lStringList.add("Random1");lStringList.add("Random1");lStringList.add("Random1");
        switch (priFakeEnum) {
            case 0:
                return CheckboxFragment.newInstance(lStringList);
            case 1:
                return DateFragment.newInstance("");
            case 2:
                return MultipleChoiceFragment.newInstance("aaa", lStringList);
            case 3:
                return TextboxFragment.newInstance("");
            case 4:
                return TimeFragment.newInstance("");
            case 5:
                return TimeSpanFragment.newInstance(new ArrayList<String>());
            case 6:
                return YesNoFragment.newInstance("");
            default:
                return null;
        }
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

    public void buttonClear_onClick(View view){
    }

    private void clearQuestion() {
        this.priRelativeLayoutForSimpleAnswer.removeAllViews();
        this.priLinearLayoutAnswer.removeAllViews();
        this.switchTaskbarToLightTheme(false);
    }
}
