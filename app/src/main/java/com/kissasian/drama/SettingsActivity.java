package com.kissasian.drama;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.oxoo.spagreen.R;

import static com.kissasian.drama.utils.MyAppClass.getContext;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchCompat,switcDarkMode;
    private TextView tvTerms;
    TextView versi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---analytics-----------
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "settings_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        switchCompat=findViewById(R.id.notify_switch);
        tvTerms=findViewById(R.id.tv_term);
        switcDarkMode=findViewById(R.id.mode_switch);

        PackageManager manager = getContext().getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        versi=findViewById(R.id.versi);
        versi.setText(version);

        SharedPreferences preferences=getSharedPreferences("push",MODE_PRIVATE);
        if (preferences.getBoolean("status", true)){
            switchCompat.setChecked(true);
        }else {
            switchCompat.setChecked(false);
        }

        if (preferences.getBoolean("dark",false)){
            switcDarkMode.setChecked(true);
        }else {
            switcDarkMode.setChecked(false);
        }


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putBoolean("status",true);
                    editor.apply();

                }else {
                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putBoolean("status",false);
                    editor.apply();
                }
            }
        });


        switcDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putBoolean("dark",true);
                    editor.apply();


                }else {
                    SharedPreferences.Editor editor = getSharedPreferences("push", MODE_PRIVATE).edit();
                    editor.putBoolean("dark",false);
                    editor.apply();
                }

                startActivity(new Intent(SettingsActivity.this,SplashscreenActivity.class));
                finish();
            }
        });


        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,TermsActivity.class));
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
