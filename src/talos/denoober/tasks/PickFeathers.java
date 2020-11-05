package talos.denoober.tasks;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Npc;

import talos.denoober.Task;
import talos.util.Identifiers;
import talos.util.MapNode;

public class PickFeathers extends Task{

	public int tools[];
	public MapNode taskZone;
	public MoveTo mover;
	public Random rdm = new Random();
	public GoToBankForTools gotobank;
	Area feathArea = new Area(new Tile(3170,3289),new Tile(3183,3300));

	
	public PickFeathers(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public PickFeathers(ClientContext ctx, MoveTo mover) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.mover = mover;
	}	

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		if(!feathArea.contains(ctx.players.local()))
			moveToZone();
		
		GameObject gate = ctx.objects.select().id(Identifiers.GATE_CLOSED).nearest().poll();
		gate.bounds(Identifiers.GATE_BOUNDS);
		if(!gate.inViewport()) {
			ctx.camera.turnTo(gate);
		}
		if(ctx.players.local().tile().distanceTo(gate) < 6)
			gate.interact("Open");
		
		int feathers2pick = 500 + rdm.nextInt(200);
		ctx.movement.findPath(feathArea.getCentralTile()).traverse();
		Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	
		
		while(ctx.inventory.select().id(Identifiers.FEATHER).count(true) < feathers2pick) {
			GroundItem feather = ctx.groundItems.select().within(feathArea).select().id(Identifiers.FEATHER).poll();
			if(!feather.inViewport()) {
				ctx.camera.turnTo(feather);
			}
			if(!feather.valid() || !feathArea.contains(feather)) {
				System.out.println("FEATHERS: No feathers in area.");
				Npc chikin = ctx.npcs.select().within(feathArea).select(npc -> !npc.interacting().valid()).id(Identifiers.NPC_CHICKENS).nearest().poll();
				System.out.println("FEATHERS: Nearest chicken in area and not in combat.");
				if(!chikin.inViewport()) {
					ctx.camera.turnTo(chikin);
				}
				chikin.interact("Attack","Chicken");
				System.out.println("FEATHERS: Attacking chicken...");
				Condition.wait(() -> chikin.animation() == Identifiers.ANI_DYING || !chikin.valid(),100, 300);
				System.out.println("FEATHERS: Player no longer attacking chicken.");
			} else {
				Condition.sleep();
				System.out.println("FEATHERS: Picking feather...");
				feather.interact("Take","Feather");
				Condition.wait(() -> ctx.players.local().inMotion(),100, 300);	
				System.out.println("FEATHERS: Waiting for player to stop moving.");
				Condition.wait(() -> !ctx.players.local().inMotion(),100, 300);	
			}
			Condition.sleep(800);
		}
		
		while(ctx.players.local().tile().distanceTo(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("POLLOS")).tiles[0]) > 6) {
			mover.navigateXYTo(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("POLLOS")).tiles[0]);
		}
		
		if(!gate.inViewport()) {
			ctx.camera.turnTo(gate);
		}
		ctx.movement.step(new Tile(3181,3289,0));
		while(ctx.players.local().inMotion()) {}
		gate.interact("Open");
		
		mover.moveSouth(ctx.players.local().tile(),1);
	}

	public void moveToZone() {
		this.mover.setDestination(mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("POLLOS")));
		this.mover.execute();
	}	
	
}
