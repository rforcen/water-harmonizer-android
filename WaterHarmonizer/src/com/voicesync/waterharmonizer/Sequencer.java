package com.voicesync.waterharmonizer;

public class Sequencer {
	private float from, to, inc, sign, val;
	private boolean change=false;

	public Sequencer(float from, float to, float inc) {this.from=val=from; this.to=to; this.inc=inc; sign=+1;}
	public float next() {
		change=false; inc();
		if (sign==+1  & val>=to)   { sign=-1; inc(); change=true; }
		else if (sign==-1  & val<=from) { sign=+1; inc(); change=true; }
		return val;
	}
	public boolean 	hasChanged() {return change;}
	public int 		iNext() {return (int)next();}
	public int 		getival() {return (int)val;}
	public float 		getval() {return val;}
	private void 		inc() {val+=sign*inc;}
}
