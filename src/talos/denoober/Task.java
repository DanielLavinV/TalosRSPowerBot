package talos.denoober;

import java.util.HashMap;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import talos.denoober.tasks.MoveTo;

public abstract class Task extends ClientAccessor{

	public Task(ClientContext ctx) {
		super(ctx);
	}
	
	public Task(ClientContext ctx, MoveTo mover, HashMap<String,Object> TaskInfo) {
		super(ctx);
	}
	
	public abstract boolean activate();
	public abstract void execute();
	
}
