package com.voicesync.waterharmonizer;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class WHActivity extends Activity {
	Renderer renderer;
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wh);
		init();
	}
	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_wh, menu);
		return true;
	}
	@Override public boolean onOptionsItemSelected(MenuItem item)	{
		if (item.getItemId()==R.id.menu_about) { about(); return true; }
		return false;
	}
	public void about() { startActivity(new Intent(this, AboutActivity.class)); return; } 
	@Override    protected void onPause()	{  	super.onPause();   }
	@Override    protected void onResume()	{  	super.onResume();   } 
	void init() {renderer=new Renderer(this,true);	}
}
