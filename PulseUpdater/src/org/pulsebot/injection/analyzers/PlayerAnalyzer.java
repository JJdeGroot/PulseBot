package org.pulsebot.injection.analyzers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.FieldAnalyzer;
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
        int stringCount = 0;
        if(node.superName.equals(classNodes.get("Actor").name)){
            ListIterator<FieldNode> fnIt = node.fields.listIterator();
            while(fnIt.hasNext()){
                FieldNode fn = fnIt.next();
                if(fn.desc.equals("Ljava/lang/String;"))
                    stringCount++;

            }
            if(stringCount == 1){
                ListIterator<MethodNode> mnIt = node.methods.listIterator();
                while(mnIt.hasNext()){
                    MethodNode mn = mnIt.next();
                    Pattern methodPattern = Pattern.compile("\\)(\\w+)");
                    Matcher matcher = methodPattern.matcher(mn.desc);
                    if(matcher.find() && matcher.groupCount() > 1){
                        if(!matcher.group(1).equals("L"+classNodes.get("Model").name))
                            return false;
                    }

                }
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
            new PlayerNameAnalyzer(node,hook,fn).run();
            new PlayerModelAnalyzer(node,hook,fn).run();
        }
        ListIterator<MethodNode> mnIt = node.methods.listIterator();
        while(mnIt.hasNext()){
            MethodNode mn = mnIt.next();
            new PlayerDefAnalyzer(node,hook,mn).run();
        }
        return hook;
    }

    private class PlayerModelAnalyzer extends FieldAnalyzer {
        private String pat = "L"+classNodes.get("Model").name + ";";
        public PlayerModelAnalyzer(ClassNode node, Hook hook, FieldNode fn) {
            super(node, hook, fn);
        }

        @Override
        protected boolean canRun() {
            return fn.desc.equals(pat) && !hook.getFieldHooks().containsKey("PlayerModel");
        }

        @Override
        protected void analyze() {
            hook.addFieldHook("PlayerModel",node.name + "." + fn.name, pat);
        }
    }

    private class PlayerDefAnalyzer extends FieldAnalyzer {
        private FieldInsnNode fn;
        private String classCall;
        public PlayerDefAnalyzer(ClassNode node, Hook hook, MethodNode mn) {
            super(node, hook, mn);
        }
        @Override
        protected boolean canRun() {
            if(!hook.getFieldHooks().containsKey("PlayerDefinition")) {
                InsSearcher searcher = new InsSearcher(mn);
                AbstractInsnNode node1;
                int[] pattern = new int[] {Opcodes.ALOAD, Opcodes.GETFIELD, Opcodes.IF_ACMPNE};
                searcher.match(pattern);
                node1 = searcher.getPrevious(Opcodes.GETFIELD);
                if(node1 != null && node1 instanceof FieldInsnNode){
                    Pattern defPattern = Pattern.compile("L(\\w+);");
                    Matcher defMatcher = defPattern.matcher(((FieldInsnNode)node1).desc);
                    if(defMatcher.find()){
                        this.classCall = defMatcher.group(1);
                        this.fn = (FieldInsnNode)node1;
                        return true;
                    }
                }
            }

            return false;
        }

        @Override
        protected void analyze() {
            hook.addFieldHook("PlayerDefinition",node.name + "." +fn.name,"L"+classCall+";");
            className.put("PlayerDefinition",classCall);
        }
    }

    private class PlayerNameAnalyzer extends FieldAnalyzer{

        public PlayerNameAnalyzer(ClassNode node, Hook hook, FieldNode fn) {
            super(node, hook, fn);
        }

        @Override
        protected boolean canRun() {
            return fn.desc.equals("Ljava/lang/String;") && !hook.getFieldHooks().containsKey("PlayerName");
        }

        @Override
        protected void analyze() {
            hook.addFieldHook("PlayerName", node.name + "." + fn.name, "Ljava/lang/String");
        }


    }
}
