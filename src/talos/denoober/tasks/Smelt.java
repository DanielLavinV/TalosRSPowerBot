package talos.denoober.tasks;

import java.util.HashMap;
import java.util.Random;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import talos.denoober.Task;
import talos.util.Identifiers;
import talos.util.MapNode;

public class Smelt extends Task {

	public int level;
	public int tools[];
	public MapNode taskZone;
	public MoveTo mover;
	public Random rdm = new Random();
	public GoToBankForTools gotobank;
	
	public Smelt(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
	public Smelt(ClientContext ctx, MoveTo mover) {
		super(ctx);
		// TODO Auto-generated constructor stub
		this.gotobank = new GoToBankForTools(ctx);
		this.mover = mover;
	}	

	@Override
	public boolean activate() {
		if(ctx.skills.level(Constants.SKILLS_SMITHING) < 15) { //smelt bronze bars
			this.tools = new int[] {Identifiers.COPPER_ORE, Identifiers.TIN_ORE};
			int[] quantities = {1,1};
			this.gotobank.configure(this.mover, this.tools, quantities, true, this.mover.getMapNode("EDGS"));
		} else {
			this.tools = new int[] {Identifiers.IRON_ORE};
			this.gotobank.configure(this.mover, this.tools, this.mover.getMapNode("EDGS"));
		}
		this.gotobank.execute();
		if(this.gotobank.missingTools.isEmpty()) {
			System.out.println("SMELT: successfully retrieved tools: " + this.tools);
			return true;
		} else {
			System.out.println("SMELT: ore missing in bank.");
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		ayy:
			while(true) {
				moveToZone();

				GameObject furnace = ctx.objects.select().id(Identifiers.FURNACE_EDGEVILLE).poll();
				if(!furnace.inViewport())
					ctx.camera.turnTo(furnace);
				furnace.interact("Smelt");
				System.out.println("SMELT: Smelting...");
				if(this.tools[0] == Identifiers.COPPER_ORE) {
					Condition.wait(() -> ctx.widgets.component(270,14).visible());
					if(ctx.widgets.component(270, 14).visible()) {
						ctx.widgets.component(270, 14).click(); //widget-270 is smelting menu, comp 14 is bronze bar
					}
				} else if (this.tools[0] == Identifiers.IRON_ORE) {
					Condition.wait(() -> ctx.widgets.component(270,15).visible());
					if(ctx.widgets.component(270, 15).visible()) {
						ctx.widgets.component(270, 15).click(); //widget-270 is smelting menu, comp 14 is iron bar
					}
				}
				Condition.wait(() -> ctx.inventory.select().id(this.tools).count() == 0 , 1500, 30);

				int[] quantities = {1,1};
				this.gotobank.configure(this.mover, this.tools, quantities, true,this.mover.getMapNode("EDGS"));
				this.gotobank.execute();
				if(this.gotobank.missingTools.isEmpty()) {
					System.out.println("SMELT: successfully retrieved tools: " + this.tools);
				} else {
					System.out.println("SMELT: no more ore in bank!");
					// TODO Auto-generated method stub
					break ayy;
				}
			}
	}

	public void moveToZone() {
		this.mover.setDestination(this.mover.getMapNode("EDGS"));
		this.mover.execute();
	}
	
}
