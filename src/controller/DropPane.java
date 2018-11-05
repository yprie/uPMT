package controller;

import application.Main;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DropPane extends StackPane{
	
	private MomentExpVBox parent=null;
	private int col=0;
	public static int PANELSIZE = 5;
	public static int DRAGGEDPANELSIZE = 120;
	public Pane mPane;
	public BorderPane mTextContainer;
	public Text mText;
	public Main main;
	
	public DropPane(int c, int initsize, Main main) {
		super();
		this.main = main;
		this.setAlignment(Pos.TOP_CENTER);
		mPane=new Pane();
		mTextContainer= new BorderPane();
		mText=new Text();
		mText.setText(main._langBundle.getString("add_moment_here"));
		mText.setStyle("-fx-fill:white;");
		mText.setTextAlignment(TextAlignment.CENTER);
		mTextContainer.setCenter(mText);
		onDragExited();
		this.getChildren().clear();
		this.getChildren().add(mPane);
		col = c;
		this.setPaneWidth(PANELSIZE);
		this.setPaneHeight(210);
		this.setStyle("-fx-padding:13 5 0 5;");
	}
	
	public DropPane(MomentExpVBox p, int c, int initsize, Main main) {
		this(c, initsize, main);
		parent = p;
	}
	
	public void onDragOver(String movement) {
		this.getChildren().clear();
		if(movement.equals("ajoutMoment") || movement.equals("dragDescripteme")) {
			mText.setText(main._langBundle.getString("add_moment_here"));
		}
		else if(movement.equals("moveMoment")) {
			mText.setText(main._langBundle.getString("move_moment_here"));
		} else{
			mText.setText("?");
		}
		this.getChildren().addAll(mPane,mTextContainer);
		mPane.setStyle(
				"-fx-background-color:black;"
			+ 	"-fx-opacity:0.6;"
			+ 	"-fx-background-radius: 10;");
		this.setPaneWidth(DRAGGEDPANELSIZE);
	}
	
	public void onDragExited() {
		this.getChildren().clear();
		this.getChildren().add(mPane);
		mPane.setStyle(
				"-fx-background-color:transparent;"
			+	"-fx-padding:0px;"
			+ 	"-fx-opacity:0,6;"
			+ 	"-fx-background-radius: 10;");
		this.setPaneWidth(PANELSIZE);
	}
	
	protected void setPaneWidth(double size) {
		mPane.setMinWidth(size);
		mPane.setMaxWidth(size);
		mPane.setPrefWidth(size);
		mTextContainer.setMinWidth(size);
		mTextContainer.setMaxWidth(size);
		mTextContainer.setPrefWidth(size);
		mText.prefWidth(size);
		mText.maxWidth(size);
		mText.minWidth(size);
		mText.setWrappingWidth(size);
		//mPane.setWidth(size);
	}
	
	protected void setPaneHeight(double size) {
		mPane.setMinHeight(size);
		mPane.setMaxHeight(size);
		mPane.setPrefHeight(size);
		mTextContainer.setMinHeight(size);
		mTextContainer.setMaxHeight(size);
		mTextContainer.setPrefHeight(size);
		//this.setHeight(size);
	}
	
	public void emptyProjectPane() {
		this.getChildren().clear();
		mText.setText(main._langBundle.getString("add_moment_here_empty_project"));
		this.getChildren().addAll(mPane,mTextContainer);
		mPane.setStyle(
				"-fx-background-color:black;"
			+ 	"-fx-opacity:0.6;"
			+ 	"-fx-background-radius: 10;");

		this.setPaneWidth(DRAGGEDPANELSIZE);
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
