import java.util.ArrayList;
import java.util.Hashtable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * TournamentPane creates a GUI for Tournament object's bracket. Each
 * Room in the bracket is associated and represented with a RoomNode which
 * displays who is competing in the Room and each RoomNode is used to construct
 * a SubTree. TournamentPane is also an Observer so it awaits any signals from
 * Observables (RoomPane) so it can update the bracket
 * 
 * To-Do
 * This GUI does not implement the following: 
 * - Update the contents of what RoomNode displays to match who won the round.
 * - Tooltip is implemented which just prints who is playing against each other in the 
 * starting rounds, but not in any other round
 * - Does not check if a level is complete before allowing the user to click on a RoomNode
 * in the level above

 * @author zionchilagan
 *
 */

public class TournamentPane extends StackPane implements Observer {
	
	/** Bracket used to map a room to RoomNode */
	private ArrayList<Room> bracket;
	/** Group to contain the SubTree layout */
	private Group treeGroup;
	/** Tournament to get bracket from and update */
	private Tournament tournament;
	/** Height of the tree */
	private int height;
	/** The top-level container for the GUI */
	private BorderPane mainGui;
	/** Map to assign a RoomNode to an index, to quickly update it without iteration */
	private Hashtable<RoomNode,Integer> roomIndex;
	/** Map to get a RoomNode by given index */
	private Hashtable<Integer,RoomNode> getRoom;
	/** int to represent the index of the RoomNode user clicks on */
	private int indexPointer;

	
	/**
	 * Constructs a TournamentPane creating SubTree and adding it to
	 * the StackPane, BorderPane instance is also passed in which represents the
	 * top-level container
	 * @param mainGui - BorderPane which uses TournamentPane
	 * @param tournament - Tournament whose bracket will be used
	 */
	public TournamentPane(BorderPane mainGui,Tournament tournament) {
		this.mainGui = mainGui;
		treeGroup = new Group();
		this.tournament = tournament;
		bracket = this.tournament.getBracket();
		height = tournament.level(bracket.get(bracket.size()-1));
		roomIndex = new Hashtable<RoomNode,Integer>();
		getRoom = new Hashtable<Integer,RoomNode>();
		indexPointer = 0;
		
		SubTree sub = new SubTree();
		treeGroup.getChildren().add(sub);
	
		this.setStyle("-fx-background-color: #f1faee;");
		setAlignment(treeGroup,Pos.CENTER);
		setMargin(treeGroup,new Insets(10,0,0,0));
		getChildren().add(treeGroup);
	}
	
	/**
	 * Overrides update method, which happens after RoomPane has declared a winner
	 * for Room, by moving the Player who won up in the bracket, and updating the text for
	 * RoomNode.
	 * If however the RoomPane represents the last and final round denoted by indexPointer being zero,
	 * the winner of the Tournament is displayed
	 * 
	 */
	@Override
	public void update() {
		
		Player player = getRoom.get(indexPointer).getRoom().getWinner();
		if(indexPointer > 0 && !getRoom.get(indexPointer).getRoom().isFinished()) {
			getRoom.get(indexPointer).getRoom().setFinished(true);
			tournament.movePlayerUp(player, indexPointer);
			
			RoomNode roomPointer = getRoom.get((indexPointer-1)/2);
			roomPointer.refresh();
			tournament.displayTournament();
			mainGui.setCenter(this);
		}
		
		else if(indexPointer == 0) {
			mainGui.setCenter(this);
			displayWinner(player.getName());
		}
		else {
			mainGui.setCenter(this);
		}
		
		
	}
	
	public void displayWinner(String name) {
		Text winningText = new Text();
		winningText.setStyle("-fx-font: 75px Tahoma;"
				+ "-fx-fill: #949618;"
				+ "-fx-stroke: black;");
		winningText.setText(name + " has won the tournament!");
		Bloom bloom = new Bloom();
		bloom.setThreshold(0);
		winningText.setEffect(bloom);
		setAlignment(winningText,Pos.TOP_CENTER);
		getChildren().add(winningText);
	}
	
	/**
	 * Get the tournament
	 * @return - tournament
	 */
	public Tournament getTournament() {
		return tournament;
	}
	
	
	/**
	 * SubTree is a nested class that creates new instances of itself
	 * to encapsulate a RoomNode and represent it as a tree, SubTree is also
	 * responsible for handling Mouse events
	 * @author zionchilagan
	 *
	 */
	private class SubTree extends StackPane {
		/** Group to contain the SubTree */
		private Group subtreeGroup;
		/** RoomNode to set as a Tree/SubTree */
		private RoomNode rootNode;
		/** Lines to draw relationships between Trees and Subtrees */
		private Line stem,childLine,childStem;
		/** Scale of the lines, which is then used to place RoomNode */
		private int startingScale;
		/** The index of the Room to represent from the bracket given by tournament */
		private int currentIndex;
		/** Collection of Line objects, mainly used for styling */
		private ArrayList<Line> lines;
		
		/**
		 * Constructs a no-arg SubTree which sets the index to zero,
		 * then creates it's left and right sub trees as it iterates through
		 * the bracket.
		 */
		public SubTree() {
			this(0);
		}
		
		/**
		 * Constructs a SubTree by given index and creates new SubTree instances
		 * as it traverses down the bracket starting from given index
		 * @param index - Index of Room to start SubTree
		 */
		public SubTree(int index) {
			currentIndex = index;
			
			lines = new ArrayList<Line>();
			rootNode = new RoomNode(bracket.get(currentIndex));
			setToolTip(rootNode);
			roomIndex.put(rootNode, index);
			getRoom.put(index, rootNode);
			setListeners();
			subtreeGroup = new Group();
			startingScale =(int)((rootNode.getRectangle().getWidth()/2) * 2 + 10);
			startingScale = startingScale * height;
			subtreeGroup.getChildren().add(rootNode);
			drawConnections();
			getChildren().add(subtreeGroup);
		}
		
		/**
		 * Constructs a SubTree by given index, scale, and X and Y
		 * coordinates to place the SubTree
		 * @param index - Index of the Room for SubTree's RoomNode to encapsulate
		 * @param scale - Scale to draw the line
		 * @param x - X coordinate for the SubTree
		 * @param y - Y coordinate for the SubTree
		 */
		public SubTree(int index,int scale,int x, int y) {
			currentIndex = index;
			lines = new ArrayList<Line>();
			rootNode = new RoomNode(bracket.get(index),x,y);
			setToolTip(rootNode);
			roomIndex.put(rootNode, index);
			getRoom.put(index, rootNode);
			setListeners();
			subtreeGroup = new Group();
			startingScale = scale/height;
			
			subtreeGroup.getChildren().add(rootNode);
			drawConnections();
			getChildren().add(subtreeGroup);
		}
		
		/**
		 * Helper method to draw the stem going down the RoomNode and the line that is used to hang
		 * the left and right sub tree
		 */
		private void drawConnections() {
			int xCoord = ((int) rootNode.getLayoutX() + ((int)rootNode.getLayoutX() +(int)rootNode.getRectangle().getWidth()))/2;
			int yCoord = ((int) rootNode.getLayoutY()  + (int)rootNode.getRectangle().getHeight()) + 4;
			int rectHeight = (int)rootNode.getRectangle().getHeight();
			stem = new Line(xCoord, yCoord, xCoord ,yCoord + rectHeight );
			childLine = new Line(stem.getEndX() - startingScale, stem.getEndY(), 
					stem.getEndX() + startingScale, stem.getEndY());
			lines.add(stem);
			lines.add(childLine);
			effectLines();
			subtreeGroup.getChildren().addAll(stem,childLine);
			
			drawChildStems(childLine);
		}
		
		/**
		 * Helper method to draw the stems to determine where the left and right sub tree will be placed
		 * @param childLine - Line to start drawing the child stems from
		 * 
		 * To-Do:
		 *  Repeated code in the instance where a tree with two subtrees are drawn,
		 *  make a method
		 */
		private void drawChildStems(Line childLine) {
			int length = (int)(childLine.getEndX() - childLine.getStartX());
			int spacing = 0;
			int numChildren = numChildren();
			if(numChildren > 1)
				spacing = length/(numChildren-1);
			int startX = (int)childLine.getStartX();
			if(numChildren == 1) {
				childStem = new Line(startX,childLine.getStartY(),startX,childLine.getStartY()+40);
				lines.add(childStem);
				effectLines();
				int childX = startX - (int)(rootNode.getRectangle().getWidth()/2);
				int childY = (int)childStem.getEndY();
				SubTree tree = new SubTree(currentIndex*2+1,startingScale,childX,childY);
				subtreeGroup.getChildren().addAll(childStem,tree.getTreeGroup());
				startX+=spacing;
			}
			else if(numChildren == 2) {
				childStem = new Line(startX,childLine.getStartY(),startX,childLine.getStartY()+40);
				int childX = startX - (int)(rootNode.getRectangle().getWidth()/2);
				int childY = (int)childStem.getEndY();
				lines.add(childStem);
				effectLines();
				subtreeGroup.getChildren().add(childStem);
				SubTree leftTree = new SubTree(currentIndex*2+1,startingScale,childX,childY);
				subtreeGroup.getChildren().add(leftTree.getTreeGroup());
				startX+=spacing;
				childStem = new Line(startX,childLine.getStartY(),startX,childLine.getStartY()+40);
				lines.add(childStem);
				effectLines();
				childX = startX - (int)(rootNode.getRectangle().getWidth()/2);
				childY = (int)childStem.getEndY();
				SubTree rightTree = new SubTree(currentIndex*2+2,startingScale,childX,childY);
				subtreeGroup.getChildren().addAll(childStem,rightTree.getTreeGroup());
				
			}
			
			
			
		}
		
		/**
		 * Set the style for each line
		 */
		private void effectLines() {
			for(Line l: lines) {
				l.setStrokeWidth(5);
				l.setStroke(Color.RED);
			}
		}
		
		/**
		 * Set the listeners for the RoomNode
		 */
		private void setListeners() {
			rootNode.setOnMouseClicked(click);
			rootNode.setOnMouseEntered(enter);
			rootNode.setOnMouseExited(exit);
		}
		
		/**
		 * Calculate the number for current SubTree
		 * @return
		 */
		private int numChildren() {
			int count = 0;
			if(!(currentIndex*2+1 >= bracket.size())) {
				count++;
			}
			if(!(currentIndex*2+2 >= bracket.size())) {
				count++;
			}
			return count;
		}
		
		/** 
		 * Get the Group that contains SubTree components
		 * @return - Group of SubTree
		 */
		public Group getTreeGroup() {
			return subtreeGroup;
		}
		
		/**
		 * Handles events where user clicks on RoomNode by setting the
		 * center pane from BorderPane to a RoomPane and updating
		 * indexPointer to the index of the RoomNode for quick retrieval.
		 * 
		 * To-Do
		 * - Handle events where user clicks on RoomNodes in a higher level
		 * without simulating all the RoomNodes in the level below
		 */
		private EventHandler<MouseEvent> click = mouseEvent -> {
			RoomNode node = (RoomNode)mouseEvent.getSource();
			if(!node.getRoom().hasNull()) {
				indexPointer = roomIndex.get(node);
				
				RoomPane pane = new RoomPane(node.getRoom());
				pane.add(TournamentPane.this);
				Group group = new Group();
				group.getChildren().add(pane);
				mainGui.setCenter(pane);
				
			}
			
		};
		
		/**
		 * Create an inner shadow whenever user hovers mouse over RoomNode
		 */
		private EventHandler<MouseEvent> enter = mouseEvent -> {
			RoomNode node = (RoomNode)mouseEvent.getSource();
			node.getRectangle().setEffect(new InnerShadow(30, Color.BLUE));
			
		};
		
		/**
		 * Sets the effect back to null whenever user exits the RoomNode
		 */
		private EventHandler<MouseEvent> exit = mouseEvent -> {
			RoomNode node = (RoomNode)mouseEvent.getSource();
			node.getRectangle().setEffect(null);
		};
		
		/**
		 * Set the ToolTip for each node
		 * 
		 * To-Do
		 * - Update the tooltip whenever the bracket is updated 
		 * (i.e whenver a player moves up in the bracket)
		 * @param node - Node to set tooltip on
		 */
		public void setToolTip(RoomNode node) {
			Tooltip toolTip = new Tooltip(node.getRoom().toString());
			toolTip.setAutoHide(false);
			toolTip.setStyle("-fx-font-size: 16px;");
			toolTip.setAnchorX(toolTip.getAnchorX() + 6);
			toolTip.setAnchorY(toolTip.getAnchorY());
			Tooltip.install(node, toolTip);
			
			
		}
		
		
	}
	
	
	
	
	
	
	/**
	 * Nested class to represent a Room as a
	 * rectangle in the GUI
	 * @author zionchilagan
	 *
	 */
	private class RoomNode extends StackPane {
		
		/** Rectangle to hold the label for Room */
		private Rectangle rectangle;
		/** Room that RoomNode encapsulates */
		private Room room;
		/** Text to set the RoomNode */
		private Text text;
		
		/**
		 * Constructs a RoomNode with given Room by
		 * creating a Rectangle and setting the Room's 
		 * toString as the text
		 * @param room - Room to get information for RoomNode
		 */
		public RoomNode(Room room) {
			this.room = room;
			rectangle = new Rectangle(80,50);
			rectangle.setFill(Color.WHITE);
			rectangle.setStroke(Color.BLACK);
			rectangle.setStrokeWidth(2);
			text = new Text();
			if(!room.isEmpty()) 
				text.setText(room.toString());
			else
				text.setText("Empty");
			getChildren().addAll(rectangle,text);
			
			
		}
		
		/**
		 * Constructs a RoomNode with given coordinates
		 * @param room - Room to get information for RoomNode
		 * @param x - X coordinate of RoomNode
		 * @param y - Y coordinate of RoomNode
		 */
		public RoomNode(Room room,int x, int y) {
			this(room);
			this.setLayoutX(x);
			this.setLayoutY(y);
		
		}
		
		/**
		 * Get the Room in this RoomNode
		 * @return - Room in RoomNode
		 */
		public Room getRoom() {
			return room;
		}
		
		/**
		 * Get the Rectangle in this RoomNode
		 * @return - Rectangle in RoomNode
		 */
		public Rectangle getRectangle() {
			return rectangle;
		}
		
		/**
		 * String representation for RoomNode
		 */
		@Override
		public String toString() {
			return room.toString();
		}
		
		/**
		 * Get the hashCode for this RoomNode which
		 * utilizes Room's hash
		 */
		@Override
		public int hashCode() {
			return room.hashCode();
		}
		
		/**
		 * Update the text for RoomNode if
		 * a new Player gets mapped to Room
		 */
		public void refresh() {
			if(!room.isEmpty()) 
				text.setText(room.toString());
			else
				text.setText("Empty");
		}
		
	}

	

}
