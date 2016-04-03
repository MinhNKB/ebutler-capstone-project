package com.guardian.ebutler.ebutler;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Condition;
import com.guardian.ebutler.ebutler.dataclasses.Question;
import com.guardian.ebutler.ebutler.dataclasses.ScriptManager;
import com.guardian.ebutler.ebutler.dataclasses.UIType;
import com.guardian.ebutler.fragments.answers.AnswerFragmentInterface;
import com.guardian.ebutler.fragments.answers.CheckboxFragment;
import com.guardian.ebutler.fragments.answers.DateFragment;
import com.guardian.ebutler.fragments.answers.MultipleChoiceFragment;
import com.guardian.ebutler.fragments.answers.TextboxFragment;
import com.guardian.ebutler.fragments.answers.TimeFragment;
import com.guardian.ebutler.fragments.answers.TimeSpanFragment;
import com.guardian.ebutler.fragments.answers.YesNoFragment;

import java.util.ArrayList;


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
    private Boolean priIsFinishedAsking = false;
    private NumberProgressBar priProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_input);
        this.findViewsByIds();
        this.setupUI(findViewById(R.id.user_info_input_parent));
        this.priScriptManager = new ScriptManager(this);
        this.createConversationStatement(priScriptManager.GetAGreeting(), true);
        this.showQuestion();
        this.initializeDatabase();
        this.preprocessProgressBar();
    }

    private void preprocessProgressBar() {
        updateProgressBar();
        priProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priIsFinishedAsking) {
                    int changedProgress = (int) (priScriptManager.GetProgress() * 100);
                    createConversationStatement("B?n dã hoàn thành " + changedProgress + "% câu h?i, b?n có mu?n tr? l?i ti?p không?", true);
                    priAnwserFragmentInterface = new YesNoFragment();
                    switchTaskbarToLightTheme(true);
                    getFragmentManager().beginTransaction().add(priRelativeLayoutForSimpleAnswer.getId(), (Fragment) priAnwserFragmentInterface).commit();
                }
            }
        });
    }

    private void updateProgressBar() {
        final int lProgress = (int)(priScriptManager.GetProgress() * 100);
        if (lProgress >= 100) {
            priProgressBar.setVisibility(View.GONE);
        } else {
            int lCurrentProgress = priProgressBar.getProgress();
            int lDisplayProgress = lProgress * 100;
            ObjectAnimator progressAnimator = ObjectAnimator.ofInt(priProgressBar, "progress", lCurrentProgress, lDisplayProgress);
            progressAnimator.setDuration(700);
            progressAnimator.setInterpolator(new DecelerateInterpolator());
            progressAnimator.start();
        }
    }

    private void initializeDatabase() {
        DatabaseHelper lHelper = DatabaseHelper.getInstance(this);
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
        this.priProgressBar = (NumberProgressBar) findViewById(R.id.user_info_input_ProgressBar);

    }

    public void buttonOk_onClick(View view) {
        if (!priIsFinishedAsking) {
            try{
                this.createConversationStatement(this.priAnwserFragmentInterface.getChatStatement(), false);
                this.clearAnswer();
                this.priScriptManager.AnwserQuestion(this.priAnwserFragmentInterface.getValues());
                this.showQuestion();
            }
            catch (Exception ex){
                showToast(ex.getMessage());
                return;
            }
        } else {
            try {
                this.createConversationStatement(this.priAnwserFragmentInterface.getChatStatement(), false);
                this.clearAnswer();
                if (this.priAnwserFragmentInterface instanceof YesNoFragment) {
                    ArrayList<Condition> lYesNoConditions =  this.priAnwserFragmentInterface.getValues();
                    if (lYesNoConditions.get(0).pubValue.equals("true")) {
                        priScriptManager.Refresh();
                        priIsFinishedAsking = false;
                        this.showQuestion();
                    }
                }
            } catch (Exception ex) {
                showToast(ex.getMessage());
                return;
            }
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
        try
        {
            this.priQuestion = this.priScriptManager.GetAQuestion();
//            if (this.priQuestion == null) {}
            this.priAnwserFragmentInterface = this.getQuestionFragment(this.priQuestion);
//            if (this.priAnwserFragmentInterface == null) {}
            if (this.priQuestion.pubUIType == UIType.Textbox || this.priQuestion.pubUIType == UIType.YesNo) {
                getFragmentManager().beginTransaction().add(this.priRelativeLayoutForSimpleAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();
            }
            else
                getFragmentManager().beginTransaction().add(this.priLinearLayoutAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();

            this.createConversationStatement(this.priQuestion.pubQuestionString, true);
            this.switchTaskbarToLightTheme(true);
        }
        catch (Exception ex){
            priIsFinishedAsking = true;
            updateProgressBar();
            this.showFinishMessage();
            this.switchTaskbarToLightTheme(false);
            return;
        }
    }

    private void showFinishMessage(){
        this.createConversationStatement(priScriptManager.GetAFinishString(), true);

    }

    private AnswerFragmentInterface getQuestionFragment(Question iQuestion) {
        switch (iQuestion.pubUIType) {
            case Checkbox:
                return CheckboxFragment.newInstance(iQuestion.pubInformationPropertiesNames, iQuestion.getOptions());
            case Date:
                return DateFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0));
            case MultipleChoice:
                return MultipleChoiceFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0), iQuestion.getOptions());
            case Textbox:
                return TextboxFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0));
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
        this.clearAnswer();
        this.showFinishMessage();
        this.switchTaskbarToLightTheme(false);
    }

    private void clearAnswer() {
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
