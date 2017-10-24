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
import java.util.ResourceBundle;

import controller.command.RenameClassSchemeCommand;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import model.Propriete;
import model.Type;
import utils.UndoCollector;

public class TypeTreeViewController implements Initializable{
	
	protected @FXML Label nomType;
	protected TypeController type;
	protected TypeTreeView tree;
	protected @FXML BorderPane typePane;
	protected @FXML Button rename;
	
	protected TextField textField;
	
	public TypeTreeViewController(TypeController type,TypeTreeView tree) {
		this.type = type;
		this.tree = tree;
		this.textField = new TextField();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.nomType.setText(type.getType().getName());
	}

	public void rename() {
		rename.setDisable(true);
		textField.setText(type.getType().getName());
		textField.setMaxWidth(100);
		textField.requestFocus();
	}

	protected void hideButtons() {}

	protected void showButtons() {}

}
