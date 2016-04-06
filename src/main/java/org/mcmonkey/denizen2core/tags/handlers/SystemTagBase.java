package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;
import org.mcmonkey.denizen2core.tags.objects.TextTag;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class SystemTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base system
    // @Group Utilities
    // @ReturnType SystemTag
    // @Returns a generic utility class full of specific helpful system-related tags.
    // -->

    @Override
    public String getName() {
        return "system";
    }

    public SystemTagBase() {
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name SystemTag.current_time_milliseconds
        // @Group Utilities
        // @ReturnType IntegerTag
        // @Returns the system's current time, in milliseconds since midnight, January 1, 1970 UTC.
        // -->
        handlers.put("current_time_milliseconds", (dat, obj) -> new IntegerTag(System.currentTimeMillis()));
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new SystemTag().handle(data.shrink());
    }

    public class SystemTag extends AbstractTagObject {

        @Override
        public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
            return handlers;
        }

        @Override
        public AbstractTagObject handleElseCase(TagData data) {
            return new TextTag(getName()).handle(data);
        }
    }
}