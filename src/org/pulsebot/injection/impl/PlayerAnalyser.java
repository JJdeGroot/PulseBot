package org.pulsebot.injection.impl;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pulsebot.injection.generic.AbstractAnalyser;

import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 5/27/13
 * Time: 7:30 PM
 */
public class PlayerAnalyser extends AbstractAnalyser {

    @Override
    protected boolean canRun(ClassNode node) {
        int inum = 0, strnum = 0;
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();
            if((fn.access & Opcodes.ACC_STATIC)==0){
                if(fn.desc.equals("Ljava/lang/String;"))
                    strnum++;
                if(fn.desc.equals("I"))
                    inum++;
            }
        }
        return inum == 15 && strnum == 1;
    }

    @Override
    protected String analyse(ClassNode node) {
        System.out.println("Found Player Class at: "+node.name);
        node.interfaces.add("org/pulsebot/injection/interfaces/Player");
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();

            if((fn.access & Opcodes.ACC_STATIC)==0){

                if(fn.desc.equals("Ljava/lang/String;")){
                    System.out.println("   Name is at: "+fn.name);
                    MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getPlayerName", "()Ljava/lang/String;", null, null);
                    getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
                    getter.instructions.add(new InsnNode(Opcodes.ARETURN));
                    int size = getter.instructions.size();
                    getter.visitMaxs(size, size);
                    getter.visitEnd();
                    node.methods.add(getter);
                    System.out.println("   Added getter at:" + getter.name);

                }


            }
        }

        return node.name;
    }

}
