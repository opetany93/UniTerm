package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        primaryStage.setTitle("UniTerm");

        Scene s = new Scene(root);
        s.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(s);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e ->
        {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    static public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    private void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }
}
