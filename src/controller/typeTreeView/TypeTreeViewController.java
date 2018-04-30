/*****************************************************************************
 * TypeTreeViewController.java
 *****************************************************************************
 * Copyright © 2017 uPMT
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston MA 02110-1301, USA.
 *****************************************************************************/

package controller.typeTreeView;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.sun.javafx.scene.control.skin.CustomColorDialog;

import application.Main;
import controller.MomentExpVBox;
import controller.command.ChangeDateMomentCommand;
import controller.command.RenameCategorySchemeCommand;
import controller.command.RenamePropertySchemeCommand;
import controller.controller.Observable;
import controller.controller.Observer;
import controller.controller.TypeController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import model.Property;
import model.Type;
import utils.UndoCollector;

public class TypeTreeViewController implements Initializable{
	
	protected @FXML Label nomType;
	protected TypeController type;
	protected TypeTreeView tree;
	protected @FXML BorderPane typePane;
	protected @FXML MenuButton treeviewMenuAction;
	
	protected Main main;
	
	protected TextField textField;
	
	public TypeTreeViewController(TypeController type,TypeTreeView tree, Main main) {
		
		this.main = main;
		this.type = type;
		this.tree = tree;
		this.textField = new TextField();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.nomType.setText(type.getType().getName());
		
		treeviewMenuAction.getItems().clear();
        MenuItem menu1 = new MenuItem(main._langBundle.getString("rename"));
        menu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				rename();
			}
        });
        /*MenuItem menu2 = new MenuItem(main._langBundle.getString("change_color"));
        menu2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				CustomColorDialog dialog = new CustomColorDialog(main.getPrimaryStage());
				dialog.setCurrentColor(Color.web(moment.getColor()));
				dialog.setShowUseBtn(false);
		        dialog.show();
		        dialog.getDialog().centerOnScreen();
		        dialog.setOnSave(new Runnable() {
					@Override
					public void run() {
						colorPicked(dialog.getCustomColor());
					}
				});
			}
        });
        MenuItem menu3 = new MenuItem(main._langBundle.getString("add_comment"));
        menu3.setDisable(true);
        menuTime = new MenuItem(main._langBundle.getString("edit_time") + " ('"+moment.getDateString()+"')");
        menuTime.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				ButtonType edit = new ButtonType(main._langBundle.getString("edit"), ButtonData.OK_DONE);
				ButtonType close = new ButtonType(main._langBundle.getString("close"), ButtonData.CANCEL_CLOSE);
				Dialog dialog = new TextInputDialog(moment.getDateString());
				dialog.getDialogPane().getButtonTypes().clear();
				dialog.getDialogPane().getButtonTypes().addAll(edit, close);
				dialog.setTitle(main._langBundle.getString("time"));
				dialog.setHeaderText(main._langBundle.getString("time_alert") + " " + moment.getName() + " " + main._langBundle.getString("is") + " " + moment.getDateString());
				dialog.initModality(Modality.APPLICATION_MODAL);
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					try {
						ChangeDateMomentCommand cmd = new ChangeDateMomentCommand(
								result.get(),
								moment.getDateString(),
								MomentExpVBox.this,
								main);
						cmd.execute();
						UndoCollector.INSTANCE.add(cmd);
						
					}catch(Exception e){}
				}
				else {
					
				}
				//actionStatus.setText("Text entered: " + entered);
			}
        });
        //menuTime.setDisable(true);*/
        treeviewMenuAction.getItems().addAll(menu1);
	}

	public void rename() {
		textField.setText(type.getType().getName());
		textField.setMaxWidth(100);
		textField.requestFocus();
		//System.out.println("On renomme");
	}

	protected void hideButtons() {}

	protected void showButtons() {}

}