package controller;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import application.Main;
import controller.command.ChangeExtractMomentCommand;
import controller.command.ChangeExtractPropertyCommand;
import controller.command.RenameMomentCommand;
import controller.controller.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Descripteme;
import model.MomentExperience;
import model.Property;
import utils.UndoCollector;

public class DescriptemeViewerController implements Initializable {
	
	
	private Main main;
	private Stage stage;
	private MomentExperience mMoment;
	private Property mProperty;
	private LinkedList<Descripteme> mDescriptemes;
	private Observable mObservable;
	
	private @FXML Button plusButton;
	private @FXML Button lessButton;
	private @FXML Button cancelButton;
	private @FXML Button validateButton;
	private @FXML ListView<String> descrList;
	private @FXML TextArea descrDetail;
	private @FXML Label momentName;
	
	private boolean isMoment = false;
	
	public DescriptemeViewerController(Main main, Stage s, MomentExperience m, Observable o) {
		this(main, s, o);
		this.mMoment = m;
		this.isMoment = true;
		this.cloneList(this.mMoment.getDescriptemes());
	}
	
	public DescriptemeViewerController(Main main, Stage s, Property p, Observable o) {
		this(main, s, o);
		this.mProperty = p;
		this.cloneList(this.mProperty.getDescriptemes());
	}
	
	private DescriptemeViewerController(Main main, Stage s, Observable o) {
		this.mDescriptemes = new LinkedList<Descripteme>();
		this.main = main;
		this.stage = s;
		this.mObservable = o;
	}

	private void cloneList(LinkedList<Descripteme> list) {
		for(Descripteme d : list) {
			this.mDescriptemes.add(new Descripteme(d.getTexte()));
		}
	}
	
	private void reloadList() {
		descrList.getItems().clear();
		for(Descripteme d : this.mDescriptemes) {
			if(d.getTexte().length()>20) 
				descrList.getItems().add(d.getTexte().substring(0, 17)+"...");
			else descrList.getItems().add(d.getTexte());
		}
			
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(isMoment) 
			momentName.setText(mMoment.getName());
		else
			momentName.setText(this.mProperty.getName());
		lessButton.setDisable(false);
		reloadList();
		
		descrList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(descrList.getSelectionModel().getSelectedIndex()>=0) {
					lessButton.setDisable(false);
					int id = descrList.getSelectionModel().getSelectedIndex();
					descrDetail.setText(mDescriptemes.get(id).getTexte());
				}
				else lessButton.setDisable(true);
			}
		});
		
		this.lessButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				mDescriptemes.remove(descrList.getSelectionModel().getSelectedIndex());
				reloadList();
			}
		});
		
		this.plusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				try {
					Stage promptWindow = new Stage(StageStyle.UTILITY);
					promptWindow.setTitle(main._langBundle.getString("select_extract"));
					//promptWindow.setAlwaysOnTop(true);
					promptWindow.initModality(Modality.APPLICATION_MODAL);
					
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/view/SelectDescriptemePart.fxml"));
					SelectDescriptemePartController select = new SelectDescriptemePartController(main,promptWindow);
					loader.setController(select);
					loader.setResources(main._langBundle);
					BorderPane layout = (BorderPane) loader.load();
					Scene launchingScene = new Scene(layout);
					promptWindow.setScene(launchingScene);
					promptWindow.showAndWait();
					if(!select.getText().equals("")) {
						mDescriptemes.add(new Descripteme(select.getText()));
						reloadList();
					}
				}
				catch(Exception e) {
					
				}
			}
		});
		
		this.cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				stage.close();
			}
		});
		
		this.validateButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(isMoment) {
					ChangeExtractMomentCommand cmd = new ChangeExtractMomentCommand(
							mObservable,
							mMoment.getDescriptemes(),
							mDescriptemes,
							main
		        			);
					cmd.execute();
					UndoCollector.INSTANCE.add(cmd);
				}
				else {
					ChangeExtractPropertyCommand cmd = new ChangeExtractPropertyCommand(
							mObservable,
							mProperty.getDescriptemes(),
							mDescriptemes,
							main
		        			);
					cmd.execute();
					UndoCollector.INSTANCE.add(cmd);
				}
				stage.close();
			}
		});
	}
}
