package talos.denoober.tasks;

import java.util.HashMap;
import java.util.Random;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import talos.denoober.Task;
import talos.util.Identifiers;
import talos.util.MapNode;

public class Cook extends Task{

	public int level;
	public int tools[];
	public MapNode taskZone;
	public MoveTo mover;
	public HashMap taskInfo;
	public Random rdm = new Random();
	public GoToBankForTools gotobank;	
	
	public Cook(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public Cook(ClientContext ctx, MoveTo mover, HashMap<String,Object> info) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.gotobank = new GoToBankForTools(ctx);
		this.mover = mover;
		this.taskInfo = info;
		this.tools = (int[])taskInfo.get("tools");
		this.taskZone = (MapNode) taskInfo.get("zone");
		this.gotobank.configure(this.mover, (int []) this.taskInfo.get("tools"), (MapNode)this.taskInfo.get("zone"));
	}		

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		this.gotobank.configure(this.mover, (int []) this.taskInfo.get("tools"), (MapNode)this.taskInfo.get("zone"));
		this.gotobank.execute();
		if(this.gotobank.missingTools.isEmpty()) {
			return true;
		} else {
			System.out.println("COOK: missing essential items/tools. Please check previous line...");
			return false;
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		moveToZone();
		
		GameObject tree = ctx.objects.select().nearest().poll();
		if(!tree.inViewport()) {
			ctx.camera.turnTo(tree);
		}
		
		tree.interact("Cut");
		
		Condition.wait(() -> ctx.players.local().animation() != -1, 500, 20);
		Condition.wait(() -> ctx.players.local().animation() == -1, 500, 20);
		
		Item matchbox = ctx.inventory.select().id(Identifiers.TINDERBOX).poll();
		matchbox.interact("Use");
		Item logs = ctx.inventory.select().id(Identifiers.LOGS).poll();
		logs.click();
		
		Condition.wait(() -> ctx.players.local().animation() != -1, 500, 20);
		Condition.wait(() -> ctx.players.local().animation() == -1, 500, 20);
		
		// a fire should now be on the ground
		
		GameObject fire = 
	}
	
	public void moveToZone() {
		this.mover.setDestination((MapNode)this.taskInfo.get("zone"));
		this.mover.execute();
	}
	
}
