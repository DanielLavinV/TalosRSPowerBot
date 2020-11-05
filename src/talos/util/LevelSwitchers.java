package talos.util;

import java.util.HashMap;

import talos.denoober.tasks.MoveTo;

public class LevelSwitchers {
	public static HashMap<String, Object> fishingLevelSw(int level, MoveTo mover) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		if(1 <= level && level <= 19) { //fish in lumbridge swamp east
			params.put("tools", new int[] {Identifiers.SMALL_FISHING_NET});
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("LSFS")));
			params.put("npcs", new int[] {Identifiers.FISHING_SPOT_LUMBRIDGE_SWAMP});
			params.put("action", "Net");
		} else if (20 <= level && level <= 50) { //fish in barbarian village
			params.put("tools", new int[] {Identifiers.FEATHER, Identifiers.FLY_FISHING_ROD});
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("BVFS")));
			params.put("npcs", new int[] {Identifiers.FISHING_SPOT_BARBARIAN_VILLAGE});
			params.put("action", "Lure");
		}
		return params;
	}
	
	public static HashMap<String, Object> miningLevelSw(int level, int smithinglv, MoveTo mover) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		if(smithinglv < 15) {
			params.put("tools", new int[] {Identifiers.BRONZE_PICKAXE});
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("LSM")));
			params.put("drop", new int[] {0});
			params.put("bank", new int[] {Identifiers.COPPER_ORE, Identifiers.TIN_ORE});
			params.put("objects", new int[] {Identifiers.COPPER_ROCK_LUMB_SWAMP, Identifiers.TIN_ROCK_LUMB_SWAMP});
			return params;
		}
		if(1 <= level && level <= 15) {
			params.put("tools", new int[] {Identifiers.BRONZE_PICKAXE});
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("LSM")));
			params.put("drop", new int[] {0});
			params.put("bank", new int[] {Identifiers.COPPER_ORE, Identifiers.TIN_ORE});
			params.put("objects", new int[] {Identifiers.COPPER_ROCK_LUMB_SWAMP, Identifiers.TIN_ROCK_LUMB_SWAMP});
		} else if(16 <= level && level <= 50) {
			params.put("tools", new int[] {Identifiers.BRONZE_PICKAXE});
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("VIMS")));
			params.put("drop", new int[] {0});
			params.put("bank", new int[] {Identifiers.IRON_ORE});			
			params.put("objects", new int[] {Identifiers.IRON_ROCK_VARROCK_SW_MINE});
		}
		return params;
	}
	//TODO: qué rol va a jugar la zona en cooking?
	public static HashMap<String, Object> cookingLevelSw(int level, MoveTo mover) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		if(1 <= level && level <= 14) {
			params.put("zone", mover.getMapNode("VBGE0"));
			params.put("tools", new int[] {Identifiers.BRONZE_AXE, Identifiers.TINDERBOX});
			params.put("drop", new int[] {Identifiers.BURNT_FISH_ANCHOVIES, Identifiers.BURNT_SHRIMP});
			params.put("cook", new int[] {Identifiers.RAW_ANCHOVIES, Identifiers.RAW_SHRIMPS});
			params.put("npc", Identifiers.TREE_VARROCK_BANK);
		} else if(15 <= level && level <= 24) {
			params.put("zone", mover.getMapNode("VBGE0"));
			params.put("tools", new int[] {Identifiers.BRONZE_AXE, Identifiers.TINDERBOX});
			params.put("drop", new int[] {Identifiers.BURNT_FISH_ANCHOVIES, Identifiers.BURNT_SHRIMP, Identifiers.BURNT_FISH_TROUT_SALMON});
			params.put("cook", new int[] {Identifiers.RAW_ANCHOVIES, Identifiers.RAW_SHRIMPS, Identifiers.RAW_TROUT});
			params.put("npc", Identifiers.TREE_VARROCK_BANK);
		} else if(25 <= level && level <= 50) {
			params.put("zone", mover.getMapNode("VBGE0"));
			params.put("tools", new int[] {Identifiers.BRONZE_AXE, Identifiers.TINDERBOX});
			params.put("drop", new int[] {Identifiers.BURNT_FISH_ANCHOVIES, Identifiers.BURNT_SHRIMP, Identifiers.BURNT_FISH_TROUT_SALMON});
			params.put("cook", new int[] {Identifiers.RAW_ANCHOVIES, Identifiers.RAW_SHRIMPS, Identifiers.RAW_TROUT, Identifiers.RAW_SALMON});
			params.put("npc", Identifiers.TREE_VARROCK_BANK);
		}
		return params;
	}
	
	public static HashMap<String,Object> meleeLevelSw(int level, MoveTo mover) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		if(1 <= level && level <= 10) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("POLLOS")));
			params.put("equip", Identifiers.BRONZE_SET);
			params.put("consume", Identifiers.EATABLES);
			params.put("kill", Identifiers.NPC_CHICKENS);
		} else if(11 <= level && level <= 20) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("VACAS")));
			params.put("equip", Identifiers.BRONZE_SET);
			params.put("consume", Identifiers.EATABLES);
			params.put("kill", Identifiers.NPC_COWS);
		} else if(21 <= level && level <= 30) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("BVCR")));
			params.put("equip", Identifiers.IRON_SET);
			params.put("consume", Identifiers.EATABLES);
			params.put("kill", Identifiers.NPC_BARBARIANS);
		} 
		return params;
	}
	
	public static HashMap<String,Object> smithingLevelSw(int level, MoveTo mover) {
		HashMap<String,Object> params = new HashMap<String,Object>();
		if(level == 1) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_DAGGER});
			params.put("component", Identifiers.COMP_SMITHING_DAGGER);
		} else if (level == 2) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_MACE});
			params.put("component", Identifiers.COMP_SMITHING_MACE);
		} else if (level == 3) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_MED_HELM});
			params.put("component", Identifiers.COMP_SMITHING_MED_HELM);
		} else if (level == 4) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_SWORD});
			params.put("component", Identifiers.COMP_SMITHING_SWORD);
		} else if (level == 5) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_SCIMITAR});
			params.put("component", Identifiers.COMP_SMITHING_SCIMITAR);
		} else if (level == 6) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_LONGSWORD});
			params.put("component", Identifiers.COMP_SMITHING_LONGSWORD);
		} else if (level == 7) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_FULL_HELM});
			params.put("component", Identifiers.COMP_SMITHING_FULL_HELM);
		} else if (level == 8) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_SQ_SHIELD});
			params.put("component", Identifiers.COMP_SMITHING_SQ_SHIELD);
		} else if (level == 9) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_WARHAMMER});
			params.put("component", Identifiers.COMP_SMITHING_WARHAMMER);
		} else if (level == 10) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_BATTLEAXE});
			params.put("component", Identifiers.COMP_SMITHING_BATTLEAXE);
		} else if (level == 11) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_CHAINBODY});
			params.put("component", Identifiers.COMP_SMITHING_CHAINBODY);
		} else if (level == 12) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_KITESHIELD});
			params.put("component", Identifiers.COMP_SMITHING_KITESHIELD);
		} else if (level == 13) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_KITESHIELD});
			params.put("component", Identifiers.COMP_SMITHING_KITESHIELD);
		} else if (level == 14) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.BRONZE_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.BRONZE_2H_SWORD});
			params.put("component", Identifiers.COMP_SMITHING_2H_SWORD);
		} else if (level == 15) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_DAGGER});
			params.put("component", Identifiers.COMP_SMITHING_DAGGER);
		} else if (level == 16) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_AXE});
			params.put("component", Identifiers.COMP_SMITHING_AXE);
		} else if (level == 17) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_MED_HELM});
			params.put("component", Identifiers.COMP_SMITHING_MED_HELM);
		} else if (level == 18) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_MED_HELM});
			params.put("component", Identifiers.COMP_SMITHING_MED_HELM);
		} else if (level == 19) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_SWORD});
			params.put("component", Identifiers.COMP_SMITHING_SWORD);
		} else if (level == 20) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_SCIMITAR});
			params.put("component", Identifiers.COMP_SMITHING_SCIMITAR);
		} else if (level == 21) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_LONGSWORD});
			params.put("component", Identifiers.COMP_SMITHING_LONGSWORD);
		} else if (level == 22) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_FULL_HELM});
			params.put("component", Identifiers.COMP_SMITHING_FULL_HELM);
		} else if (level == 23) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_SQ_SHIELD});
			params.put("component", Identifiers.COMP_SMITHING_SQ_SHIELD);
		} else if (level == 24) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_WARHAMMER});
			params.put("component", Identifiers.COMP_SMITHING_WARHAMMER);
		} else if (level == 25) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_BATTLEAXE});
			params.put("component", Identifiers.COMP_SMITHING_BATTLEAXE);
		} else if (level == 26) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_CHAINBODY});
			params.put("component", Identifiers.COMP_SMITHING_CHAINBODY);
		} else if (level == 27) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_KITESHIELD});
			params.put("component", Identifiers.COMP_SMITHING_KITESHIELD);
		} else if (level == 28) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_KITESHIELD});
			params.put("component", Identifiers.COMP_SMITHING_KITESHIELD);
		} else if (level == 29) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_2H_SWORD});
			params.put("component", Identifiers.COMP_SMITHING_2H_SWORD);
		} else if (level == 30) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_2H_SWORD});
			params.put("component", Identifiers.COMP_SMITHING_2H_SWORD);
		} else if (level == 31) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_PLATELEGS});
			params.put("component", Identifiers.COMP_SMITHING_PLATELEGS);
		} else if (level == 32) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_PLATELEGS});
			params.put("component", Identifiers.COMP_SMITHING_PLATELEGS);
		} else if (level >= 33) {
			params.put("zone", mover.mapper.mapNodes.get(mover.mapper.nameIndexMap.get("ZKL9")));
			params.put("tools", new int[] {Identifiers.HAMMER, Identifiers.IRON_BAR});
			params.put("objects", new int[] {Identifiers.ANVIL_VARROCK_WEST});
			params.put("create", new int[] {Identifiers.IRON_PLATEBODY});
			params.put("component", Identifiers.COMP_SMITHING_PLATEBODY);
		} 
		
		return params;
	}	
}
