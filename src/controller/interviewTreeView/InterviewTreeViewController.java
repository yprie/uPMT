/*****************************************************************************
 * InterviewTreeViewController.java
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

package controller.interviewTreeView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import controller.ShowTextWindow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import model.DescriptionEntretien;
import utils.ResourceLoader;

public class InterviewTreeViewController extends TreeViewController implements Initializable {
	
	private @FXML Button afficherDescritpeme;
	private @FXML Button renameInterview;
	private @FXML Button deleteInterview;
	private @FXML Label nomEntretien;
	private @FXML BorderPane interviewPane;
	private DescriptionEntretien interview;
	private InterviewTreeView interviewTreeView;

	public InterviewTreeViewController(DescriptionEntretien interview, InterviewTreeView interviewTreeView) {
		this.interview = interview;
		this.interviewTreeView = interviewTreeView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomEntretien.setText(interview.getNom());
		

		Node icon = new ImageView(ResourceLoader.loadImage("show.png"));
		this.afficherDescritpeme.setGraphic(icon);

		Node iconDel = new ImageView(ResourceLoader.loadImage("delete.gif"));
		this.deleteInterview.setGraphic(iconDel);
		
		Node iconRename = new ImageView(ResourceLoader.loadImage("rename.png"));
		this.renameInterview.setGraphic(iconRename);
		
		Tooltip affDescrip = new Tooltip("Afficher le Verbatim de l'entretien");
		afficherDescritpeme.setTooltip(affDescrip);
		
		Tooltip rename = new Tooltip("Renommer l'entretien");
		renameInterview.setTooltip(rename);
		
		Tooltip delete = new Tooltip("Supprimer l'entretien");
		deleteInterview.setTooltip(delete);
		
		hideButtons();
	}
	
	public void setSelected(String couleur){
		this.nomEntretien.setTextFill(Paint.valueOf(couleur));
		this.nomEntretien.setStyle("-fx-font-weight: bold;");
	}
	
	public void showInterview(){
		ShowTextWindow t = new ShowTextWindow(interview.getDescripteme().getTexte());
		t.show();
	}
	
	public void renameInter(){
		renameInterview.setDisable(true);
		TextField textField = new TextField();
		textField.setText(interview.getNom());
		textField.setMaxWidth(100);
		textField.requestFocus();
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			 @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue)
			        {
			        	nomEntretien.setText(textField.getText());
			        	interview.setNom(textField.getText());
						interviewPane.setLeft(nomEntretien);
						renameInterview.setDisable(false);
			        }
			    }
		});
		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					nomEntretien.setText(textField.getText());
					interview.setNom(textField.getText());
					interviewPane.setLeft(nomEntretien);
					renameInterview.setDisable(true);
				}
				if(event.getCode() == KeyCode.ESCAPE){
					interviewPane.setLeft(nomEntretien);
					renameInterview.setDisable(true);
				}
			}
		});
		interviewPane.setLeft(textField);
		Platform.runLater(()->textField.requestFocus());
		Platform.runLater(()->textField.selectAll());
		
	}
	
	public void deleteInter(){
		this.interviewTreeView.deleteInterview(interview);
	}
	
	public void hideButtons(){
		this.afficherDescritpeme.setVisible(false);
		this.deleteInterview.setVisible(false);
		this.renameInterview.setVisible(false);
	}

	public void showButtons() {
		this.afficherDescritpeme.setVisible(true);
		this.deleteInterview.setVisible(true);
		this.renameInterview.setVisible(true);

	}
}
