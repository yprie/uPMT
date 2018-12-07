/*****************************************************************************
 * RootLayoutController.java
 *****************************************************************************
 * Copyright 锟� 2017 uPMT
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TitledPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.Project;
import utils.UndoCollector;
import utils.Utils;

public class RootLayoutController implements Initializable{
		
	private @FXML MenuItem openProject;
	private @FXML MenuItem newInterview;
	private @FXML MenuItem quitterBouton;
	private @FXML MenuItem saveProject;
	private @FXML MenuItem saveProjectAs;
	private @FXML MenuItem exportProject;
	private @FXML MenuItem undo;
	private @FXML MenuItem redo;
	private @FXML MenuItem aboutUs; 
//	private @FXML TextArea document;
	private @FXML MenuItem userGuide;
	private @FXML MenuItem stats;
	
	private Main main;
	private Stage window;
	
	public RootLayoutController(Main main,Stage window) {
		this.main = main;
		this.window = window;
	}
	
	@FXML
	public void openProject(){
		main.showLaunchingScreen();
	}
	
	@FXML
	public void openProjectAs() throws IOException, ClassNotFoundException{
		main.openProjectAs();
	}
	
	@FXML
	public void saveProject(){
		main.saveCurrentProject();
	}
	
	@FXML
	public void saveProjectAs() throws IOException{
		Stage primaryStage = null;
		final FileChooser directoryChooser = new FileChooser();
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		directoryChooser.setInitialFileName(main.getCurrentProject().getName()+Project.FORMAT);
		File dir = directoryChooser.showSaveDialog(primaryStage);
		if(dir != null){
			try {
				main.saveCurrentProjectAs(dir.getCanonicalPath(), dir.getName());
			} catch(IOException e ) {
				e.printStackTrace();
			}	
		}
	}
	
	@FXML
	public void exportProject(){
		main.exportCurrentProject();
	}
	
	@FXML
	public void undo(){
		UndoCollector.INSTANCE.showUndoRedoStack();
		UndoCollector.INSTANCE.undo();
	}
	
	@FXML
	public void redo(){
		UndoCollector.INSTANCE.showUndoRedoStack();
		UndoCollector.INSTANCE.redo();
	}
	
	@FXML
	public void aboutUs(){
		Stage helpWindow = new Stage(StageStyle.UTILITY);
		helpWindow.setTitle(main._langBundle.getString("about_us"));
		helpWindow.setResizable(false);
		//helpWindow.setAlwaysOnTop(true);
		helpWindow.initModality(Modality.APPLICATION_MODAL);
		//helpWindow.setWidth(610);  
		//helpWindow.setHeight(350);
 
		try {
			FXMLLoader loader = new FXMLLoader();
			
            loader.setLocation(getClass().getResource("/view/HelpView.fxml"));
            loader.setController(new AboutController(main, helpWindow));
            
//          loader.setController(new HelpController(main, helpWindow, this.document));
            loader.setResources(main._langBundle);
            AnchorPane layout = (AnchorPane) loader.load();
            Scene sc = new Scene(layout);
			helpWindow.setScene(sc);
			helpWindow.showAndWait();
			
		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	// ----------------------------modifier: open a document
	@FXML
	public void openALink() {
		Stage web = new Stage();
		
//		link.onActionProperty().addListener(listener);

		userGuide.setOnAction((ActionEvent action)->{ 
			try {
				java.awt.Desktop.getDesktop().browse(new URI("https://github.com/coco35700/uPMT/wiki"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        });
	     //web.setAlwaysOnTop(true); 
	     web.centerOnScreen();
	     web.close(); 
	}
	
	@FXML
	public void newInterview(){

		Stage promptWindow = new Stage(StageStyle.UTILITY);
		promptWindow.setTitle(main._langBundle.getString("new_interview"));
		promptWindow.setResizable(false);
		//promptWindow.setAlwaysOnTop(true);
		promptWindow.initModality(Modality.APPLICATION_MODAL);
		try {
			FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getResource("/view/NouveauEntretienDialogLayout.fxml"));
			loader.setLocation(getClass().getResource("/view/NewInterview.fxml"));
            //loader.setController(new NewInterviewDialogController(main,promptWindow));
			loader.setController(new NewInterviewDialogController(main,promptWindow));
            loader.setResources(main._langBundle);
            
            //BorderPane layout = (BorderPane) loader.load();
            AnchorPane layout = (AnchorPane) loader.load();
			Scene main = new Scene(layout);
			promptWindow.setScene(main);
			promptWindow.showAndWait();
			
		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	private void saveRequest(WindowEvent event){
		if(!main.isNeedToBeSaved()) {
			try {
   	    	 event.consume();
			} catch (NullPointerException e) {
			//System.out.println("no event, exit normally");
			}
			Platform.exit();
	        System.exit(0);
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle(main._langBundle.getString("quit"));
	    	alert.setHeaderText(main._langBundle.getString("quit_alarm"));
	    	ButtonType buttonTypeOne = new ButtonType(main._langBundle.getString("ok"));
	    	ButtonType buttonTypeTwo = new ButtonType(main._langBundle.getString("no"));
	    	ButtonType buttonTypeCancel = new ButtonType(main._langBundle.getString("cancel"));
	    	
	    	alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo,buttonTypeCancel);
	
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if (result.get() == buttonTypeOne){
	    		saveProject();
	    		alert.close();
	    		Platform.exit();
		        System.exit(0);
	    	} else if (result.get() == buttonTypeTwo) {
	    		Utils.deleteRecovery();
	    		alert.close();
	    		Platform.exit();
		        System.exit(0);
	    	} else if (result.get() == buttonTypeCancel){
	    	    alert.close();
	    	    try {
	    	    	 event.consume();
				} catch (NullPointerException e) {
				//System.out.println("no event, exit normally");
				}
	    	}
		}
	}
	
	@FXML
	public void statsCategory(){
		Stage statsWindow = new Stage(StageStyle.UTILITY);
		statsWindow.setTitle(main._langBundle.getString("stats_category"));
		statsWindow.setResizable(false);
		statsWindow.initModality(Modality.APPLICATION_MODAL);
 
		try {
			FXMLLoader loader = new FXMLLoader();
			
            loader.setLocation(getClass().getResource("/view/StatsView.fxml"));
            loader.setController(new StatsCategoryController(main, statsWindow));
           
            loader.setResources(main._langBundle);
    		
            //////////
            AnchorPane layout = (AnchorPane) loader.load();
            Scene sc = new Scene(layout);
            statsWindow.setScene(sc);
			statsWindow.showAndWait();
			
		} catch (IOException e) {
			// TODO Exit Program
			e.printStackTrace();
		}
	}
	
	@FXML
	public void close(){
		this.saveRequest(null);
	}
	
	@FXML
	public void openEnglishVersion(){
		main.changeLocaleAndReload("en");
	}
	@FXML
	public void openFrenchVersion(){
		main.changeLocaleAndReload("fr");
	}
	@FXML
	public void openSpanishVersion(){
		main.changeLocaleAndReload("es");
	}
	@FXML
	public void openItalienVersion(){
		main.changeLocaleAndReload("it");
	}
	@FXML
	public void openJapaneseVersion(){
		main.changeLocaleAndReload("jp");
	}
	@FXML
	public void openChineseVersion(){
		main.changeLocaleAndReload("cn");
	}
	// Add your new language here
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_ANY));
		redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_ANY));
		saveProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY));
		saveProject.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY));
		newInterview.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_ANY));
		this.window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				saveRequest(event);
				Platform.exit();
		        System.exit(0);
			}
		});
	}

}
