import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Tournament class represents a single elimination tournament where each
 * Room represents a round in the tournament. To implement a bracket for the tournament
 * a binary tree is implemented using an array list. A HashMap is also used to quickly
 * add a Player to a different room to represent a Player "moving up" in the tournament.
 * 
 * 
 * @author zionchilagan
 *
 */
public class Tournament {
	
	/** Map that contains a Room object for the Key and a list of Players as it's value */
	private HashMap<Room,List<Player>> hashMap;
	/** Collection of Room objects which implements a binary tree structure */
	private ArrayList<Room> bracket;
	/** Collection of Player objects participating in the tournament */
	private List<Player> players;
	
	
	/**
	 * Constructs a Tournament by passing in a list of Players, initializing
	 * a Map and initializing the bracket
	 * @param players
	 */
	public Tournament(List<Player> players) {
		hashMap = new HashMap<Room,List<Player>>();
		this.players = players;
		bracket = new ArrayList<Room>();
		initializeBracket();
		
	}
	
	/**
	 * Initializes the bracket by adding Rooms to hold all the Players
	 * which represents the starting rounds, then inserting an empty Room
	 * in the first index to represent the different stages in the tournament
	 */
	private void initializeBracket() {
		int startingRounds = players.size()/2; //total number of rooms to hold everyone initially
		int maxRounds = startingRounds * 2 - 1; //total number of rooms to represent entire bracket
		
		for(int i = 0; i < startingRounds; i++) { //creates 1st round of the bracket and initial mapping
			
			bracket.add(new Room());
			List<Player> list = new ArrayList<Player>();
			list.add(players.get(i*2));
			list.add(players.get(i*2+1));
			
			updateBracket(bracket.get(i),list);
		}
		for(int i = 0; i < (maxRounds - startingRounds); i++) {
			bracket.add(0, new Room());
			hashMap.put(bracket.get(0), new ArrayList<Player>());
		}
		
	}
	
	/**
	 * Get the room at a specific index
	 * @param index - index of specific Room
	 * @return - Room at given index
	 */
	public Room getRoom(int index) {
		return bracket.get(index);
	}
	
	/**
	 * Method to track what level
	 * a Room object is in
	 * 
	 * This algorithm was implemented by Evan Panahi in
	 * Binary Tree Lab: CS208, changes were made by me
	 * to implement traversing through a list
	 * 
	 * @param room - Room to find which level it is on in the tree
	 * @return - level of the room
	 */
	public int level(Room room) {
		if(bracket.get(0).equals(room)) {
			return 1;
		}
		else if(level(room,1) > 0)
			return 1+level(room,1);
		else if(level(room,2) > 0)
			return 1 + level(room, 2);
		else
			return -1;
	}
	
	/**
	 * Recursive method to track the level of a room 
	 * by checking to see if the Room at a specific index
	 * matches, if not then it recursively goes down 
	 * the Room's left and right sub tree, returns 0 if
	 * no match was found
	 * @param room - Room to find which level it is on
	 * @param index - Index of room to check for equality 
	 * @return - level of the Room, 0 if no match was found
	 */
	private int level(Room room, int index) {
		int leftIndex = index*2+1;
		int rightIndex = index*2+2;
		
		if(index >= bracket.size())
			return 0;
		else if(bracket.get(index).equals(room)) 
			return 1;
		else if(level(room,leftIndex) > 0)
			return 1 + level(room,leftIndex);
		else if(level(room,rightIndex) > 0) 
			return 1 + level(room,rightIndex);
		else
			return 0;
	}
	
	/**
	 * Method to check if a Room is a leaf node
	 * in the list
	 * @param index - Index of Room to check if it is a leaf
	 * @return -  True if the room is a leaf, false if it isnt
	 */
	public boolean isLeaf(int index) {
		if(index*2+1 >= bracket.size() || index*2+2 >= bracket.size()) {
			return true;
		}
		else return false;
	}
	
	/**
	 * Moves a player up in the bracket by finding it's current 
	 * Room's parent index and then mapping that Player to that room
	 * @param player - Player to move up in the bracket
	 * @param oldIndex - Current Index of the Room that the Player is on
	 */
	public void movePlayerUp(Player player,int oldIndex) {
		int newIndex = (oldIndex-1)/2;
		Room newRoom = bracket.get(newIndex);
		hashMap.get(newRoom).add(player);
		if(hashMap.get(newRoom).size() == 1) {
			newRoom.setPlayer1(hashMap.get(newRoom).get(0));
			System.out.println(player.getName() + " set as player 1");
		}
		else if(hashMap.get(newRoom).size() ==2) {
			newRoom.setPlayer2(hashMap.get(newRoom).get(1));
			System.out.println(player.getName() + " set as player 2");
		}
		
	}
	
	/**
	 * Helper method to update Bracket by mapping a Room to a list of Players,
	 * then Room assigns it's Player1 and Player2 based on it's position in the
	 * list. Index 0 represents Player 1, Index 1 represents Player 2
	 * @param room - Room to be mapped to a list of Player objects
	 * @param list - Collection of Player objects
	 */
	private void updateBracket(Room room, List<Player> list) {
		
		hashMap.put(room, list);
		
		room.setPlayer1(hashMap.get(room).get(0));
		room.setPlayer2(hashMap.get(room).get(1));
		
	}
	
	/**
	 * Method to print the contents in the Map
	 */
	public void displayHashMap() {
		hashMap.forEach((room,player) -> {
			System.out.print("[Room] => ");
			for(Player p: player) {
				System.out.print("[" + p.getName() + "] ");
				
			}
			System.out.println();
			
		});
	}
	
	/**
	 * Method to print the contents in the bracket
	 */
	public void displayTournament() {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for(int i = 0; i < bracket.size(); i++) {
			if(i != bracket.size()-1)
				sb.append(bracket.get(i) + ", ");
			else
				sb.append(bracket.get(i));
			
		}
		sb.append(" ]");
		System.out.println(sb.toString());
	}
	/**
	 * Returns the bracket for the tournament
	 * @return - bracket for the tournament
	 */
	public ArrayList<Room> getBracket() {
		return bracket;
	}
	
	
	

}
