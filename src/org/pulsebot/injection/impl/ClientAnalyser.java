package org.pulsebot.injection.impl;


import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.pulsebot.injection.generic.AbstractAnalyser;
import org.pulsebot.loader.ClientApplet;

import java.util.HashMap;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 5/27/13
 * Time: 2:22 PM
 */
public class ClientAnalyser extends AbstractAnalyser {
    @Override
    protected boolean canRun(ClassNode node) {
        int fields = 0,self=0;
        ListIterator<FieldNode> fnit = node.fields.listIterator();
        while(fnit.hasNext()){
            FieldNode fn = fnit.next();
            if(fn != null)
                fields++;
            if(fn.desc.contains(node.name))
                self++;

        }
        return fields >= 200&&self==1;
    }

    @Override
    protected String analyse(ClassNode node) {
        System.out.println("Client class is at: "+node.name);
        HashMap<String,String> names = ClientApplet.CLASSNAMES;
        node.interfaces.add("org/pulsebot/injection/interfaces/ClientInterface");
        ListIterator<FieldNode> fnIt = node.fields.listIterator();
        while(fnIt.hasNext()){
            FieldNode fn = fnIt.next();
            if(fn.desc.equals("[L"+names.get("Player")+";")){
                System.out.println("   Player Array at: " + fn.name);
                MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getPlayerArray", "()[Lorg/pulsebot/injection/interfaces/Player;", null, null);
                getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, node.name, fn.name, fn.desc));
                getter.instructions.add(new InsnNode(Opcodes.ARETURN));
                int size = getter.instructions.size();
                getter.visitMaxs(size, size);
                getter.visitEnd();
                node.methods.add(getter);
            }




        }

        return node.name;
    }
}
