/*****************************************************************************
 * Main.java
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

package application;
	
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import Project.Controllers.ProjectSelectionController;
import SchemaTree.Cell.Models.IPropertyAdapter;
import application.Configuration.Configuration;
import controller.MainViewController;
import controller.MomentExpVBox;
import controller.RootLayoutController;
import controller.controller.TypeController;
import controller.typeTreeView.TypeTreeView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Category;
import model.DescriptionInterview;
import model.MomentExperience;
import model.Project;
import model.Schema;
import utils.MomentID;
import utils.ResourceLoader;
import utils.SchemaTransformations;
import utils.Utils;


public class Main extends Application {
	
	public final static boolean activateBetaDesign = true;
	public final static Color colorSelectedCell = Color.BLACK;
	
	// Main Layout and Stage
	private BorderPane rootLayout;
	private Stage primaryStage;
	
	// Main Model references and containers
	private LinkedList<Project> projects = new LinkedList<Project>();
	private Project currentProject;
	private DescriptionInterview currentDescription;

	private TreeView<TypeController> treeViewSchema;
	private TreeView<DescriptionInterview> treeViewInterview;
	private GridPane grid;
	private MainViewController mainViewController = new MainViewController(this);
	private RootLayoutController rootLayoutController;

	public ResourceBundle _langBundle;

	//Main reference to the clicked moment
	private MomentExpVBox currentMoment;
	private String bundleRes=null;
	private File fProperties=null;
	private boolean needSave = false;
	public final String fileOfPath = System.getProperty("user.home")+"/.upmt/path.json";
	
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		       @Override
		       public void handle(WindowEvent e) {
		          Platform.exit();
		          System.exit(0);
		       }
		    });

		Configuration.loadAppConfiguration();
		_langBundle = Configuration.langBundle;

		currentProject = new Project("--emptyProject--", new Schema("SchemaTemporaire"));

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(Configuration.langBundle.getString("main_title"));
		
		//Launching layouts
		initRootLayout();
		ProjectSelectionController controller = ProjectSelectionController.openProjectSelection();
	}


	/**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            loader.setResources(Configuration.langBundle);
            this.rootLayoutController = new RootLayoutController(this, primaryStage);
            loader.setController(rootLayoutController);
            loader.setResources(Configuration.langBundle);
            rootLayout = (BorderPane) loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void showRecentProject(Menu openProject) {
    	openProject.getItems().clear();
    	System.out.println(this.getProjects().size());
		for(Project p : this.getProjects()) {
			MenuItem child = new MenuItem(p.getName() + " (from " + p.getPath() +")");
			child.setOnAction(new EventHandler<ActionEvent>() {
		        public void handle(ActionEvent t) {
		        	setCurrentProject(p);
					launchMainView();
		        }
		    });
			openProject.getItems().addAll(child);
		}
    }
    
    /**
     * Load and sets the main vue (center)
     */
    public void launchMainView(){
    	try {
	    	FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("/view/MainView.fxml"));
	        loader.setController(mainViewController);
	        loader.setResources(Configuration.langBundle);
	        
	        rootLayoutController.fonct_test(Configuration.locale);
	        
	        BorderPane mainView = (BorderPane) loader.load();
	        this.primaryStage.setTitle("uPMT - "+this.currentProject.getName()+".uPMT");

	        // Show the scene containing the root layout.
	        Scene scene = new Scene(mainView);
	        rootLayout.setCenter(mainView);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    
    /**
     * Method used to refresh the TreeView related to the scheme if it is out of synch with the model
     */
    public void refreshDataTreeView(){
		//TreeItem<TypeController> schemaRoot = new TreeItem<TypeController>();
		TreeItem<DescriptionInterview> interviewRoot = new TreeItem<DescriptionInterview>();
		//schemaRoot.getChildren().add(SchemaTransformations.SchemaToTreeView(this.currentProject.getSchema()));
		interviewRoot.getChildren().add(SchemaTransformations.EntretienToTreeView(this.currentProject.getInterviews()));
		//this.treeViewSchema.setRoot(schemaRoot);
		this.treeViewInterview.setRoot(interviewRoot);
	}
	private Project projetInCreation=null;
    public void setProjectInCreation(Project p) {
    	projetInCreation = p;
    }
    public Project getProjectInCreation() {
    	return this.projetInCreation;
    }
    
    public boolean haveCurrentProject() {
    	return !currentProject.getName().equals("--emptyProject--");
    }
    
    /**
     * Method used to change the current moment
     * also updates the inspector
     */
	public void setCurrentMoment(MomentExpVBox s){
		//render to the inspector
		if(this.getCurrentMoment() != s) {
			currentMoment = s;
		}	
	}
	
	/**
	 * saves the current project
	 */
	public void saveCurrentProject(){

		needSave = false;
		currentProject.save();
		this.primaryStage.setTitle("uPMT - "+this.currentProject.getName()+".uPMT");
	}
	
	/**
	 * Save Current Project as
	 * @ pathLocation: project path to save
	 * @ name: project name to save
	 */
	public void saveCurrentProjectAs(String pathLocation, String name) throws IOException{
		needSave = false;
		currentProject.saveAs(pathLocation, name.replace(".upmt", ""));
		Configuration.addToProjects(pathLocation);
		this.primaryStage.setTitle("uPMT - "+this.currentProject.getName()+".uPMT");
	}


	/**
	 *  Open project from a path
	 */
	public void openProjectAs() throws IOException{
		/*final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open your project");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("uPMT", "*.upmt"));
		File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
        	String projectToLoad = file.getName().replace(".upmt", "");
        	boolean isProject = false;
        	for(Project project : projects) {
        		if(project.getName().equals(projectToLoad)) {
        			this.setCurrentProject(project);
        			this.primaryStage.setTitle("uPMT - "+this.currentProject.getName()+".uPMT");
        			this.launchMainView();
        			isProject=true;
        		}
        	}

        	if(!isProject) { //project is not in the project's list
        		String name = file.getPath();
*//*        		name = name.replace("/"+file.getName(), "");
        		name = name.replace("\\"+file.getName(), "");*//*
        		Configuration.addToProjects(name);
        		Utils.loadProjects(projects, this);
        		for(Project p : projects) {
        			if(p.getName().equals(projectToLoad)) {
                		this.setCurrentProject(p);
                		this.launchMainView();
                		isProject=true;
               		}
               	}
        	}
         }*/
	}


	public void needToSave(){
		needSave = true;
		currentProject.autosave();
		this.primaryStage.setTitle("uPMT - "+this.currentProject.getName()+".uPMT*");
	}

	public boolean isNeedToBeSaved() {return needSave;}

	public void changeLocaleAndReload(String locale){
		saveCurrentProject();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(Configuration.langBundle.getString("confirmation_dialog"));
        if(locale.equals("it")) {
        	alert.setHeaderText(Configuration.langBundle.getString("take_effect_text_it"));
        	locale = "en";
        } else if (locale.equals("es")) {
        	alert.setHeaderText(Configuration.langBundle.getString("take_effect_text_es"));
        	locale = "en";
        } else {
        	 alert.setHeaderText(Configuration.langBundle.getString("take_effect_text"));
        }
        
        alert.setContentText(Configuration.langBundle.getString("ok_text"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        	try {
    	        Properties props = new Properties();
    	        props.setProperty("locale", locale);
    	        OutputStream out= ResourceLoader.loadBundleOutput("Current.properties");
    	        props.store(out, "This is an optional header comment string");
    	    }
    	    catch (Exception e ) {
    	        e.printStackTrace();
    	    }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
	}
	
	private String format(String c){
		if(c == null){
			return "\"\"";
		}else{
			return "\""+c+"\"";
		}
	}
	
	/**
	 * Recursive method used to recursively transform a Moment and its sub Moments into CSV compatible DATAZ
	 */
	private void writeMoment(DescriptionInterview ent, MomentExperience m, PrintWriter writer, String hierarchy){
		LinkedList<String> classes = new LinkedList<String>();
		//SchemaCategory, SchemaProperty, Value
		for(Category c : m.getCategories()){
			for(IPropertyAdapter p : c.getProperties()){
				classes.add(format(c.getName())+";"+format(p.getValue())+";"+format(p.getValue()) +";"+ format(p.toStringDescripteme()));
			}
		}
		
		if(!classes.isEmpty()){
			//Interview, ID, Name, Descripteme, Color, Duration + //SchemaCategory, SchemaProperty, Value
			for(String s : classes){
				writer.println(format(ent.getName())+";\""+hierarchy+"\""+";"+format(m.getName())+";"+format(m.toStringDescripteme())+";"
			+format(m.getColor())+";"+format(m.getDuration())+";"+s);
			}
		}else{
			writer.println(format(ent.getName())+";\""+hierarchy+"\""+";"+format(m.getName())+";"+format(m.toStringDescripteme())+";"
		+format(m.getColor())+";"+format(m.getDuration())+";\"\";\"\";\"\"");
		}
		
		for (int i = 0; i < m.getSubMoments().size(); i++) {
			MomentExperience sub = m.getSubMoments().get(i);
			writeMoment(ent,sub, writer, hierarchy+"."+(i+1));
		}
	}
	

	/**
	 * Exports the project p into a CSV file
	 */
	public void export(Project p){
		ObjectOutputStream oos = null;
		try {
			//get date and time
			DateFormat df = new SimpleDateFormat("dd.MM.yy_HH//mm");
			Calendar calobj = Calendar.getInstance();
	        String date = df.format(calobj.getTime());
	       
	        date = date.replace("//", "h");
	        date = date.replace(":", "m");
	        date = date.replaceAll("/", ":");
	        
			PrintWriter writer = new PrintWriter(p.getPath() + "/" + p.getName()+ "_" + date +".csv", "UTF-8");
			
		    writer.println("\"INTERVIEW\";\"ID\";\"NAME\";\"DESCRIPTEME\";\"COLOR\";\"DURATION\";\"CATEGORY\";\"PROPERTY\";\"VALUE\";\"\"PROPERTY'S DESCRIPTEME");
			for(DescriptionInterview ent : p.getInterviews()){
			    for (int i = 0; i < ent.getMoments().size(); i++) {
					MomentExperience m = ent.getMoments().get(i);
					writeMoment(ent, m, writer,Integer.toString(i+1));
				}
			}
		    writer.close();
		    
		    Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(Configuration.langBundle.getString("export_project"));
	        alert.setHeaderText(Configuration.langBundle.getString("export_ok"));
	        alert.show();
	        
		} catch (final IOException ex){
			ex.printStackTrace();
		}
	}
	
	public MomentExpVBox getCurrentMoment(){
		return currentMoment;
	}

	public void exportCurrentProject() {
		export(currentProject);
	}
    
	public static void main(String[] args) {
		launch(args);
	}
    
    public MainViewController getMainViewController(){
    	return this.mainViewController;
    }
    
	public BorderPane getRootLayout() {
		return rootLayout;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public LinkedList<Project> getProjects() {
		return projects;
	}
	
	public Project getCurrentProject() {
		return currentProject;
	}
	
	public void setCurrentProject(Project current){
		currentProject = current;
		if(currentProject!=null) {
			this.primaryStage.setTitle("uPMT - "+this.currentProject.getName()+".uPMT");
		}
		else {

		}
	}
	
	public Schema getDefaultSchema(){
		return null;
	}
	
	public TypeTreeView droppingTmp;
	
	public TreeView<TypeController> getTreeViewSchema() {
		return treeViewSchema;
	}
	
	public void setTreeViewSchema(TreeView<TypeController> treeViewSchema) {
		this.treeViewSchema = treeViewSchema;
	}
	
	public TreeView<DescriptionInterview> getTreeViewInterview() {
		return treeViewInterview;
	}
	
	public void setTreeViewInterview(TreeView<DescriptionInterview> treeViewInterview) {
		this.treeViewInterview = treeViewInterview;
	}
	
	public DescriptionInterview getCurrentDescription() {
		return currentDescription;
	}
	
	public void setCurrentDescription(DescriptionInterview currentDescription) {
		this.currentDescription = currentDescription;
		mainViewController.setDroppableText(currentDescription.getDescripteme().getTexte());
		MomentID.initID(currentDescription.getMoments());
	}
	
	public void setGrid(GridPane g){
		this.grid = g;
	}
	
	public GridPane getGrid(){
		return this.grid;
	}
	
	public RootLayoutController getRootLayoutController(){
		return rootLayoutController;
	}

}
