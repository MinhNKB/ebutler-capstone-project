package com.guardian.ebutler.ebutler.custom;

import com.guardian.ebutler.ebutler.dataclasses.TaskType;

import java.util.jar.Attributes;

/**
 * Created by Duy on 1/27/2016.
 */
public class CustomListItem
{
    public TaskType pubTaskType;
    public String pubFirstLine;
    public String pubSecondLine;
    public String pubThirdLine;
    public int pubImageButton;
    public int pubImageButtonSize = 0;
    public Object pubObject;

    public CustomListItem(TaskType iTaskType, String iFirstLine, String iSecondLine, String iThirdLine, int iImageButton)
    {
        this.pubTaskType = iTaskType;
        this.pubFirstLine = iFirstLine;
        this.pubSecondLine = iSecondLine;
        this.pubThirdLine = iThirdLine;
        this.pubImageButton = iImageButton;
    }

    public CustomListItem(Object iObject, TaskType iTaskType, String iFirstLine, String iSecondLine, String iThirdLine, int iImageButton)
    {
        this.pubObject = iObject;
        this.pubTaskType = iTaskType;
        this.pubFirstLine = iFirstLine;
        this.pubSecondLine = iSecondLine;
        this.pubThirdLine = iThirdLine;
        this.pubImageButton = iImageButton;
    }

    public CustomListItem(TaskType iTaskType, String iFirstLine, String iSecondLine, String iThirdLine, int iImageButton, int iImageButtonSize)
    {
        this.pubTaskType = iTaskType;
        this.pubFirstLine = iFirstLine;
        this.pubSecondLine = iSecondLine;
        this.pubThirdLine = iThirdLine;
        this.pubImageButton = iImageButton;
        this.pubImageButtonSize = iImageButtonSize;
    }
}
