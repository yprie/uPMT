package model;

import java.io.Serializable;

import controller.MomentExpVBox;

public class SerializedMomentVBox implements Serializable{
	private static final long serialVersionUID = 1420672609912364060L;
	private transient MomentExpVBox moment;
	public SerializedMomentVBox(MomentExpVBox m) {
		moment = m;
	}
	
	public MomentExpVBox getMomentExpVBox() {
		return moment;
	}

}
