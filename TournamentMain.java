import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * TournamentMain creates an instance of TournamentLayout and sets that
 * as the Parent for the scene
 * @author zionchilagan
 *
 */

public class TournamentMain extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TournamentLayout parent = new TournamentLayout();
		primaryStage.setTitle("Key Tournament");
		
		Scene scene = new Scene(parent,1400,900);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
	
	}
	
	
	

}
