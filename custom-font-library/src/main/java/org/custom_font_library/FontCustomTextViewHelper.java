package org.custom_font_library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Share functionality across multiple view subclasses using composition.
 */
class FontCustomTextViewHelper {

    FontCustomTextViewHelper() {

    }

    /**
     * For identify class in Log
     */
    private static final String TAG = "ViewHelper";

    /**
     * Corresponds to the constructor of TextView and children
     */
    public static void initialize(TextView view, Context context,
                                  AttributeSet attributeSet) {

        TypedArray attributes = context.obtainStyledAttributes(attributeSet,
                R.styleable.FontCustom, R.attr.typeface, 0);

        String typefaceDesc = attributes
                .getString(R.styleable.FontCustom_typeface);

        // If not set in the style, attempt to pull from the messageTypeface
        if (typefaceDesc == null) {
            typefaceDesc = attributes
                    .getString(R.styleable.FontCustom_messageTypeface);
        }

        if (typefaceDesc != null) {
            Typeface typeface = FontCustomTypefaceHelper.getTypeface(context, typefaceDesc);
            Log.d(TAG, "setTypeface(" + typeface.toString() + ")");
            view.setTypeface(typeface);
        } else {
            Log.w(TAG, "View had typeface attribute but no typeface was found in /assets");
        }
    }
}
