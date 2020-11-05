package talos.denoober;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.powerbot.script.*;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import talos.denoober.tasks.Buy;
import talos.denoober.tasks.Fish;
import talos.denoober.tasks.GoToBankForTools;
import talos.denoober.tasks.Mine;
import talos.denoober.tasks.MoveTo;
import talos.denoober.tasks.PickFeathers;
import talos.denoober.tasks.Smith;
import talos.util.*;

@Script.Manifest(description = "Trains fishing, mining, woodcutting, cooking and smithing. Also, does first five quests.", name = "Talos Denoober")
public class Denoober extends PollingScript<ClientContext>{
	
	List<Task> taskList = new ArrayList<Task>();
	HashMap<String,Integer> taskNumbersMap = new HashMap<String,Integer>();
	GoToBankForTools goToBank = new GoToBankForTools(ctx);
	HashMap<String, Object> taskInfo;
	Random rdm = new Random();
	MoveTo mover;
 	
	@Override
	public void start() {
		Locations mapper = new Locations();
		log.info("Started Talos Denoober.");
		log.info("Creating map nodes...");
		mapper.createMapNodes();
		log.info("Creating map network...");		
		mapper.connectMapNodes();
		mover = new MoveTo(ctx,mapper);

	}
	
	@Override
	public void poll() {
		int decider = rdm.nextInt(3);
		log.info("DENOOBER: Deciding on a task...");
		switch(decider) {
		case 0: //Fish
			log.info("DENOOBER: New task: FISH");
			taskInfo = LevelSwitchers.fishingLevelSw(ctx.skills.level(Constants.SKILLS_FISHING), this.mover);
			Fish fishingTask = new Fish(ctx, mover,taskInfo);
			if(fishingTask.activate()) {
				fishingTask.execute();
			}
			break;
		case 1: //Mine
			log.info("DENOOBER: New task: MINE");
			taskInfo = LevelSwitchers.miningLevelSw(ctx.skills.level(Constants.SKILLS_MINING), ctx.skills.level(Constants.SKILLS_SMITHING), this.mover);
			Mine miningTask = new Mine(ctx, mover,taskInfo);
			if(miningTask.activate()) {
				miningTask.execute();
			}
			break;
		case 2: //Smith
			log.info("DENOOBER: New task: SMITH");
			taskInfo = LevelSwitchers.smithingLevelSw(ctx.skills.level(Constants.SKILLS_SMITHING), this.mover);
			Smith smithingTask = new Smith(ctx, mover,taskInfo);
			if(smithingTask.activate()) {
				smithingTask.execute();
			}
			break;
		}
	}

	@Override
	public void stop() {
		log.info("Stopped Talos Denoober.");
	}
	
	
}
