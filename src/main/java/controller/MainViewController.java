/*****************************************************************************
 * MainViewController.java
 *****************************************************************************
 * Copyright 锟 2017 uPMT
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.lang.Object;

import Project.Models.Project;
import Project.Persistency.ProjectLoader;
import Project.Persistency.ProjectSaver;
import SchemaTree.Cell.Models.SchemaCategory;
import SchemaTree.Cell.Models.SchemaFolder;
import SchemaTree.Cell.Models.SchemaProperty;
import SchemaTree.Cell.Models.SchemaTreeRoot;
import SchemaTree.SchemaTree;
import javafx.application.Platform;
import application.Main;
import controller.controller.Observer;
import controller.controller.TypeController;
import controller.controller.Observable;
import controller.interviewTreeView.InterviewTreeView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import model.DescriptionInterview;
import utils.MainViewTransformations;
import utils.ResourceLoader;
import utils.SchemaTransformations;
import utils.Utils;

public class MainViewController implements Initializable, Observer {

	private @FXML SplitPane mainSplitPane;
	private @FXML SchemaTree schemaTree;
	private @FXML TreeView<DescriptionInterview> treeViewInterview;
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
	
	private HashMap<DescriptionInterview, GridPane> interviewsPane;
	

	
	
	Main main;

	public MainViewController(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
		this.interviewsPane = new HashMap<DescriptionInterview, GridPane>();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = ResourceLoader.loadImage("momentIcon.png");
		this.ajoutMomentButton.setImage(image);
		
		if (!main.getCurrentProject().getInterviews().isEmpty()) {
			main.setCurrentDescription(main.getCurrentProject().getInterviews().getLast());
			// get the last one
		}

		treeViewInterview.setEditable(true);
		treeViewInterview.setCellFactory((TreeView<DescriptionInterview> t) -> new InterviewTreeView(main));

		TreeItem<DescriptionInterview> Interviewroot;
		Interviewroot = new TreeItem<DescriptionInterview>();
		Interviewroot.getChildren()
				.add(SchemaTransformations.EntretienToTreeView(main.getCurrentProject().getInterviews()));

		treeViewInterview.setRoot(Interviewroot);
		treeViewInterview.setShowRoot(false);
		main.setTreeViewInterview(treeViewInterview);

		//Testing code for schema !
		SchemaTreeRoot root = new SchemaTreeRoot("Racine !");
		SchemaFolder f = new SchemaFolder("SuperFolder");
		SchemaProperty prop = new SchemaProperty("property");
		SchemaCategory c = new SchemaCategory("c");
		c.addChild(prop);
		f.addChild(c);
		root.addChild(f);
		schemaTree.setTreeRoot(root);
		//End of testing code !


		if (main.getCurrentDescription() != null) {
			// Give time to end initializing the scheme on the left
			
			Platform.runLater(new Runnable() {
                @Override public void run() {
                	for(DescriptionInterview d : main.getCurrentProject().getInterviews()) {
                		
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


		//Quand on clique sur la panel qui s'est mit par dessus le texte, on l'enleve pour  nouveau rendre le texte selectionnable
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
				if (main.getCurrentDescription() != null) {
					Dragboard db = ajoutMomentButton.startDragAndDrop(TransferMode.ANY);
					ClipboardContent content = new ClipboardContent();
					content.putString("dragDescripteme");
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
		textInterviewTitle.setText(main.getCurrentDescription().getName());
		droppableText.setText(text);
	}
	
	public void updateGrid() {
		gridScrollPane.setContent((interviewsPane.get(main.getCurrentDescription())));
		main.setGrid(interviewsPane.get(main.getCurrentDescription()));
	}
	
	public void addGridPaneInterview(DescriptionInterview d) {
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

	@Override
	public void updateVue(Observable obs, Object value) {
		// TODO Auto-generated method stub
	}

}
