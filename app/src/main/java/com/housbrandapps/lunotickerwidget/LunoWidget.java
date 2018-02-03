package com.housbrandapps.lunotickerwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LunoWidget extends AppWidgetProvider {

    private static final String CLICK_EVENT = "CLICK_EVENT";
    public static final String INIT_EVENT = "INIT_EVENT";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_luno_how_much);
        views.setOnClickPendingIntent(R.id.luno_full_widget_layout, getPendingSelfIntent(context, CLICK_EVENT));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, LunoWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        if (CLICK_EVENT.equals(intent.getAction())) {
            if (LunoWidgetSharedSettings.getHasBeenClickedYet(context)) {
                final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_luno_how_much);
                getTickerService(context, views);
            } else {
                context.startActivity(new Intent(context, LunoWidgetSettingsActivity.class));
            }
        } else if (INIT_EVENT.equals(intent.getAction())) {
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_luno_how_much);
            getTickerService(context, views);
        }
    }

    private void getTickerService(final Context context, final RemoteViews views) {
        try {
            views.setTextViewText(R.id.luno_widget_text_your_btc_value, "");
            views.setTextViewText(R.id.luno_widget_text_trade_value, "UPDATING...");
            views.setTextViewText(R.id.luno_widget_text_btc_cash_value, "");
            notifyWidgetChanged(context, views);
            LunoTickerRepo repo = new LunoTickerRepo(ServiceFactory.createService(LunoTickerRepo.LunoTickerService.class));
            repo.setSubscriber(new Observer<LunoTickerRepo.LunoTickerResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(LunoTickerRepo.LunoTickerResponse value) {
                    if (value != null) {
                        double howMuchBitcoinYouHave = LunoWidgetSharedSettings.getHowMuchBitcoinYouHave(context);
                        double bitcoinInValue = Double.valueOf(value.lastTrade) * howMuchBitcoinYouHave;

                        views.setTextViewText(R.id.luno_widget_text_btc_cash_value, "You have BTC " + howMuchBitcoinYouHave);

                        views.setTextViewText(R.id.luno_widget_text_trade_value, "1 BTC = " + String.format(
                                Locale.ENGLISH,
                                CurrencyUtil.getCurrencySymbol(LunoWidgetSharedSettings.getWhichCurrency(context)) + " %1$,.2f",
                                Double.valueOf(value.lastTrade)
                        ));

                        views.setTextViewText(
                                R.id.luno_widget_text_your_btc_value,
                                "Your BTC is worth: " +
                                        String.format(
                                                Locale.ENGLISH,
                                                CurrencyUtil.getCurrencySymbol(LunoWidgetSharedSettings.getWhichCurrency(context)) + " %1$,.2f",
                                                bitcoinInValue
                                        )
                        );
                        notifyWidgetChanged(context, views);
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                }
            });
            repo.getTickerResponse("XBT" + LunoWidgetSharedSettings.getWhichCurrency(context));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private static void notifyWidgetChanged(Context context, RemoteViews views) {
        ComponentName componentName = new ComponentName(context, LunoWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        LunoWidgetSharedSettings.clear(context);
    }
}
