package org.pulsebot.injection.analyzers;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;

/**
 * Created with IntelliJ IDEA.
 * User: 40900011
 * Date: 9/27/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class NPCDefinitionAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {

        return node.name.equals(className.get("NPCDefinition"));
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("NPCDefinition",node.name);
        classNodes.put("NPCDefinition",node);
        return hook;
    }
}
