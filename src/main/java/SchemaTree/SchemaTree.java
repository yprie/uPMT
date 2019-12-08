package SchemaTree;

import SchemaTree.Cell.SchemaTreePluggable;
import SchemaTree.Cell.Models.SchemaTreeRoot;
import SchemaTree.Cell.Visitors.CreateSchemaTreeItemVisitor;
import SchemaTree.Cell.SchemaTreeCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SchemaTree extends BorderPane implements Initializable {

    @FXML
    private
    TreeView<SchemaTreePluggable> schemaTree;

    public SchemaTree() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SchemaTree.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exc) {
            exc.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        schemaTree.setEditable(true);
        schemaTree.setCellFactory(modelTreeElementTreeView -> {
            return new SchemaTreeCell();
        });
    }

    public void setTreeRoot(SchemaTreeRoot root) {
        CreateSchemaTreeItemVisitor visitor = new CreateSchemaTreeItemVisitor();
        root.accept(visitor);
        schemaTree.setRoot(visitor.getSchemaTreeItem());
    }
}