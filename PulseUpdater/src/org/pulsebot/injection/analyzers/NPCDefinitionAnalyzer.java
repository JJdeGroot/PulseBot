package org.pulsebot.injection.analyzers;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.FieldAnalyzer;
import org.pulsebot.injection.generic.Hook;

import java.util.ListIterator;

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
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();
            new NPCActionsAnalyzer(node,hook,fn).run();
            new NPCNameAnalyzer(node,hook,fn).run();
        }

        return hook;
    }


    private class NPCNameAnalyzer extends FieldAnalyzer {
        public NPCNameAnalyzer(ClassNode node, Hook hook, FieldNode fn) {
            super(node, hook, fn);
        }

        @Override
        protected boolean canRun() {
            return !hook.getFieldHooks().containsKey("NPCName") && fn.desc.equals("Ljava/lang/String;");
        }

        @Override
        protected void analyze() {
            hook.addFieldHook("NPCName",fn.name,fn.desc);
        }
    }

    private class NPCActionsAnalyzer extends FieldAnalyzer {
        public NPCActionsAnalyzer(ClassNode node, Hook hook, FieldNode fn) {
            super(node, hook, fn);
        }

        @Override
        protected boolean canRun() {
            return !hook.getFieldHooks().containsKey("NPCActions") && fn.desc.equals("[Ljava/lang/String;");
        }

        @Override
        protected void analyze() {
            hook.addFieldHook("NPCActions",fn.name,fn.desc);
        }
    }

}
