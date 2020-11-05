package talos.denoober.tasks;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import talos.denoober.Task;
import talos.util.*;

public class Mine extends Task{
	
	public MapNode taskZone = null;
	private int decision = 0;
	public Random rdm = new Random();
	public int[] tools;
	public HashMap<String,Object> taskInfo;
	public GoToBankForTools gotobank;
	public MoveTo mover;

	public Mine(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public Mine(ClientContext ctx, MoveTo mover, HashMap<String,Object> taskInfo) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.gotobank = new GoToBankForTools(ctx);
		this.taskInfo = taskInfo;
		this.taskZone = (MapNode) taskInfo.get("zone");
		this.tools = (int[]) taskInfo.get("tools");
		this.mover = mover;
		this.gotobank.configure(this.mover, (int []) this.taskInfo.get("tools"), (MapNode)this.taskInfo.get("zone"));
		
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		this.gotobank.configure(this.mover, (int []) this.taskInfo.get("tools"), (MapNode)this.taskInfo.get("zone"));
		this.gotobank.execute();
		if(this.gotobank.missingTools.isEmpty()) {
			if(ctx.inventory.select().id(this.tools).count() == 0) {
				System.out.println("MINE: couldn't retrieve tools.");
				return false;
			}
			return true;
		} else {
			System.out.println("MINE: MINE: missing essential items/tools. Please check previous line...");
			return false;
		}
		
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		moveToZone();
		
		LocalDateTime ldt = LocalDateTime.now();

		timer:
			while(ChronoUnit.MINUTES.between(ldt, LocalDateTime.now()) <= 20 + rdm.nextInt(15)) {		

				decision = 1 + rdm.nextInt(999);

				if(1 <= decision && decision <= 950) {
					if (ctx.inventory.select().count() >= 27) {
						System.out.println("MINE: Inventory full, dropping ores...");
						Condition.wait(() -> dropOres(), 100, 300);	
						if(ctx.inventory.select().id((int[])this.taskInfo.get("bank")).count() != 0) {
							System.out.println("MINE: Inventory full, dropping ores...");
							this.gotobank.configure(this.mover, (int []) this.taskInfo.get("tools"), (MapNode)this.taskInfo.get("zone"));
							this.gotobank.execute();
							moveToZone();
						}
					} else if (ctx.players.local().animation() == -1) {
						System.out.println("MINE: Mining...");
						mine();
					}
				} else if (950 < decision && decision <= 970) {
					System.out.println("MINE: Moving mouse...");
					AntiBan.moveMouseRandom(ctx,2 + rdm.nextInt(3));
				} else if (970 < decision && decision<= 980) {
					System.out.println("MINE: Checking skill...");
					AntiBan.checkSkill(ctx, Constants.SKILLS_MINING);
				} else if (980 < decision && decision <= 1000) {
					System.out.println("MINE: Taking a break...");
					AntiBan.takeABreak(ctx);
				}
				
				AntiBan.checkRandomEvent(ctx);

				try {
					TimeUnit.SECONDS.sleep(1 + rdm.nextInt(3));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}
	
	public void moveToZone() {
		this.mover.setDestination((MapNode)this.taskInfo.get("zone"));
		this.mover.execute();
	}	
	
	public void mine() {
        GameObject rock = ctx.objects.select().id((int [])this.taskInfo.get("objects")).nearest().poll();
        if(ctx.skills.level(Constants.SKILLS_SMITHING) < 15) {
        	if(ctx.inventory.select().id(Identifiers.COPPER_ORE).count() >= 13)
        		rock = ctx.objects.select().id(Identifiers.TIN_ROCK_LUMB_SWAMP).nearest().poll();
        	else
        		rock = ctx.objects.select().id(Identifiers.COPPER_ROCK_LUMB_SWAMP).nearest().poll();
        }
        if(!rock.inViewport()) {
        	ctx.camera.turnTo(rock);
        }
        Condition.sleep(500);
        rock.interact("Mine");
		Condition.wait(() -> ctx.players.local().animation() != -1,100, 30);	
		Condition.wait(() -> ctx.players.local().animation() == -1,100, 30);	
        Condition.sleep(100 + rdm.nextInt(200));
	}

	public boolean dropOres() {
		try {
			for(Item i : ctx.inventory.id((int[]) this.taskInfo.get("drop"))) {
				i.interact("Drop");
				TimeUnit.MILLISECONDS.sleep(50+rdm.nextInt(800));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		if (ctx.inventory.select().id((int[]) this.taskInfo.get("drop")).count() == 0) {
			return true;
		} else {
			dropOres();
		}
		return true;
	} 	
	
}
