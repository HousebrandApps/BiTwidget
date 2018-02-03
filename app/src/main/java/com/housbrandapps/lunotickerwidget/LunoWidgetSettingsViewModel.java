package com.housbrandapps.lunotickerwidget;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

public class LunoWidgetSettingsViewModel extends BaseObservable {

    private LunoSettingsView view;
    private String bitcoinValue;

    public LunoWidgetSettingsViewModel(LunoSettingsView view){
        this.view = view;
    }

    @Bindable
    public String getBitcoinValue() {
        return bitcoinValue;
    }

    public void setBitcoinValue(String bitcoinValue) {
        this.bitcoinValue = bitcoinValue;
        notifyPropertyChanged(BR.bitcoinValue);
    }

    public void saveClicked(View v){
        LunoWidgetSharedSettings.setHowMuchBitcoinYouHave(v.getContext(), bitcoinValue);
        LunoWidgetSharedSettings.setHasBeenClickedYet(v.getContext(), true);
        view.onSaved();
    }

    interface LunoSettingsView {
        void onSaved();
    }
}
