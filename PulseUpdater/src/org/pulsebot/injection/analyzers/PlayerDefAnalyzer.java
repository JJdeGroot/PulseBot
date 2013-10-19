package org.pulsebot.injection.analyzers;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;

/**
 * @Author = NKN
 */
public class PlayerDefAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        return node.name.equals(className.get("PlayerDefinition"));
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("PlayerDefinition", node.name);
        classNodes.put("PlayerDefinition",node);
        return hook;
    }
}
