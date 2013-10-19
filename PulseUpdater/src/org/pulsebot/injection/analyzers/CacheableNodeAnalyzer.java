package org.pulsebot.injection.analyzers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;

import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 9/24/13
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class CacheableNodeAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        int selfCount = 0;
        if(node.superName.equals(classNodes.get("Node").name)){
            ListIterator<FieldNode> fnIt = node.fields.listIterator();
            while(fnIt.hasNext()){
                FieldNode fn = fnIt.next();
                if(fn.desc.equals("L"+node.name+";"))
                    selfCount++;
            }
            return selfCount == 2;
        }

        return false;
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("CacheableNode", node.name);
        classNodes.put("CacheableNode", node);
        return hook;
    }
}
