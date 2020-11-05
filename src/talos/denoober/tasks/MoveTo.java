package talos.denoober.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.LocalPath;
import org.powerbot.script.rt4.TilePath;

import talos.denoober.Task;
import talos.util.Locations;
import talos.util.MapNode;

public class MoveTo extends Task{
	
	public Locations mapper;
	public MapNode destination;
	public MapNode currentLoc;
	public Random rdm = new Random();
	public int attempts2path = 0;

	public MoveTo(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public MoveTo(ClientContext ctx, Locations mapper) {
		super(ctx);
		this.mapper = mapper;
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		if(destination.area == null) {
			System.out.println("MOVETO: Destination is a tileset.");
			boolean nothere = true;
			outerouter:
			while(nothere) {
				System.out.println("MOVETO: Generating tile path.");
				Tile[] tilepath = generateTilePath();
				System.out.println("MOVETO: Generating walkable.");
				TilePath path = ctx.movement.newTilePath(tilepath);
				if(path.toArray().length == 0) {
					System.out.println("MOVETO: Already there.");
					break outerouter;
				}
				path.traverse();
				
				Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	
				
				for(Tile t : destination.tiles) {
					if(t.compareTo(ctx.players.local().tile()) == 0) {
						nothere = false;
						break;
					}
				}
			}
		}
		
		if(destination.tiles == null) {
			System.out.println("MOVETO: Destination is an area.");
			while(!destination.area.contains(ctx.players.local().tile())) {
				System.out.println("MOVETO: Generating tile path.");
				Tile[] tilepath = generateTilePath();
				System.out.println("MOVETO: Generating walkable.");
				TilePath path = ctx.movement.newTilePath(tilepath);
				if(!path.valid()) {
					navigateXYTo(destination.area.getRandomTile());
				} else {
					path.traverse();
				}
				Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	
			}
		}
	}

	public void setDestination(MapNode dest) {
		this.destination = dest;
	}
	
	public Tile[] generateTilePath() {
		
		/**
		 * First find the current location.
		 * */
		
		Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	 //wait until the player stops moving
		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.currentLoc = findCurrentLocation(); //then attempt to locate.
		
		/**
		 * Now, generate a tree using currentLoc as root node
		 * */
		
		MapNode rootNode = this.currentLoc;
		
		System.out.println("MOVETO: Currently in: " + rootNode.name + " and going to: " + this.destination.name);
		
		rootNode.setChildNodes(rootNode.getSiblings());
		rootNode.setParentNode(rootNode);
				
		for(MapNode mn : rootNode.getSiblings()) {
//			System.out.println("MOVETO: Constructing tree for node: " + mn.name);
			this.mapper.mapNodes = tree(this.mapper.mapNodes,rootNode, mn); //recursively construct the tree
		}
		
		for (MapNode mn : this.mapper.mapNodes) {
			if(mn.getParentNode() == null) {
				
			} else {
//			System.out.println("MOVETO: Node: " + mn.name);
//			System.out.println("MOVETO: \t Parent: " + mn.getParentNode().name);
//			for(MapNode sib : mn.getChildNodes()) {
//				System.out.println("MOVETO: \t\t Child: " + sib.name);
//			}
			}
		}
		
		/**
		 * Now all nodes know their children and their parent. 
		 * Recursively ask from the destination node who their papa is.
		 * */
		
		MapNode asking = this.destination;
		ArrayList<Tile> tilepath = new ArrayList<Tile>();
		
		while(asking != rootNode) {
			MapNode paparent = asking.getParentNode();
			if(asking.tiles == null) {
				tilepath.add(asking.area.getRandomTile());
			} else {
				if(asking.tiles.length == 1)
					tilepath.add(asking.tiles[0]);
				else
					tilepath.add(asking.tiles[rdm.nextInt(asking.tiles.length-1)]);
			}
			asking = paparent;
		}
		
		Collections.reverse(tilepath);
		
		Tile[] rtilepath = new Tile[tilepath.size()];
		int i = 0;
		for (Tile t : tilepath) {
			rtilepath[i] = t;
			i++;
		}
		
		return rtilepath;
	}
	
	public void attemptToGetInPath () {
		double shortest = 10000;
		double dist = 0;
		MapNode gotos = null;
		for(MapNode mn : this.mapper.mapNodes) {
			if(mn.area == null) {
				boolean validlength = mn.tiles.length != 1 ? true : false;
				if(validlength) {
					dist = ctx.players.local().tile().distanceTo(mn.tiles[rdm.nextInt(mn.tiles.length-1)]);
				} else {
					dist = ctx.players.local().tile().distanceTo(mn.tiles[0]);
				}
			} else if(mn.tiles == null) {
				dist = ctx.players.local().tile().distanceTo(mn.area.getCentralTile());
			}
			if (dist < shortest) {
				shortest = dist;
				gotos = mn;
			}
		}
		System.out.println("MOVETO: Closest map node: " + gotos.name);
		if(gotos.tiles == null) {
			LocalPath path = ctx.movement.findPath(gotos.area.getClosestTo(ctx.players.local().tile()));
			if(!path.valid() || this.attempts2path > 5) {
				System.out.println("MOVETO: Failed normal attempts. NavigatingXY...");
				navigateXYTo(gotos.area.getCentralTile());
			}
			path.traverse();
		} else if (gotos.area == null) {
			LocalPath path = ctx.movement.findPath(gotos.tiles[rdm.nextInt(gotos.tiles.length-1)]);
			if(!path.valid() || this.attempts2path > 5) {
				System.out.println("MOVETO: Failed normal attempts. NavigatingXY...");
				navigateXYTo(gotos.tiles[rdm.nextInt(gotos.tiles.length-1)]);
			}
			path.traverse();
		}
		this.attempts2path++;
	}
	
	public MapNode findCurrentLocation() {
		Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	
		MapNode here = null;
		for(MapNode mn : this.mapper.mapNodes) {
			if (mn.tiles == null) {
				if (mn.area.contains(ctx.players.local().tile())) {
					here = mn;
					break;
				}
			} else if (mn.area == null) {
				for (Tile t : mn.tiles) {
					if (t.compareTo(ctx.players.local().tile()) == 0) {
						here = mn;
						break;
					}
				}
			}
		}
		
		if(here == null) {
			System.out.println("MOVETO: Couldn't locate player! - outside of defined zones");
			System.out.println("MOVETO: Attempting to get back to path...");
			attemptToGetInPath();
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch(Exception e) {
				e.printStackTrace();
			}
			here = findCurrentLocation();
		} else {
			this.attempts2path = 0;
		}
		
		return here;
	}	
	
	public Tile[] generateTilePath(MapNode fromHere, MapNode toHere) {
		
		/**
		 * Generate a tree using fromHere as root node
		 * */
		
		MapNode rootNode = fromHere;
		
		System.out.println("MOVETO: Generating tilepath from: " + rootNode.name + " to: " + toHere.name);
		
		rootNode.setChildNodes(rootNode.getSiblings());
		rootNode.setParentNode(rootNode);
				
		for(MapNode mn : rootNode.getSiblings()) {
//			System.out.println("MOVETO: Constructing tree for node: " + mn.name);
			this.mapper.mapNodes = tree(this.mapper.mapNodes,rootNode, mn); //recursively construct the tree
		}
		
//		for (MapNode mn : this.mapper.mapNodes) {
//			if(mn.getParentNode() == null) {
//				
//			} else {
//			System.out.println("MOVETO: Node: " + mn.name);
//			System.out.println("MOVETO: \t Parent: " + mn.getParentNode().name);
//			for(MapNode sib : mn.getChildNodes()) {
//				System.out.println("MOVETO: \t\t Child: " + sib.name);
//			}
//			}
//		}
		
		/**
		 * Now all nodes know their children and their parent. 
		 * Recursively ask from the destination node who their papa is.
		 * */
		
		MapNode asking = toHere;
		ArrayList<Tile> tilepath = new ArrayList<Tile>();
		
		while(asking != rootNode) {
			MapNode paparent = asking.getParentNode();
			if(asking.tiles == null) {
				tilepath.add(asking.area.getRandomTile());
			} else {
				tilepath.add(asking.tiles[rdm.nextInt(asking.tiles.length-1)]);
			}
			asking = paparent;
		}
		
		Collections.reverse(tilepath);
		
		Tile[] rtilepath = new Tile[tilepath.size()];
		int i = 0;
		for (Tile t : tilepath) {
			rtilepath[i] = t;
			i++;
		}
		
		return rtilepath;
	}	
	
	public MapNode getMapNode(String name) {
		return this.mapper.mapNodes.get(this.mapper.nameIndexMap.get(name));
	}
	
	public ArrayList<MapNode> tree(ArrayList<MapNode> mapNodes, MapNode papa, MapNode tumerengues) {
//		System.out.println("MOVETO: Constructing tree for node: " + tumerengues.name);		
//		System.out.println("MOVETO: Called by (papa): " + papa.name);
		tumerengues.setParentNode(papa);
		ArrayList<MapNode> siblings = new ArrayList<MapNode>(tumerengues.getSiblings());
//		System.out.println("MOVETO: Siblings: ");
//		for(MapNode mn : siblings) {
//			System.out.print(mn.name + " ");
//		}
//		System.out.println();
		siblings.remove(papa); //remove the parent node
//		System.out.println("MOVETO: Siblings after removing papa (children): ");
//		for(MapNode mn : siblings) {
//			System.out.print(mn.name + " ");
//		}
//		System.out.println();
		tumerengues.setChildNodes(siblings);
		for(MapNode mn : mapNodes) {
			if (tumerengues.name.equals(mn.name)) {
				mn = tumerengues;
			}
		}
		for(MapNode mn : tumerengues.getChildNodes()) {
			mapNodes = tree(mapNodes, tumerengues, mn); //recursively construct the tree
		}
		
		return mapNodes;
	}

	public void navigateXYTo(Tile destination) {
		LocalPath path;
		Tile nextLocation;
		Tile currentLocation = ctx.players.local().tile();
		int speed = 6;
		
		int xdif = destination.x() - currentLocation.x();
		int ydif = destination.y() - currentLocation.y();
		
		if(Math.abs(xdif) < 10 || Math.abs(ydif) < 10) {
			speed = 3;
		}
		
		if(xdif == 0) {
			if(ydif > 0) {
				moveNorth(currentLocation, speed);
			} else if( ydif < 0) {
				moveSouth(currentLocation, speed);
			}
			
			return;
		}
		
		int angle = (int) Math.floor((180/Math.PI) * Math.atan(ydif/xdif));
		
		if(xdif > 0) {
			if(-20 < angle && angle < 20) {
				moveEast(currentLocation, speed);
			} else if(20 <= angle && angle < 70) {
				moveNortheast(currentLocation, speed);
			} else if(angle >= 70) {
				moveNorth(currentLocation,speed);
			} else if(-70 <= angle && angle <= -20) {
				moveSoutheast(currentLocation, speed);
			} else if(angle <= -70) {
				moveSouth(currentLocation,speed);
			}
		} else {
			if(-20 < angle && angle < 20) {
				moveWest(currentLocation, speed);
			} else if(20 <= angle && angle < 70) {
				moveNorthwest(currentLocation, speed);
			} else if(angle >= 70) {
				moveNorth(currentLocation,speed);
			} else if(-70 <= angle && angle <= -20) {
				moveSouthwest(currentLocation, speed);
			} else if(angle <= -70) {
				moveSouth(currentLocation,speed);
			}
		}
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() != -1;
			}
		}); //wait until you stop moving
	}

	public void moveNortheast(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x() + 3 + rdm.nextInt(speed), currentLocation.y()+3+rdm.nextInt(speed), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving northeast.");
	}
	public void moveNorth(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x(), currentLocation.y()+3+rdm.nextInt(speed), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving north.");
	}
	public void moveNorthwest(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x()-3-rdm.nextInt(speed), currentLocation.y()+3+rdm.nextInt(speed), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving northwest.");
	}
	public void moveWest(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x()-3-rdm.nextInt(speed), currentLocation.y(), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving west.");
	}
	public void moveSouthwest(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x()-3-rdm.nextInt(speed), currentLocation.y()-3-rdm.nextInt(speed), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving southwest.");
	}
	public void moveSouth(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x()-3-rdm.nextInt(speed), currentLocation.y(), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving south.");
	}
	public void moveSoutheast(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x()+3+rdm.nextInt(speed), currentLocation.y()-3-rdm.nextInt(speed), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving southeast.");
	}
	public void moveEast(Tile currentLocation, int speed) {
		Tile nextLocation = new Tile(currentLocation.x()+3+rdm.nextInt(speed), currentLocation.y(), currentLocation.floor());
		LocalPath path = ctx.movement.findPath(nextLocation);
		path.traverse();
		System.out.println("MOVETO: Moving east.");
	}
}
