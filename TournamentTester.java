import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class TournamentTester extends Application {
	
	public static void main(String[] args) {
		
		
		launch(args);
		
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Room.initializeKeys();
		TournamentLayout parent = new TournamentLayout();
		primaryStage.setTitle("Tester");
		
		Scene scene = new Scene(parent,1400,900);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	
	}
	
	/**
	 *
	 
	public class MyBorderPane extends BorderPane {
		
		private List<Player> players = Player.playersFromFile("playerinfo.txt");
		
		private Tournament tournament;
		private List<Room> bracket;
		
		private TournamentPane tournamentPane;
		public MyBorderPane() {
			Collections.shuffle(players);
			tournament = new Tournament(players);
			
		
			bracket = tournament.getBracket();
			tournamentPane = new TournamentPane(this,tournament);
			
			
			tournament.displayTournament();
		
			this.setCenter(tournamentPane);
		}
	} */
	
	

}
