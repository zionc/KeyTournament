import java.util.ArrayList;
import java.util.Hashtable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * TournamentPane creates the GUI for the tournament
 * @author zionchilagan
 *
 */

public class TournamentPane extends StackPane implements Observer {
	
	private ArrayList<Room> bracket;
	private Group treeGroup;
	private Tournament tournament;
	private int height;
	private BorderPane mainGui;
	private Hashtable<RoomNode,Integer> roomIndex;
	private Hashtable<Integer,RoomNode> getRoom;
	private int indexPointer;
	private RoomNode roomPointer;
	
	public TournamentPane(BorderPane mainGui,Tournament tournament) {
		this.mainGui = mainGui;
		treeGroup = new Group();
		this.tournament = tournament;
		bracket = this.tournament.getBracket();
		height = tournament.level(bracket.get(bracket.size()-1));
		System.out.println("HEIGHT: " + height);
		roomIndex = new Hashtable<RoomNode,Integer>();
		getRoom = new Hashtable<Integer,RoomNode>();
		indexPointer = 0;
		roomPointer = null;
		SubTree sub = new SubTree();
		treeGroup.getChildren().add(sub);
	
		this.setStyle("-fx-background-color: #f1faee;");
		setAlignment(treeGroup,Pos.CENTER);
		setMargin(treeGroup,new Insets(10,0,0,0));
		getChildren().add(treeGroup);
	}
	
	
	
	private class SubTree extends StackPane {
		private Group subtreeGroup;
		private RoomNode rootNode;
		private Line stem,childLine,childStem;
		private int startingScale;
		private int currentIndex;
		private ArrayList<Line> lines;
		
		public SubTree() {
			this(0);
		}
		public SubTree(int index) {
			currentIndex = index;
			
			lines = new ArrayList<Line>();
			rootNode = new RoomNode(bracket.get(currentIndex));
			//setToolTip(rootNode);
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
		
		
		public SubTree(int index,int scale,int x, int y) {
			currentIndex = index;
			lines = new ArrayList<Line>();
			rootNode = new RoomNode(bracket.get(index),x,y);
			//setToolTip(rootNode);
			roomIndex.put(rootNode, index);
			getRoom.put(index, rootNode);
			setListeners();
			subtreeGroup = new Group();
			startingScale = scale/height;
			
			subtreeGroup.getChildren().add(rootNode);
			drawConnections();
			getChildren().add(subtreeGroup);
		}
		
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
		
		private void drawChildStems(Line childLine) {
			int length = (int)(childLine.getEndX() - childLine.getStartX());
			int spacing = 0;
			int numChildren = numChildren();
			System.out.println("NUM CHILDREN: " + numChildren);
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
		private void effectLines() {
			for(Line l: lines) {
				l.setStrokeWidth(5);
				l.setStroke(Color.RED);
			}
		}
		
		private void setListeners() {
			rootNode.setOnMouseClicked(click);
			rootNode.setOnMouseEntered(enter);
			rootNode.setOnMouseExited(exit);
		}
		
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
		
		public Group getTreeGroup() {
			return subtreeGroup;
		}
		
		private EventHandler<MouseEvent> click = mouseEvent -> {
			RoomNode node = (RoomNode)mouseEvent.getSource();
			if(!node.getRoom().isEmpty()) {
				indexPointer = roomIndex.get(node);
				
				RoomPane pane = new RoomPane(node.getRoom());
				pane.add(TournamentPane.this);
				Group group = new Group();
				group.getChildren().add(pane);
				mainGui.setCenter(pane);
				
			}
			System.out.println(node);
		};
		
		private EventHandler<MouseEvent> enter = mouseEvent -> {
			RoomNode node = (RoomNode)mouseEvent.getSource();
			node.getRectangle().setEffect(new InnerShadow(30, Color.BLUE));
			
		};
		
		private EventHandler<MouseEvent> exit = mouseEvent -> {
			RoomNode node = (RoomNode)mouseEvent.getSource();
			node.getRectangle().setEffect(null);
		};
		

		public void setToolTip(RoomNode node) {
			Tooltip toolTip = new Tooltip(node.getRoom().toString());
			toolTip.setAutoHide(false);
			toolTip.setStyle("-fx-font-size: 16px;");
			toolTip.setAnchorX(toolTip.getAnchorX() + 6);
			toolTip.setAnchorY(toolTip.getAnchorY());
			Tooltip.install(node, toolTip);
			System.out.println(toolTip.isActivated());
			
		}
		
		
	}
	
	
	
	
	
	
	/**
	 * Nested class to represent a Room as a
	 * rectangle in the GUI
	 * @author zionchilagan
	 *
	 */
	private class RoomNode extends StackPane {
		
		private Rectangle rectangle;
		private Room room;
		private Text text;
		private Tooltip toolTip;
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
			setToolTip();
			
		}
		public RoomNode(Room room,int x, int y) {
			this(room);
			this.setLayoutX(x);
			this.setLayoutY(y);
		
		}
		
		public Room getRoom() {
			return room;
		}
		
		public Rectangle getRectangle() {
			return rectangle;
		}
		
		public String toString() {
			return room.toString();
		}
		
		public void refresh() {
			if(!room.isEmpty()) 
				text.setText(room.toString());
			else
				text.setText("Empty");
		}
		
		public void setToolTip() {
			toolTip = new Tooltip("Test");
			toolTip.setText(room.toString());
			Tooltip.install(this, toolTip);
			
		}
		
	}






	@Override
	public void update() {
		Player player = bracket.get(indexPointer).getWinner();
		if(indexPointer > 0) {
			tournament.movePlayerUp(player, indexPointer);
		
			roomPointer = getRoom.get((indexPointer-1)/2);
			roomPointer.refresh();
		
			System.out.println(roomPointer.getRoom());
		
			tournament.displayTournament();
			mainGui.setCenter(this);
		}
		else {
			mainGui.setCenter(this);
			Alert a = new Alert(AlertType.INFORMATION); 
			a.setContentText("Congrats: " + player.getName() + "!!!");
			a.showAndWait();
		}
		
		
	}

}
