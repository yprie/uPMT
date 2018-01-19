/*****************************************************************************
 * MainViewController.java
 *****************************************************************************
 * Copyright � 2017 uPMT
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Classe;
import model.DescriptionEntretien;
import model.Enregistrement;
import model.MomentExperience;
import model.Propriete;
import model.Type;
import utils.MainViewTransformations;
import utils.SchemaTransformations;
import utils.UndoCollector;
import utils.Utils;

public class MainViewController implements Initializable, Observer {

	private @FXML TreeView<TypeController> treeViewSchema;
	private @FXML TreeView<DescriptionEntretien> treeViewInterview;
	private @FXML ImageView ajoutMomentButton;

	private @FXML Button choixExtrait;
	private @FXML Button ajoutExtrait;
	private @FXML Button ajoutMomentSimilaire;
	
	private @FXML ScrollPane gridScrollPane;
	
	private HashMap<DescriptionEntretien, GridPane> interviewsPane;
	
	// Inspector
	private @FXML Label nomMoment;
//	private @FXML TextField debut;
//	private @FXML TextField fin;
	private @FXML TextField duree;
	private @FXML TextArea extraitEntretien;
	private @FXML ScrollPane inspector_pane;
	private @FXML Button closeBtn;
	private @FXML VBox vboxInspecteur;
	private @FXML ColorPicker couleurMoment;
	private @FXML FlowPane typesArea;
	private @FXML Button showExtractButton;
//	private @FXML Button editName;
	private @FXML HBox vBoxLabel;
	private @FXML SplitPane mainSplitPane;
	
	
	Main main;

	public MainViewController(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
		this.interviewsPane = new HashMap<DescriptionEntretien, GridPane>();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		this.setLabelChangeName(main,this);
		mainSplitPane.setDividerPosition(0,0.15);
		mainSplitPane.setDividerPosition(1,0.85);
		
		File fileImage = new File("./img/momentIcon.png");
		Image image = new Image(fileImage.toURI().toString());
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
				.add(SchemaTransformations.EntretienToTreeView(main.getCurrentProject().getEntretiens()));
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
		
		
		initInspector();
	}
	
	private void setLabelChangeName(Main main,MainViewController thiss){
		nomMoment.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				if(arg0.getClickCount() == 2){
					System.out.println("DoubleClick");
					editNameMode();
				}
			}
		});
	}
	
	//modifier 
	private void editNameMode() {
		TextField t = new TextField();
		t.setText(main.getCurrentMoment().getMoment().getNom());
		t.requestFocus();
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	//Le Textfield demande le Focus
            	t.requestFocus();
            	//Si le text n'est pas vide, on selectionne tout.
            	if(!t.getText().isEmpty())
            		t.selectAll();
            }
        });
		
		ChangeListener<Boolean>	 listener = new ChangeListener<Boolean>() {
			 @Override
			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
			    {
			        if (!newPropertyValue)
			        {
			        	RenameMomentCommand cmd = new RenameMomentCommand(main.getCurrentMoment().getMomentNameController(),
								main.getCurrentMoment().getMoment().getNom(),t.getText());
						cmd.execute();
						UndoCollector.INSTANCE.add(cmd);
						vBoxLabel.getChildren().remove(t);
						vBoxLabel.getChildren().add(0,nomMoment);
						t.focusedProperty().removeListener(this);
			        }
			    }
		};
		t.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.ENTER){
					t.setText(t.getText());
					vBoxLabel.getChildren().remove(t);
				}
				if(event.getCode() == KeyCode.ESCAPE){
					vBoxLabel.getChildren().remove(t);
					vBoxLabel.getChildren().add(0, nomMoment);
				}
			}
		});
		t.focusedProperty().addListener(listener);
//		Platform.runLater(()->t.requestFocus());
//		Platform.runLater(()->t.selectAll());
		vBoxLabel.getChildren().add(0, t);
		vBoxLabel.getChildren().remove(nomMoment);
	}
	
//	@FXML
//	public void editNameLabel() {
//		TextField t = new TextField();
//		t.setText(main.getCurrentMoment().getMoment().getNom());
//		
//		Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//            	//Le Textfield demande le Focus
//            	t.requestFocus();
//            	//Si le text n'est pas vide, on selectionne tout.
//            	if(!t.getText().isEmpty())
//            		t.selectAll();
//            }
//        });
//		
//		ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
//			 @Override
//			    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
//			    {
//			        if (!newPropertyValue)
//			        {
//			        	RenameMomentCommand cmd = new RenameMomentCommand(main.getCurrentMoment().getMomentNameController(),
//						main.getCurrentMoment().getMoment().getNom(),t.getText());
//						cmd.execute();
//						UndoCollector.INSTANCE.add(cmd);
//						vBoxLabel.getChildren().remove(t);
//						vBoxLabel.getChildren().add(0,nomMoment);
//						editName.setDisable(false);
//						t.focusedProperty().removeListener(this);
//			        }
//			    }
//		};
//		t.setOnKeyPressed(new EventHandler<KeyEvent>() {
//
//			@Override
//			public void handle(KeyEvent event) {
//				if (event.getCode() == KeyCode.ENTER) {
//					t.setText(t.getText());
//					vBoxLabel.getChildren().remove(t);
//				}
//				if (event.getCode() == KeyCode.ESCAPE) {
//					vBoxLabel.getChildren().remove(t);
//					vBoxLabel.getChildren().add(0, nomMoment);
//					editName.setDisable(false);
//
//				}
//			}
//		});
//		t.focusedProperty().addListener(listener);
//		vBoxLabel.getChildren().add(0, t);
//		vBoxLabel.getChildren().remove(nomMoment);
//		editName.setDisable(true);
//	}
	
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
	
	public void setNameInsepctor(String name){
		this.nomMoment.setText(name);
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

	private void initInspector() {

		inspector_pane.setManaged(false);
		inspector_pane.setVisible(false);
		this.extraitEntretien.setEditable(false);
		this.extraitEntretien.setText("");
		extraitEntretien.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			main.getCurrentMoment().getMoment().setMorceauDescripteme(newValue);

		});
//		debut.textProperty().addListener((observable, oldValue, newValue) -> {
//			System.out.println("debut changed from " + oldValue + " to " + newValue);
//			main.getCurrentMoment().getMoment().setDebut(newValue);
//
//		});
//		fin.textProperty().addListener((observable, oldValue, newValue) -> {
//			System.out.println("fin changed from " + oldValue + " to " + newValue);
//			main.getCurrentMoment().getMoment().setFin(newValue);
//		});
		duree.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("duree changed from " + oldValue + " to " + newValue);
			main.getCurrentMoment().getMoment().setDuree(newValue);

		});

	}

	public void renderInspector() {
		openInspector();
//		File image = new File("./img/rename.png");
//		Node icon = new ImageView(new Image(image.toURI().toString()));
//		this.editName.setGraphic(icon);
		
		File image2 = new File("./img/close.gif");
		Node icon2 = new ImageView(new Image(image2.toURI().toString()));
		this.closeBtn.setGraphic(icon2);
		
		File image3 = new File("./img/show.png");
		Node icon3 = new ImageView(new Image(image3.toURI().toString()));
		this.showExtractButton.setGraphic(icon3);
		
		// setting the value of the color Picker
		System.out.println(main.getCurrentMoment().getMoment().getCouleur());

		if (main.getCurrentMoment().getMoment().getCouleur() != null) {
			couleurMoment.setValue(Color.valueOf(main.getCurrentMoment().getMoment().getCouleur()));
		} else {
			couleurMoment.setValue(Color.WHITE);
		}

		MomentExperience currentMoment = main.getCurrentMoment().getMoment();
		nomMoment.setText(currentMoment.getNom());
//		debut.setText(currentMoment.getDebut());
//		fin.setText(currentMoment.getFin());
		duree.setText(currentMoment.getDuree());
		extraitEntretien.setText(currentMoment.getMorceauDescripteme());

		typesArea.getChildren().clear();
		loadInspectorType();
	}
	
	private Classe duplicate(Classe c){
		Classe newc = new Classe(c.getName());
		newc.setCouleur(c.getCouleur());
		for(Type t : c.getTypes()){
			newc.addType(t);
		}
		return newc;
	}

	public void loadInspectorType() {
	
	for (Type t : main.getCurrentMoment().getMoment().getType()) {
			Classe dup = (Classe) t;
			TypeClassRepresentationController elementPane = new TypeClassRepresentationController((Classe) dup,main.getCurrentMoment(),main);
			MainViewTransformations.addTypeListener(elementPane, main.getCurrentMoment(), (Type) dup, main);
			typesArea.getChildren().add(elementPane);
		}
	}

	public void openInspector() {
		inspector_pane.setManaged(true);
		inspector_pane.setVisible(true);
	}

	public void closeInspector() {
		inspector_pane.setManaged(false);
		inspector_pane.setVisible(false);
	}

	public boolean isInspectorOpen() {
		return inspector_pane.isVisible();
	}

	// function called by the button to choose the extract
	@FXML
	public void pickExtract() {
		Stage promptWindow = new Stage();
		promptWindow.setTitle("Selection de l'extrait");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/SelectDescriptemePart.fxml"));
			loader.setController(new SelectDescriptemePartController(main, promptWindow, this.extraitEntretien, Enregistrement.MOMENT));
			loader.setResources(main._langBundle);
			BorderPane layout = (BorderPane) loader.load();
			Scene launchingScene = new Scene(layout);
			promptWindow.setScene(launchingScene);
			promptWindow.show();

		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	@FXML
	public void pickExtractAttribute() {
		/*Stage promptWindow = new Stage();
		promptWindow.setTitle("Selection de l'extrait");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/SelectDescriptemePart.fxml"));
			loader.setController(new SelectDescriptemePartController(main, promptWindow, this.extraitEntretien));
			loader.setResources(main._langBundle);
			BorderPane layout = (BorderPane) loader.load();
			Scene launchingScene = new Scene(layout);
			promptWindow.setScene(launchingScene);
			promptWindow.show();

		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}*/
	}

	// function used to select a color for the moment
	@FXML
	public void pickColor() {
		Color couleur = couleurMoment.getValue();
		String colorString = Utils.toRGBCode(couleur);
		ChangeColorMomentCommand cmd = new ChangeColorMomentCommand(main.getCurrentMoment().getMomentColorController(),
				main.getCurrentMoment().getMoment().getCouleur(),colorString);
		cmd.execute();
		UndoCollector.INSTANCE.add(cmd);
		main.getCurrentMoment().getMomentColorController().update(colorString);
	}



	@FXML
	public void showExtract() {
		try {
			if (!extraitEntretien.getText().isEmpty()) {
				ShowTextWindow win = new ShowTextWindow(extraitEntretien.getText());
				win.show();
			}
		} catch (Exception e) {
			System.out.println("No DATA to display");
		}
	}

	@Override
	public void updateVue(Observable obs, Object value) {
		// TODO Auto-generated method stub
		if(obs.getClass().equals(new MomentNameController(null).getClass())) {
			nomMoment.setText((String) value);
		}
		if(obs.getClass().equals(new MomentColorController(null).getClass())) {
			couleurMoment.setValue(Color.valueOf((String) value));
		}
		if(obs.getClass().equals(new MomentExtractController(null).getClass())) {
			this.extraitEntretien.setText((String) value);
		}
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
    		System.out.println("IL SEST PASSE UN TRUC");
    	    alert.close();
    	}
	}

}
