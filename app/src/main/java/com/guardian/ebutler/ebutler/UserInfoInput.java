package com.guardian.ebutler.ebutler;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Question;
import com.guardian.ebutler.ebutler.dataclasses.ScriptManager;
import com.guardian.ebutler.ebutler.dataclasses.UIType;
import com.guardian.ebutler.fragments.*;


public class UserInfoInput extends Activity {

    private ScrollView priScrollViewConversation;
    private LinearLayout priLinearLayoutConversation;
    private RelativeLayout priRelativeLayoutTaskbar;
    private AnswerFragmentInterface priAnwserFragmentInterface;
    private ImageButton priButtonDecline;
    private ImageButton priButtonOk;
    private ImageButton priButtonDashboard;
    private ImageButton priButtonAdd;
    private ScrollView priScrollViewAnswer;
    private LinearLayout priLinearLayoutAnswer;
    private RelativeLayout priRelativeLayoutForSimpleAnswer;
    private ScriptManager priScriptManager;
    private Question priQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_input);
        this.findViewsByIds();
        this.setupUI(findViewById(R.id.user_info_input_parent));
        this.priScriptManager = new ScriptManager(this);
        this.showQuestion();
        DatabaseHelper.getInstance(this);
    }

    private void findViewsByIds() {
        this.priScrollViewConversation = (ScrollView) findViewById(R.id.user_info_input_scrollViewConversation);
        this.priLinearLayoutConversation = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutConversation);
        this.priRelativeLayoutTaskbar = (RelativeLayout) findViewById(R.id.user_info_input_taskbar);
        this.priButtonDecline = (ImageButton) findViewById(R.id.user_info_input_buttonDecline);
        this.priButtonOk = (ImageButton) findViewById(R.id.user_info_input_buttonOk);
        this.priButtonDashboard = (ImageButton) findViewById(R.id.user_info_input_buttonDashboard);
        this.priButtonAdd = (ImageButton) findViewById(R.id.user_info_input_buttonAdd);
        this.priScrollViewAnswer = (ScrollView) findViewById(R.id.user_info_input_customScrollViewAnswer);
        this.priLinearLayoutAnswer = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutAnswer);
        this.priRelativeLayoutForSimpleAnswer = (RelativeLayout) findViewById(R.id.user_info_input_relativeLayoutForSimpleAnswer);
    }

    public void buttonOk_onClick(View view) {
        try{
            this.createConversationStatement(this.priAnwserFragmentInterface.getChatStatement(), false);
            this.clearQuestion();
            this.priScriptManager.AnwserQuestion(this.priAnwserFragmentInterface.getValues());
            this.showQuestion();
        }
        catch (Exception ex){
            showToast(ex.getMessage());
            return;
        }
    }

    private void showToast(String iMessage){
        Toast toast = Toast.makeText(this, iMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    private void scrollScrollViewConversation(final int iDirection) {
        priScrollViewConversation.post(new Runnable() {
            @Override
            public void run() {
                priScrollViewConversation.fullScroll(iDirection);
            }
        });
    }

    private void scrollScrollViewQuestion(final int iDirection) {
        priScrollViewConversation.post(new Runnable() {
            @Override
            public void run() {
                scrollScrollViewConversation(iDirection);
            }
        });
    }

    private void createConversationStatement(String iStatement, boolean iIsButler){
        RelativeLayout lResult = new RelativeLayout(this);
        lResult.setLayoutParams(this.createStatementLayoutParam(iIsButler));
        lResult.setBackgroundResource(iIsButler ? R.drawable.out_message_bg : R.drawable.out_message_bg_opposite);
        lResult.addView(this.createStatementTextView(iStatement));
        this.priLinearLayoutConversation.addView(lResult);
        this.scrollScrollViewQuestion(View.FOCUS_DOWN);
    }

    private LinearLayout.LayoutParams createStatementLayoutParam(boolean iIsButler) {
        LinearLayout.LayoutParams lResult = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lResult.gravity = iIsButler ? Gravity.LEFT : Gravity.RIGHT;
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
        this.priQuestion = this.priScriptManager.GetAQuestion();
        if (this.priQuestion == null) {
            this.showFinishMessage();
            this.switchTaskbarToLightTheme(false);
            return;
        }

        this.priAnwserFragmentInterface = this.getQuestionFragment(this.priQuestion);
        if (this.priAnwserFragmentInterface == null) {
            this.showFinishMessage();
            this.switchTaskbarToLightTheme(false);
            return;
        }
        if (this.priQuestion.pubUIType == UIType.Textbox || this.priQuestion.pubUIType == UIType.YesNo) {
            getFragmentManager().beginTransaction().add(this.priRelativeLayoutForSimpleAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();
        }
        else
            getFragmentManager().beginTransaction().add(this.priLinearLayoutAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();

        this.createConversationStatement(this.priQuestion.pubQuestionString, true);
        this.switchTaskbarToLightTheme(true);
    }

    private void showFinishMessage(){
        this.createConversationStatement(getResources().getString(R.string.user_info_input_greetings), true);
    }

    private AnswerFragmentInterface getQuestionFragment(Question iQuestion) {
        switch (iQuestion.pubUIType) {
            case Checkbox:
                return CheckboxFragment.newInstance(iQuestion.getOptions());
            case Date:
                return DateFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0));
            case MultipleChoice:
                return MultipleChoiceFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0), iQuestion.getOptions());
            case Textbox:
                return TextboxFragment.newInstance("");
            case Time:
                return TimeFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0));
            case TimeSpan:
                return TimeSpanFragment.newInstance(iQuestion.pubInformationPropertiesNames);
            case YesNo:
                return YesNoFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0));
            default:
                return null;
        }
    }

    private void switchTaskbarToLightTheme(boolean iIsLightTheme) {
        this.changeButtonSet(iIsLightTheme);
        this.priRelativeLayoutTaskbar.setBackgroundColor(iIsLightTheme == true ?
                getResources().getColor(R.color.background) : getResources().getColor(R.color.colorPrimary));
    }

    private void changeButtonSet(boolean iIsLightTheme){
        this.changeButtonSetVisibility(iIsLightTheme);
        this.changeButtonSetGraphics(iIsLightTheme);
    }

    private void changeButtonSetGraphics(boolean iIsLightTheme) {
        this.priButtonOk.setBackground(iIsLightTheme == true ?
                getResources().getDrawable(R.drawable.white_button) : getResources().getDrawable(R.drawable.blue_button));
        this.priButtonDecline.setBackground(iIsLightTheme == true ?
                getResources().getDrawable(R.drawable.white_button) : getResources().getDrawable(R.drawable.blue_button));
        this.priButtonOk.setImageResource(iIsLightTheme == true ?
                R.mipmap.ic_done_blue : R.mipmap.ic_done);
        this.priButtonDecline.setImageResource(iIsLightTheme == true ?
                R.mipmap.ic_clear_blue : R.mipmap.ic_clear);
    }

    private void changeButtonSetVisibility(boolean iIsLightTheme) {
        this.priButtonOk.setVisibility(iIsLightTheme == true ? View.VISIBLE: View.GONE);
        this.priButtonDecline.setVisibility(iIsLightTheme == true ? View.VISIBLE: View.GONE);
        this.priButtonDashboard.setVisibility(iIsLightTheme == true ? View.GONE : View.VISIBLE);
        this.priButtonAdd.setVisibility(iIsLightTheme == true ? View.GONE : View.VISIBLE);
    }

    public void buttonClear_onClick(View view){
        this.priScriptManager.AnwserQuestion(this.priAnwserFragmentInterface == null ?
                null : this.priAnwserFragmentInterface.getValues());
        this.clearQuestion();
        this.showFinishMessage();
        this.switchTaskbarToLightTheme(false);
    }

    private void clearQuestion() {
        this.priRelativeLayoutForSimpleAnswer.removeAllViews();
        this.priLinearLayoutAnswer.removeAllViews();
    }

    public static void showSoftKeyboard(Activity iActivity, int iType) {
        InputMethodManager lInputMethodManager = (InputMethodManager)  iActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (iActivity.getCurrentFocus() != null)
            lInputMethodManager.hideSoftInputFromWindow(iActivity.getCurrentFocus().getWindowToken(), iType);
    }

    public void setupUI(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(UserInfoInput.this, InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public void buttonAdd_onClick(View view) {
        Intent intent = new Intent(this, CategoryList.class);
        startActivity(intent);
    }

    public void buttonMenu_onClick(View view) {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}
