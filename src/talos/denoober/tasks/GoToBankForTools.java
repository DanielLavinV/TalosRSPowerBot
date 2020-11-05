package talos.denoober.tasks;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.LocalPath;
import org.powerbot.script.rt4.TilePath;
import org.powerbot.script.rt4.Bank;
import java.util.logging.Logger;

import talos.denoober.Task;
import talos.util.*;

public class GoToBankForTools extends Task{

	public Random rdm = new Random();
	public MoveTo mover;
	int stairsId = 16671;
	int stairsId2 = 16672;
	int stairsId3 = 16673;
	int bankBoothId = 18491;
	public int[] tools = null;
	public int[] quantities = null;
	public MapNode taskZone;
	public ArrayList<Integer> missingTools = new ArrayList<Integer>();	
	boolean atleast = false;
	
	public GoToBankForTools(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public GoToBankForTools(ClientContext ctx, MoveTo mover, int[] tools, MapNode taskZone) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.mover = mover;
		this.tools = tools;
		this.taskZone = taskZone;
	}	

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		
		if(this.taskZone.name.equals("EDGS")) { //if we gonnna smith just go to edgeville bank
			edgevilleBank();
			return;
		}
		
		MapNode currentLoc = this.mover.findCurrentLocation();
		Tile[] here2lumb = this.mover.generateTilePath(currentLoc, this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCB")));
		Tile[] here2varr = this.mover.generateTilePath(currentLoc, this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("VWBK")));
		Tile[] task2lumb = this.mover.generateTilePath(taskZone, this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCB")));
		Tile[] task2varr = this.mover.generateTilePath(taskZone, this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("VWBK")));
		
		if((here2lumb.length+task2lumb.length) <= (here2varr.length+task2varr.length)) {
			lumbridgeBank();
		} else {
			varrockWestBank();
		}
		
		return;
	}
	
	public void configure(MoveTo mover, int[] tools, MapNode taskZone) {
		this.mover = mover;
		this.tools = tools;
		this.taskZone = taskZone;
		this.atleast = false;
		this.quantities = null;
	}
	
	public void configure(MoveTo mover, int[] tools, int[] quantities, MapNode taskZone) {
		this.mover = mover;
		this.tools = tools;
		this.quantities = quantities;
		this.taskZone = taskZone;
		this.atleast = false;
	}
	
	public void configure(MoveTo mover, int[] tools, int[] atleast, boolean alst, MapNode taskZone) {
		this.mover = mover;
		this.tools = tools;
		this.quantities = atleast;
		this.taskZone = taskZone;
		this.atleast = alst;
	}	
	
	public void edgevilleBank() {
		this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("EDGB")));
		this.mover.execute(); //first go to varrock bank
		
		Condition.sleep(100 + rdm.nextInt(500));
		
		ctx.camera.turnTo(ctx.bank.nearest());
		
		Condition.wait(() -> ctx.bank.open(), 100, 300);	 //open bank

		if(ctx.bank.opened()) { //just in case
			
			ctx.bank.depositInventory(); //dump inventory
			
			Condition.sleep(200+rdm.nextInt(500));
			
			if(this.quantities == null) {
				System.out.println("GOTOBANK: withdrawing without quantities...");
				this.missingTools = withdraw();
			} else if(this.atleast) {
				System.out.println("GOTOBANK: withdrawing with at least quantities...");
				this.missingTools = withdrawAtLeast();
				this.quantities = null;
			} else {
				System.out.println("GOTOBANK: withdrawing with quantities...");
				this.missingTools = withdraw(1);
				this.quantities = null;
			}
			
			ctx.bank.close(); //close the bank
		}
	}	
	
	public void varrockWestBank() {
		this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("VWBK")));
		this.mover.execute(); //first go to varrock bank
		
		try {
			TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ctx.camera.turnTo(ctx.bank.nearest());
		
		Condition.wait(() -> ctx.bank.open(), 100, 300);	 //open bank

		if(ctx.bank.opened()) { //just in case
			
			ctx.bank.depositInventory(); //dump inventory
			
			Condition.sleep(200+rdm.nextInt(500));
			
			if(this.quantities == null) {
				System.out.println("GOTOBANK: withdrawing without quantities...");
				this.missingTools = withdraw();
			} else if(this.atleast) {
				System.out.println("GOTOBANK: withdrawing with at least quantities...");
				this.missingTools = withdrawAtLeast();
				this.quantities = null;
			} else {
				System.out.println("GOTOBANK: withdrawing with quantities...");
				this.missingTools = withdraw(1);
				this.quantities = null;
			}
			
			ctx.bank.close(); //close the bank
		}
	}
	
	public void lumbridgeBank() {
		if(ctx.players.local().tile().floor() == 0) { //on ground floor
			this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCSF0")));
			this.mover.execute(); //first go to stairs in lumbdrige castle
			
			while(ctx.players.local().tile().floor() != 2) {
				System.out.println("GOTOBANK:Attempting to climb stairs...");
				GameObject stairs = ctx.objects.select().id(Identifiers.LUMBRIDGE_CASTLE_NORTH_STAIRS_F0,Identifiers.LUMBRIDGE_CASTLE_NORTH_STAIRS_F1).nearest().poll(); // once you're there, use the stairs
				ctx.camera.turnTo(stairs.tile());
				stairs.interact("Climb-up"); //climb up the stairs until you're at bank floor
				try {
					TimeUnit.MILLISECONDS.sleep(500 + rdm.nextInt(1200));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	 //wait until you stop moving
			
			this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCB")));
			this.mover.execute();  //then actually go to the bank
			
		} else { // already at bank floor
			this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCB")));
			this.mover.execute(); 
		}
		
		Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	
		
		/**
		 * We're now at the bank area on top of Lumbdrige castle.
		 * **/
		
		ctx.camera.turnTo(ctx.bank.nearest());
		
		Condition.wait(() -> ctx.bank.open(), 100, 300);	 //open bank

		if(ctx.bank.opened()) { //just in case
			
			ctx.bank.depositInventory(); //dump inventory
			
			Condition.sleep(100 + rdm.nextInt(500));
			
			if(this.quantities == null) {
				System.out.println("GOTOBANK: withdrawing without quantities...");
				this.missingTools = withdraw();
			} else if(this.atleast) {
				System.out.println("GOTOBANK: withdrawing with at least quantities...");
				this.missingTools = withdrawAtLeast();
				this.quantities = null;
			} else {
				System.out.println("GOTOBANK: withdrawing with quantities...");
				this.missingTools = withdraw(1);
				this.quantities = null;
			}
			
			ctx.bank.close(); //close the bank
		}
		
		/**
		 * Now that we have the tools we require, we can go back to the Lumbdrige courtyard.
		 * **/
		
		this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCSF2")));
		this.mover.execute(); 
		
		Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	 //wait until you stop moving
		
		while(ctx.players.local().tile().floor() != 0) { //do this until you're at ground floor
			System.out.println("GOTOBANK:Attempting to climb down stairs...");
			GameObject stairs = ctx.objects.select().id(Identifiers.LUMBRIDGE_CASTLE_NORTH_STAIRS_F2,Identifiers.LUMBRIDGE_CASTLE_NORTH_STAIRS_F1).nearest().poll(); // once you're there, use the stairs
			stairs.interact("Climb-down"); //climb down the stairs until you're at ground floor
			try {
				TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.mover.setDestination(this.mover.mapper.mapNodes.get(this.mover.mapper.nameIndexMap.get("LCC")));
		this.mover.execute(); 
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() != -1;
			}
		}); //wait until you stop moving
	}
	
	public ArrayList<Integer> withdraw() {
		ArrayList<Integer> missingTools = new ArrayList<Integer>();
		
		int tries = 0;
		int toolsnothere = 0;
		while(tries < 3) {
			if(ctx.bank.select().id(this.tools).count() != this.tools.length)
				toolsnothere++;
			tries++;
			Condition.sleep(500);
		}

		if(toolsnothere >= 2) {
			System.out.println("GOTOBANK:Tools not found in bank.");
			System.out.print("GOTOBANK:Missing tools are: ");
			for(int i : this.tools) {
				if(ctx.bank.select().id(i).count() == 0) {
					System.out.print(" " + i);
					missingTools.add(i);
				}
			System.out.println();
			}
		} else {
			System.out.println("GOTOBANK:Tools successfully retrieved from bank.");
		popa:
			while(true) {
				for(int i : this.tools) { //get the tools for the task ahead
					ctx.bank.withdraw(i, 0);
					try {
						TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if(ctx.bank.select().id(this.tools).count(true) == 0) {
					break popa;
				}
			}
		}
		Condition.wait(() -> ctx.bank.close(), 1000, 3);

		return missingTools;
	}
	
	public ArrayList<Integer> withdraw(int qty) {
		ArrayList<Integer> missingTools = new ArrayList<Integer>();
		
		int tries = 0;
		boolean[] faltan = new boolean[this.tools.length];
		int inBank[] = new int[this.tools.length];
		while(tries < 3) {
			for(int i = 0; i < this.tools.length; i++) {
				inBank[i] = ctx.bank.select().id(this.tools[i]).count(true);
				if(inBank[i] < this.quantities[i]) //if what you have on the bank is not what you expected
					faltan[i] = true;
				else
					faltan[i] = false;
			}
			tries++;
			Condition.sleep(500);
		}

		boolean sifa = false;
		for(boolean b : faltan) {
			if(b) //if this tool is missing
				sifa = true; //notifiy that at least 1 tool is missing
		}
		
		if(sifa) {
			System.out.println("GOTOBANK: Tools not found in bank.");
			System.out.print("GOTOBANK: Missing tools are: ");
			for(int i=0; i < this.tools.length; i++) {
				if(ctx.bank.select().id(this.tools[i]).count(true) < this.quantities[i]) {
					System.out.print(" " + this.tools[i]);
					missingTools.add(this.tools[i]);
				}
			}
			System.out.println();
		} else {
			System.out.println("GOTOBANK:Tools successfully retrieved from bank.");
			
		popa:
			while(true) {
				for(int i = 0; i < this.tools.length; i++) { //get the tools for the task ahead
					ctx.bank.withdraw(this.tools[i], this.quantities[i]);
					try {
						TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
						
				int suma = 0;
				for(int i : this.quantities) {
					suma = suma + i;
				}

				if(ctx.inventory.select().count() >= suma) {
					break popa;
				}
			}
		}
		Condition.wait(() -> ctx.bank.close(), 1000, 3);

		return missingTools;
	}

	public ArrayList<Integer> withdrawAtLeast() {
		ArrayList<Integer> missingTools = new ArrayList<Integer>();
		int lotSize = 0;
		for(int i : this.quantities) {
			lotSize = lotSize + i; //sum of all the quantities
		}
		if(lotSize > 28) {
			System.out.println("GOTOBANK: Error, can't withdraw <<at least>> more than 28.");
			return null;
		}
		int maxLots = (int) Math.floor(28/lotSize); //how many complete lots we can hold
		int tries = 0;
		boolean[] faltan = new boolean[this.tools.length];
		int inBank[] = new int[this.tools.length];
		while(tries < 3) { //try three times because sometimes bank.count() glitches and returns bad info
			for(int i = 0; i < this.tools.length; i++) {
				inBank[i] = ctx.bank.select().id(this.tools[i]).count(true);
				if(inBank[i] < this.quantities[i]) //if what you have on the bank is not what you expected
					faltan[i] = true;
				else
					faltan[i] = false;
			}
			tries++;
			Condition.sleep(500);
		}

		boolean sifa = false;
		for(boolean b : faltan) {
			if(b) //if this tool is missing
				sifa = true;
		}
		
		if(sifa) {
			System.out.println("GOTOBANK: Tools not found in bank.");
			System.out.print("GOTOBANK: Missing tools are: ");
			for(int i=0; i < this.tools.length; i++) {
				if(inBank[i] != this.quantities[i]) {
					System.out.print(" " + this.tools[i]);
					missingTools.add(this.tools[i]);
				}
			}
			System.out.println();
		} else {	
		System.out.println("GOTOBANK:Tools successfully retrieved from bank.");
	
		int smolest = 10000;
		for(int i = 0; i <this.quantities.length;i++) {
			
			if((int)Math.floor(inBank[i] / this.quantities[i]) < smolest) // find the smallest number of lots we can form with what we have on our bank
				smolest = (int)Math.floor(inBank[i] / this.quantities[i]);
		}

		if(maxLots < smolest) { //make sure that the smallest number is not bigger than what we can hold
			smolest = maxLots;
		}
		
		int[] toWithdraw = new int[this.tools.length];
		for(int i = 0 ; i<this.quantities.length;i++) {
			toWithdraw[i] = smolest * this.quantities[i];
		}		
		
		popa:
			while(true) {
				for(int i = 0; i < this.tools.length; i++) { //get the tools for the task ahead
					ctx.bank.withdraw(this.tools[i], toWithdraw[i]);
					try {
						TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
//				boolean[] faltan2 = new boolean[this.tools.length];
//				for(int i = 0; i < this.tools.length; i++) {
//					if(ctx.inventory.select().id(this.tools[i]).count() == 0) //if what you have on the bank is not what you expected
//						faltan2[i] = true;
//					else
//						faltan2[i] = false;
//				}
//
//				boolean sifa2 = false;
//				for(boolean b : faltan2) {
//					if(b) //if this (b) tool is missing
//						sifa2 = true;
//				}				

				if(ctx.inventory.select().count() >= smolest*lotSize) {
					break popa;
				}
			}
		}
		Condition.wait(() -> ctx.bank.close(), 1000, 3);
		return missingTools;
	}
	
	public void navigateXYTo(Area destination) {
		LocalPath path;
		Tile nextLocation;
		Tile currentLocation = ctx.players.local().tile();
		int speed = 10;
		
		int xdif = destination.getRandomTile().x() - currentLocation.x();
		int ydif = destination.getRandomTile().y() - currentLocation.y();
		
		if(xdif < 10 || ydif < 10) {
			speed = 3;
		}
		
		if(xdif >= 0 && ydif > 0) { //path orientation block
			nextLocation = new Tile(currentLocation.x() + 3 + rdm.nextInt(speed), currentLocation.y()+3+rdm.nextInt(speed), currentLocation.floor());
			path = ctx.movement.findPath(nextLocation);
			path.traverse();
			System.out.println("GOTOBANK:Moving northeast.");
		} else if(xdif < 0 && ydif >= 0) {
			nextLocation = new Tile(currentLocation.x()-3-rdm.nextInt(speed), currentLocation.y()+3+rdm.nextInt(speed), currentLocation.floor());
			path = ctx.movement.findPath(nextLocation);
			path.traverse();
			System.out.println("GOTOBANK:Moving northwest.");
		} else if(xdif >= 0 && ydif < 0) {
			nextLocation = new Tile(currentLocation.x()+3+rdm.nextInt(speed), currentLocation.y()-3-rdm.nextInt(speed), currentLocation.floor());
			path = ctx.movement.findPath(nextLocation);
			path.traverse();
			System.out.println("GOTOBANK:Moving southeast.");
		} else if(xdif < 0 && ydif <= 0) {
			nextLocation = new Tile(currentLocation.x()-3-rdm.nextInt(speed), currentLocation.y()-3-rdm.nextInt(speed), currentLocation.floor());
			path = ctx.movement.findPath(nextLocation);
			path.traverse();
			System.out.println("GOTOBANK:Moving southwest.");

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
		
		if(!destination.contains(ctx.players.local())) {
			navigateXYTo(destination);
		}
		
	}

	public void setTools(int[] tools) {
		this.tools = tools;
	}
}
