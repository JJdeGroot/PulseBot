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
public class NodeHashTableAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();
            if(fn.desc.equals("[L"+classNodes.get("Node").name+";"))
                return true;
        }
        return false;
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("NodeHashTable",node.name);
        classNodes.put("NodeHashTable",node);
        return hook;
    }
}
