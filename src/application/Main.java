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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Classe;
import model.DescriptionEntretien;
import model.Dossier;
import model.MomentExperience;
import model.Projet;
import model.Propriete;
import model.Schema;
import model.Type;
import utils.LoadDataProjects;
import utils.SchemaTransformations;
import utils.Utils;
import java.util.ResourceBundle;

public class Main extends Application {
	// Main Layout and Stage
	private BorderPane rootLayout;
	private Stage primaryStage;
	
	// This is implemented for internationalization	
	private Locale _locale;
	public ResourceBundle _langBundle;
	
	// Main Model references and containers
	private LinkedList<Projet> projects;
	private Projet currentProject;
	private DescriptionEntretien currentDescription;
	private Schema BasicSchema;
	private TreeView<TypeController> treeViewSchema;
	private TreeView<DescriptionEntretien> treeViewInterview;
	private GridPane grid;
	
	private MainViewController mainViewController = new MainViewController(this);
	private RootLayoutController rootLayoutController;
	
	//Main reference to the clicked moment
	private MomentExpVBox currentMoment;
	
	private boolean needSave = false;
	
	public void start(Stage primaryStage) throws IOException {
		loadProperties();
		initProjects();
		createBasicSchema();
		
		if(!projects.isEmpty()){
			currentProject = projects.getFirst();
			System.out.println(currentProject.getSchemaProjet());
		}
		else {
			currentProject = new Projet("projetTMP", new Schema("SchemaTemporaire"));
		}
		
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
		InputStream is = null;
		try {
			File f = new File("/bundles/Current.properties");
			is = new FileInputStream(f);
		} catch (Exception e) {
			System.out.println("load file failed");
			is = null;
		}
		try {
			if (is == null) {
				is = getClass().getResourceAsStream("../bundles/Current.properties");
			}
			pros.load(is);
		} catch (Exception e){
			e.printStackTrace();
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
	}
	
	/**
	 * Method used to load all the projects
	 */
	private void initProjects(){
		this.projects = new LinkedList<Projet>();
		LoadDataProjects dc = LoadDataProjects.instance();
		dc.setProjets(projects);
		if(Utils.checkRecovery()) {
			this.mainViewController.alertRecovery();
		}
		Utils.loadProjects(projects);
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
		Stage promptWindow = new Stage();
		promptWindow.setTitle(get_langBundle().getString("welcome"));
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LaunchingScreen.fxml"));
            loader.setController(new LaunchingScreenController(this,promptWindow));
            loader.setResources(ResourceBundle.getBundle("bundles.Lang", _locale));
            BorderPane layout = (BorderPane) loader.load();
			Scene launchingScene = new Scene(layout,404,250);
			//ENLEVER LE COMMENTAIRE POUR ACTIVER LA BETA CSS FLAT DESIGN
			//rootLayout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			promptWindow.setScene(launchingScene);
			promptWindow.showAndWait();
			
			// if project empty -> launch interview creation
			if(this.getCurrentProject().getEntretiens().isEmpty()){
				rootLayout.setCenter(null);
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
    	Type s = new Schema("Schema par default");
    	Type general = new Dossier("general");
    	Type autre = new Dossier("autre");
		Type visuel = new Classe("visuel");
		Type image = new Propriete("image");
		Type sensoriel = new Classe("sensoriel");
		Type emmotionnel = new Classe("emotionnel");
		Type sonore = new Classe("sonore");
		visuel.addType(image);
		general.addType(visuel);
		general.addType(sensoriel);
		general.addType(emmotionnel);
		general.addType(sonore);
		s.addType(general);
		s.addType(autre);
		this.BasicSchema = (Schema) s;
    }
    
    /**
     * Method used to refresh the TreeView related to the scheme if it is out of synch with the model
     */
    public void refreshDataTreeView(){
		TreeItem<TypeController> schemaRoot = new TreeItem<TypeController>();
		TreeItem<DescriptionEntretien> interviewRoot = new TreeItem<DescriptionEntretien>();
		schemaRoot.getChildren().add(SchemaTransformations.SchemaToTreeView(this.currentProject.getSchemaProjet()));
		interviewRoot.getChildren().add(SchemaTransformations.EntretienToTreeView(this.currentProject.getEntretiens()));
		this.treeViewSchema.setRoot(schemaRoot);
		this.treeViewInterview.setRoot(interviewRoot);
	}
	
    /**
     * Method used to change the current moment
     * also updates the inspector
     */
	public void setCurrentMoment(MomentExpVBox s){
		//render to the inspector
		if(this.getCurrentMoment() != s) {
			currentMoment = s;	
			mainViewController.renderInspector();
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
		System.out.println("NEED TO SAVE");
		this.primaryStage.setTitle(_langBundle.getString("main_title")+" *");
	}
	
	public boolean isNeedToBeSaved() {return needSave;}
	
	public void changeLocaleAndReload(String locale){
		saveCurrentProject();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will take effect after reboot");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
        	try {
    	        Properties props = new Properties();
    	        props.setProperty("locale", locale);
    	        File f = new File(getClass().getResource("../bundles/Current.properties").getFile());
    	        OutputStream out = new FileOutputStream( f );
    	        props.store(out, "This is an optional header comment string");
    	        
    	        start(primaryStage);
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
	private void writeMoment(DescriptionEntretien ent, MomentExperience m, PrintWriter writer, String hierarchy){
		LinkedList<String> classes = new LinkedList<String>();
		for(Type t : m.getType()){
			for(Type prop : t.getTypes()){
				Propriete p = (Propriete) prop;
				classes.add(format(t.getName())+","+format(p.getName())+","+format(p.getValeur()));
			}
		}
		
//		if(!classes.isEmpty()){
//			for(String s : classes){
//				writer.println(format(ent.getNom())+",\""+hierarchy+"\""+","+format(m.getNom())+","+format(m.getMorceauDescripteme())+","+format(m.getCouleur())+","+format(m.getDebut())
//				+","+format(m.getFin())+","+s);
//			}
//		}else{
//			writer.println(format(ent.getNom())+",\""+hierarchy+"\""+","+format(m.getNom())+","+format(m.getMorceauDescripteme())+","+format(m.getCouleur())+","+format(m.getDebut())
//			+","+format(m.getFin())+",\"\",\"\",\"\"");
//		}

		if(!classes.isEmpty()){
			for(String s : classes){
				writer.println(format(ent.getNom())+",\""+hierarchy+"\""+","+format(m.getNom())+","+format(m.getMorceauDescripteme())+","
			+format(m.getCouleur())+","+format(m.getDuree())+","+s);
			}
		}else{
			writer.println(format(ent.getNom())+",\""+hierarchy+"\""+","+format(m.getNom())+","+format(m.getMorceauDescripteme())+","
		+format(m.getCouleur())+","+format(m.getDuree())+",\"\",\"\",\"\"");
		}
		
		for (int i = 0; i < m.getSousMoments().size(); i++) {
			MomentExperience sub = m.getSousMoments().get(i);
			writeMoment(ent,sub, writer, hierarchy+"."+(i+1));
		}
	}	
	
	/**
	 * Exports the project p into a CSV file
	 */
	public void export(Projet p){
		ObjectOutputStream oos = null;
		try {
			PrintWriter writer = new PrintWriter("exports/"+p.getName()+".csv", "UTF-8");
		    writer.println("\"ENTRETIEN\",\"ID\",\"NOM\",\"EXTRAIT\",\"COULEUR\",\"DEBUT\",\"FIN\",\"CLASSE\",\"PROP\",\"VALEUR\"");
			for(DescriptionEntretien ent : p.getEntretiens()){
			    for (int i = 0; i < ent.getMoments().size(); i++) {
					MomentExperience m = ent.getMoments().get(i);
					writeMoment(ent, m, writer,Integer.toString(i+1));
				}
			}
		    writer.close();
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
	
	public LinkedList<Projet> getProjects() {
		return projects;
	}
	
	public Projet getCurrentProject() {
		return currentProject;
	}
	
	public void setCurrentProject(Projet current){
		currentProject = current;
	}
	
	public Schema getDefaultSchema(){
		return this.BasicSchema;
	}
	
	public TreeView<TypeController> getTreeViewSchema() {
		return treeViewSchema;
	}
	
	public void setTreeViewSchema(TreeView<TypeController> treeViewSchema) {
		this.treeViewSchema = treeViewSchema;
	}
	
	public TreeView<DescriptionEntretien> getTreeViewInterview() {
		return treeViewInterview;
	}
	
	public void setTreeViewInterview(TreeView<DescriptionEntretien> treeViewInterview) {
		this.treeViewInterview = treeViewInterview;
	}
	
	public DescriptionEntretien getCurrentDescription() {
		return currentDescription;
	}
	
	public void setCurrentDescription(DescriptionEntretien currentDescription) {
		this.currentDescription = currentDescription;
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
