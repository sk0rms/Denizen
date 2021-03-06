package net.aufdemrand.denizen.scripts.containers;

import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.objects.dScript;
import net.aufdemrand.denizen.scripts.ScriptBuilder;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.core.CooldownCommand;
import net.aufdemrand.denizen.scripts.requirements.RequirementsContext;
import net.aufdemrand.denizen.scripts.requirements.RequirementsMode;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.List;

public class ScriptContainer {

    public ScriptContainer(ConfigurationSection configurationSection, String scriptContainerName) {
        contents = configurationSection;
        this.name = scriptContainerName.toUpperCase();
    }

    ConfigurationSection contents;

    public ConfigurationSection getContents() {
        return contents;
    }

    public <T extends ScriptContainer> T getAsContainerType(Class<T> type) {
        return (T) type.cast(this);
    }

    private String name;

    public dScript getAsScriptArg() {
        return dScript.valueOf(name);
    }

    public String getType() {
        if (contents.contains("TYPE"))
            return contents.getString("TYPE").toUpperCase();
        else return null;
    }

    public boolean contains(String path) {
        return contents.contains(path.toUpperCase());
    }

    public String getString(String path) {
        return contents.getString(path.toUpperCase());
    }

    public String getString(String path, String def) {
        return contents.getString(path.toUpperCase(), def);
    }

    public List<String> getStringList(String path) {
        return contents.getStringList(path.toUpperCase());
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return contents.getConfigurationSection(path.toUpperCase());
    }

    public void set(String path, Object object) {
        contents.set(path.toUpperCase(), object);
    }

    public String getName() {
        return name;
    }

    public boolean checkBaseRequirements(dPlayer player, dNPC npc) {
        return checkRequirements(player, npc, "");
    }

    public boolean checkRequirements(dPlayer player, dNPC npc, String path) {
        if (path == null) path = "";
        if (path.length() > 0) path = path + ".";
        // Get requirements
        List<String> requirements = contents.getStringList(path + "REQUIREMENTS.LIST");
        String mode = contents.getString(path + "REQUIREMENTS.MODE", "ALL");
        // No requirements? Meets requirements!
        if (requirements == null || requirements.isEmpty()) return true;
        // Return new RequirementsContext built with info extracted from the ScriptContainer
        RequirementsContext context = new RequirementsContext(new RequirementsMode(mode), requirements, this);
        context.attachPlayer(player);
        context.attachNPC(npc);
        return DenizenAPI.getCurrentInstance().getScriptEngine().getRequirementChecker().check(context);
    }

    public List<ScriptEntry> getBaseEntries(dPlayer player, dNPC npc) {
        return getEntries(player, npc, null);
    }

    public List<ScriptEntry> getEntries(dPlayer player, dNPC npc, String path) {
        List<ScriptEntry> list = new ArrayList<ScriptEntry>();
        if (path == null) path = "script";
        List<String> stringEntries = contents.getStringList(path.toUpperCase());
        if (stringEntries == null || stringEntries.size() == 0) return list;
        list = ScriptBuilder.buildScriptEntries(stringEntries, this, player, npc);
        return list;
    }

    public boolean checkCooldown(dPlayer player) {
        return CooldownCommand.checkCooldown((player != null ? player.getName() : null), name);
    }

}
