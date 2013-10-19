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

public class NodeAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        int selfCount = 0, jCount = 0;
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()) {
            FieldNode fn = fnIt.next();
            if(fn.desc.equals("L"+node.name+";"))
                selfCount++;
            if(fn.desc.equals("J"))
                jCount++;
        }
        return selfCount == 2 && jCount == 1;
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("Node",node.name);
        classNodes.put("Node",node);
        return hook;
    }
}
