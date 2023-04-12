package lockbox.screen;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static lockbox.screen.LoginProcess.loginStage;

public class SuccessProcess extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Success!");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/SuccessScreen.fxml")));
        primaryStage.setTitle("Success Screen");
        primaryStage.setScene(new Scene(root, 800, 600));
        System.out.println("Loaded Success Screen");

        // get a reference to the "Ok" button
        Button okButton = (Button) root.lookup("#okButton");

        // add an event handler to the "Ok" button to close the stage when clicked
        okButton.setOnAction(this::handleOkButton);

        primaryStage.show();
    }

    private void handleOkButton(ActionEvent actionEvent) {
        System.out.println("Clicked Ok Button");
        // close both the Success Screen and Login Screen
        loginStage.close();
        Stage successStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        successStage.close();

        // TODO: Decrypt the File and add the ShutdownHook to encrypt it again.
    }
}