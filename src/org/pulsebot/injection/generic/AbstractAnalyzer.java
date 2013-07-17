package org.pulsebot.injection.generic;

import java.util.HashMap;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public abstract class AbstractAnalyzer {

	public static HashMap<String, String> classnames = new HashMap<String, String>();
	public ClassNode classnode;
	
	/** Runs the analyzer */
    public String run(ClassNode node) {
        if (canRun(node))
            return analyse(node);
        return null;
    }

    /** Checks if the analyzer can run */
    protected abstract boolean canRun(ClassNode node);

    /** Checks if the analyzes the node */
    protected abstract String analyse(ClassNode node);
    
    /** Makes a new static method */
    public void createStaticMethod(ClassNode node, FieldNode fn, String methodName, String returnType){
        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, methodName, returnType, null, null);
        getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, node.name, fn.name, fn.desc));
        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
        int size = getter.instructions.size();
        getter.visitMaxs(size, size);
        getter.visitEnd();
        node.methods.add(getter);
        System.out.println("Created static method " + methodName + " returns " + returnType);
    }
    
    /** Makes a new normal method */
    public void createNormalMethod(ClassNode node, FieldNode fn, String methodName, String returnType){
        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, methodName, returnType, null, null);
        getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, fn.name, fn.desc));
        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
        int size = getter.instructions.size();
        getter.visitMaxs(size, size);
        getter.visitEnd();
        node.methods.add(getter);
        System.out.println("Created normal method " + methodName + " returns " + returnType);
    }
    
    

}