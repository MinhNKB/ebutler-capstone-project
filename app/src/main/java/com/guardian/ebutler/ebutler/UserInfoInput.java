package com.guardian.ebutler.ebutler;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.Spanned;
import android.view.Gravity;
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

import com.guardian.ebutler.ebutler.custom.CustomListAdapter;
import com.guardian.ebutler.ebutler.databasehelper.DatabaseHelper;
import com.guardian.ebutler.ebutler.dataclasses.Condition;
import com.guardian.ebutler.ebutler.dataclasses.Question;
import com.guardian.ebutler.ebutler.dataclasses.ScriptManager;
import com.guardian.ebutler.ebutler.dataclasses.Task;
import com.guardian.ebutler.ebutler.dataclasses.UIType;
import com.guardian.ebutler.fragments.ProgressBarFragment;
import com.guardian.ebutler.fragments.answers.AnswerFragmentInterface;
import com.guardian.ebutler.fragments.answers.CheckboxFragment;
import com.guardian.ebutler.fragments.answers.DateFragment;
import com.guardian.ebutler.fragments.answers.MultipleChoiceFragment;
import com.guardian.ebutler.fragments.answers.TextboxFragment;
import com.guardian.ebutler.fragments.answers.TimeFragment;
import com.guardian.ebutler.fragments.answers.TimeSpanFragment;
import com.guardian.ebutler.fragments.answers.YesNoFragment;
import com.guardian.ebutler.timehelper.DateTimeHelper;
import com.guardian.ebutler.world.Global;

import java.util.ArrayList;
import java.util.List;


public class UserInfoInput extends Activity {

    private ScrollView priScrollViewConversation;
    private LinearLayout priLinearLayoutConversation;
    private RelativeLayout priRelativeLayoutTaskbar;
    private AnswerFragmentInterface priAnwserFragmentInterface;
    private ImageButton priButtonDecline;
    private ImageButton priButtonOk;
    private ImageButton priButtonDashboard;
    private ScrollView priScrollViewAnswer;
    private LinearLayout priLinearLayoutAnswer;
    private RelativeLayout priRelativeLayoutForSimpleAnswer;
    private ScriptManager priScriptManager;
    private Question priQuestion;
    private Boolean priIsFinishedAsking = false;
    private View priProgressBar;
    private int priToBeAskedQuestionCategory = -1;
    private RelativeLayout priPreviousButlerChatStatement = null;
    private Boolean priIsFirstTimeAsking = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_input);
        this.findViewsByIds();
        this.setupUI(findViewById(R.id.user_info_input_parent));
        this.priScriptManager = new ScriptManager(this);
        this.initializeDatabase();
        this.preprocessProgressBar();
        this.setupConversation();

    }

    private void setupConversation() {
        switchTaskbarToLightTheme(false);

        int lTiming = 500;
        final UserInfoInput lSelf = this;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        lSelf.createConversationStatement(priScriptManager.GetAGreeting(), true);
                    }
                },
                lTiming
        );
        if (Global.getInstance().pubFirstTimeInput) {
            Boolean lHasPayTask = false;
            DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
            List<Task> lComingTask = lHelper.GetComingTasks();
            String lTasks = "";
            for (Task lTask : lComingTask) {
                lTasks += " - " + lTask.pubName + " lúc " + DateTimeHelper.getDateStringFromDate(lTask.pubTime) + "<br/>";
                if (CustomListAdapter.normalizeVietnameseString(lTask.pubName).toLowerCase().contains("dong tien")) {
                    lHasPayTask = true;
                }
            }

            lTiming += 2000;
            final String fTasks = lTasks;
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            lSelf.createConversationStatementToDashboardEvent(priScriptManager.CreateTaskNotification(fTasks, true), true);
                        }
                    },
                    lTiming
            );

            if (lHasPayTask) {
                lTiming += 2000;
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                lSelf.createConversationStatementToMapAPIEvent(priScriptManager.ToMapAPIChatStatement(), true);
                            }
                        },
                        lTiming
                );
            }
        }

        Global.getInstance().pubFirstTimeInput = false;

        lTiming += 2000;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        lSelf.showQuestionGroup();
                    }
                },
                lTiming
        );

        lTiming += 2000;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        lSelf.showQuestion();
                    }
                },
                lTiming
        );

    }

    private void showQuestionGroup() {
        String lQuestionString = this.priScriptManager.GetAQuestionGroupString();
        if(lQuestionString!=null && lQuestionString!="")
            this.createConversationStatement(lQuestionString, true);
    }

    private void preprocessProgressBar() {
        updateProgressBar();
    }

    public void progressBubbleClicked(int iCategory) {
        if (iCategory == -1) {
            return;
        }
        if (priIsFinishedAsking) {
            priToBeAskedQuestionCategory = iCategory;
            createConversationStatement("Bạn có muốn trả lời bộ câu hỏi thứ " + Integer.toString(iCategory) + " không?", true);
            priAnwserFragmentInterface = new YesNoFragment();
            switchTaskbarToLightTheme(true);
            getFragmentManager().beginTransaction().add(priRelativeLayoutForSimpleAnswer.getId(), (Fragment) priAnwserFragmentInterface).commit();
        }
    }

    private void updateProgressBar() {
        List<Boolean> lProgress = priScriptManager.GetProgress();
        ProgressBarFragment lProgressBarFragment = ((ProgressBarFragment)getFragmentManager().findFragmentById(R.id.user_info_input_progressBar));
        if (lProgressBarFragment != null) {
            lProgressBarFragment.setProgress(lProgress);
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
        this.priScrollViewAnswer = (ScrollView) findViewById(R.id.user_info_input_customScrollViewAnswer);
        this.priLinearLayoutAnswer = (LinearLayout) findViewById(R.id.user_info_input_linearLayoutAnswer);
        this.priRelativeLayoutForSimpleAnswer = (RelativeLayout) findViewById(R.id.user_info_input_relativeLayoutForSimpleAnswer);
        this.priProgressBar = findViewById(R.id.user_info_input_progressBar);
    }

    public void buttonOk_onClick(View view) {
        if (!priIsFinishedAsking) {
            try{
                this.createConversationStatement(this.priAnwserFragmentInterface.getChatStatement(), false);
                this.clearAnswer();
                Task lNewTask = this.priScriptManager.AnwserQuestion(this.priAnwserFragmentInterface.getValues());
                if(lNewTask!=null) {
                    DatabaseHelper lHelper = DatabaseHelper.getInstance(null);
                    lHelper.InsertATask(lNewTask);
                }
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
                        priScriptManager.Refresh(priToBeAskedQuestionCategory);
                        priIsFinishedAsking = false;
                        this.showQuestionGroup();
                        this.showQuestion();
                    }
                    else
                    {
                        this.clearAnswer();
                        this.switchTaskbarToLightTheme(false);
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

    private void removeButlerChatHeadline() {
        if (priPreviousButlerChatStatement != null) {
            priPreviousButlerChatStatement.setBackgroundResource(R.drawable.out_message_bg);
        }
    }

    private void setButlerChatHeadline(RelativeLayout rRelativeLayout) {
        rRelativeLayout.setBackgroundResource(R.drawable.out_message_bg_headline);
        priPreviousButlerChatStatement = rRelativeLayout;
    }

    public void createConversationStatementToDashboardEvent(Spanned iStatement, boolean iIsButler) {
        RelativeLayout lResult = createConversationStatement(iStatement, iIsButler);
        final UserInfoInput lSelf = this;
        lResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lSelf.navigateToDashboard();
            }
        });
    }

    public void createConversationStatementToMapAPIEvent(String iStatement, boolean iIsButler) {
        RelativeLayout lResult = createConversationStatement(iStatement, iIsButler);
        final UserInfoInput lSelf = this;
        lResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lSelf.startMapAPIForResult();
            }
        });
    }

    public RelativeLayout createConversationStatement(String iStatement, boolean iIsButler){
        removeButlerChatHeadline();
        RelativeLayout lResult = new RelativeLayout(this);
        lResult.setLayoutParams(this.createStatementLayoutParam(iIsButler));
        if (iIsButler) {
            setButlerChatHeadline(lResult);
        } else {
            lResult.setBackgroundResource(R.drawable.out_message_bg_opposite);
        }
        lResult.addView(this.createStatementTextView(iStatement));
        this.priLinearLayoutConversation.addView(lResult);
        this.scrollScrollViewQuestion(View.FOCUS_DOWN);
        return lResult;
    }

    public RelativeLayout createConversationStatement(Spanned iStatement, boolean iIsButler) {
        removeButlerChatHeadline();
        RelativeLayout lResult = new RelativeLayout(this);
        lResult.setLayoutParams(this.createStatementLayoutParam(iIsButler));
        if (iIsButler) {
            setButlerChatHeadline(lResult);
        } else {
            lResult.setBackgroundResource(R.drawable.out_message_bg_opposite);
        }
        lResult.addView(this.createStatementTextView(iStatement));
        this.priLinearLayoutConversation.addView(lResult);
        this.scrollScrollViewQuestion(View.FOCUS_DOWN);
        return lResult;
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

    private TextView createStatementTextView(Spanned iStatement) {
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
            if (priQuestion != null) {
                priIsFirstTimeAsking = false;
//            if (this.priQuestion == null) {}
                this.priAnwserFragmentInterface = this.getQuestionFragment(this.priQuestion);
//            if (this.priAnwserFragmentInterface == null) {}
                if (this.priQuestion.pubUIType == UIType.Textbox || this.priQuestion.pubUIType == UIType.YesNo) {
                    getFragmentManager().beginTransaction().add(this.priRelativeLayoutForSimpleAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();
                } else
                    getFragmentManager().beginTransaction().add(this.priLinearLayoutAnswer.getId(), (Fragment) this.priAnwserFragmentInterface).commit();

                this.createConversationStatement(this.priQuestion.pubQuestionString, true);
                this.switchTaskbarToLightTheme(true);
            }
            else
            {
                priIsFinishedAsking = true;
                updateProgressBar();
                if (!priIsFirstTimeAsking) {
                    this.showFinishMessage();
                }
                this.clearAnswer();
                this.switchTaskbarToLightTheme(false);
            }
        }
        catch (Exception ex){
            priIsFinishedAsking = true;
            updateProgressBar();
            if (!priIsFirstTimeAsking) {
                this.showFinishMessage();
            }
            this.clearAnswer();
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
                return CheckboxFragment.newInstance(iQuestion.pubInformationPropertiesNames, iQuestion.getOptions(), iQuestion.getDefaultValue());
            case Date:
                return DateFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0), iQuestion.getDefaultValue());
            case MultipleChoice:
                return MultipleChoiceFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0), iQuestion.getOptions());
            case Textbox:
                return TextboxFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0));
            case Time:
                return TimeFragment.newInstance(iQuestion.pubInformationPropertiesNames.get(0), iQuestion.getDefaultValue());
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

    private void changeButtonSet(boolean iIsLightTheme) {
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
        this.priButtonOk.setVisibility(iIsLightTheme == true ? View.VISIBLE : View.GONE);
        this.priButtonDecline.setVisibility(iIsLightTheme == true ? View.VISIBLE : View.GONE);
        this.priButtonDashboard.setVisibility(iIsLightTheme == true ? View.GONE : View.VISIBLE);
    }

    public void buttonClear_onClick(View view){
//        this.priScriptManager.AnwserQuestion(this.priAnwserFragmentInterface == null ?
//                null : this.priAnwserFragmentInterface.getValues());
//        this.priIsFinishedAsking = true;
//        updateProgressBar();
//        this.clearAnswer();
//        this.showFinishMessage();
//        this.switchTaskbarToLightTheme(false);
        if(!priIsFinishedAsking) {
            this.priScriptManager.AnwserQuestion(null);
//            this.priIsFinishedAsking = true;
            updateProgressBar();
            this.createConversationStatement(getResources().getString(R.string.user_info_denyAQuestion), false);
            this.clearAnswer();
            this.showQuestion();
        }
        else {
            this.createConversationStatement("Không", false);
            this.clearAnswer();
            this.switchTaskbarToLightTheme(false);
        }
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
//        Intent intent = new Intent(this, CategoryList.class);
//        startActivity(intent);
    }

    public void buttonMenu_onClick(View view) {
        navigateToDashboard();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    private void startMapAPIForResult() {
        Intent lIntent = new Intent(this, MapAPI.class);
        startActivityForResult(lIntent, MapAPI.NULL_REQUEST);
    }
}
