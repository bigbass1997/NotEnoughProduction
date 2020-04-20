package com.bigbass.nep.gui.nodes;

import com.bigbass.nep.gui.Path;

import java.util.List;
import java.util.Map;

public abstract class ANode {
    public Map<String, List<Path>> inputs;
    public Map<String, Path> outputs;
}
