/*****************************************************************************
 * ShowTextWindow.java
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

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ShowTextWindow implements Initializable{

	private String text;
	private @FXML TextArea textArea;
	private Stage window;

	public ShowTextWindow(String text) {
		this.text = text;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		textArea.setEditable(false);
	}

	public void show(){
		try {
			window = new Stage();
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/view/ShowTextWindow.fxml"));
	        loader.setController(this);
	        BorderPane mainView = (BorderPane) loader.load();

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(mainView);
	        window.setScene(scene);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}

		textArea.setText(text);
		window.show();
	}
}
