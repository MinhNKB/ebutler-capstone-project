package com.guardian.ebutler.ebutler;

import java.util.jar.Attributes;

/**
 * Created by Duy on 1/27/2016.
 */
public class CustomListItem
{
    public String pubFirstLine;
    public String pubSecondLine;
    public String pubThirdLine;
    public int pubFlag;

    public CustomListItem(String iFirstLine, String iSecondLine, String iThirdLine, int iFlag)
    {
        this.pubFirstLine = iFirstLine;
        this.pubSecondLine = iSecondLine;
        this.pubThirdLine = iThirdLine;
        this.pubFlag = iFlag;
    }



}
