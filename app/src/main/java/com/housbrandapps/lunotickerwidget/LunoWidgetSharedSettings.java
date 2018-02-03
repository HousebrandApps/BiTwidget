package com.housbrandapps.lunotickerwidget;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by natiek on 2018/02/02.
 */

public class LunoWidgetSharedSettings {

    private static final String HAS_BEEN_CLICKED_YET = "HAS_BEEN_CLICKED_YET";
    private static final String HOW_MUCH_BITCOIN_YOU_HAVE = "HOW_MUCH_BITCOIN_YOU_HAVE";
    private static final String WHICH_CURRENCY = "WHICH_CURRENCY";

    private static SharedPreferences getPrefs(Context ctx) {
        return ctx.getSharedPreferences(BuildConfig.APPLICATION_ID + BuildConfig.VERSION_CODE, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getPrefsEditor(Context ctx) {
        return getPrefs(ctx).edit();
    }

    public static void setWhichCurrency(Context ctx, String currency) {
        getPrefsEditor(ctx).putString(WHICH_CURRENCY, currency).apply();
    }

    public static String getWhichCurrency(Context ctx) {
        return getPrefs(ctx).getString(WHICH_CURRENCY, "ZAR");
    }

    public static void setHasBeenClickedYet(Context ctx, boolean hasBeenClickedYet) {
        getPrefsEditor(ctx).putBoolean(HAS_BEEN_CLICKED_YET, hasBeenClickedYet).apply();
    }

    public static boolean getHasBeenClickedYet(Context ctx) {
        return getPrefs(ctx).getBoolean(HAS_BEEN_CLICKED_YET, false);
    }

    public static void setHowMuchBitcoinYouHave(Context ctx, String howMuchBitcoin) {
        getPrefsEditor(ctx).putString(HOW_MUCH_BITCOIN_YOU_HAVE, howMuchBitcoin).apply();
    }

    public static double getHowMuchBitcoinYouHave(Context ctx) {
        return Double.valueOf(getPrefs(ctx).getString(HOW_MUCH_BITCOIN_YOU_HAVE, "1.0"));
    }

    public static void clear(Context ctx) {
        getPrefsEditor(ctx).clear().apply();
    }
}
