package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;

/**
 * NodeHashTable has
 * - Node array field
 * 
 * @author JJ
 *
 */
public class NodeHashTableAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Looping through all fields
		List<FieldNode> fieldNodes = node.fields;
		String arrayName = "[L" + classnames.get("Node") + ";";
		for(FieldNode fieldNode : fieldNodes){
			if(fieldNode.desc.equals(arrayName)){ // Array of type Node
				return true;
			}
		}

		//System.out.println(node.name + " node array fields: " + arrayFields);
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ NodeHashTable ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("NodeHashTable", node.name);
		classnode = node;
		return "NodeHashTable";
	}

}
