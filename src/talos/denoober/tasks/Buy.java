package talos.denoober.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.Tile;

import talos.denoober.Task;
import talos.util.Identifiers;
import talos.util.MapNode;

public class Buy extends Task{

	public int tools[];
	public MapNode taskZone;
	public MoveTo mover;
	public Random rdm = new Random();
	public GoToBankForTools gotobank;	
	public int tobuy;
	
	public Buy(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public Buy(ClientContext ctx, MoveTo mover, int tobuy) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.mover = mover;
		this.tobuy = tobuy;
	}	

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		switch(this.tobuy) {
		case Identifiers.FLY_FISHING_ROD:
			buyFlyFishingRod();
			break;
		case Identifiers.HAMMER:
			buyHammer();
			break;
		}
		
	}
	
	public void buyFlyFishingRod() {
		
		ctx.camera.turnTo(ctx.bank.nearest());
		
		while(!ctx.bank.open()) {} //open bank

		if(ctx.bank.opened()) { //just in case
			
			ctx.bank.depositInventory(); //dump inventory
			
			Condition.sleep(200 + rdm.nextInt(500));
			
			popa:
				while(true) {
					ctx.bank.withdraw("Coins", 0);
					Condition.sleep(400 + rdm.nextInt(400));
					if( ctx.bank.select().name("Coins").count() == 0) {
						break popa;
					}
				}
		}
		
		ctx.bank.close();	
		this.mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("PSFS")));
		this.mover.execute();
		
		GameObject door = ctx.objects.select().id(Identifiers.PORT_SARIM_FISHING_STORE_DOOR_CLOSED).nearest().poll();
		door.bounds(Identifiers.DOOR_BOUNDS);
		if(!door.inViewport())
			ctx.camera.turnTo(door);
		door.interact("Open");
		
		Npc shop = ctx.npcs.select().id(Identifiers.NPC_GERRANT).nearest().poll();
		if(!shop.inViewport())
			ctx.camera.turnTo(shop);
		shop.interact("Trade");
		
		Component shopwd = ctx.widgets.component(300,16,3);
		while(!shopwd.visible()) {}
		shopwd.interact("Buy 1");
		
		door = ctx.objects.select().id(Identifiers.PORT_SARIM_FISHING_STORE_DOOR_CLOSED).nearest().poll();
		door.bounds(Identifiers.DOOR_BOUNDS);
		if(!door.inViewport())
			ctx.camera.turnTo(door);
		door.interact("Open");		
		
		this.mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("PSFS")));
		this.mover.execute();
	}
	
	public void buyHammer() {
		ctx.camera.turnTo(ctx.bank.nearest());
		
		while(!ctx.bank.open()) {} //open bank

		if(ctx.bank.opened()) { //just in case
			
			ctx.bank.depositInventory(); //dump inventory
			
			Condition.sleep(200 + rdm.nextInt(500));
			
			popa:
				while(true) {
					ctx.bank.withdraw("Coins", 0);
					Condition.sleep(400 + rdm.nextInt(400));
					if( ctx.bank.select().name("Coins").count() == 0) {
						break popa;
					}
				}
		}
		
		ctx.bank.close();	
		this.mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("VFN")));
		this.mover.execute();
		
		ctx.movement.step(new Tile(4217,3420,0));
		
		GameObject door = ctx.objects.select().id(Identifiers.VARROCK_GENERAL_STORE_DOOR_NORTH_CLOSED).nearest().poll();
		if(ctx.players.local().tile().distanceTo(door) < 2) {
			door.bounds(Identifiers.DOOR_BOUNDS);
			if(!door.inViewport())
				ctx.camera.turnTo(door);
			door.interact("Open");
		}
		
		Npc shop = ctx.npcs.select().id(Identifiers.NPC_SHOP_KEEPER_VARROCK).nearest().poll();
		if(!shop.inViewport())
			ctx.camera.turnTo(shop);
		shop.interact("Trade");
		
		Component shopwd = ctx.widgets.component(300,16,11); //hammer
		while(!shopwd.visible()) {}
		shopwd.interact("Buy 1");
		
		if(ctx.players.local().tile().distanceTo(door) < 2) {
			door = ctx.objects.select().id(Identifiers.PORT_SARIM_FISHING_STORE_DOOR_CLOSED).nearest().poll();
			door.bounds(Identifiers.DOOR_BOUNDS);
			if(!door.inViewport())
				ctx.camera.turnTo(door);
			door.interact("Open");		
		}
		
		this.mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("VFN")));
		this.mover.execute();
	}

}
