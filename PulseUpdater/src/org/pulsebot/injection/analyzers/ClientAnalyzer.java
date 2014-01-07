package org.pulsebot.injection.analyzers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.FieldAnalyzer;
import org.pulsebot.injection.generic.Hook;
import org.pulsebot.searchers.InsSearcher;
import org.pulsebot.util.Utilities;

import java.util.ArrayList;
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
            new NPCArrayAnalyzer(node,hook,fn).run();
            new PlayerArrayAnalyzer(node,hook,fn).run();
        }

        for(Object mn : node.methods){
            new YawPitchAnalyzer(node,hook,(MethodNode)mn).run();
        }
        return hook;

    }

   private class NPCArrayAnalyzer extends FieldAnalyzer {
       public NPCArrayAnalyzer(ClassNode node, Hook hook, FieldNode fn) {
           super(node, hook, fn);
       }

       @Override
       protected boolean canRun() {

           return !hook.getFieldHooks().containsKey("NPCArray") && classNodes.containsKey("NPC") && fn.desc.equals("[L"+classNodes.get("NPC").name+";");
       }

       @Override
       protected void analyze() {
          hook.addFieldHook("NPCArray",node.name + "." + fn.name,fn.desc);
       }
   }

    private class PlayerArrayAnalyzer extends FieldAnalyzer {
        public PlayerArrayAnalyzer(ClassNode node, Hook hook, FieldNode fn) {
            super(node, hook, fn);
        }

        @Override
        protected boolean canRun() {

            return !hook.getFieldHooks().containsKey("PlayerArray") && classNodes.containsKey("Player") && fn.desc.equals("[L"+classNodes.get("Player").name+";");
        }

        @Override
        protected void analyze() {
            hook.addFieldHook("PlayerArray",node.name + "." + fn.name,fn.desc);
        }
    }

    private class YawPitchAnalyzer extends FieldAnalyzer {
        ArrayList<AbstractInsnNode> arrList = new ArrayList<>();
        InsSearcher searcher = new InsSearcher(mn);
        public YawPitchAnalyzer(ClassNode node, Hook hook, MethodNode mn) {
            super(node, hook, mn);
        }

        @Override
        protected boolean canRun() {

            int[] pat = {Opcodes.INVOKESTATIC, Opcodes.LDC,Opcodes.DMUL,Opcodes.D2I,Opcodes.SIPUSH};
            arrList = searcher.match(pat);
            return arrList != null;
        }

        @Override
        protected void analyze() {
            for(AbstractInsnNode node1 : arrList){
                searcher.setIndex(node1);
                if(((LdcInsnNode)searcher.getPrevious(Opcodes.LDC)).cst.equals(325.949)){
                    AbstractInsnNode ab = searcher.getNext(Opcodes.PUTSTATIC);
                    if(ab != null && searcher.getIndex(node1) - searcher.getIndex(ab) > -10 && !hook.getFieldHooks().containsKey("CameraPitch"))
                        hook.addFieldHook("CameraPitch",((FieldInsnNode)ab).owner + "." + ((FieldInsnNode)ab).name,((FieldInsnNode)ab).desc,Utilities.getMultiplier(node,((FieldInsnNode)ab).owner,((FieldInsnNode)ab).name));
                }
                else if(((LdcInsnNode)searcher.getPrevious(Opcodes.LDC)).cst.equals(-325.949)){
                    AbstractInsnNode ab = searcher.getNext(Opcodes.PUTSTATIC);
                    if(ab != null && !hook.getFieldHooks().containsKey("CameraYaw"))
                        hook.addFieldHook("CameraYaw",((FieldInsnNode)ab).owner + "." + ((FieldInsnNode)ab).name,((FieldInsnNode)ab).desc,Utilities.getMultiplier(node,((FieldInsnNode)ab).owner,((FieldInsnNode)ab).name));
                }
            }

        }
    }
}
