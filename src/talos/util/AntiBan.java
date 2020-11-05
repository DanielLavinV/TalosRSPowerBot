package talos.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Npc;

public class AntiBan {
	
	public static Random rdm = new Random();

	public static void checkSkill (ClientContext ctx, int skill) {
		
		int xmin=0,xmax=0,ymin=0,ymax=0;
		
		switch(skill) {
		case Constants.SKILLS_FISHING:
			xmin = 714;
			xmax = 769;
			ymin = 274;
			ymax = 296;
			break;
		case Constants.SKILLS_MINING:
			xmin = 716;
			xmax = 769;
			ymin = 211;
			ymax = 232;
			break;
		case Constants.SKILLS_ATTACK:
			break;
		case Constants.SKILLS_DEFENSE:
			break;
		case Constants.SKILLS_STRENGTH:
			break;
		}
		
		
		try {
			ctx.input.move(563 + rdm.nextInt(586 - 563), 170 + rdm.nextInt(200-170)); //move to a random point over the skills button
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200)); //wait for a little
			ctx.input.click(true); // click
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200)); //wait for a little
			ctx.input.move(xmin + rdm.nextInt(xmax - xmin), ymin + rdm.nextInt(ymax-ymin)); //move to a random point over the fishing skill
			TimeUnit.MILLISECONDS.sleep(2000 + rdm.nextInt(2000)); //wait for a little
			ctx.input.move(631 + rdm.nextInt(656 - 631), 170 + rdm.nextInt(200-170)); //move to a random point over the backpack icon
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200)); //wait for a little
			ctx.input.click(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void moveMouseRandom (ClientContext ctx, int times) {
		for(int i = 0; i<= times; i++) {
			ctx.input.move(20 + rdm.nextInt(760), 20 + rdm.nextInt(500));
			try {
				TimeUnit.MILLISECONDS.sleep(rdm.nextInt(600));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void checkRandomEvent (ClientContext ctx) {
		/* attempt to dismiss a random event if one has appeared */
		
		Npc randomNpc = ctx.npcs.select().within(2.0).select(new Filter<Npc>() {

			@Override
			public boolean accept(Npc npc) {
				return npc.overheadMessage().contains(ctx.players.local().name());
			}

		}).poll();

		/* a random npc is present, dismiss them */
		if (randomNpc.valid() && rdm.nextBoolean() == true) {
			System.out.println("Detected random event. Dismissing...");
			try {
				TimeUnit.MILLISECONDS.sleep(3000 + rdm.nextInt(1500));
			} catch (Exception e) {
				e.printStackTrace();
			}
			String action = randomNpc.name().equalsIgnoreCase("genie") ? "Talk-to" : "Dismiss";
			randomNpc.interact(action);
		}
	}
	
	public static void takeABreak (ClientContext ctx) {
		try {
			TimeUnit.SECONDS.sleep(20 + rdm.nextInt(40));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
}
