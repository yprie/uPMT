package model;

import java.io.Serializable;

import controller.MomentExpVBox;

public class SerializedMomentVBox implements Serializable{
	private static final long serialVersionUID = 1420672609912364060L;
	private transient MomentExperience moment;
	public SerializedMomentVBox(MomentExperience m) {
		moment = m;
	}
	
	public MomentExperience getMomentExpVBox() {
		return moment;
	}

}
