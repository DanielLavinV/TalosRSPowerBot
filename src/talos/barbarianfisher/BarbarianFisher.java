package talos.barbarianfisher;

import java.util.concurrent.TimeUnit;
import java.util.Random;

import org.powerbot.script.rt4.Npc;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

@Script.Manifest(description = "Fishes near barbarian outpost", name = "Barbarian Fisher")
public class BarbarianFisher extends PollingScript<ClientContext> {

	public int fishingSpotId = 1542;
	public int[] fishIds = {11330, 11328, 11332};
	Random rdm = new Random();
	int decision = 0;
	
	@Override
	public void start() {
		log.info("Started BarbarianFisher.");
	}
	
	@Override
	public void stop() {
		log.info("Stopped BarbarianFisher.");
	}
	
	@Override
	public void poll() {
		// TODO Auto-generated method stub
		decision = 1 + rdm.nextInt(999);
		
		if(1 <= decision && decision <= 900) {
			if (ctx.inventory.isFull()) {
				log.info("Inventory full, dropping fish...");
				while(!dropFish()) {}
			} else if (ctx.players.local().animation() == -1) {
				log.info("Fishing...");
				fish();
			}
		} else if (900 < decision && decision <= 940) {
			log.info("Moving mouse...");
			moveMouseRandom(2 + rdm.nextInt(3));
		} else if (940 < decision && decision<= 980) {
			log.info("Checking skill...");
			checkSkill();
		} else if (980 < decision && decision <= 1000) {
			log.info("Taking a break...");
			takeABreak();
		}
		
		try {
			TimeUnit.SECONDS.sleep(5 + rdm.nextInt(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void takeABreak () {
		ctx.input.click(389,267,true);
		try {
			TimeUnit.SECONDS.sleep(20 + rdm.nextInt(40));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public void checkSkill () {
		try {
			ctx.input.move(582 + rdm.nextInt(607 - 582), 195 + rdm.nextInt(225-195)); //move to a random point over the skills button
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200)); //wait for a little
			ctx.input.click(true); // click
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200)); //wait for a little
			ctx.input.move(696 + rdm.nextInt(752 - 696), 299 + rdm.nextInt(323-299)); //move to a random point over the fishing skill
			TimeUnit.MILLISECONDS.sleep(2000 + rdm.nextInt(2000)); //wait for a little
			ctx.input.move(648 + rdm.nextInt(673 - 648), 195 + rdm.nextInt(223-195)); //move to a random point over the fishing skill
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1200)); //wait for a little
			ctx.input.click(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void moveMouseRandom (int times) {
		for(int i = 0; i<= times; i++) {
			ctx.input.move(20 + rdm.nextInt(760), 20 + rdm.nextInt(500));
			try {
				TimeUnit.MILLISECONDS.sleep(rdm.nextInt(600));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void fish() {
            Npc fishingSpot = ctx.npcs.select().id(fishingSpotId).nearest().poll();
            try {
				TimeUnit.MILLISECONDS.sleep(rdm.nextInt(2000));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
            fishingSpot.interact("Use-rod");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
	}

	public boolean dropFish() {
		try {
			for(Item i : ctx.inventory.id(fishIds)) {
				i.interact("Drop");
				TimeUnit.MILLISECONDS.sleep(rdm.nextInt(800));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		if (ctx.inventory.select().id(fishIds).count() == 0) {
			return true;
		} else {
			dropFish();
		}
		return true;
	} 
}
