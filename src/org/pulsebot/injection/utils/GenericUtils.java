package org.pulsebot.injection.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.math.BigInteger;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/7/13
 * Time: 11:16 PM
 */
public class GenericUtils {
    /**
     *
     * @param node  Classnode to add the method to
     * @param owner Owner of the field
     * @param fieldName Name of the field
     * @param fieldDesc Description of the field
     * @param methodName  The name of the method
     * @param returnType  What type of interface is it returning to
     * @param multiplier  The fields multiplier, return -1 if no multiplier
     * @param statc  Return true if the field static
     *
     * @author NKN
     */
    public static void addMethod(ClassNode node, String owner,String fieldName, String fieldDesc, String methodName, String returnType,int multiplier,boolean statc){
        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, methodName, returnType, null, null);
        getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        if(statc)
            getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, owner, fieldName, fieldDesc));
        else
            getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, owner, fieldName, fieldDesc));
        if(multiplier != -1){
            getter.instructions.add(new LdcInsnNode(multiplier));
            getter.instructions.add(new InsnNode(Opcodes.IMUL));
            getter.instructions.add(new InsnNode(Opcodes.IRETURN));
        } else
            getter.instructions.add(new InsnNode(Opcodes.ARETURN));
        int size = getter.instructions.size();
        getter.visitMaxs(size, size);
        getter.visitEnd();
        node.methods.add(getter);


    }
    public static int getMultiplier(ClassNode cn, String owner, String fieldName) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        ListIterator<MethodNode> mnit = cn.methods.listIterator();
        while (mnit.hasNext()) {
            InsnList in = mnit.next().instructions;
            Iterator<AbstractInsnNode> anin = in.iterator();
            while (anin.hasNext()) {
                AbstractInsnNode an = anin.next();
                if (an instanceof FieldInsnNode) {
                    if (((FieldInsnNode) an).owner.equals(owner) && ((FieldInsnNode) an).name.equals(fieldName)) {
                        if (an.getNext().getOpcode() == Opcodes.LDC)
                            a.add((Integer) ((LdcInsnNode) an.getNext()).cst);
                        else if (an.getPrevious().getOpcode() == Opcodes.LDC)
                            a.add((Integer) ((LdcInsnNode) an.getPrevious()).cst);
                    }

                }
            }
        }
        if (a.size() > 0)
            return (GenericUtils.getMostFrequentNLogN(a).getKey());
        return -1;
    }

    public static void setSuper(ClassNode node, String superClass) {
        String replacedSuper = "";
        if (node.superName != "")
            replacedSuper = node.superName;
        if (replacedSuper != "") {
            ListIterator<?> mli = node.methods.listIterator();
            while (mli.hasNext()) {
                MethodNode mn = (MethodNode) mli.next();
                ListIterator<?> ili = mn.instructions.iterator();
                while (ili.hasNext()) {
                    AbstractInsnNode ain = (AbstractInsnNode) ili.next();
                    if (ain.getOpcode() == Opcodes.INVOKESPECIAL) {
                        MethodInsnNode min = (MethodInsnNode) ain;
                        if (min.owner.equals(replacedSuper)) {
                            min.owner = superClass;
                        }
                    }
                }
            }
        }
        node.superName = superClass;
    }

    public static int modInverse(String integer) {
        BigInteger modulus = new BigInteger(String.valueOf(1L << 32));
        BigInteger m1 = new BigInteger(integer);
        return m1.modInverse(modulus).intValue();
    }

    private static AbstractMap.SimpleEntry<Integer, Integer> getMostFrequentNLogN(ArrayList<Integer> values) {
        ArrayList<Integer> tmp = new ArrayList(values);

        Collections.sort(tmp);

        AbstractMap.SimpleEntry<Integer, Integer> max = new AbstractMap.SimpleEntry<>(0, 0);

        int current = tmp.get(0);
        int count = 0;
        for (int i = 0; i < tmp.size(); ++i) {
            if (tmp.get(i) == current) {
                count++;
            } else {
                if (count > max.getValue()) {
                    max = new AbstractMap.SimpleEntry<Integer, Integer>(current, count);
                }

                current = tmp.get(i);

                count = 1;
            }
        }

        if (count > max.getValue()) {
            max = new AbstractMap.SimpleEntry<Integer, Integer>(current, count);
        }

        return max;
    }

}
