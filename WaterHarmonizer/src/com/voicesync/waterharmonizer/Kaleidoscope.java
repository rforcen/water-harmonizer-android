package com.voicesync.waterharmonizer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;
import android.util.FloatMath;

class Kaleidoscope { //usage: new, loadTexture, create, draw, changeTexture(x,y)
	public boolean changeTexture=false;
	float tx, ty;
	int segments=12;
	float pi=(float)Math.PI, twoPi=pi*2;
	Bitmap bmp=null;

	float[]defaultTexture=new float[]{0,0, 1,0, 1,1, 0,1}, trigTexture=defaultTexture;
	float angR, r2d=180/pi, cosR, sinR;
	int nTrigs;
	int type=0; // 0=circle, 1=trigs, 2=mosaic

	Kaleidoscope() {}
	void changeTexture(float x, float y) {
		if (x>1) x-=(int)x; 
		if (y>1) y-=(int)y;
		switch (type) {
		case 0:	changeTextureCircle(x,y); break;
		case 1:	changeTextureTrigs(x,y); break;
		case 2:changeTextureMosaic(x,y); break;
		}
	}
	void changeTexture(Bitmap bmp) {
		this.bmp=bmp;
		changeTexture=true;
	}
	void changeTextureCircle(float x, float y) {
		trigTexture[0]=trigTexture[6]=x;
		trigTexture[1]=trigTexture[3]=y;
		changeTexture=true;
	}
	void changeTextureTrigs(float x, float y) {
		for (int ix=0; ix<trigTexture.length; ) {
			trigTexture[ix++]=0;trigTexture[ix++]=1; trigTexture[ix++]=1;trigTexture[ix++]=1; trigTexture[ix++]=x;trigTexture[ix++]=y; //01 11 xy     
			trigTexture[ix++]=x;trigTexture[ix++]=y; trigTexture[ix++]=0;trigTexture[ix++]=1; trigTexture[ix++]=1;trigTexture[ix++]=1; //xy 01 11
		}
		changeTexture=true;
	}
	void changeTextureMosaic(float x, float y) {
		changeTextureCircle(x, y);
	}
	void setTexture(GL10 gl) {
		if (changeTexture) {
			if (bmp!=null) loadTexture(gl, bmp);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, putCoords(trigTexture));
			bmp=null;
		}
		changeTexture=false;
	}
	void loadTexture(GL10 gl, Bitmap bmp) {
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,  GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterx(GL10.GL_TEXTURE_2D,  GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		bmp.recycle();
	}
	void create(GL10 gl, int segments) { 
		switch (type) {
		case 0: nTrigs=createCircle(gl,	segments);	break; 
		case 1: nTrigs=createTrigs(gl, 	segments);	break;
		case 2: nTrigs=createMosaic(gl,	segments); 	break;
		}
	}
	void setType(int type) {this.type=type;}
	void draw(GL10 gl) 	{ 
		setTexture(gl);
		switch (type) {
		case 0:	drawCircle(gl); break;
		case 1:	drawTrigs(gl);	break;
		case 2:	drawMosaic(gl); break;
		}
	}
	int createMosaic(GL10 gl, int segments) {
		createCircle(gl,6);
		return 0;
	}
	void drawMosaic(GL10 gl) { // draw Kale in a circle
		float zoom=8f;
		float mx=zoom+2, my=mx, dy=0;
		for (float x=-mx; x<mx; x+=1+cosR) {
			for (float y=-my; y<my; y+=2*sinR) {
				gl.glLoadIdentity(); gl.glTranslatef(x, y+dy, -zoom);
				drawCircle(gl);
			}
			dy=(dy==0) ? sinR:0;
		}
	}
	int createCircle(GL10 gl, int segments) {
		this.segments=segments;
		// create the segments mask triangle (0,0) - (1,0) - (sin(a), cos(a)
		angR=(float)(twoPi/segments); // angle=2*pi/segs
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, putCoords(new float[]{0,0, 1,0, cosR=FloatMath.cos(angR), sinR=FloatMath.sin(angR)}));
		// assign texture coords
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, putCoords(trigTexture));
		return 3;
	}
	void drawCircle(GL10 gl) { // draw Kale in a circle
		setTexture(gl);
		for (int j=0; j<2; j++) {
			for (int i=0; i<segments/2; i++) {
				gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
				gl.glRotatef(angR*2*r2d, 0, 0, 1);
			}
			gl.glRotatef(180, 1, 0, 0);
		}
	}
	int createTrigs(GL10 gl, int segments) {
		float tx=4; // top left
		this.segments=segments;
		angR=(float)(twoPi/segments); // angle=2*pi/segs
		float l=FloatMath.cos(angR/2), l2=l*l, d=2*FloatMath.sqrt(1-l2), d2=d/2, d32=d+d2;
		int nv=0;
		for (float i=-tx; i<tx; i+=d) for (float j=-tx; j<tx; j+=1) nv+=12; 
		float[]vx =new float[nv]; int iv=0;
		trigTexture=new float[nv]; int ix=0;
		for (float i=-tx; i<tx; i+=d) {
			for (float j=-tx; j<tx; j+=1) {
				vx[iv++]=i;vx[iv++]=j; 		vx[iv++]=i+d;vx[iv++]=j; 		vx[iv++]=i+d2;vx[iv++]=j+1;	// trig 1
				vx[iv++]=i+d;vx[iv++]=j; 	vx[iv++]=i+d32;vx[iv++]=j+1; 	vx[iv++]=i+d2;vx[iv++]=j+1;	// trig inv

				trigTexture[ix++]=0;trigTexture[ix++]=1; trigTexture[ix++]=1;trigTexture[ix++]=1; trigTexture[ix++]=0;trigTexture[ix++]=0; //01 11 00     
				trigTexture[ix++]=0;trigTexture[ix++]=0; trigTexture[ix++]=0;trigTexture[ix++]=1; trigTexture[ix++]=1;trigTexture[ix++]=1; //00 01 11
			}
		}
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, putCoords(vx));
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, putCoords(trigTexture));
		return iv/2;
	}
	void drawTrigs(GL10 gl) { // draw the trigs pattern
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, nTrigs);
	}

	// assign a set of (float) coords to a FloatBuffer
	private FloatBuffer putCoords(float []_coords)
	{
		FloatBuffer pntBuff;
		ByteBuffer vbb = ByteBuffer.allocateDirect(_coords.length*4);
		vbb.order(ByteOrder.nativeOrder());
		pntBuff = vbb.asFloatBuffer();
		pntBuff.put(_coords);
		pntBuff.position(0);
		return pntBuff;
	}
}