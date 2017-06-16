package org.custom_font_library;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;

/**
 * Helper for acquiring font from assets. Lazy loads and keeps fonts cached.
 * <p/>
 * Should be thread-safe since a font is generally a read-only thing.
 */
public class FontCustomTypefaceHelper {
    private static final String TAG = "TypefaceHelper";
    private static final HashMap<String, Typeface> TYPEFACE_CACHE = new HashMap<String, Typeface>();

    /**
     * Retrieve a type-face. Does not load twice, uses lazy loading.
     */
    public static Typeface getTypeface(Context context1, String name) {
        // ensure the global context is used. just in case.
        Context context = context1.getApplicationContext();
        Log.d(TAG, "name=" + name);
        if (TYPEFACE_CACHE.containsKey(name)) {
            return TYPEFACE_CACHE.get(name);
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), name);

        if (typeface != null) {
            TYPEFACE_CACHE.put(name, typeface);
        }

        return typeface;
    }

    FontCustomTypefaceHelper() {
    }
}