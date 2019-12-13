import java.util.Collections;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class TournamentLayout extends BorderPane {
	
	private List<Player> players = Player.playersFromFile("playerinfo.txt");
	
	private Tournament tournament;
	//private List<Room> bracket;
	
	private TournamentPane tournamentPane;
	public TournamentLayout() {
		Collections.shuffle(players);
		tournament = new Tournament(players);
		
	
		//bracket = tournament.getBracket();
		tournamentPane = new TournamentPane(this,tournament);
		
		
		tournament.displayTournament();
		this.setStyle("-fx-background-color: #CAE7DF");
		this.setCenter(tournamentPane);
		HBox titlePane = getTitlePane();
		this.setTop(titlePane);
		this.setPadding(new Insets(10,10,10,10));
		setMargin(titlePane,new Insets(10,0,10,0));
	}
	
	public HBox getTitlePane() {
		
		Label titleLabel = new Label();
		titleLabel.setText("Key Tournament");
		titleLabel.setStyle("-fx-font: 70 arial;");
		titleLabel.setTextFill(Color.web("#e63946"));
		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(5);
		dropShadow.setOffsetY(5);
		dropShadow.setColor(Color.GRAY);
		Reflection r = new Reflection();
		r.setFraction(0);
		dropShadow.setInput(r);
		titleLabel.setEffect(dropShadow);
		
		HBox titleBox = new HBox();
		titleBox.getChildren().add(titleLabel);
		titleBox.setAlignment(Pos.CENTER);
		titleBox.setStyle("-fx-background-color: #f1faee;");
		titleBox.setPrefHeight(USE_COMPUTED_SIZE + 150);
		
		
		return titleBox;
	}

}
