package com.voicesync.waterharmonizer;

import android.os.Bundle;
import android.webkit.WebView;
import android.app.Activity;

public class AboutActivity extends Activity {
    @Override  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((WebView) findViewById(R.id.webView1)).loadUrl("file:///android_asset/html/WaterHarmonizer.html");
    }
}
