package talos.cooker;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.powerbot.bot.rt4.BankPin;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Menu;

import talos.denoober.tasks.MoveTo;
import talos.util.*;

@Script.Manifest(description = "Cooks", name = "Cooker")

public class Cooker extends PollingScript <ClientContext>{

	MoveTo mover;
	public Random rdm = new Random();
	public Tile[] insideCookingGuild = {new Tile(3142,3444,0), new Tile(3143,3444,0), new Tile(3144,3444,0)};
	
	@Override
	public void start() {
		log.info("Started Cooker.");
		Locations mapper = new Locations();
		log.info("Creating map nodes...");
		mapper.createMapNodes();
		log.info("Creating map network...");		
		mapper.connectMapNodes();
		mover = new MoveTo(ctx,mapper);

//		mover.setDestination(mapper.mapNodes.get(mapper.nameIndexMap.get("LCC")));
//		mover.execute();
	}
	
	@Override
	public void stop() {
		log.info("Stopped Cooker.");
	}
	
	@Override
	public void poll() {
		// TODO Auto-generated method stub
		mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("VWBK"))); //move to varrock west bank
		
		System.out.println("Moving...");
		
		mover.execute();
		
		while(ctx.players.local().inMotion()) {}
		
		System.out.println("Opening bank...");
		if(!ctx.bank.inViewport()) {
			ctx.camera.turnTo(ctx.bank.nearest());
			try {
				TimeUnit.MILLISECONDS.sleep(200+rdm.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while(!ctx.bank.open()) {}
		
		if(ctx.bank.opened()) {
			ctx.bank.depositInventory();
		}
		
		try {
			TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Withdrawing items...");
		
		ctx.bank.withdraw(Identifiers.RAW_LOBSTER, 28);
		
		System.out.println("Closing bank...");
		
		ctx.bank.close();
		
		System.out.println("Moving to Cooking Guild...");
		
		mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("COGD"))); //move to the cooking guild
		mover.execute();
		while(ctx.players.local().animation() != -1) {}
		
		System.out.println("Opening door...");

		
		GameObject cookingGuildDoor = ctx.objects.select().id(Identifiers.DOOR).poll();
		cookingGuildDoor.bounds(11, 117, -214, 0, 125, 107);
		if(!cookingGuildDoor.inViewport()) {
			ctx.camera.turnTo(cookingGuildDoor.tile());
			try {
				TimeUnit.MILLISECONDS.sleep(200+rdm.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cookingGuildDoor.interact("Open");
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() != -1;
			}
		});
		
		try {
			TimeUnit.MILLISECONDS.sleep(200+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Climbing up stairs...");		
		
		GameObject cgStairs = ctx.objects.select().id(Identifiers.COOKING_GUILD_STAIRS_F0).poll();
		
		if(!cgStairs.inViewport()) {
			ctx.camera.turnTo(cgStairs.tile());
			try {
				TimeUnit.MILLISECONDS.sleep(200+rdm.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cgStairs.interact("Climb-up");
		
		try {
			TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() != -1;
			}
		});
		
		
		cook();
		
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() == -1;
			}
		});
		
		try {
			TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		System.out.println("Dropping burnt...");
		drop(Identifiers.BURNT_LOBSTER);
		
		System.out.println("Climbing down stairs...");
		
		cgStairs = ctx.objects.select().id(Identifiers.COOKING_GUILD_STAIRS_F1).poll();
		if(!cgStairs.inViewport()) {
			ctx.camera.turnTo(cgStairs.tile());
		}
		cgStairs.interact("Climb-down");
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() != -1;
			}
		});		
		
		try {
			TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Exiting the cooking guild...");
		
		boolean insideCG = true;
		int count = 0;
		while(insideCG) {
			count = 0;
			cookingGuildDoor = ctx.objects.select().id(Identifiers.DOOR).poll();
			if(!cookingGuildDoor.inViewport()) {
				ctx.camera.turnTo(cookingGuildDoor.tile());
				try {
					TimeUnit.MILLISECONDS.sleep(200+rdm.nextInt(100));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			cookingGuildDoor.interact("Open");

			for(Tile t : insideCookingGuild) {
				if (t.compareTo(ctx.players.local().tile()) == 0) {
					count++;
				}
			}
			if(count == 0) {
				insideCG = false;
			}
		}
		
		try {
			TimeUnit.MILLISECONDS.sleep(500+rdm.nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public boolean drop(int id) {
		try {
			for(Item i : ctx.inventory.id(id)) {
				i.interact("Drop");
				TimeUnit.MILLISECONDS.sleep(rdm.nextInt(800));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		if (ctx.inventory.select().id(id).count() == 0) {
			return true;
		} else {
			drop(id);
		}
		return true;
	}
	
	public void cook() {
		
		while(!ctx.widgets.component(270, 14).visible()) {		
			GameObject cookingRange = ctx.objects.select().id(Identifiers.COOKING_GUILD_RANGE).poll();

			Item rawLobster = ctx.inventory.select().id(Identifiers.RAW_LOBSTER).poll();

			rawLobster.interact("Use");

			try {
				TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		

			if(!cookingRange.inViewport()) {
				ctx.camera.turnTo(cookingRange.tile());
			}
			cookingRange.click();

			try {
				TimeUnit.MILLISECONDS.sleep(100+rdm.nextInt(500));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Cooking...");
		if(ctx.widgets.component(270, 14).visible()) {
			ctx.widgets.component(270, 14).click(); //widget-270 is cooking menu, component 14 is "cook lobster" option
		}	
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.players.local().animation() != -1;
			}
		});			
		
		try {
			TimeUnit.MILLISECONDS.sleep(1000+rdm.nextInt(500));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(ctx.players.local().animation() == 896) {}

		if(ctx.inventory.select().id(Identifiers.RAW_LOBSTER).count()>0 && ctx.players.local().animation() == -1) {
			System.out.println("Still have cookables - trying again...");
			cook();
		}
		
	}
	
}
