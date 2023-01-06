package components.schemaTree.Controllers;

import application.configuration.Configuration;
import components.schemaTree.Cell.SchemaTreeCell;
import components.schemaTree.Cell.SchemaTreePluggable;
import components.schemaTree.Cell.Visitors.CreateSchemaTreeItemVisitor;
import components.schemaTree.Section;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import models.SchemaTreeRoot;
import utils.GlobalVariables;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeController implements Initializable {

    @FXML
    private
    TreeView<SchemaTreePluggable> schemaTree;

    @FXML private Pane bottomScroll;
    @FXML private Pane topScroll;

    private int visibleRow;
    private long lastScrollTime;
    private long milliSecondsBetweenScrolls = 200;
    private SchemaTreeRoot root;

    private GlobalVariables globalVariables = GlobalVariables.getGlobalVariables();

    public SchemaTreeController(SchemaTreeRoot root) { this.root = root; }

    public static Node createSchemaTree(SchemaTreeRoot root) {
        SchemaTreeController controller = new SchemaTreeController(root);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(controller.getClass().getResource("/views/SchemaTree/SchemaTree.fxml"));
            loader.setController(controller);
            loader.setResources(Configuration.langBundle);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        schemaTree.setEditable(true);
        schemaTree.setCellFactory(modelTreeElementTreeView -> new SchemaTreeCell());
        setTreeRoot(root);
        globalVariables.setSchemaTreeRoot(root);

        //Scrolling system
        bottomScroll.setOnDragEntered(event -> {
            lastScrollTime = System.currentTimeMillis();
            visibleRow = schemaTree.getRow(schemaTree.getSelectionModel().getSelectedItem())-1;
        });
        bottomScroll.setOnDragOver(event -> {
            scrollSchemaTree(Section.bottom);
        });

        topScroll.setOnDragEntered(event -> {
            lastScrollTime = System.currentTimeMillis();
            visibleRow = schemaTree.getRow(schemaTree.getSelectionModel().getSelectedItem())+1;
        });
        topScroll.setOnDragOver(event -> {
            scrollSchemaTree(Section.top);
        });
    }

    private void setTreeRoot(SchemaTreeRoot root) {
        CreateSchemaTreeItemVisitor visitor = new CreateSchemaTreeItemVisitor();
        root.accept(visitor);
        schemaTree.setRoot(visitor.getSchemaTreeItem());
    }

    private void scrollSchemaTree(Section s) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastScrollTime > milliSecondsBetweenScrolls){
            if(s == Section.top) {
                if(visibleRow > 0)
                    visibleRow--;
                schemaTree.scrollTo(visibleRow);
                lastScrollTime = currentTime;
            }
            else if(s == Section.bottom) {
                visibleRow++;
                schemaTree.scrollTo(visibleRow);
                lastScrollTime = currentTime;
            }
        }

    }
}
