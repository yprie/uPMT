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
	
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

import controller.LaunchingScreenController;
import controller.MainViewController;
import controller.MomentExpVBox;
import controller.RootLayoutController;
import controller.controller.TypeController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Category;
import model.DescriptionInterview;
import model.Folder;
import model.MomentExperience;
import model.Project;
import model.Property;
import model.Schema;
import model.Type;
import utils.LoadDataProjects;
import utils.MomentID;
import utils.ResourceLoader;
import utils.SchemaTransformations;
import utils.Utils;
import java.util.ResourceBundle;


public class Main extends Application {
	
	public final static boolean activateBetaDesign = true;
	public final static Color colorSelectedCell = Color.BLACK;
	
	// Main Layout and Stage
	private BorderPane rootLayout;
	private Stage primaryStage;
	
	// This is implemented for internationalization	
	private Locale _locale;
	public ResourceBundle _langBundle;
	
	// Main Model references and containers
	private LinkedList<Project> projects = new LinkedList<Project>();
	private Project currentProject;
	private DescriptionInterview currentDescription;
	private Schema BasicSchema;
	private TreeView<TypeController> treeViewSchema;
	private TreeView<DescriptionInterview> treeViewInterview;
	private GridPane grid;
	
	private MainViewController mainViewController = new MainViewController(this);
	private RootLayoutController rootLayoutController;
	
	//Main reference to the clicked moment
	private MomentExpVBox currentMoment;
	
	private String bundleRes=null;
	
	private File fProperties=null;
	
	
	private boolean needSave = false;
	
	public void start(Stage primaryStage) throws IOException {
		loadProperties();
		initProjects();
		createBasicSchema();
		
		/*if(!projects.isEmpty()){
			currentProject = projects.getFirst();
			//System.out.println(currentProject.getSchemaProjet());
		}
		else {
			currentProject = new Projet("projetTMP", new Schema("SchemaTemporaire"));
		}*/
		currentProject = new Project("--emptyProject--", new Schema("SchemaTemporaire"));
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(_langBundle.getString("main_title"));
		
		//Launching layouts
		initRootLayout();
		showLaunchingScreen();
	}
	
	/**
	 *  Method used to load the selected LANGUAGES
	 */
	private void loadProperties () {
		Properties pros = new Properties();
		InputStream is = ResourceLoader.loadBundleInput("Current.properties");
			
		try {
 			pros.load(is);
 			//System.out.println("succeed load with: "+url);
 		} catch (Exception e) {
 			//System.out.println("fail with: "+url);
 		}
		String loc = pros.getProperty("locale","fr");
		_locale= new Locale(pros.getProperty("locale","fr"));
		set_langBundle(ResourceBundle.getBundle("bundles.Lang", _locale));
		
		//set locale for local control language
		
		if (loc.equals("fr")){
			Locale.setDefault(Locale.FRANCE);
		} else if (loc.equals("en")) {
			Locale.setDefault(Locale.US);
		} else if (loc.equals("cn")) {
			Locale.setDefault(Locale.CHINA);
		}
		else {
		//System.out.println("ERREUR");
		}
	}
	
	/**
	 * Method used to load all the projects
	 */
	private void initProjects(){
		this.projects = new LinkedList<Project>();
		LoadDataProjects dc = LoadDataProjects.instance();
		dc.setProjets(projects);
		if(Utils.checkRecovery()) {
			this.mainViewController.alertRecovery();
		}
		Utils.loadProjects(projects, this);
	}
	
	/**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang",new Locale("en", "EN")));
            this.rootLayoutController = new RootLayoutController(this, primaryStage);
            loader.setController(rootLayoutController);
            loader.setResources(get_langBundle());
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
    
    /**
     * Launch and show the launching screen
     */
    public void showLaunchingScreen(){
		Stage promptWindow = new Stage(StageStyle.UTILITY);
		promptWindow.setTitle(get_langBundle().getString("home"));
		//promptWindow.setAlwaysOnTop(true);
		promptWindow.initModality(Modality.APPLICATION_MODAL);
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LaunchingScreen.fxml"));
            loader.setController(new LaunchingScreenController(this,promptWindow));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", _locale));
            BorderPane layout = (BorderPane) loader.load();
			Scene launchingScene = new Scene(layout);
			//ENLEVER LE COMMENTAIRE POUR ACTIVER LA BETA CSS FLAT DESIGN
			if(activateBetaDesign)
				rootLayout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			promptWindow.setScene(launchingScene);
			promptWindow.showAndWait();
			
			// if project empty -> launch interview creation
			//if(this.getCurrentProject().getEntretiens().isEmpty()){
			//if(this.getCurrentProject()==null){
			if(this.getProjectInCreation()!=null) {
				//rootLayout.setCenter(null);
				this.getRootLayoutController().newInterview();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	        loader.setResources(ResourceBundle.getBundle("bundles.Lang", _locale));
	        BorderPane mainView = (BorderPane) loader.load();
	
	        // Show the scene containing the root layout.
	        Scene scene = new Scene(mainView);
	        rootLayout.setCenter(mainView);
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * Creates a basic scheme as a default 
     * should be improved if desired
     */
    private void createBasicSchema(){
    	Schema s = new Schema((_langBundle.getString("default_scheme")));
    	Folder general = new Folder(_langBundle.getString("general"));
    	Folder autre = new Folder(_langBundle.getString("other"));
    	Category visuel = new Category(_langBundle.getString("visual"));
    	Property image = new Property(_langBundle.getString("picture"));
		Category sensoriel = new Category(_langBundle.getString("sensory"));
		Category emotionnel = new Category(_langBundle.getString("emotional"));
		Category sonore = new Category(_langBundle.getString("acoustic"));
		visuel.addProperty(image);
		general.addCategory(visuel);
		general.addCategory(sensoriel);
		general.addCategory(emotionnel);
		general.addCategory(sonore);
		s.addFolder(general);
		s.addFolder(autre);
		this.BasicSchema = s;
    }
    
    /**
     * Method used to refresh the TreeView related to the scheme if it is out of synch with the model
     */
    public void refreshDataTreeView(){
		TreeItem<TypeController> schemaRoot = new TreeItem<TypeController>();
		TreeItem<DescriptionInterview> interviewRoot = new TreeItem<DescriptionInterview>();
		schemaRoot.getChildren().add(SchemaTransformations.SchemaToTreeView(this.currentProject.getSchema()));
		interviewRoot.getChildren().add(SchemaTransformations.EntretienToTreeView(this.currentProject.getInterviews()));
		this.treeViewSchema.setRoot(schemaRoot);
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
		this.primaryStage.setTitle(_langBundle.getString("main_title"));
	}
	
	public void needToSave(){
		needSave = true;
		currentProject.autosave();
		//System.out.println("NEED TO SAVE");
		this.primaryStage.setTitle(_langBundle.getString("main_title")+" *");
	}
	
	public boolean isNeedToBeSaved() {return needSave;}
	
	public void changeLocaleAndReload(String locale){
		saveCurrentProject();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(_langBundle.getString("confirmation_dialog"));
        alert.setHeaderText(_langBundle.getString("take_effect_text"));
        alert.setContentText(_langBundle.getString("ok_text"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        	try {
    	        Properties props = new Properties();
    	        props.setProperty("locale", locale);
    	        OutputStream out= ResourceLoader.loadBundleOutput("Current.properties");
    	        props.store(out, "This is an optional header comment string");
    	        
    	        //start(primaryStage);
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
		//Category, Property, Value
		for(Category c : m.getCategories()){
			for(Property p : c.getProperties()){
				classes.add(format(c.getName())+";"+format(p.getName())+";"+format(p.getValue()));
			}
		}
		
		if(!classes.isEmpty()){
			//Interview, ID, Name, Descripteme, Color, Duration + //Category, Property, Value
			for(String s : classes){
				writer.println(format(ent.getName())+";\""+hierarchy+"\""+";"+format(m.getName())+";"+format(m.getDescripteme())+";"
			+format(m.getColor())+";"+format(m.getDuration())+";"+s);
			}
		}else{
			writer.println(format(ent.getName())+";\""+hierarchy+"\""+";"+format(m.getName())+";"+format(m.getDescripteme())+";"
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
			

			if (!Files.exists(Paths.get("./exports/"))) {
			    new File("./exports/").mkdir();
			}
			PrintWriter writer = new PrintWriter("./exports/"+p.getName()+".csv", "UTF-8");
		    writer.println("\"INTERVIEW\";\"ID\";\"NAME\";\"DESCRIPTEME\";\"COLOR\";\"DURATION\";\"CATEGORY\";\"PROPERTY\";\"VALUE\"");
			for(DescriptionInterview ent : p.getInterviews()){
			    for (int i = 0; i < ent.getMoments().size(); i++) {
					MomentExperience m = ent.getMoments().get(i);
					writeMoment(ent, m, writer,Integer.toString(i+1));
				}
			}
		    writer.close();
		    
		    Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(_langBundle.getString("export_project"));
	        alert.setHeaderText(_langBundle.getString("export_ok"));
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
			//System.out.println("SET ! : "+this.currentProject.getName());
		}
		else {
			//System.out.println("SET ! : null");
		}
	}
	
	public Schema getDefaultSchema(){
		return this.BasicSchema;
	}
	
	public TreeView<TypeController> getTreeViewSchema() {
		return treeViewSchema;
	}
	
	public void setTreeViewSchema(TreeView<TypeController> treeViewSchema) {
		this.treeViewSchema = treeViewSchema;
		/*this.treeViewSchema.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				 if (!newValue.booleanValue()) {
					 Main.this.treeViewSchema.getSelectionModel().clearSelection();
				 }
			}
			
		});*/

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
	
	public ResourceBundle get_langBundle() {
		return _langBundle;
	}

	public void set_langBundle(ResourceBundle _langBundle) {
		this._langBundle = _langBundle;
	}

}
