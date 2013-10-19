package org.pulsebot.searchers;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class InsSearcher {
    private InsnList list;
    private AbstractInsnNode currentNode;
    public InsSearcher(MethodNode mn){
        list = mn.instructions;
        currentNode = list.getFirst();
    }
    public AbstractInsnNode match(int... opcodes){
        refresh();
        for (int i = 0; i < opcodes.length; i++){
            if(currentNode == null){
                refresh();
                return null;
            }
            if(currentNode.getOpcode() == opcodes[i]){
                currentNode = currentNode.getNext();
                continue;
            }
            else if(currentNode.getOpcode() == Opcodes.GOTO){

                if(((JumpInsnNode)currentNode).label.getNext().getOpcode() == opcodes[i]){
                    currentNode = getNextMulti(2);
                    continue;
                }
            }
            i = 0;
            currentNode = currentNode.getNext();
        }
        return currentNode.getPrevious();
    }
    public AbstractInsnNode getNext(){
        if(currentNode != null){
            currentNode = currentNode.getNext();
            while(currentNode != null && currentNode.getOpcode() != -1)
                currentNode = currentNode.getNext();
        }
        return currentNode;

    }

    public AbstractInsnNode getNextMulti(int number){
        for (int i = 0; i < number; i++){
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }

    public AbstractInsnNode getNext(int opcode){
        while(currentNode != null){
            if(currentNode.getOpcode() == opcode){
                AbstractInsnNode old = currentNode;
                currentNode = currentNode.getNext();
                return old;
            }
            currentNode = currentNode.getNext();
        }

        return currentNode;
    }

    public AbstractInsnNode getPrevious(){
        if(currentNode != null){
            currentNode = currentNode.getPrevious();
            while(currentNode != null && currentNode.getOpcode() != -1)
                currentNode = currentNode.getPrevious();
        }
        return currentNode;
    }

    public AbstractInsnNode getPrevious(int opcode){
        while(currentNode != null){
            if(currentNode.getOpcode() == opcode){
                AbstractInsnNode old = currentNode;
                currentNode = currentNode.getNext();
                return old;
            }
            currentNode = currentNode.getPrevious();
        }
        return currentNode;
    }

    public void refresh(){
        currentNode = list.getFirst();
    }

    public void setIndex(int i){
        currentNode = list.get(i);
    }

    public int getIndex(){
        return list.indexOf(currentNode);
    }

    public AbstractInsnNode getCurrentNode(){
        return currentNode;
    }

}
