package org.pulsebot.injection.impl;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.pulsebot.injection.generic.AbstractAnalyser;

import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/2/13
 * Time: 12:40 PM
 */
public class CanvasAnalyser extends AbstractAnalyser {
    @Override
    protected boolean canRun(ClassNode node) {
        if(node.superName.contains("Canvas"))
            return true;
        return false;
    }

    @Override
    protected String analyse(ClassNode node) {
        System.out.println("Canvas is: "+node.superName);
        setSuper(node,"org/pulsebot/injection/generic/RSCanvas");
        System.out.println("Canvas is now: "+node.superName);
        System.out.println("Canvas successfully hacked.");
        return "Canvas";
    }
    public static void setSuper(ClassNode node, String superClass) {
        String replacedSuper = "";
        if(node.superName != "")
            replacedSuper = node.superName;
        if(replacedSuper != "") {
            ListIterator<?> mli = node.methods.listIterator();
            while (mli.hasNext()) {
                MethodNode mn = (MethodNode) mli.next();
                ListIterator<?> ili = mn.instructions.iterator();
                while (ili.hasNext()) {
                    AbstractInsnNode ain = (AbstractInsnNode) ili.next();
                    if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if(min.owner.equals(replacedSuper)) {
                            min.owner = superClass;
                        }
                    }
                }
            }
        }
        node.superName = superClass;
    }

}

