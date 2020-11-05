package talos.denoober.tasks;

import org.powerbot.script.rt4.ClientContext;

import talos.denoober.Task;

public class TrainMelee extends Task{

	public TrainMelee(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		moveToZone();
		combat();
	}
	
	public void stop() {
		
	}

	public void moveToZone() {
		
	}

	public void combat() {
		
	}
	
}

