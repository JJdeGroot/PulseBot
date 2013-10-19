package org.pulsebot.injection.analyzers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 9/24/13
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class RenderableAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        if(node.superName.equals(classNodes.get("CacheableNode").name)){
            if(node.access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_SUPER)){
                return true;
            }
        }
        return false;
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("Renderable",node.name);
        classNodes.put("Renderable",node);
        return hook;
    }
}
