package talos.util;

import org.powerbot.script.Tile;

public class JsonLocationGenerator {
	
	/**
	 * FROM LUMBRIDGE GATES TO BARBARIAN VILLAGE FISHING SPOT
	 * */
	private static final Tile[] path = new Tile[] {
			new Tile(3236, 3225, 0), //SMN0
			new Tile(3231, 3231, 0),
			new Tile(3226, 3236, 0),
			new Tile(3223, 3241, 0),
			new Tile(3219, 3246, 0),
			new Tile(3218, 3257, 0),
			new Tile(3216, 3267, 0),
			new Tile(3216, 3276, 0),
			new Tile(3209, 3280, 0),
			new Tile(3200, 3280, 0),
			new Tile(3191, 3282, 0),
			new Tile(3184, 3286, 0),
			new Tile(3176, 3286, 0),
			new Tile(3169, 3286, 0),
			new Tile(3161, 3288, 0),
			new Tile(3154, 3293, 0),
			new Tile(3146, 3295, 0),
			new Tile(3137, 3296, 0),
			new Tile(3127, 3296, 0),
			new Tile(3120, 3298, 0),
			new Tile(3114, 3295, 0),
			new Tile(3108, 3295, 0),
			new Tile(3102, 3295, 0),
			new Tile(3094, 3293, 0),
			new Tile(3088, 3290, 0),
			new Tile(3085, 3299, 0),
			new Tile(3080, 3307, 0),
			new Tile(3078, 3319, 0),
			new Tile(3076, 3328, 0),
			new Tile(3074, 3343, 0),
			new Tile(3074, 3353, 0),
			new Tile(3076, 3364, 0),
			new Tile(3077, 3373, 0),
			new Tile(3078, 3385, 0),
			new Tile(3083, 3395, 0),
			new Tile(3092, 3404, 0),
			new Tile(3098, 3413, 0),
			new Tile(3100, 3420, 0), //SMN37
			new Tile(3104, 3431, 0) //BVFS
	};
	
	/**
	 * From the Barbarian Village Bridge to the Center of Varrock
	 * */
	
	private static Tile[] pathZKL = new Tile[] {
			new Tile(3104, 3422, 0), //ZKL0, barbarian Village bridge
			new Tile(3111, 3420, 0),
			new Tile(3118, 3419, 0),
			new Tile(3126, 3415, 0),
			new Tile(3137, 3417, 0),
			new Tile(3148, 3416, 0),
			new Tile(3158, 3419, 0),
			new Tile(3167, 3424, 0),
			new Tile(3174, 3429, 0),
			new Tile(3183, 3430, 0),
			new Tile(3192, 3431, 0),
			new Tile(3198, 3428, 0),
			new Tile(3205, 3428, 0),
			new Tile(3212, 3427, 0) //VFN Varrock City Centre
	};
	
	public static final Tile[] pathCRQ = new Tile[] {
			new Tile(3171, 3421, 0),
			new Tile(3171, 3412, 0),
			new Tile(3171, 3402, 0),
			new Tile(3174, 3392, 0),
			new Tile(3179, 3383, 0),
			new Tile(3181, 3371, 0) //CRQ5 just before varrock mine VIMS
	};	
	
	public static final Tile[] pathPSP = new Tile[] { //from draynor crossroads to port sarim's fishing store
			new Tile(3080, 3289, 0),
			new Tile(3075, 3280, 0),
			new Tile(3065, 3277, 0),
			new Tile(3055, 3275, 0),
			new Tile(3045, 3274, 0),
			new Tile(3035, 3273, 0),
			new Tile(3025, 3268, 0),
			new Tile(3020, 3259, 0),
			new Tile(3020, 3249, 0),
			new Tile(3022, 3239, 0),
			new Tile(3019, 3229, 0),
			new Tile(3020, 3223, 0),
			new Tile(3014, 3217, 0) //PSFS port sarim fishing store
	};
	public static final Tile[] pathBMR = new Tile[] { //from barbarian village bridge to center
			new Tile(3100, 3420, 0), //BMR0
			new Tile(3090, 3420, 0),
			new Tile(3076, 3418, 0) //BVCR Barbarian Villager CenteR
	};
	
	public static final Tile[] pathBES = new Tile[] { //from Barbarian Village to Edgeville Smith
			new Tile(3093, 3426, 0), //BES0
			new Tile(3091, 3432, 0),
			new Tile(3092, 3442, 0),
			new Tile(3091, 3452, 0),
			new Tile(3089, 3462, 0),
			new Tile(3083, 3465, 0),
			new Tile(3079, 3471, 0),
			new Tile(3080, 3480, 0),
			new Tile(3084, 3487, 0),
			new Tile(3087, 3493, 0),
			new Tile(3094, 3495, 0), //EDGB
			new Tile(3102, 3497, 0),
			new Tile(3107, 3498, 0) //EDGS
	};
	
	public static void main(String args[]) {
		generateJson();
	}
	
	public static void generateJson() {
		int ite = 0;
		for(Tile t : pathBES) {
			System.out.println("\t\t{");
			System.out.println("\t\t\t\"name\":\"BES"+ite+"\",");
			System.out.println("\t\t\t\"tiles\":[");
			System.out.println("\t\t\t\t{");
			System.out.println("\t\t\t\t\t\"x\":"+t.x()+",\"y\":"+t.y()+",\"z\":"+t.floor()+"");
			System.out.println("\t\t\t\t},");
			System.out.println("\t\t\t\t{");
			System.out.println("\t\t\t\t\t\"x\":"+(t.x()+1)+",\"y\":"+t.y()+",\"z\":"+t.floor()+"");
			System.out.println("\t\t\t\t},");
			System.out.println("\t\t\t\t{");
			System.out.println("\t\t\t\t\t\"x\":"+t.x()+",\"y\":"+(t.y()+1)+",\"z\":"+t.floor()+"");
			System.out.println("\t\t\t\t},");
			System.out.println("\t\t\t\t{");
			System.out.println("\t\t\t\t\t\"x\":"+(t.x()+1)+",\"y\":"+(t.y()+1)+",\"z\":"+t.floor()+"");
			System.out.println("\t\t\t\t}");
			System.out.println("\t\t\t],");
			System.out.println("\t\t\t\"siblings\":[");
			System.out.print("\t\t\t\t\"BES"+(ite-1)+"\",");
			System.out.println("\"BES"+(ite+1)+"\"");
			System.out.println("\t\t\t]");
			System.out.println("\t\t},");			
			ite++;
		}
	}
}
