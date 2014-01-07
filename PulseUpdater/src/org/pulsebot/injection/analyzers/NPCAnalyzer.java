package org.pulsebot.injection.analyzers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.FieldAnalyzer;
import org.pulsebot.injection.generic.Hook;
import org.pulsebot.searchers.InsSearcher;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: 40900011
 * Date: 9/27/13
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class NPCAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {

        return !(!node.superName.equals(classNodes.get("Actor").name) || node.name.equals(classNodes.get("Player").name));
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("NPC", node.name);
        classNodes.put("NPC",node);
        for (Object mn : node.methods) {
            new NPCDefAnalyzer(node,hook,(MethodNode)mn).run();
        }
        return hook;
    }

    private class NPCDefAnalyzer extends FieldAnalyzer {
        private ArrayList<AbstractInsnNode> arrList = new ArrayList<>();
        Pattern defPattern = Pattern.compile("L(\\w+);");
        Matcher defMatcher;
        InsSearcher searcher = new InsSearcher(mn);
        int[] pattern = {Opcodes.ALOAD, Opcodes.GETFIELD};
        public NPCDefAnalyzer(ClassNode node, Hook hook, MethodNode mn) {
            super(node, hook, mn);
        }

        @Override
        protected boolean canRun() {

           arrList =  searcher.match(pattern);

            return !hook.getFieldHooks().containsKey("NPCDefinition") && arrList != null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void analyze() {
            for(AbstractInsnNode node1 : arrList){
                searcher.setIndex(node1);
                node1 = searcher.getPrevious(Opcodes.GETFIELD);
                if(node1 != null && node1 instanceof FieldInsnNode)
                    defMatcher = defPattern.matcher(((FieldInsnNode) node1).desc);
                if(defMatcher.find() && defMatcher != null){
                    className.put("NPCDefinition", defMatcher.group(1));
                    hook.addFieldHook("NPCDefinition", node.name + "." + ((FieldInsnNode) node1).name, "L" + defMatcher.group(1) + ";");
                    break;
                }

            }

        }
    }
}
