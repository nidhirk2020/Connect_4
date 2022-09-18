package com.example.connect_4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController implements Initializable {
    private static final int COLUMNS =7;
    private static final int ROWS =6;
    private static final int CIRCLE_DIAMETER =80;
    private static final String DISC_COLOR1="#00008B";
    private static final String DISC_COLOR2="#FF69B4";

    private static String Player_One="Player1";
    private  static String Player_two="Player2";

    private boolean isPlayerOneTurn = true;

    private boolean isAllowedToInsert =true;

    public void createPlayGround(){
        Shape RectangleWithHoles = new Rectangle((COLUMNS+1) * CIRCLE_DIAMETER ,(ROWS+1)*CIRCLE_DIAMETER);
        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLUMNS;col++){
                Circle circle =new Circle();
                circle.setRadius(CIRCLE_DIAMETER/2);
                circle.setCenterX(CIRCLE_DIAMETER/2);
                circle.setCenterY(CIRCLE_DIAMETER/2);
                circle.setSmooth(true);
                circle.setTranslateX(col*(CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+ CIRCLE_DIAMETER/4);
                RectangleWithHoles = Shape.subtract(RectangleWithHoles,circle);



            }

        }
        RectangleWithHoles.setFill(Color.WHITE);
        rootGridPane.add(RectangleWithHoles,0,1);
        List<Rectangle> rectangleList = CreateClickableColumns();
        for (Rectangle rectangle: rectangleList) {
            rootGridPane.add(rectangle,0,1);
        }

    }


    private Disc [][] insertedDiscsArray = new Disc[ROWS][COLUMNS];




    private List<Rectangle> CreateClickableColumns(){

        List <Rectangle> rectangleList = new ArrayList<>();
        for(int col=0;col<COLUMNS;col++) {
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER / 4);
            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int column =col;

            rectangle.setOnMouseClicked(event -> {
                if(isAllowedToInsert) {
                    isAllowedToInsert=false;
                    insertDiscs(new Disc(isPlayerOneTurn), column);
                }
            });

            rectangleList.add(rectangle);
        }
        return rectangleList;
    }

    private void insertDiscs(Disc disc, int column) {
        int row =ROWS-1;
        while(row>=0){
            if(discIfPresent(row,column) == null){
                break;
            }
           row--;
            if(row<0){
                return ;
            }
        }
        insertedDiscsArray [row][column] = disc;
        insertedDiscsPane.getChildren().add(disc);
        int currentRow =row;
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER / 4);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER / 4);
        translateTransition.setOnFinished(event -> {
            isAllowedToInsert=true;
            if(gameEnded (currentRow,column)){

                gameOver();

            }
            isPlayerOneTurn = !isPlayerOneTurn;
            playerNameLabel.setText(isPlayerOneTurn ? Player_One : Player_two);
        });
         translateTransition.play();
    }

    private void gameOver() {
        String winner = isPlayerOneTurn ? Player_One : Player_two;
        System.out.println("Winner is: "+ winner);

        Alert alert =new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The winner is "+winner);
        alert.setContentText("Want to play again ?");

        ButtonType yesBtn = new ButtonType("Yes");

        ButtonType noBtn = new ButtonType("No, Exit");
        alert.getButtonTypes().setAll(yesBtn , noBtn);

        Platform.runLater(()->{
            Optional<ButtonType> btnClicked = alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get()== yesBtn) {
                resetGme();
            }
            else{
                Platform.exit();
                System.exit(0);
            }
        });

    }

    public void resetGme() {
      insertedDiscsPane.getChildren().clear();
        for (int row = 0; row < insertedDiscsArray.length; row++) {
            for (int col = 0; col < insertedDiscsArray[row].length; col++){
                insertedDiscsArray[row][col]=null;
            }

        }
        isPlayerOneTurn =true;
        playerNameLabel.setText(Player_One);
        createPlayGround();
    }

    private boolean gameEnded(int row, int column) {
     List<Point2D> verticalPoints  = IntStream.rangeClosed(row-3,row+3).mapToObj(r->new Point2D(r,column)).collect(Collectors.toList());
     List<Point2D> horizontalPoints  = IntStream.rangeClosed(column-3,column+3).mapToObj(col->new Point2D(row,col)).collect(Collectors.toList());
     Point2D startPoint1 = new Point2D(row-3 , column+3);
     List<Point2D> diagonalPoints   = IntStream.rangeClosed(0,6).mapToObj(i->startPoint1.add(i,-i)).collect(Collectors.toList());
        Point2D startPoint2 = new Point2D(row-3 , column+3);
     List<Point2D> diagonal2Points   = IntStream.rangeClosed(0,6).mapToObj(i->startPoint2.add(i,i)).collect(Collectors.toList());

     boolean isEnded = checkCombination (verticalPoints) || checkCombination(horizontalPoints) || checkCombination(diagonalPoints) || checkCombination(diagonal2Points);
        return isEnded;
    }

    private boolean checkCombination(List<Point2D> points) {
        int chain=0;
        for (Point2D point : points) {

            int rowIndexForArray = (int) point.getX();
            int columnIndexForArray =(int)  point.getY();
            Disc disc = discIfPresent(rowIndexForArray,columnIndexForArray);
            if(disc !=null && disc.isPlayerOneMove == isPlayerOneTurn){
                chain++;
                if(chain==4){
                    return true;
                }
            }
            else{
                chain=0;
            }
        }
        return false;
    }

    private Disc discIfPresent (int row , int column){
        if(row>=ROWS || row<0 || column>=COLUMNS || column<0)
          return null;

         return insertedDiscsArray[row][column];
    }

    private static class Disc extends Circle {
        private final boolean isPlayerOneMove;
        public Disc(boolean isPlayerOneMove){
          this.isPlayerOneMove =isPlayerOneMove;
          setRadius(CIRCLE_DIAMETER/2);
          setFill(isPlayerOneMove ? Color.valueOf(DISC_COLOR1) : Color.valueOf(DISC_COLOR2));
          setCenterX(CIRCLE_DIAMETER/2);
          setCenterY(CIRCLE_DIAMETER/2);

        }
    }

    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiscsPane;
    @FXML
    public Pane menuPane;
    @FXML
    public Label playerNameLabel;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}