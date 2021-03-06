package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.NullTag;

public class NullTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base null
    // @Group Common Base Types
    // @ReturnType NullTag
    // @Returns a NullTag.
    // -->

    @Override
    public String getName() {
        return "null";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return NullTag.NULL.handle(data.shrink());
    }
}
