import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Tournament represents the bracket of Rooms
 * @author zionchilagan
 *
 */
public class Tournament {
	
	private HashMap<Room,List<Player>> hashMap;
	private ArrayList<Room> bracket;
	private List<Player> players;
	
	public Tournament(List<Player> players) {
		
		hashMap = new HashMap<Room,List<Player>>();
		
		this.players = players;
		this.bracket = new ArrayList<Room>();
		
		initializeBracket();
		
	}
	
	
	private void initializeBracket() {
		int startingRounds = players.size()/2; //total number of rooms to hold everyone initially
		int maxRounds = startingRounds * 2 - 1; //total number of rooms to represent entire bracket
		
		for(int i = 0; i < startingRounds; i++) { //creates 1st round of the bracket and initial mapping
			
			bracket.add(new Room());
			List<Player> list = new ArrayList<Player>();
			list.add(players.get(i*2));
			list.add(players.get(i*2+1));
			
			updateBracket(bracket.get(i),list);
			
			//hashMap.put(rooms.get(i), list); //maps room to set of players
			
			
		}
		
		/*hashMap.forEach((room,player) -> { //sets players for the room
			room.setPlayer1(player.get(0));
			room.setPlayer2(player.get(1));
		}); */ 
		
		for(int i = 0; i < (maxRounds - startingRounds); i++) {
			bracket.add(0, new Room());
			hashMap.put(bracket.get(0), new ArrayList<Player>());
		}
		
	}
	
	public Room getRoom(int index) {
		return bracket.get(index);
	}
	
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
	
	public boolean isLeaf(int index) {
		if(index*2+1 >= bracket.size() || index*2+2 >= bracket.size()) {
			return true;
		}
		else return false;
	}
	
	/*public int height() {
		if(bracket.size() == 0) {
			
			return 0;
		}
		else
			return height(0);
		
	}
	
	public int height(int index) {
		int leftIndex = index*2+1;
		int rightIndex = index*2+2;
		if(index > bracket.size())
			return 0;
		else {
			if(1 + (height(leftIndex)) < height(rightIndex)) {
				return height(rightIndex);
			}
			else
				return height(leftIndex);
		}
	} */
	
	
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
	
	/*public void simulate() {
		
		List<Player> list = new ArrayList<Player>();
		for(int i = bracket.size() - 1; i >= 0; i--) {
			Player winner = bracket.get(i).simulate();
			
			if(i != 0)
				movePlayerUp(winner,i);
		}
	} */
	
	private void updateBracket(Room room, List<Player> list) {
		
		hashMap.put(room, list);
		
		room.setPlayer1(hashMap.get(room).get(0));
		room.setPlayer2(hashMap.get(room).get(1));
		
	}
	
	
	
	public void displayHashMap() {
		hashMap.forEach((room,player) -> {
			System.out.print("[Room] => ");
			for(Player p: player) {
				System.out.print("[" + p.getName() + "] ");
				
			}
			System.out.println();
			
		});
	}
	
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
	
	public ArrayList<Room> getBracket() {
		return bracket;
	}
	
	
	
	public static void main(String[] args) {
		
		Player player1 = new Player("Zion","MA","default");
		Player player2 = new Player("John","PA","default");
		Player player3 = new Player("Sarah","ME","default");
		Player player4 = new Player("Oliver","FL","default");
		
		List<Player> players = Player.playersFromFile("playerinfo.txt");
		
		Tournament tournament = new Tournament(players);
		System.out.println("Starting tournament: ");
		tournament.displayTournament();
		//System.out.println("HEIGHT: " + tournament.height());
		
		System.out.println("\nSimulating tournament: ");
		
		tournament.displayTournament();
		
		System.out.println("\nHash Map: ");
		tournament.displayHashMap();
		
		
		
	} 
	

}
