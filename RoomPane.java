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
 * RoomPane is the GUI for displaying letters that each user must press.
 * It utilizes Room to update the Key to press from each Room. This class also
 * implements Observable so that once the game is finished, which can only happen
 * if someone correctly types their set of letters the fastest, it notifies any
 * observers.
 * 
 * 
 * TO-DO
 * 	- RoomPane currently only displays the letters without a label for
 * which player must press which letter not making it user friendly, must
 * add a label for both stacks from Room
 * @author zionchilagan
 *
 */

public class RoomPane extends StackPane implements Observable  {
	
	/** Room to gather data from */
	private Room room;
	/** Text to display the letter */
	private Text targetKey1, targetKey2;
	/** Layout to hold the letter */
	private HBox container = new HBox(10);
	/** Group to hold any components for container */
	private Group group = new Group();
	/** List of Observers to notify */
	private ArrayList<Observer> observers;
	
	/**
	 * Constructs a RoomPane by initializing the 
	 * text to the first letter in both stacks from room
	 * and handling key events.
	 * @param room - Room to display letters from
	 */
	public RoomPane(Room room) {
		//set focus to this class to accept key event
		observers = new ArrayList<Observer>();
		this.setFocusTraversable(true);
		this.requestFocus();
		this.room = room;
	
		targetKey1 = new Text();
		targetKey2 = new Text();
		
		container.getChildren().addAll(targetKey1,targetKey2);
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
	
	/**
	 * Helper method to set a style for the keys
	 */
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
	
	/**
	 * Updates the letter for users by calling on
	 * peek from their respective Stack, it the Room
	 * is empty then a winner is set for the Room
	 */
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
	
	/**
	 * Method to display who won the typing round
	 * @param stackNum - Empty Stack to signify who won in the Room
	 */
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
	
		Bloom bloom = new Bloom();
		bloom.setThreshold(0);
		winningText.setEffect(bloom);
		container.getChildren().add(winningText);
		container.getChildren().add(finish());
		container.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Handles KeyEvents by checking if a Key was entered that matched
	 * the top of either Stacks from Room, if a key was entered that matched
	 * the letter from a Stack then updateStack is invoked which pops the letter
	 */
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

	/**
	 * Add observers to a list of Observer, currently only using one
	 * but having a list will make it very easy to add a different or more
	 * observers
	 */
	@Override
	public void add(Observer o) {
		
		observers.add(o);
		
	}

	/**
	 * Alerts Observers by calling on their update() method, this currently
	 * only happens when user presses the back button which appears when someone
	 * has won
	 */
	@Override
	public void alert() {
		for(Observer o: observers) {
			o.update();
		}
		
	}

	

}
