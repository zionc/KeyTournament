import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;


/**
 * Room represents a round in the tournament
 * @author zionchilagan
 *
 */
public class Room   {
	
	private Player player1,player2,winner;
	private Stack<String> p1Stack;
	private Stack<String> p2Stack;
	public static final  ArrayList<String> p1Keys = new ArrayList<String>();
	public static final ArrayList<String> p2Keys = new ArrayList<String>();
	
	
	
	public Room() {
		initializeStacks(p1Keys,p2Keys);
	}
	
	public boolean isEmpty() {
		return player1 == null && player2 == null; 
	}
	
	public boolean isReady() {
		return player1 == null || player2==null;
	}
	
	private void initializeStacks(ArrayList<String> p1Keys, ArrayList<String> p2Keys) {
		shuffle();
		p1Stack = new Stack<String>();
		p2Stack = new Stack<String>();
		for(int i = 0; i < p1Keys.size(); i++) {
			p1Stack.push(p1Keys.get(i));
			p2Stack.push(p2Keys.get(i));
		}
	}
	
	public String peekStack1() {
		return p1Stack.peek();
	}
	
	public String peekStack2() {
		return p2Stack.peek();
	}
	
	public String updateStack1() {
		return p1Stack.pop();
	}
	
	public String updateStack2() {
		return p2Stack.pop();
	}
	
	public Stack<String> getStack1() {
		return p1Stack;
	}
	
	public Stack<String> getStack2() {
		return p2Stack;
	}
	/**
	 * Simulates a round in the tournament
	 * @return - Winner of this round
	 */
	/*public Player simulate() {
		return player1;
	}
	
	public Player simulate(Player player1, Player player2) {
		return new Player();
	} */
	
	public void setWinner(Player player) {
		winner = player;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	
	/**
	 * Adds letters to specific lists
	 * 
	 */
	public static void initializeKeys() {
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File("playerkeys.txt")));
		
			String data;
			
			while((data = reader.readLine()) != null) {
				if(data.equals("Player 1")) {
					
					while(!(data = reader.readLine()).equals("End")) {
						
						p1Keys.add(data);
						
					}
				}
				else if(data.equals("Player 2")) {
					
					while(!(data = reader.readLine()).equals("End")) {
						
						p2Keys.add(data);
					}
				} 
				
			}
			
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null)
				reader.close();
				System.out.println("playerkeys.txt succesfully closed");
			} catch(IOException e) {
				System.out.println("Could not close playerinfo.txt");
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(p1Keys);
		Collections.shuffle(p2Keys);
	}
	

	/**
	 * @return the player1
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public Player getPlayer2() {
		return player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	@Override
	public String toString() {
		String s = "";
		if(player1 != null && player2 != null) {
			s = player1.getName() + "\n vs. \n" + player2.getName();
		}
		else if(player1 != null &&player2 == null) {
			s = player1.getName() + "\n vs. \n";
		}
		else {
			s = "Empty";
		}
		return s;
	}


	

	
	

}
