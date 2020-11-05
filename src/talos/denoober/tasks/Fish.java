package talos.denoober.tasks;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

import talos.denoober.Task;
import talos.util.Identifiers;
import talos.util.MapNode;
import talos.util.AntiBan;

public class Fish extends Task{

	public int level;
	public int tools[];
	public MapNode taskZone;
	public MoveTo mover;
	public HashMap taskInfo;
	public Random rdm = new Random();
	public GoToBankForTools gotobank;

	public Fish(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	public Fish(ClientContext ctx, MoveTo mover, HashMap<String,Object> info) {
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
		} else if (this.gotobank.missingTools.contains(new Integer(Identifiers.FEATHER))) {
			System.out.println("FISH: missing feathers. Will go grab some...");
			Task featherTask = new PickFeathers(ctx,this.mover);
			featherTask.execute();
		} else if (this.gotobank.missingTools.contains(new Integer (Identifiers.FLY_FISHING_ROD))) {
			System.out.println("FISH: missing fly fishing rod. Will go get one...");
			Task buyRod = new Buy(ctx,this.mover,Identifiers.FLY_FISHING_ROD);
			 buyRod.execute();
		} else {
			System.out.println("FISH: missing essential items/tools. Please check previous line...");
			return false;
		}
		if(ctx.inventory.select().id(this.tools).count() == 0) {
			System.out.println("FISH: couldn't retrieve tools.");
			return false;
		}
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

		moveToZone();

		int decision = 0;

		LocalDateTime ldt = LocalDateTime.now();

		timer:
			while(ChronoUnit.MINUTES.between(ldt, LocalDateTime.now()) <= 20 + rdm.nextInt(15)) {
				decision = 1 + rdm.nextInt(999);

				if(1 <= decision && decision <= 900) {
					if (ctx.inventory.isFull()) {
						System.out.println("FISH: Banking fish...");
						this.gotobank.configure(this.mover, this.tools, this.taskZone);
						this.gotobank.execute();
						if(!this.gotobank.missingTools.isEmpty()) {
							System.out.println("FISH: missing tools after intermediate banking. Exiting fishing...");
							break timer;
						}
						moveToZone();
					} else if (ctx.players.local().animation() == -1) {
						System.out.println("FISH: Fishing...");
						fish();
					}
				} else if (900 < decision && decision <= 940) {
					System.out.println("FISH: Moving mouse...");
					AntiBan.moveMouseRandom(ctx,2 + rdm.nextInt(3));
				} else if (940 < decision && decision<= 990) {
					System.out.println("FISH: Checking skill...");
					AntiBan.checkSkill(ctx,Constants.SKILLS_FISHING);
				} else if (990 < decision && decision <= 1000) {
					System.out.println("FISH: Taking a break...");
					AntiBan.takeABreak(ctx);
				}
				
				AntiBan.checkRandomEvent(ctx); //checks for random event
				
				try {
					TimeUnit.SECONDS.sleep(5 + rdm.nextInt(5));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	public void moveToZone() {
		this.mover.setDestination((MapNode)this.taskInfo.get("zone"));
		this.mover.execute();
	}

	public void fish() {
		Npc fishingSpot = ctx.npcs.select().id((int[])taskInfo.get("npcs")).nearest().poll();
		if (!fishingSpot.inViewport()) {
			ctx.camera.turnTo(fishingSpot);
		}
		try {
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(2000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		fishingSpot.interact((String)taskInfo.get("action")); // Net, Lure, Harpoon, Cage etc-
		Condition.wait(() -> ctx.players.local().animation() == Identifiers.ANI_FISHING,100, 300);	//wait until you start fishing
		Condition.wait(() -> ctx.players.local().animation() == -1,100, 300);	// wait until you stop fishing
	}

	public void drop() {
		ctx.inventory.drop(ctx.inventory.id((int []) taskInfo.get("drop")));
		if (ctx.inventory.select().id((int []) taskInfo.get("drop")).count() == 0) {
		} else {
			drop();
		}
	}	
}
