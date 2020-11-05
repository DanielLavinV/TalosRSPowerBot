package talos.util;

import java.util.ArrayList;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public class MapNode {
	public Area area;
	public Tile[] tiles;
	public MapNode parentNode;
	public ArrayList<MapNode> childNodes = new ArrayList<MapNode>();
	public ArrayList<MapNode> siblings = new ArrayList<MapNode>();
	public String name;
	
	public MapNode() {
		
	}
	
	public MapNode(String name, Area area) {
		this.name = name;
		this.area = area;
		this.tiles = null;
	}
	
	public MapNode(String name, Tile[] tiles) {
		this.name = name;
		this.area = null;
		this.tiles = tiles;
	}
	
	public void setSiblings(ArrayList<MapNode> siblings) {
		this.siblings = siblings;
	}
	
	public void setParentNode(MapNode node) {
		this.parentNode = node;
	}
	
	public void setChildNodes(ArrayList<MapNode> nodes) {
		this.childNodes = nodes;
	}
	
	public MapNode getParentNode() {
		return this.parentNode;
	}
	
	public ArrayList<MapNode> getChildNodes() {
		return this.childNodes;
	}
	
	public ArrayList<MapNode> getSiblings() {
		return this.siblings;
	}
	
}

