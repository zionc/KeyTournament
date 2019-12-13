import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Player class represents a player in the tournament, holds the
 * value of a name, origin and description.
 * 
 * Hashcode method multiplies the String's hashcode return value
 * for name, origin and description
 * @author zionchilagan
 *
 */

public class Player {
	
	/** Name of Player */
	private String name;
	/** Origin of Player */
	private String origin;
	/** Description of Player */
	private String description;

	
	/**
	 * Constructs a Player with a name, origin and description
	 * @param name - Name of Player
	 * @param origin - Origin of Player
	 * @param description - Description of Player
	 */
	public Player(String name, String origin, String description) {
		this.name = name;
		this.origin = origin;
		this.description = description;
	}
	
	/**
	 * Constructs a no-arg Player with
	 * name, origin and description set
	 * to "default"
	 */
	public Player() {
		this("default", "default", "default");
	}



	/**
	 * @return the name of Player
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the origin of Player
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * @return the description of Player
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Method to read and create Player objects from File
	 * @param filename the filepath of the text file of Players
	 * @return the ArrayList of Players
	 * 
	 * added by Lila
	 */
	public static List<Player> playersFromFile(String filename){

		List<Player> playerList = new ArrayList<>();
		
		BufferedReader reader = null;
		try{

			String name;
			String origin;
			String description;

			reader = new BufferedReader(new FileReader(new File(filename)));
			do{

				name = reader.readLine();
				origin = reader.readLine();
				description = reader.readLine();


				playerList.add(new Player(name, origin, description));


			}
			while(reader.readLine() != null);

		} catch(FileNotFoundException e) {
			e.printStackTrace();
			
		} catch(IOException e){

			System.out.println(e.getMessage());

		} finally {
			try {
				if(reader != null) {
					reader.close();
					System.out.println(filename + " closed");
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}


		return playerList;



	}

	/**
	 * String representation of Player
	 */
	@Override
	public String toString() {
		String s = "Fighter: " + name + ", born in " + origin +"\n" + description;
		return s;
	}
	
	/**
	 * Compares Object and this class for equality
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
			Player player = (Player) obj;
			return player.getName().equals(this.getName()) && player.getOrigin().equals(this.getOrigin())
					&& player.getDescription().equals(this.getOrigin());
		}
		return false;
	}

	/**
	 * Constructs an int representation of a Player
	 */
	@Override
	public int hashCode() {
		return name.hashCode() * origin.hashCode() * description.hashCode();
	}


}