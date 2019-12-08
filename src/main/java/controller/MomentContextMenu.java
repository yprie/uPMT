package controller;

import application.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MomentContextMenu{

	private MomentExpVBox momentView;
	private Main main;
	private ContextMenu contextMenu;
	
	public MomentContextMenu(MomentExpVBox mv, Main m) {
		momentView = mv;
		init(main.getPrimaryStage());
	}
	
	public void init(Stage stage){

        HBox root = new HBox();
        root.setPadding(new Insets(5));
        root.setSpacing(5);
 
 
        // Create ContextMenu
        contextMenu = new ContextMenu();
 
        MenuItem item1 = new MenuItem("Menu Item 1");
        item1.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                //clique sur item1
            }
        });
        MenuItem item2 = new MenuItem("Menu Item 2");
        item2.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                //clique sur item2
            }
        });
        
        Button item3 = new Button("Bt");
 
        // Add MenuItem to ContextMenu
        //contextMenu.getItems().addAll(item1, item2, item3);
 
       
 
	}
	
	public void show() {
		 contextMenu.show(momentView, momentView.getLayoutX(), momentView.getLayoutY());
	}

}
