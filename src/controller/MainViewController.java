/*****************************************************************************
 * MainViewController.java
 *****************************************************************************
 * Copyright é”Ÿï¿½ 2017 uPMT
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.lang.Object;

import javafx.application.Platform;
import application.Main;
import controller.command.ChangeColorMomentCommand;
import controller.command.RemoveTypeCommand;
import controller.command.RenameMomentCommand;
import controller.controller.MomentAddTypeController;
import controller.controller.MomentColorController;
import controller.controller.MomentExtractController;
import controller.controller.MomentNameController;
import controller.controller.MomentRemoveTypeController;
import controller.controller.Observer;
import controller.controller.TypeController;
import controller.controller.Observable;
import controller.interviewTreeView.InterviewTreeView;
import controller.typeTreeView.TypeTreeView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.Classe;
import model.DescriptionEntretien;
import model.Enregistrement;
import model.MomentExperience;
import model.Propriete;
import model.Type;
import utils.MainViewTransformations;
import utils.ResourceLoader;
import utils.SchemaTransformations;
import utils.UndoCollector;
import utils.Utils;

public class MainViewController implements Initializable, Observer {

	private @FXML SplitPane mainSplitPane;
	private @FXML TreeView<TypeController> treeViewSchema;
	private @FXML TreeView<DescriptionEntretien> treeViewInterview;
	private @FXML ImageView ajoutMomentButton;
	private @FXML BorderPane topBarContainerTextInterview;

	private @FXML Button choixExtrait;
	private @FXML Button ajoutExtrait;
	private @FXML Button ajoutMomentSimilaire;
	private @FXML TextArea droppableText;
	private @FXML BorderPane paneOfTextArea;
	private @FXML StackPane stackForDragDrop;
	private @FXML ImageView buttonTextInterview;
	private @FXML Text textInterviewTitle;
	private Pane paneDragText;
	
	private @FXML ScrollPane gridScrollPane;
	
	private HashMap<DescriptionEntretien, GridPane> interviewsPane;
	

	
	
	Main main;

	public MainViewController(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
		this.interviewsPane = new HashMap<DescriptionEntretien, GridPane>();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = ResourceLoader.loadImage("momentIcon.png");
		this.ajoutMomentButton.setImage(image);

		if (!main.getCurrentProject().getEntretiens().isEmpty()) {
			main.setCurrentDescription(main.getCurrentProject().getEntretiens().getFirst());
		}

		treeViewSchema.setEditable(true);

		treeViewInterview.setEditable(true);

		treeViewSchema.setCellFactory((TreeView<TypeController> t) -> new TypeTreeView(main));
		treeViewInterview.setCellFactory((TreeView<DescriptionEntretien> t) -> new InterviewTreeView(main));

		TreeItem<TypeController> Schemaroot;
		TreeItem<DescriptionEntretien> Interviewroot;
		Schemaroot = new TreeItem<TypeController>();
		Interviewroot = new TreeItem<DescriptionEntretien>();

		Schemaroot.getChildren()
				.add(SchemaTransformations.SchemaToTreeView(main.getCurrentProject().getSchemaProjet()));
		Interviewroot.getChildren()
				.add(SchemaTransformations.EntretienToTreeView(main, main.getCurrentProject().getEntretiens()));
		treeViewSchema.setRoot(Schemaroot);
		treeViewSchema.setShowRoot(false);
		treeViewInterview.setRoot(Interviewroot);
		treeViewInterview.setShowRoot(false);

		main.setTreeViewSchema(treeViewSchema);
		main.setTreeViewInterview(treeViewInterview);
		
		if (main.getCurrentDescription() != null) {
			// Give time to end initializing the scheme on the left
			Platform.runLater(new Runnable() {
                @Override public void run() {
                	for(DescriptionEntretien d : main.getCurrentProject().getEntretiens()) {
                		
                		GridPane gp = new GridPane();
                		gp.setMinHeight(200);
                		gp.setPadding(new Insets(100, 0, 0, 0));
                		addLinesToGrid(gp);
                		main.setGrid(gp);
                		MainViewTransformations.loadGridData(gp, main, d);
                		interviewsPane.put(d, gp);
                	}
                	updateGrid();
                }
            });
		}

		ajoutMomentButton.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (main.getCurrentDescription() != null) {
					Dragboard db = ajoutMomentButton.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString("ajoutMoment");
					content.putRtf("Moment Vide");
					db.setContent(content);
				}
			}
		});
		if(main.getCurrentDescription()!=null)
			setDroppableText(main.getCurrentDescription().getDescripteme().getTexte().trim());
		paneDragText = new Pane();
		paneDragText.setStyle("-fx-background-color:#f4f4f4;");
		paneDragText.setCursor(Cursor.MOVE);
		paneDragText.setOpacity(0.2);
		
		//Quand on selectionne du texte, on met un panel devant le text pour pouvoir le dragger
		droppableText.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		    	if(!droppableText.getSelectedText().equals("") && !droppableText.getSelectedText().equals(" ") && !droppableText.getSelectedText().equals("\n"))
		    		stackForDragDrop.getChildren().add(paneDragText);
		    }
		});


		//Quand on clique sur la panel qui s'est mit par dessus le texte, on l'enleve pour à nouveau rendre le texte selectionnable
		paneDragText.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				droppableText.deselect();
				stackForDragDrop.getChildren().clear();
				stackForDragDrop.getChildren().add(droppableText);
			}
		});
		//Quand on drag le panel qui s'est mit par dessus le text
		paneDragText.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (main.getCurrentDescription() != null) {
					Dragboard db = ajoutMomentButton.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString("ajoutMoment");
					content.put(DataFormat.HTML, droppableText.getSelectedText());
					db.setContent(content);
				}
			}
		});
		buttonTextInterview.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(droppableText.isDisabled()) {
					buttonTextInterview.setImage(ResourceLoader.loadImage("closeMenuBlack.png"));
					droppableText.setDisable(false);
					mainSplitPane.setDividerPosition(1, splitPos);
					stackForDragDrop.getChildren().add(droppableText);
					topBarContainerTextInterview.setCenter(textInterviewTitle);
					paneOfTextArea.setMaxWidth(paneOfTextArea.USE_COMPUTED_SIZE);
				}else {
					buttonTextInterview.setImage(ResourceLoader.loadImage("openMenuBlack.png"));
					droppableText.deselect();
					droppableText.setDisable(true);
					splitPos = mainSplitPane.getDividers().get(1).getPosition();
					mainSplitPane.setDividerPosition(1, 1);
					stackForDragDrop.getChildren().clear();
					topBarContainerTextInterview.setCenter(null);
					paneOfTextArea.setMaxWidth(buttonTextInterview.getFitWidth());
				}
			}
		});
		
	}
	
	private double splitPos =0;
	
	public void setDroppableText(String text) {
		//droppableText.setText(main.getCurrentDescription().getDescripteme().getTexte().trim());
		droppableText.setText(text);
	}
	
	public void updateGrid() {
		gridScrollPane.setContent((interviewsPane.get(main.getCurrentDescription())));
		main.setGrid(interviewsPane.get(main.getCurrentDescription()));
	}
	
	public void addGridPaneInterview(DescriptionEntretien d) {
		GridPane gp = new GridPane();
		gp.setMinHeight(200);
		gp.setPadding(new Insets(100, 0, 0, 0));
		addLinesToGrid(gp);
		main.setGrid(gp);
		MainViewTransformations.loadGridData(gp, main, d);
		interviewsPane.put(d, gp);
	}
	
	public void addLinesToGrid(GridPane g) {

		g.setOnDragEntered(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* the drag-and-drop gesture entered the target */
				/* show to the user that it is an actual gesture target */
				g.setGridLinesVisible(true);
				event.consume();
			}
		});

		g.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data dropped */
				g.setGridLinesVisible(false);
				event.consume();
			}
		});

		g.setOnDragExited(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				/* data dropped */
				g.setGridLinesVisible(false);
				event.consume();
			}
		});
	}

	
	private Classe duplicate(Classe c){
		Classe newc = new Classe(c.getName());
		newc.setCouleur(c.getCouleur());
		for(Type t : c.getTypes()){
			newc.addType(t);
		}
		return newc;
	}



	


	@Override
	public void updateVue(Observable obs, Object value) {
		// TODO Auto-generated method stub
	}
	
	public void alertRecovery(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	/*alert.setTitle(main._langBundle.getString("recovery"));
    	alert.setHeaderText(main._langBundle.getString("recovery_alarm"));*/
		alert.setTitle("TRADUCTION NEEDED: Recovery");
    	alert.setHeaderText("TRADUCTION NEEDED: We detected that the application did not close properly. Do you want to recover a copy of the project that was interrupted?");
    	ButtonType buttonTypeOne = new ButtonType(main._langBundle.getString("ok"));
    	ButtonType buttonTypeTwo = new ButtonType(main._langBundle.getString("no"));
    	
    	alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == buttonTypeOne){
    		Utils.replaceRecovery();
    		alert.close();
    	} else if (result.get() == buttonTypeTwo) {
    		Utils.deleteRecovery();
    		alert.close();
    	} else{
    		//System.out.println("IL SEST PASSE UN TRUC");
    	    alert.close();
    	}
	}

	public String getDroppableText() {
		if(droppableText==null) return null;
		else return this.droppableText.getText();
	}

}
