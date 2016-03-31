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

    public CustomListItem(TaskType iTaskType, String iFirstLine, String iSecondLine, String iThirdLine, int iImageButton)
    {
        this.pubTaskType = iTaskType;
        this.pubFirstLine = iFirstLine;
        this.pubSecondLine = iSecondLine;
        this.pubThirdLine = iThirdLine;
        this.pubImageButton = iImageButton;
    }
}
