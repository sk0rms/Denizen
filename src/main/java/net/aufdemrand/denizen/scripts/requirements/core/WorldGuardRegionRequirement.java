package net.aufdemrand.denizen.scripts.requirements.core;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.aufdemrand.denizen.exceptions.RequirementCheckException;
import net.aufdemrand.denizen.scripts.requirements.AbstractRequirement;
import net.aufdemrand.denizen.scripts.requirements.RequirementsContext;
import net.aufdemrand.denizen.utilities.Depends;
import net.aufdemrand.denizen.utilities.WorldGuardUtilities;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

/**
 * Checks if Player is inside specified WorldGuard region.
 * 
 * @author Mason Adkins
 */

public class WorldGuardRegionRequirement extends AbstractRequirement {

	@Override
	public void onEnable() {
		// nothing to do here
	}
	
	/* INREGION [NAME:regionname]

	/* Arguments: [] - Required, () - Optional 
	 * [NAME:regionname] region to check if player is in.
	 * 
	 * Example usages:
	 * INREGION NAME:ilovejeebiss
	 */


    @Override
    public boolean check(RequirementsContext context, List<String> args) throws RequirementCheckException {
		
		/*
		 * Instalize variables
		 */
		String region = null;
		Boolean outcome = false;
		
		/*
		 * If there are no arguments, throw an exception.
		 */
		if (args == null)
			throw new RequirementCheckException("Must provide a NAME:regionname!");
		
		/*
		 * Parse through the given arguments
		 */
		for (String arg : args) {
			if (aH.matchesValueArg("NAME, N", arg, ArgumentType.String)) {
				region = aH.getStringFrom(arg);
				dB.echoDebug("...region set as: " + region);
			} else throw new RequirementCheckException("Invalid argument specified!");
		}
		
		/*
		 * Check if player is in the given region.
		 */
		outcome = WorldGuardUtilities.checkPlayerWGRegion(context.getPlayer(), region);
		
		/*
		 * Display proper debug output
		 */
		if (outcome == true) dB.echoDebug("...player in region!");
		else dB.echoDebug("...player is not in region!");

		return outcome;
	}
}