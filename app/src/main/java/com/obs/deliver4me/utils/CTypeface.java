package com.obs.deliver4me.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.obs.deliver4me.configs.Constants;

public class CTypeface {
    private Typeface fontType;

    //Setting Font
    public Typeface getFontStyle(Constants.FontStyle fontStyle, Context context){
        switch (fontStyle){
            case BOLD:
//                fontType = Typeface.createFromAsset(context.getAssets(), "fonts/CircularStd_Bold.otf");
                break;
            case REGULAR:
//                fontType = Typeface.createFromAsset(context.getAssets(), "fonts/CircularStd_Book.ttf");
                break;
            default:
                break;
        }

        return fontType;
    }
}
