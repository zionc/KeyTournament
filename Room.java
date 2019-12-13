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
 * Room represents a round in the tournament in which a two Players are assigned
 * a stack of letters. Room works with RoomPane to display the letter on the top of
 * the stack, and updates the stack if a user enters the corresponding letter. 
 * 
 * Issues
 * The idea was to set the Room as the Key for the map so I decided
 * to not pass in Player objects through the constructor since I can set Player
 * objects using set method instead. I also wanted to avoid creating default Players
 * for each empty Room because hashing a no-arg Player would result in the same hashCode. 
 * That being said, the problem lies in finding a good hashCode method for null instances.
 *   
 * @author zionchilagan
 *
 */
public class Room   {
	
	/** Players that are competing as well as the winner */
	private Player player1,player2,winner;
	/** Stack to hold/update the Key that has to be pressed */
	private Stack<String> p1Stack,p2Stack;
	/** List of Keys Player 1 has to press */
	public static final  ArrayList<String> p1Keys = new ArrayList<String>();
	/** List of Keys Player 2 has to press */
	public static final ArrayList<String> p2Keys = new ArrayList<String>();
	/** ID of Room which is set to the combination of both stacks */
	private String id;
	/** int to lock a hash to handle removed Players */
	private int lockedHash;
	
	
	/**
	 * Constructs a Room by initializing Stacks, creating an ID for the room,
	 * and setting Player 1, Player 2, and Winner to null (This was done with the goal to reduce dependency)
	 */
	public Room() {
		initializeStacks(p1Keys,p2Keys);
		initializeStrings();
		player1 = null;
		player2 = null;
		winner = null;
		lockedHash = -1;
		
	}
	
	/**
	 * Initializes Stacks by shuffling Keys and pushing\
	 * them onto the Stack
	 * @param p1Keys - Keys to be shuffled for Player 1
	 * @param p2Keys - Keys to be shuffled for Player 2
	 */
	private void initializeStacks(ArrayList<String> p1Keys, ArrayList<String> p2Keys) {
		shuffle();
		p1Stack = new Stack<String>();
		p2Stack = new Stack<String>();
		for(int i = 0; i < p1Keys.size(); i++) {
			p1Stack.push(p1Keys.get(i));
			p2Stack.push(p2Keys.get(i));
		}
		
	}
	
	/**
	 * Sets the ID of the room to the concatenation of 
	 * both stacks
	 */
	private void initializeStrings() {
		id = "";
		
		for(int i = 0; i < p1Stack.size(); i++) {
			id+=p1Stack.get(i);
			if(i < p2Stack.size())
				id+= p2Stack.get(i);
		}
	}
	
	/**
	 * Method to shuffle the Keys for both players
	 */
	private void shuffle() {
		Collections.shuffle(p1Keys);
		Collections.shuffle(p2Keys);
	}
	
	/**
	 * Get the first letter in the stack without removing it,
	 * this is used to let Player 1 know which Key to press
	 * @return - First letter in the stack
	 */
	public String peekStack1() {
		return p1Stack.peek();
	}
	
	/**
	 * Get the first letter in the stack without removing it,
	 * this is used to let Player 2 know which Key to press
	 * @return - First letter in the stack
	 */
	public String peekStack2() {
		return p2Stack.peek();
	}
	
	/**
	 * Updates the stack for Player 1 by popping
	 * the letter the user pressed
	 * @return - Value of the letter being popped
	 */
	public String updateStack1() {
		return p1Stack.pop();
	}
	
	/**
	 * Updates the stack for Player 2 by popping
	 * the letter the user pressed
	 * @return - Value of the letter being popped
	 */
	public String updateStack2() {
		return p2Stack.pop();
	}
	
	/**
	 * Get the stack of letters for Player 1
	 * @return - Stack of letters for Player 1
	 */
	public Stack<String> getStack1() {
		return p1Stack;
	}
	
	/**
	 * Get the stack of keys for Player 2
	 * @return - Stack of letters for Player 2
	 */
	public Stack<String> getStack2() {
		return p2Stack;
	}
	
	/**
	 * Get the ID for this room
	 * @return - ID for the room
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Set the winner of the Room, denoted by
	 * whoever pressed the sequence of keys the fastest
	 * @param player - Winner of the room
	 */
	public void setWinner(Player player) {
		winner = player;
		
	}
	
	/**
	 * Get the winner for this room
	 * @return - Winner 
	 */
	public Player getWinner() {
		return winner;
	}
	
	/**
	 * Checks if the room has no players
	 * @return - True if the room has no Players, false
	 * if it has at least one Player
	 */
	public boolean isEmpty() {
		return player1 == null && player2 == null; 
	}
	
	/**
	 * Checks if a Room is ready
	 * @return
	 */
	public boolean isReady() {
		return player1 != null && player2 != null;
	}
	
	
	/**
	 * Initializes Key List for both Players from playerkeys.txt file
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
	 * Get Player 1
	 * @return the player1
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Set Player 1
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	/**
	 * Get Player 2
	 * @return the player2
	 */
	public Player getPlayer2() {
		return player2;
	}

	/**
	 * Set Player 2
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	/**
	 * String representation of the Room
	 */
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
	
	/**
	 * Compares Object with this class for equality
	 * 
	 * Commented code: See "Issues above"
	 */
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
			
			return room.getID().equals(this.getID());
			/*(if(room.getPlayer1() == null && room.getPlayer2() == null) {
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
	 * Hashcode method hashes Room's ID
	 * 
	 * Commented code: See "Issues" above
	 * 
	 */
	@Override
	public int hashCode() {
		/*int result = 0;
		result = player1 == null || player2 == null ? 1 : player1.hashCode() + player2.hashCode();
		if(result == 1 && getWinner() != null) {
			return lockedHash;
		}
		result = 31 * result + id.hashCode();
		lockedHash = result; */
		
		int result = 31 * id.hashCode();
		
		return result;
	} 
	
	public static void main(String[] args) {
		/*Room.initializeKeys();
		Room room1 = new Room();
		Room room2 = new Room();
		String evaluation = room1.equals(room2) && room1.hashCode() == room2.hashCode()? "Same obj same hash ": " different hash";
		//System.out.println(evaluation);
		System.out.println(room1.hashCode());
		room1.setPlayer1(new Player());
		room1.setPlayer2(new Player());
		System.out.println(room1.hashCode());
		//System.out.println(room1.equals(room2));
		
		int counter = 0;
		for(int i = 0; i < 100000; i++) {
			Room room3 = new Room();
			Room room4 = new Room();
			if(room3.hashCode() == room4.hashCode()) {
				counter++;
			}
		}
		System.out.println(counter);
		
		//System.out.println(room1.hashCode());
		//System.out.println(room2.hashCode());
		System.out.println(room1.getStack1());
		System.out.println(room1.getStack2());
		System.out.println(room2.getStack1());
		System.out.println(room2.getStack2());  */
		Room.initializeKeys();
		Room room1 = new Room();
		System.out.println(room1.hashCode());
		HashMap<Room,List<Player>> map = new HashMap<Room,List<Player>>();
		map.put(room1, new ArrayList<Player>());
		room1.setPlayer1(new Player());
		room1.setPlayer2(new Player());
		System.out.println(room1.hashCode());
		System.out.println(map.get(room1));
		
	}
	


	

	
	

}
