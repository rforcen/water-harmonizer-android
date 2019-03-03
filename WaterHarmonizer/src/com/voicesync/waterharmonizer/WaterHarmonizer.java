package com.voicesync.waterharmonizer;

import javax.microedition.khronos.opengles.GL10;

public class WaterHarmonizer {
	Kaleidoscope kale;
	Textures fractals;
	
	public WaterHarmonizer(GL10 gl, Renderer renderer) {
		kale=new Kaleidoscope();
		kale.create(gl, 12);
		fractals=new Textures(renderer);
		fractals.loadAllTexturesFromAssets(gl, "fractals");
		fractals.actualTexture(gl);
	}
	public void draw(GL10 gl) {
		kale.draw(gl);
	}
}
