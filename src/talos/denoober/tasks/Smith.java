package talos.denoober.tasks;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Random;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;

import talos.denoober.Task;
import talos.util.Identifiers;
import talos.util.LevelSwitchers;
import talos.util.MapNode;

public class Smith extends Task{

	public MapNode taskZone = null;
	private int decision = 0;
	public Random rdm = new Random();
	public int[] tools;
	public HashMap<String,Object> taskInfo;
	public GoToBankForTools gotobank;
	public MoveTo mover;	
	
	public Smith(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public Smith(ClientContext ctx, MoveTo mover, HashMap<String,Object> taskInfo) {
		super(ctx);
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
		
		this.gotobank.configure(this.mover, (int []) this.taskInfo.get("tools"), new int[] {1,26}, (MapNode)this.taskInfo.get("zone"));
		this.gotobank.execute();
		if(this.gotobank.missingTools.isEmpty()) {
			if(ctx.inventory.select().id(this.tools).count() == 0) {
				System.out.println("SMITH: couldn't retrieve tools.");
				return false;
			}
			return true;
		} else if (this.gotobank.missingTools.contains(new Integer(Identifiers.BRONZE_BAR))){
			System.out.println("SMITH: missing bronze bars. Attempting to craft some...");
			Task smeltTask = new Smelt(ctx, this.mover);
			if(smeltTask.activate()) {
				System.out.println("SMITH: successfully obtained copper and tin ore.");
				smeltTask.execute();
				return this.activate(); //try again
			} else {
				System.out.println("SMITH: no copper/tin ore found in bank.");
				return false;
			}
		} else if(this.gotobank.missingTools.contains(new Integer(Identifiers.IRON_BAR))) {
			System.out.println("SMITH: missing iron bars. Attempting to craft some...");
			Task smeltTask = new Smelt(ctx, this.mover);
			if(smeltTask.activate()) {
				System.out.println("SMITH: successfully obtained iron ore.");
				smeltTask.execute();
				return this.activate(); //try again
			} else {
				System.out.println("SMITH: no iron ore found in bank.");
				return false;
			}
		} else if (this.gotobank.missingTools.contains(new Integer(Identifiers.HAMMER))) {
			System.out.println("SMITH: missing hammer. Will go get one...");
			Task buyHammer = new Buy(ctx,this.mover,Identifiers.HAMMER);
			 buyHammer.execute();
			 return this.activate(); // try again
		} else {
			System.out.println("SMITH: missing essential items/tools. Please check previous line...");
			return false;
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		LocalDateTime ldt = LocalDateTime.now();
		
		loopity:
		while(ChronoUnit.MINUTES.between(ldt, LocalDateTime.now()) <= 30 + rdm.nextInt(30)) {
			int currentLevel = ctx.skills.level(Constants.SKILLS_SMITHING);
			taskInfo = LevelSwitchers.smithingLevelSw(currentLevel, this.mover);
			while(ctx.skills.level(Constants.SKILLS_SMITHING) < currentLevel + 1) {
				goToZone();
				GameObject anvil = ctx.objects.select().id(Identifiers.ANVIL_VARROCK_WEST).nearest().poll();
				if(!anvil.inViewport())
					ctx.camera.turnTo(anvil);
				anvil.interact("Smith");
				int component = (Integer) this.taskInfo.get("component");
				Condition.wait(() -> ctx.widgets.component(Identifiers.WDG_SMITHING_MENU, component).visible());
				ctx.widgets.component(Identifiers.WDG_SMITHING_MENU, component).click();
				Condition.wait(() -> ctx.inventory.select(item -> item.id() == this.tools[1]).count() == 0 || ctx.skills.level(Constants.SKILLS_SMITHING) == currentLevel +1, 1500, 30);
				this.gotobank.quantities = new int[] {1,26};
				this.gotobank.varrockWestBank();
				if(!this.gotobank.missingTools.isEmpty()) {
					System.out.println("SMITH: no more bars to smith!");
					break loopity;
				}
			}
		}
	}

	public void goToZone() {
		this.mover.setDestination(this.mover.getMapNode("ZKL9"));
		this.mover.execute();
	}
}
