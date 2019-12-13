import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
	private String id;
	private int lockedHash;
	
	public Room() {
		initializeStacks(p1Keys,p2Keys);
		initializeStrings();
		player1 = null;
		player2 = null;
		winner = null;
		lockedHash = -1;
		
	}
	
	private void initializeStacks(ArrayList<String> p1Keys, ArrayList<String> p2Keys) {
		//shuffle();
		p1Stack = new Stack<String>();
		p2Stack = new Stack<String>();
		for(int i = 0; i < p1Keys.size(); i++) {
			p1Stack.push(p1Keys.get(i));
			p2Stack.push(p2Keys.get(i));
		}
		
	}
	
	private void initializeStrings() {
		id = "";
		
		for(int i = 0; i < p1Stack.size(); i++) {
			id+=p1Stack.get(i);
			if(i < p2Stack.size())
				id+= p2Stack.get(i);
		}
	}
	
	private void shuffle() {
		Collections.shuffle(p1Keys);
		Collections.shuffle(p2Keys);
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
	
	public String getID() {
		return id;
	}
	
	public void setWinner(Player player) {
		winner = player;
		
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public boolean isEmpty() {
		return player1 == null && player2 == null; 
	}
	
	public boolean isReady() {
		return player1 == null || player2==null;
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(obj.getClass() == this.getClass()) {
			Room room = (Room) obj;
			
			return true;
			/*if(room.getPlayer1() == null && room.getPlayer2() == null) {
				return room.getID().equals(this.getID());
			}
			else if(room.getPlayer1() == null && room.getPlayer2()!=null) {
				return room.getPlayer2().equals(this.getPlayer2()) && room.getID().equals(this.getID());
			} 
			else if(room.getPlayer1() != null && room.getPlayer2() == null) {
				return room.getPlayer1().equals(this.getPlayer1()) && room.getID().equals(this.getID());
			}
			else {
				return room.getPlayer1().equals(this.getPlayer1()) && room.getPlayer2().equals(this.getPlayer2())
						&& room.getID().equals(this.getID());
			} */
			
		}
		return false;
	}
	
	/**
	 * Hashcode method creates a hash code for specific objects and
	 * handles instances of null Player values.
	 * 
	 */
	@Override
	public int hashCode() {
		int result = 0;
		result = player1 == null || player2 == null ? 1 : player1.hashCode() + player2.hashCode();
		if(result == 1 && getWinner() != null) {
			return lockedHash;
		}
		result = 31 * result + id.hashCode();
		lockedHash = result;
		
		
		return result;
	} 
	
	public static void main(String[] args) {
		Room.initializeKeys();
		Room room1 = new Room();
		Room room2 = new Room();
		String evaluation = room1.equals(room2) && room1.hashCode() == room2.hashCode()? "Same obj same hash ": " different hash";
		System.out.println(evaluation);
		room1.setPlayer1(new Player());
		System.out.println(room1.equals(room2));
		
		
		
		//System.out.println(room1.hashCode());
		//System.out.println(room2.hashCode());
		/*System.out.println(room1.getStack1());
		System.out.println(room1.getStack2());
		System.out.println(room2.getStack1());
		System.out.println(room2.getStack2());  */
		
	}
	


	

	
	

}
