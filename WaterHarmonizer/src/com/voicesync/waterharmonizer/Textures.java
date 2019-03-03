package com.voicesync.waterharmonizer;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.BitmapFactory;
import com.voicesync.lib.ScalePowerof2;

public class Textures {

	String[]		textPics; // textures mgr
	int				numTextures, tAct, mark;
	int[]			Textures;
	Renderer		renderer; // should save context

	public Textures(Renderer renderer) {this.renderer=renderer; tAct=0;}
	public void loadAllTexturesFromAssets(GL10 gl, String texturesPath) {
		try {
			textPics=renderer.context.getAssets().list(texturesPath);
			numTextures=textPics.length;
			Textures=new int[numTextures];
			gl.glGenTextures(numTextures, Textures, 0); // generate the texture set
			for (int i=0; i<numTextures; i++) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, Textures[i]);
				renderer.loadTextureAsset(gl,texturesPath+"/"+textPics[i]);
			}
		} catch (IOException e) {numTextures=0;}
	}
	public void load(GL10 gl, String[]fns) {
		if (fns!=null) {
			numTextures=fns.length;
			Textures=new int[numTextures];
			gl.glGenTextures(numTextures, Textures, 0); // generate the texture set
			for (int i=0; i<numTextures; i++) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, Textures[i]);
				renderer.loadTexture(gl, ScalePowerof2.scalePowerof2(BitmapFactory.decodeFile(fns[i])));
			}
		}
	}
	public void setTexture(GL10 gl, int i)	{ if (numTextures!=0 & i<numTextures) gl.glBindTexture(GL10.GL_TEXTURE_2D, Textures[i]); }
	public void nextTexture(GL10 gl)		{ ++tAct; rangeIndex(); setTexture(gl,tAct);}
	public void actualTexture(GL10 gl)		{ rangeIndex(); setTexture(gl,tAct);}
	public void setMark()					{ rangeIndex(); mark=tAct; }
	public void goMark()					{ tAct=mark; rangeIndex(); }
	private void rangeIndex()				{ tAct%=numTextures;}
}
