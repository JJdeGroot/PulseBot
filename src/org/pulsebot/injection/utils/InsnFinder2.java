package org.pulsebot.injection.utils;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class InsnFinder2 {

	private static String[] opcodes = new String[] { "NOP", "ACONST_NULL",
		"ICONST_M1", "ICONST_0", "ICONST_1", "ICONST_2", "ICONST_3",
		"ICONST_4", "ICONST_5", "LCONST_0", "LCONST_1", "FCONST_0",
		"FCONST_1", "FCONST_2", "DCONST_0", "DCONST_1", "BIPUSH", "SIPUSH",
		"LDC", "LDC_W", "LDC2_W", "ILOAD", "LLOAD", "FLOAD", "DLOAD",
		"ALOAD", "ILOAD_0", "ILOAD_1", "ILOAD_2", "ILOAD_3", "LLOAD_0",
		"LLOAD_1", "LLOAD_2", "LLOAD_3", "FLOAD_0", "FLOAD_1", "FLOAD_2",
		"FLOAD_3", "DLOAD_0", "DLOAD_1", "DLOAD_2", "DLOAD_3", "ALOAD_0",
		"ALOAD_1", "ALOAD_2", "ALOAD_3", "IALOAD", "LALOAD", "FALOAD",
		"DALOAD", "AALOAD", "BALOAD", "CALOAD", "SALOAD", "ISTORE",
		"LSTORE", "FSTORE", "DSTORE", "ASTORE", "ISTORE_0", "ISTORE_1",
		"ISTORE_2", "ISTORE_3", "LSTORE_0", "LSTORE_1", "LSTORE_2",
		"LSTORE_3", "FSTORE_0", "FSTORE_1", "FSTORE_2", "FSTORE_3",
		"DSTORE_0", "DSTORE_1", "DSTORE_2", "DSTORE_3", "ASTORE_0",
		"ASTORE_1", "ASTORE_2", "ASTORE_3", "IASTORE", "LASTORE",
		"FASTORE", "DASTORE", "AASTORE", "BASTORE", "CASTORE", "SASTORE",
		"POP", "POP2", "DUP", "DUP_X1", "DUP_X2", "DUP2", "DUP2_X1",
		"DUP2_X2", "SWAP", "IADD", "LADD", "FADD", "DADD", "ISUB", "LSUB",
		"FSUB", "DSUB", "IMUL", "LMUL", "FMUL", "DMUL", "IDIV", "LDIV",
		"FDIV", "DDIV", "IREM", "LREM", "FREM", "DREM", "INEG", "LNEG",
		"FNEG", "DNEG", "ISHL", "LSHL", "ISHR", "LSHR", "IUSHR", "LUSHR",
		"IAND", "LAND", "IOR", "LOR", "IXOR", "LXOR", "IINC", "I2L", "I2F",
		"I2D", "L2I", "L2F", "L2D", "F2I", "F2L", "F2D", "D2I", "D2L",
		"D2F", "I2B", "I2C", "I2S", "LCMP", "FCMPL", "FCMPG", "DCMPL",
		"DCMPG", "IFEQ", "IFNE", "IFLT", "IFGE", "IFGT", "IFLE",
		"IF_ICMPEQ", "IF_ICMPNE", "IF_ICMPLT", "IF_ICMPGE", "IF_ICMPGT",
		"IF_ICMPLE", "IF_ACMPEQ", "IF_ACMPNE", "GOTO", "JSR", "RET",
		"TABLESWITCH", "LOOKUPSWITCH", "IRETURN", "LRETURN", "FRETURN",
		"DRETURN", "ARETURN", "RETURN", "GETSTATIC", "PUTSTATIC",
		"GETFIELD", "PUTFIELD", "INVOKEVIRTUAL", "INVOKESPECIAL",
		"INVOKESTATIC", "INVOKEINTERFACE", "INVOKEDYNAMIC", "NEW",
		"NEWARRAY", "ANEWARRAY", "ARRAYLENGTH", "ATHROW", "CHECKCAST",
		"INSTANCEOF", "MONITORENTER", "MONITOREXIT", "WIDE",
		"MULTIANEWARRAY", "IFNULL", "IFNONNULL", "GOTO_W", "JSR_W" };
	
	private MethodNode mn;
	private AbstractInsnNode[] instructions;
	private int[] offsets;
	private String insnString;
	
	/**
	 * Constructor for InsnFinder
	 * @param clazz class node
	 * @param method methode node
	 */
	public InsnFinder2(MethodNode mn) {
		this.mn = mn;
		buildInstructionString();
	}
	
	/**
	 * Builds the instruction string
	 */
	public void buildInstructionString() {
		final List<AbstractInsnNode> il = new ArrayList<AbstractInsnNode>();
		for (final AbstractInsnNode ain : mn.instructions.toArray())
			if (ain.getOpcode() >= 0) {
				il.add(ain);
			}
		
		instructions = il.toArray(new AbstractInsnNode[il.size()]);
		offsets = new int[instructions.length];
		insnString = "";
		
		for (int i = 0; i < instructions.length; i++) {
			offsets[i] = -1;
			final AbstractInsnNode ain = instructions[i];
			offsets[i] = insnString.length();
			insnString += opcodes[ain.getOpcode()];
			
			switch (ain.getType()) {
				case AbstractInsnNode.INT_INSN:
					final IntInsnNode iin = (IntInsnNode) ain;
					insnString += "{" + iin.operand + "}";
					break;
				case AbstractInsnNode.LDC_INSN:
					final LdcInsnNode lin = (LdcInsnNode) ain;
					insnString += "{" + lin.cst.toString().replace("}", "\\}")
							+ "}";
					break;
				case AbstractInsnNode.VAR_INSN:
					final VarInsnNode vin = (VarInsnNode) ain;
					insnString += "_" + vin.var;
					break;
				case AbstractInsnNode.IINC_INSN:
					final IincInsnNode iiin = (IincInsnNode) ain;
					insnString += "{" + iiin.var + "," + iiin.incr + "}";
					break;
				case AbstractInsnNode.FIELD_INSN:
					final FieldInsnNode fin = (FieldInsnNode) ain;
					insnString += "{" + fin.desc + "," + fin.owner + ","
							+ fin.name + "}";
					break;
				case AbstractInsnNode.METHOD_INSN:
					final MethodInsnNode min = (MethodInsnNode) ain;
					insnString += "{" + min.desc + "," + min.owner + ","
							+ min.name + "}";
					break;
				case AbstractInsnNode.TYPE_INSN:
					final TypeInsnNode tin = (TypeInsnNode) ain;
					insnString += "{" + tin.desc + "}";
					break;
				case AbstractInsnNode.MULTIANEWARRAY_INSN:
					final MultiANewArrayInsnNode manain = (MultiANewArrayInsnNode) ain;
					insnString += "{" + manain.dims + "," + manain.desc + "}";
					break;
			}
			insnString += " ";
		}
	}
	
	public String getInsnString(){
		return insnString;
	}
	
	
	
	
	
	
}
