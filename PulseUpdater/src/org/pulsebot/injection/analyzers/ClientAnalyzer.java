package org.pulsebot.injection.analyzers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
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
public class ClientAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        return node.name.contains("client");
    }

    @Override
    protected Hook analyse(ClassNode node) {
        String pat;
        Hook hook = new Hook("Client",node.name);
        classNodes.put("Client",node);
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();
            if(classNodes.containsKey("NPC"))
                pat = "[L"+classNodes.get("NPC").name+";";
            else
                pat = "";
            if(fn.desc.equals(pat))
                if(!hook.getFieldHooks().containsKey("NPCArray"))
                    hook.addFieldHook("NPCArray",node.name + "." + fn.name, pat);

            if(classNodes.containsKey("Player"))
                pat = "[L"+classNodes.get("Player").name+";";
            else
                pat = null;
            if(fn.desc.equals(pat))
                if(!hook.getFieldHooks().containsKey("PlayerArray"))
                    hook.addFieldHook("PlayerArray",node.name + "." +fn.name, pat);
        }
        return hook;

    }
}
