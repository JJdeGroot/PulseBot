package org.pulsebot.injection.analyzers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;
import org.pulsebot.searchers.InsSearcher;

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
            InsSearcher searcher = new InsSearcher((MethodNode)mn);
            AbstractInsnNode node1;
            int[] pattern = {Opcodes.ALOAD, Opcodes.GETFIELD};
            searcher.match(pattern);
            node1 = searcher.getPrevious(Opcodes.GETFIELD);
            if (node1 != null && node1 instanceof FieldInsnNode) {
                Pattern defPattern = Pattern.compile("L(\\w+);");
                Matcher defMatcher = defPattern.matcher(((FieldInsnNode) node1).desc);
                if (defMatcher.find()) {

                    className.put("NPCDefinition", defMatcher.group(1));
                    if (!hook.getFieldHooks().containsKey("NPCDefinition"))
                        hook.addFieldHook("NPCDefinition", node.name + "." + ((FieldInsnNode) node1).name, "L" + defMatcher.group(1) + ";");
                }

            }

        }
        return hook;
    }
}
