package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.yaml.StringHolder;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class YamlTag extends AbstractTagObject {
    // <--[object]
    // @Since 0.3.0
    // @Type YamlTag
    // @SubType TextTag
    // @Group Script Data
    // @Description Represents a YAML file.
    // -->

    private String name;

    private YAMLConfiguration internal;

    public YamlTag(String n, YAMLConfiguration yml) {
        name = n;
        internal = yml;
    }

    public String getName() {
        return name;
    }

    public YAMLConfiguration getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.name
        // @Updated 2017/02/19
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the name of the YAML file.
        // @Example "test" .name returns "test".
        // -->
        handlers.put("name", (dat, obj) -> new TextTag(((YamlTag) obj).name));
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.read[<TextTag>]
        // @Updated 2017/02/19
        // @Group Data
        // @ReturnType TextTag
        // @Returns the contents of the YAML key, as text. Note that read_object is preferred!
        // -->
        handlers.put("read", (dat, obj) -> {
            String val = ((YamlTag) obj).internal.getString(dat.getNextModifier().toString());
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No valid text at the specified YAML key! Does it exist?");
                }
                return NullTag.NULL;
            }
            return new TextTag(val);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.read_object[<TextTag>]
        // @Updated 2017/05/20
        // @Group Data
        // @ReturnType Dynamic
        // @Returns the contents of the YAML key, as the correct object type. Interprets lists correctly.
        // -->
        handlers.put("read_object", (dat, obj) -> {
            String modif = dat.getNextModifier().toString();
            if (((YamlTag) obj).internal.isList(modif)) {
                List<String> val = ((YamlTag) obj).internal.getStringList(dat.getNextModifier().toString());
                ListTag list = new ListTag();
                for (String str : val) {
                    list.getInternal().add(Denizen2Core.loadFromSaved(dat.error, str));
                }
                return list;
            }
            String val = ((YamlTag) obj).internal.getString(dat.getNextModifier().toString());
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No valid text at the specified YAML key! Does it exist?");
                }
                return NullTag.NULL;
            }
            return Denizen2Core.loadFromSaved(dat.error, val);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.has_key[<TextTag>]
        // @Updated 2017/02/24
        // @Group Data
        // @ReturnType BooleanTag
        // @Returns whether the YAML has the specified key.
        // -->
        handlers.put("has_key", (dat, obj) -> BooleanTag.getForBoolean(((YamlTag) obj).internal.contains(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.is_list[<TextTag>]
        // @Updated 2017/04/27
        // @Group Data
        // @ReturnType BooleanTag
        // @Returns whether the YAML has the specified key and it is a list typed YAML key.
        // -->
        handlers.put("is_list", (dat, obj) -> BooleanTag.getForBoolean(((YamlTag) obj).internal.isList(dat.getNextModifier().toString())));
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.read_list[<TextTag>]
        // @Updated 2017/02/19
        // @Group Data
        // @ReturnType ListTag
        // @Returns the contents of the YAML key, as a list of text.
        // -->
        handlers.put("read_list", (dat, obj) -> {
            List<String> val = ((YamlTag) obj).internal.getStringList(dat.getNextModifier().toString());
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No valid list at the specified YAML key! Does it exist?");
                }
                return NullTag.NULL;
            }
            ListTag list = new ListTag();
            for (String str : val) {
                list.getInternal().add(new TextTag(str));
            }
            return list;
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name YamlTag.list_keys[<TextTag>]
        // @Updated 2017/02/19
        // @Group Data
        // @ReturnType ListTag
        // @Returns the contents of the YAML key, as a list of keys.
        // -->
        handlers.put("list_keys", (dat, obj) -> {
            Set<StringHolder> val = ((YamlTag) obj).internal.getConfigurationSection(
                    dat.hasNextModifier() ? dat.getNextModifier().toString() : "").getKeys(false);
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No valid keys at the specified YAML key! Does it exist?");
                }
                return NullTag.NULL;
            }
            ListTag list = new ListTag();
            for (StringHolder str : val) {
                list.getInternal().add(new TextTag(str.str));
            }
            return list;
        });
    }

    public static YamlTag getFor(Action<String> error, String text) {
        text = CoreUtilities.toLowerCase(text);
        Object o = Denizen2Core.filesLoadedByScripts.get(text);
        if (o == null || !(o instanceof YAMLConfiguration)) {
            error.run("That's not a valid YAML file!");
            return null;
        }
        return new YamlTag(text, (YAMLConfiguration) o);
    }

    public static YamlTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof YamlTag) ? (YamlTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString());
    }

    @Override
    public String getTagTypeName() {
        return "YamlTag";
    }

    @Override
    public String toString() {
        return name;
    }
}
