package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * CacheNode has:
 * - Super class Node
 * - Two instances of itself -> getNext(), getPrev()
 * 
 * @author JJ
 */
public class CacheableNodeAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is Node
		if(node.superName.equals(classnames.get("Node"))){
			//System.out.println(node.name + " extends Node");
			
			// Checking how many instances of itself
			int self = 0;
			List<FieldNode> fieldNodes = node.fields;
			for(FieldNode fieldNode : fieldNodes){
				if(fieldNode.desc.equals("L" + node.name + ";")){ // Instance of itself
					self++;
				}
			}
			//System.out.println(node.name + " has " + self + " fields of itself");

			return self == 2;
		}
		
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ CacheableNode ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("CacheableNode", node.name);
		classnode = node;
		return "CacheableNode";
	}

}
