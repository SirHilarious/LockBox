package lockbox.screen;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lockbox.data.FileDecryption;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.Scanner;

public class LoginProcess extends Application {
    public static final String devUsername = ""; // Credentials to log in to the actual Server the File is hosted on
    public static final String devPassword = ""; // Credentials to log in to the actual Server the File is hosted on
    public static PasswordField passwordField;
    public static PasswordField usernameField;
    public static TextField authenticationKeyField;
    public static Stage loginStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting login process");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LoginScreen.fxml")));
        loginStage = primaryStage;
        primaryStage.setTitle("Login Screen");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("User clicked the Close Button.");
            System.exit(0);
        });
        System.out.println("Login process started");

        // Get references to the input fields
        passwordField = (PasswordField) root.lookup("#passwordField");
        usernameField = (PasswordField) root.lookup("#usernameField");
        authenticationKeyField = (TextField) root.lookup("#authenticationKeyField");

        // Get a reference to the "Submit" button
        Button submitButton = (Button) root.lookup("#submitButton");

        // Get a reference to the "Cancel" button
        Button cancelButton = (Button) root.lookup("#cancelButton");

        // Add an event handler to the button
        submitButton.setOnAction(this::handleSubmitButton);
        cancelButton.setOnAction(this::handleCancelButton);

        System.out.println("Login process finished");
    }

    private void handleCancelButton(ActionEvent actionEvent) {
        // TODO: Exit the Application when the User clicks the Cancel Button.
        System.out.println("User clicked the Cancel Button.");
    }

    private void handleSubmitButton(ActionEvent event) {
        System.out.println("User clicked the Submit Button.");
        // Read the values entered by the user in the input fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        String authenticationKey = authenticationKeyField.getText();

        // Print out the User-Input for debugging purposes
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Authentication Key: " + authenticationKey);

        // Do something with the user input, e.g. validate the login credentials
        boolean isValid = validateLogin(username, password, authenticationKey);

        // Show an error message if the login credentials are invalid
        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid login credentials!");
            alert.showAndWait();
        }
    }

    private boolean validateLogin(String username, String password, String authenticationKey) {
        boolean isValid = false;
        try {
            // Step 1: Log in to the server
            System.out.println("Logging in...");
            URL loginUrl = new URL(""); // TODO: Change this to the URL of the actual server where you login
            HttpURLConnection loginConnection = (HttpURLConnection) loginUrl.openConnection();
            loginConnection.setRequestMethod("POST");
            loginConnection.setDoOutput(true);
            String loginData = "username=" + URLEncoder.encode(devUsername, "UTF-8") +
                    "&password=" + URLEncoder.encode(devPassword, "UTF-8");
            loginConnection.getOutputStream().write(loginData.getBytes());

            // Check the response code to see if the login was successful
            int loginResponseCode = loginConnection.getResponseCode();
            System.out.println("Login Response Code: " + loginResponseCode);
            if (loginResponseCode == HttpURLConnection.HTTP_OK) {
                // Step 2: Get the contents of the LockBox.json data
                System.out.println("Getting LockBox.json...");
                URL lockBoxUrl = new URL(""); // TODO: Change this to the URL of the actual File to compare with the User-Input
                HttpURLConnection lockBoxConnection = (HttpURLConnection) lockBoxUrl.openConnection();
                lockBoxConnection.setRequestMethod("GET");

                // Check the response code to see if the data was found
                int lockBoxResponseCode = lockBoxConnection.getResponseCode();
                System.out.println("LockBox Response Code: " + lockBoxResponseCode);
                if (lockBoxResponseCode == HttpURLConnection.HTTP_OK) {
                    // Step 3: Compare the user input to the contents of the data
                    System.out.println("Comparing user input to LockBox.json...");
                    InputStream lockBoxStream = lockBoxConnection.getInputStream();
                    Scanner scanner = new Scanner(lockBoxStream, "UTF-8");
                    String lockBoxContents = scanner.useDelimiter("\\A").next();
                    scanner.close();
                    JSONObject lockBoxJson = new JSONObject(lockBoxContents);
                    if (lockBoxJson.getString("username").equals(username) &&
                            lockBoxJson.getString("password").equals(password) &&
                            lockBoxJson.getString("authenticationKey").equals(authenticationKey)) {
                        isValid = true;
                        System.out.println("User input is valid!");
                    } else {
                        System.out.println("User input is invalid.");
                    }
                } else {
                    System.out.println("LockBox File Not Found!");
                }
            } else {
                System.out.println("Login Failed!");
            }
            // If the login is valid, display the Success Screen and continue with the program
            if (isValid) {
                executeValidProcess();
            }
            loginConnection.disconnect();
            System.out.println("Done!");

        } catch (Exception e) {
            // Handle any errors that occur while making the HTTP request or parsing the JSON
            e.printStackTrace();
        } finally {
            System.out.println("Done!");
        }
        // Set the style of the input fields based on whether the login was valid or not
        if (!isValid) {
            usernameField.setStyle("-fx-border-color: red");
            passwordField.setStyle("-fx-border-color: red");
        } else {
            usernameField.setStyle("");
            passwordField.setStyle("");
        }

        return isValid;
    }

    private void executeValidProcess() {
        System.out.println("User input is valid!");
        System.out.println("Starting Main Application Process...");

        try {
            System.out.println("Decrypting File...");
            FileDecryption.decryptFile("C:\\Test\\Authentication.json.enc");
            System.out.println("File decrypted!");

            System.out.println("Starting Main Application Process...");
        } catch (Exception e) {
            System.out.println("Error decrypting file: " + e.getMessage());
        }
    }
}