package com.denizenscript.denizen2core.scripts.commontypes;

import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

public class TaskScript extends CommandScript {

    // <--[explanation]
    // @Since 0.3.0
    // @Name Task Scripts
    // @Group Script Types
    // @Description
    // A task script is the most basic executable script in Denizen.
    // It runs its code (usually the "script" section) when told to run it by the
    // <@link command run>run<@/link> or <@link command inject>inject<@/link> commands.
    // -->

    public TaskScript(String name, YAMLConfiguration section) {
        super(name, section);
    }

    @Override
    public boolean isExecutable(String section) {
        return !section.startsWith("constant");
    }

    public CommandScriptSection getSection(String name) {
        if (name == null || name.length() == 0) {
            return sections.get("script");
        }
        return sections.get(CoreUtilities.toLowerCase(name));
    }

    public enum TaggableType {
        NONE,
        ON,
        ALWAYS
    }

    public TaggableType isTaggable = TaggableType.NONE;

    @Override
    public boolean init() {
        isTaggable = TaggableType.NONE;
        if (super.init()) {
            if (contents.contains("taggable")) {
                String tt = CoreUtilities.toLowerCase(contents.getString("taggable"));
                if (tt.equals("true") || tt.equals("on")) {
                    isTaggable = TaggableType.ON;
                }
                else if (tt.equals("always")) {
                    isTaggable = TaggableType.ALWAYS;
                }
            }
            return true;
        }
        return false;
    }
}
