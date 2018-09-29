package com.nrda;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nrda.Dialogs.PrettyDialog;
import com.nrda.Dialogs.PrettyDialogCallback;

import java.util.Locale;

/**
 * Created by Diwash Choudhary on 09-12-2016.
 */
public class Language_Setting extends AppCompatActivity {
    RadioButton radio_language_hindi, radio_language_english;
    RadioGroup rgLanguage;
    int f = 0;
    private TextView tool_title;
    private ImageView tool_back_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        tool_title              =(TextView)findViewById(R.id.appTitle);
        tool_back_icon          =(ImageView)findViewById(R.id.tool_back_icon);
        radio_language_english  = (RadioButton) findViewById(R.id.radio_language_english);
        radio_language_hindi    = (RadioButton) findViewById(R.id.radio_language_hindi);
        rgLanguage              = (RadioGroup)  findViewById(R.id.rglanguage);

        tool_title.setText(getResources().getString(R.string.language_setting));
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_Bus_Timin.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);

       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String langSett = prefs.getString("LANG", "");

        if(langSett.equalsIgnoreCase("en")){
            radio_language_english.setChecked(true);
            *//*radio_language_english.setVisibility(View.GONE);
            radio_language_hindi.setVisibility(View.VISIBLE);*//*
        }else if(langSett.equalsIgnoreCase("hi")){
            radio_language_hindi.setChecked(true);
            *//*radio_language_hindi.setVisibility(View.GONE);
            radio_language_english.setVisibility(View.VISIBLE);*//*
        }*/


        final PrettyDialog dialog = new PrettyDialog(this);
        dialog
                .setTitle("")
                .setMessage(Language_Setting.this.getString(R.string.choose_your_language))
                .setIcon(R.drawable.pdlg_icon_info,R.color.pdlg_color_blue,null)
                .addButton("हिंदी", R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {
                        f=1;

                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();
                        setLangRecreate("hi");

                        Intent i = new Intent(Language_Setting.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(i);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"OK selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                })
                .addButton("English", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                    @Override
                    public void onClick() {

                        f=0;
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                        setLangRecreate("en");
                        Intent ii = new Intent(Language_Setting.this, MainActivity.class);
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(ii);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        dialog.dismiss();
                        //Toast.makeText(Language_Setting.this,"Cancel selected",Toast.LENGTH_SHORT).show();
                        return;


                    }
                });
        dialog.show();
    }

    public void language(View view) {

        switch (view.getId()) {

            // if in case select hindi

            case R.id.radio_language_hindi:
                radio_language_hindi.isChecked();
                f=1;

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").commit();
                setLangRecreate("hi");

                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return;

            //if case select english

            case R.id.radio_language_english:
                radio_language_english.isChecked();

                f=0;
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                setLangRecreate("en");
                Intent ii = new Intent(this, MainActivity.class);
                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(ii);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                return;

            default: //By default set to english
                /*radio_language_english.isChecked();
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                setLangRecreate("en");*/
                return;
        }
    }

    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.action_back);
       *//* MenuItem itemsetting = (MenuItem) menu.findItem(R.id.setting);
        itemsetting.setVisible(true);*//*
        item.setVisible(false);*/

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_back:
               *//* Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();*//*

                break;
            case R.id.setting:
                this.finish();
                Intent intent = new Intent(Language_Setting.this, MapActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            default:
                break;
        }*/

        return true;
    }

}
