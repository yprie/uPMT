package components.aboutUs.Controllers;

import application.configuration.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ResourceLoader;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutUsController implements Initializable {


        private @FXML Button buttonCloseHelp;
        private @FXML Button buttonLink2Github;

        private Stage stage;

        public static void createAboutUs() {
            try {

                Stage stage = new Stage(StageStyle.UTILITY);
                stage.setTitle(Configuration.langBundle.getString("about_us"));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setWidth(610);
                stage.setHeight(350);

                AboutUsController controller = new AboutUsController(stage);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(controller.getClass().getResource("/views/AboutUs/AboutUs.fxml"));
                loader.setResources(Configuration.langBundle);
                loader.setController(controller);
                AnchorPane node = loader.load();
                stage.setScene(new Scene(node));
                stage.showAndWait();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        public AboutUsController(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            buttonCloseHelp.setText(Configuration.langBundle.getString("close"));
            Image res = ResourceLoader.loadImage("github.png");
            ImageView icon = new ImageView(res);
            icon.prefWidth(10);
            icon.maxWidth(10);
            icon.maxHeight(10);
            icon.prefHeight(10);
            buttonLink2Github.setGraphic(icon);
        }

        @FXML
        private void closeAbout() { stage.close(); }

        @FXML
        private void link2Github() {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/coco35700/uPMT"));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

}
