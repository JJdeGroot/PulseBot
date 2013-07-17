package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * NodeQueue has
 * - Class type public, extend
 * - Extends java/lang/Object
 * - No instance of itself
 * - Two or more fields of type Node
 *
 * @author JJ
 *
 */
public class NodeQueueAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Checking class type
		if(node.access == (Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER)){
			// Checking super class name
            if(node.superName.contains("java/lang/Object")){
            	// Looping through all fields
        		List<FieldNode> fieldNodes = node.fields;
        		int nodeCount = 0;
        		String cnField = "L" + classnames.get("Node") + ";";
        		for(FieldNode fieldNode : fieldNodes){
        			if(fieldNode.desc.equals(cnField)){ // Type Node
        				nodeCount++;
        			}else if(fieldNode.desc.equals("L"+node.name+";")){ // Instance of itself
                        return false;
        			}
        		}
        		return nodeCount >= 2;
            }
		}
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ NodeQueue ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("NodeQueue", node.name);
		classnode = node;
		return "NodeQueue";
	}

}
