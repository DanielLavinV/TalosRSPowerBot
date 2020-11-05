package talos.kharidminer;

import java.util.concurrent.TimeUnit;
import java.util.Random;

import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

@Script.Manifest(description = "Mines at Al Kharid mine", name = "Kharid Miner")
public class KharidMiner extends PollingScript<ClientContext> {

	public int ironRockId = 11364;
	public int ironRockId2 = 11365;
	public int ironOreId = 440;
	Random rdm = new Random();
	int decision = 0;
	
	@Override
	public void start() {
		log.info("Started KharidMiner.");
	}
	
	@Override
	public void stop() {
		log.info("Stopped KharidMiner.");
	}
	
	@Override
	public void poll() {
		// TODO Auto-generated method stub
		decision = 1 + rdm.nextInt(999);
		
		if(1 <= decision && decision <= 900) {
			if (ctx.inventory.isFull()) {
				log.info("Inventory full, dropping ores...");
				while(!dropOres()) {}
			} else if (ctx.players.local().animation() == -1) {
				log.info("Mining...");
				mine();
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
			TimeUnit.SECONDS.sleep(1 + rdm.nextInt(3));
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
	
	public void mine() {
        GameObject rock = ctx.objects.select().id(ironRockId).nearest().poll();
        if (!rock.valid()) {
        	rock = ctx.objects.select().id(ironRockId2).nearest().poll();
        }
        try {
			TimeUnit.MILLISECONDS.sleep(rdm.nextInt(1000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
        rock.interact("Mine");
        try {
            Thread.sleep(1000);
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
			ctx.input.move(697 + rdm.nextInt(747 - 697), 235 + rdm.nextInt(256-235)); //move to a random point over the fishing skill
			TimeUnit.MILLISECONDS.sleep(1000 + rdm.nextInt(1000)); //wait for a little
			ctx.input.move(648 + rdm.nextInt(673 - 648), 195 + rdm.nextInt(223-195)); //move to a random point over the bag
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
	


	public boolean dropOres() {
		try {
			for(Item i : ctx.inventory.id(ironOreId)) {
				i.interact("Drop");
				TimeUnit.MILLISECONDS.sleep(50+rdm.nextInt(800));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		if (ctx.inventory.select().id(ironOreId).count() == 0) {
			return true;
		} else {
			dropOres();
		}
		return true;
	} 
}
