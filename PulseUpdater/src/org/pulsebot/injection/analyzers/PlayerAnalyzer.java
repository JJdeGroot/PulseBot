package org.pulsebot.injection.analyzers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;
import org.pulsebot.searchers.InsSearcher;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 9/24/13
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        if(node.superName.equals(classNodes.get("Actor").name)){
            String modelPattern = "L"+classNodes.get("Model").name+";";
            ListIterator<FieldNode> fnIt = node.fields.listIterator();
            while(fnIt.hasNext()){
                FieldNode fn = fnIt.next();
                if(fn.desc.equals(modelPattern))
                    return true;
            }
        }
        return false;
    }

    @Override
    protected Hook analyse(ClassNode node) {
        Hook hook = new Hook("Player",node.name);
        classNodes.put("Player",node);
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();
            if(fn.desc.equals("Ljava/lang/String;")){
                if(!hook.getFieldHooks().containsKey("PlayerName"))
                    hook.addFieldHook("PlayerName", node.name + "." + fn.name, "Ljava/lang/String");
            }
            String pat = "L"+classNodes.get("Model").name + ";";
            if(fn.desc.equals(pat))
                if(!hook.getFieldHooks().containsKey("PlayerModel"))
                    hook.addFieldHook("PlayerModel",node.name + "." + fn.name, pat);
        }
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        while(mnIt.hasNext()){
            MethodNode mn = mnIt.next();
            InsSearcher searcher = new InsSearcher(mn);
            AbstractInsnNode node1;








            // PlayerDefinition Analyzer

            int[] pattern = new int[] {Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IF_ACMPNE};
            searcher.match(pattern);
            node1 = searcher.getPrevious(Opcodes.GETFIELD);
            if(node1 != null && node1 instanceof FieldInsnNode){
                Pattern defPattern = Pattern.compile("L(\\w+);");
                Matcher defMatcher = defPattern.matcher(((FieldInsnNode)node1).desc);
                if(defMatcher.find()){

                    if(!hook.getFieldHooks().containsKey("PlayerDefinition")){
                        hook.addFieldHook("PlayerDefinition",node.name + "." +((FieldInsnNode)node1).name,"L"+defMatcher.group(1)+";");
                        className.put("PlayerDefinition",defMatcher.group(1));
                    }
                }

            }

            //End PlayerDefinition Analyzer

        }
        return hook;
    }
}
