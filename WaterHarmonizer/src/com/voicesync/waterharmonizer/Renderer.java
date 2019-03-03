package com.voicesync.waterharmonizer;

import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import com.voicesync.lib.OGLRenderer;

public class Renderer extends OGLRenderer { 	
	Kaleidoscope kaleidos;
	Textures kaleText;
	Sequencer seqK=new Sequencer(-.77f,.77f,18e-4f);
	
	public Renderer(Context context, boolean hasTx) 	{ 
		super(context,R.id.glSurface, hasTx);
	}
	@Override public void drawModel(GL10 gl) {
		this.setScale(gl, 1.4f);
		float next=seqK.next();
		if (seqK.hasChanged()) 	kaleText.nextTexture(gl); // next texture in kale when seq switch
		else 					kaleText.actualTexture(gl);
		kaleidos.changeTexture(next, next);
		kaleidos.draw(gl);
	} 
	@Override public void postCreate(GL10 gl) 			{
		enableTextures(gl);
		setZoom(-1.5f); setMoveSpeed(.7f);
		initHarm(gl);
		startAnimation();
	}
	private void initHarm(GL10 gl) {
		kaleidos=new Kaleidoscope();
		kaleidos.create(gl, 12);
		kaleText=new Textures(this);
		kaleText.loadAllTexturesFromAssets(gl, "fractals");
		kaleText.actualTexture(gl);
	}
}