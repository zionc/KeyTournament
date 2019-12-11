import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.Bloom;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Simple GUI to display keys user must press, communicates
 * with Room to display Keys, definitely needs some cleaning up
 * 
 * @author zionchilagan
 *
 */

public class RoomPane extends StackPane implements Observable  {
	
	private Room room;
	
	private Text targetKey1;
	private Text targetKey2;
	
	private HBox container = new HBox(10);
	private Group group = new Group();
	private ArrayList<Observer> observers;
	public RoomPane(Room room) {
		//set focus to this class to accept key event
		observers = new ArrayList<Observer>();
		this.setFocusTraversable(true);
		this.requestFocus();
		this.room = room;
		//initialize text
		targetKey1 = new Text();
		targetKey2 = new Text();
		
		//HBox, lays out nodes horizontally, adds target key1 and key2 to layout
		container.getChildren().addAll(targetKey1,targetKey2);
		//set the style
		container.setStyle("-fx-border-color: #a800eb;"
				+"-fx-border-width: 5px;"); 
		container.setPadding(new Insets(10,10,10,10));
		group.getChildren().add(container);
		getChildren().add(group);
		
		addEventFilter(KeyEvent.KEY_PRESSED, keyInput);
		setAlignment(group,Pos.CENTER);
		styleKeys();
		updateKeys();
		this.setStyle("-fx-background-color: #f1faee;");
		
	}
	
	private void styleKeys() {
		targetKey1.setStyle("-fx-font: 100px Tahoma;"
				+ "-fx-fill: #0015fa;"
				+ "-fx-stroke: black;"
				+ "-fx-stroke-width: 1 ;");
		targetKey2.setStyle("-fx-font: 100px Tahoma;"
				+ "-fx-fill: #fa0000;"
				+ "-fx-stroke: black;"
				+ "-fx-stroke-width: 1 ;");
	}
	
	public Button finish() {
		Button goBack = new Button("Back");
		goBack.setStyle("-fx-border-color: #5900ff;"
				+"-fx-border-width: 2px;"
				+ "-fx-font-size: 2em;"
				+ "-fx-text-fill: #0000ff");
		goBack.setOnAction(e -> alert());
		goBack.setPrefSize(100, 50);
		return goBack;
		
	}
	
	
	
	private void updateKeys() {
		if(!room.getStack1().isEmpty()) {
			targetKey1.setText(room.peekStack1());
		}
		if(!room.getStack2().isEmpty()) {
			targetKey2.setText(room.peekStack2());
		}
		
		if(room.getStack1().isEmpty())
			displayWinner(1);
		else if(room.getStack2().isEmpty())
			displayWinner(2);
	}
	
	private void displayWinner(int stackNum) {
		container.getChildren().clear();
		Text winningText = new Text();
		winningText.setStyle("-fx-font: 75px Tahoma;"
				+ "-fx-fill: #949618;"
				+ "-fx-stroke: black;");
		if(stackNum == 1) {
			
			winningText.setText(room.getPlayer1().getName() + " has won!");
			
			
			room.setWinner(room.getPlayer1());
		}
		else if(stackNum == 2) {
			winningText.setText(room.getPlayer2().getName() + " has won!");
		
			room.setWinner(room.getPlayer2());
		}
		//group.getChildren().addAll(winningText,finish());
		Bloom bloom = new Bloom();
		bloom.setThreshold(0);
		winningText.setEffect(bloom);
		container.getChildren().add(winningText);
		container.getChildren().add(finish());
		container.setAlignment(Pos.CENTER);
	}
	
	private EventHandler<KeyEvent> keyInput = keyEvent -> {
		if(keyEvent.getCode().toString().equals(targetKey1.getText())) {
			if(!room.getStack1().isEmpty()) {
				room.updateStack1();
				updateKeys();
				
			}
			
		}
		else if(keyEvent.getCode().toString().equals(targetKey2.getText())) {
			if(!room.getStack2().isEmpty()) {
				room.updateStack2();
				updateKeys();
			}
		}
		System.out.println(keyEvent.getCharacter());
		keyEvent.consume();
	};

	@Override
	public void add(Observer o) {
		TournamentPane tournamentPane = null;
		if(o instanceof TournamentPane) {
			tournamentPane = (TournamentPane)o;
			observers.add(tournamentPane);
		}
		
	}

	@Override
	public void alert() {
		for(Observer o: observers) {
			o.update();
		}
		
	}

	

}
