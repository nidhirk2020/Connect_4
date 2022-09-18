package com.example.connect_4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    private HelloController controller ;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        GridPane rootGridPane = fxmlLoader.load();

        MenuBar menuBar = createMenu();
        Pane menuPane= (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        menuBar.prefWidthProperty().bind(stage.widthProperty());
         controller=fxmlLoader.getController();
         controller.createPlayGround();
        Scene scene = new Scene(rootGridPane);
        stage.setTitle("Connect Four");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    private MenuBar createMenu(){
        //file Menu
        Menu file = new Menu("File");
        MenuItem newGame = new MenuItem("newGame");

        newGame.setOnAction(actionEvent -> controller.resetGme());

        MenuItem resetGame = new MenuItem("resetGame");

        resetGame.setOnAction(actionEvent -> controller.resetGme());

        SeparatorMenuItem  SeparatorMenuItem =new  SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("exitGame");

        exitGame.setOnAction(actionEvent -> exitGame());

        file.getItems().addAll(newGame,resetGame,SeparatorMenuItem,exitGame);
       //Help menu
        Menu help = new Menu("Help");
        MenuItem aboutGame = new MenuItem("aboutGame");

        aboutGame.setOnAction(actionEvent -> aboutGame());


        SeparatorMenuItem  SeparatorMenuItem1 =new  SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("aboutMe");
        aboutMe.setOnAction(actionEvent -> aboutMe());


        help.getItems().addAll(aboutGame,SeparatorMenuItem1,aboutMe);


        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file,help);

       return menuBar;

    }

    private void aboutMe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Nidhi Verma");
        alert.setContentText("I am an engineering student "+
                " In my free time I love to code and create Games "+
                " and Connect 4 is one such game. "+
                " Hope you enjoy playing it ");
        alert.show();
    }

    private void aboutGame() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About connect 4");
        alert.setHeaderText("How to Play?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping"+
                "colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying"+
                "the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or"+
                "diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();

    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }



    public static void main(String[] args) {
        launch();
    }
}