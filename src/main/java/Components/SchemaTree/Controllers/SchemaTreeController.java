package Components.SchemaTree.Controllers;

import Components.SchemaTree.Cell.SchemaTreePluggable;
import Components.SchemaTree.Cell.Models.SchemaTreeRoot;
import Components.SchemaTree.Cell.Visitors.CreateSchemaTreeItemVisitor;
import Components.SchemaTree.Cell.SchemaTreeCell;
import application.Configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTreeController implements Initializable {

    @FXML
    private
    TreeView<SchemaTreePluggable> schemaTree;

    private SchemaTreeRoot root;

    public SchemaTreeController(SchemaTreeRoot root) { this.root = root; }

    public static Node createSchemaTree(SchemaTreeController controller) {
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
        schemaTree.setCellFactory(modelTreeElementTreeView -> {
            return new SchemaTreeCell();
        });
        setTreeRoot(root);
    }

    private void setTreeRoot(SchemaTreeRoot root) {
        CreateSchemaTreeItemVisitor visitor = new CreateSchemaTreeItemVisitor();
        root.accept(visitor);
        schemaTree.setRoot(visitor.getSchemaTreeItem());
    }
}