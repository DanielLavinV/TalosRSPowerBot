package talos.util;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Locations {
	
	public ArrayList<MapNode> mapNodes = new ArrayList<MapNode>();
	public HashMap<String, Integer> nameIndexMap = new HashMap<String, Integer>();
	
	public void createMapNodes() {
		try {
			Object obj = new JSONParser().parse(new FileReader("locations.json"));
			JSONObject locations = (JSONObject) obj; //the whole JSON array
			
			JSONArray jarr = (JSONArray) locations.get("areas"); //get the areas subarray
			Iterator ite = jarr.iterator();
			
			while(ite.hasNext()) { //for every area
				JSONObject jarea = (JSONObject) ite.next();
				String nodeName = (String) jarea.get("name"); //get the name
								
				int sx = Math.toIntExact((Long)((JSONObject) jarea.get("start")).get("x")); //get the start coordinates
				int sy = Math.toIntExact((Long)((JSONObject) jarea.get("start")).get("y"));
				int sz = Math.toIntExact((Long)((JSONObject) jarea.get("start")).get("z"));
				int ex = Math.toIntExact((Long)((JSONObject) jarea.get("end")).get("x")); //get the end coordinates
				int ey = Math.toIntExact((Long)((JSONObject) jarea.get("end")).get("y"));
				int ez = Math.toIntExact((Long)((JSONObject) jarea.get("end")).get("z"));
				Area area = new Area(new Tile(sx,sy,sz), new Tile(ex,ey,ez)); //create a new area
//				System.out.println("Added node: " + nodeName);
				mapNodes.add(new MapNode(nodeName, area)); //and a node map with that area
			}
			
			jarr  = (JSONArray) locations.get("tileareas"); //now get the tileareas subarray
			ite = jarr.iterator();
			
			while(ite.hasNext()) {
				JSONObject jtilearea = (JSONObject) ite.next();
				String nodeName = (String) jtilearea.get("name"); //get the name 
				Iterator ite2 = ((JSONArray)jtilearea.get("tiles")).iterator();
				Tile[] tiles = new Tile[((JSONArray)jtilearea.get("tiles")).size()]; //create a new Tile[] array for the MapNode with a size
																					//equal to the number of tiles defined in the json file				
				int i = 0;
				while(ite2.hasNext()) {
					JSONObject tile = (JSONObject) ite2.next();
					int sx = Math.toIntExact((Long) tile.get("x")); //get the coordinates for each tile
					int sy = Math.toIntExact((Long) tile.get("y"));
					int sz = Math.toIntExact((Long) tile.get("z"));
					tiles[i] = new Tile(sx,sy,sz); //create the tile and add it to the Tile[] array
					i++;
				}
//				System.out.println("Added node: " + nodeName);
				mapNodes.add(new MapNode(nodeName, tiles)); //create a MapNode with the Tile[] array and name
			}
			
			for (MapNode mn : mapNodes) {
				nameIndexMap.put(mn.name, mapNodes.indexOf(mn));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public void connectMapNodes() { //TODO: handle the instantiation of the "siblings" element of each MapNode using the json file information
		try {
			Object obj = new JSONParser().parse(new FileReader("locations.json"));
			JSONObject locations = (JSONObject) obj; //the whole JSON array
			
			JSONArray jarr = (JSONArray) locations.get("areas"); //get the areas subarray
			Iterator ite = jarr.iterator();
			
			
			while(ite.hasNext()) { //for every area
				JSONObject jarea = (JSONObject) ite.next();
				String nodeName = (String) jarea.get("name"); //get the name
				
//				System.out.println("Nodename: " + nodeName);
				
				Iterator ite2 = ((JSONArray)jarea.get("siblings")).iterator();
				ArrayList<MapNode> siblings = new ArrayList<MapNode>();																	//equal to the number of siblings defined in the json file
				
				int i = 0;
				while(ite2.hasNext()) { //iterate through the siblings
					String sibling = ite2.next().toString();
					
					for(int k = 0; k < mapNodes.size(); k++) {
						if(mapNodes.get(k).name.equals(sibling)) {
							siblings.add(i,mapNodes.get(k));
						}
					}
					i++;
				}
				
				for(int j = 0; j < mapNodes.size() ; j++) {
					if(mapNodes.get(j).name.equals(nodeName)) {
						mapNodes.get(j).siblings = siblings;	
					}
				}	
			}
			
			jarr = (JSONArray) locations.get("tileareas"); //get the areas subarray
			ite = jarr.iterator();
			
			
			while(ite.hasNext()) { //for every area
				JSONObject jarea = (JSONObject) ite.next();
				String nodeName = (String) jarea.get("name"); //get the name
				
				Iterator ite2 = ((JSONArray)jarea.get("siblings")).iterator();
				ArrayList<MapNode> siblings = new ArrayList<MapNode>();																	//equal to the number of siblings defined in the json file
				
//				System.out.println("Nodename: " + nodeName);
				
				int i = 0;
				while(ite2.hasNext()) { //iterate through the siblings
					String sibling = ite2.next().toString();
					
//					System.out.println("\t Sibling name: " + sibling);					
					
					for(int k = 0; k < mapNodes.size(); k++) {
						if(mapNodes.get(k).name.equals(sibling)) {
							siblings.add(i,mapNodes.get(k));
						}
					}
					i++;
				}
				
				for(int j = 0; j < mapNodes.size() ; j++) {
					if(mapNodes.get(j).name.equals(nodeName)) {
						mapNodes.get(j).siblings = siblings;	
					}
				}	
			}			
			
			for(MapNode mn : mapNodes) {
//				System.out.print("Node: " + mn.name + " has siblings: ");
				for(MapNode sib : mn.siblings) {
//					System.out.print(sib.name + " ");
				}
//				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static Tile TILEBETWEEN[]			={new Tile(3088,3475)};
    public static Area A_VARROCK_EASTBANK		=new Area(new Tile(3250,3422),new Tile(3258,3419));
    public static Area A_VARROCK_EAST			=new Area(new Tile(3246,3476),new Tile(3252,3470));
    public static Area A_GE_BANK				=new Area(new Tile(3162,3486),new Tile(3171,3492));
    public static Area A_GE_YEWS				=new Area(new Tile(3201,3506),new Tile(3226,3498));
    public static Area A_DRAYNOR_YEWS 		=new Area(new Tile(3049,3273,0),new Tile(3059, 3268,0));
    public static Area A_DRAYNOR_BANK			=new Area(new Tile(3091,3247),new Tile(3098,3240));
    public static Area A_LUMB_SOUTH			=new Area(new Tile(3245,3199),new Tile(3250,3205));
}
