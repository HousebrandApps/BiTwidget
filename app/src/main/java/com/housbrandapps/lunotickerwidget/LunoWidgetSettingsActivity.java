package com.housbrandapps.lunotickerwidget;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.housbrandapps.lunotickerwidget.databinding.ActivityLunoWidgetSettingsBinding;

public class LunoWidgetSettingsActivity extends AppCompatActivity implements LunoWidgetSettingsViewModel.LunoSettingsView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLunoWidgetSettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_luno_widget_settings);
        binding.setVm(new LunoWidgetSettingsViewModel(this));
    }

    @Override
    public void onSaved() {
        Intent intent = new Intent(this, LunoWidget.class);
        intent.setAction(LunoWidget.INIT_EVENT);
        sendBroadcast(intent);
        finish();
    }
}
