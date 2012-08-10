package net.aufdemrand.denizen.activities.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.aufdemrand.denizen.activities.AbstractActivity;
import net.aufdemrand.denizen.npc.DenizenNPC;
import org.bukkit.Location;
import org.bukkit.World;

public class WanderActivity extends AbstractActivity {

	public Location getNewLocation(double X, double Y, double Z, World world) {

		plugin.getLogger().info("Received: " + X + " " + Y + " " + Z);
		Location newLocation = new Location(world, X, Y, Z);

		Random intRandom = new Random();
		int randomX = intRandom.nextInt(12) - 6;
		int randomZ = intRandom.nextInt(12) - 6;
		int randomY = intRandom.nextInt(4) - 4;
		//plugin.getLogger().info("Adding: " + randomX + " " + randomY + " " + randomZ);

		newLocation.setX(newLocation.getX() + randomX);
		newLocation.setZ(newLocation.getZ() + randomZ);
		newLocation.setY(newLocation.getY() + randomY);

		return newLocation;
	}

	private Map<DenizenNPC, WanderGoal> wanderMap = new HashMap<DenizenNPC, WanderGoal>();

	public void addGoal(DenizenNPC npc, int priority) {

		if (wanderMap.containsKey(npc)) {
			wanderMap.get(npc).reset();
			plugin.getLogger().info("NPC already has this Goal assigned! Resetting instead...");
		} else {
			wanderMap.put(npc, new WanderGoal(npc, this));
			npc.getCitizensEntity().getDefaultGoalController().addGoal(wanderMap.get(npc), priority);	
			plugin.getLogger().info("NPC assigned Wander Activity...");
		}
	}

	public void removeGoal(DenizenNPC npc) {

		if (wanderMap.containsKey(npc)) {
			npc.getCitizensEntity().getDefaultGoalController().removeGoal(wanderMap.get(npc));
			wanderMap.remove(npc);
			plugin.getLogger().info("Removed Wander Activity from NPC.");
		} else {
			plugin.getLogger().info("NPC does not have this activity...");
		}
	}

	private Map<DenizenNPC, Long> cooldownMap = new HashMap<DenizenNPC, Long>();

	public void cooldown(DenizenNPC denizenNPC) {
		cooldownMap.put(denizenNPC, System.currentTimeMillis() + 10000);
	}

	public boolean isCool(DenizenNPC denizenNPC) {
		if (cooldownMap.containsKey(denizenNPC))
			if (cooldownMap.get(denizenNPC) < System.currentTimeMillis()) return true;
			else return false;
		else return true;
	}

}