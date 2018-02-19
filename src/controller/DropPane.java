package controller;

import javafx.scene.layout.Pane;

public class DropPane extends Pane{
	
	private MomentExpVBox parent=null;
	private int col=0;
	
	public DropPane(int c) {
		super();
		col = c;
	}
	
	public DropPane(MomentExpVBox p, int c) {
		this(c);
		parent = p;
	}
	
	public MomentExpVBox getMomentParent() {
		return parent;
	}
	
	public int getCol() {
		return col;
	}
	
	public boolean hasMomentParent() {
		return (parent!=null);
	}
}
