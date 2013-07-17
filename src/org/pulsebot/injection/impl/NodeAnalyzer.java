package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;

/**
 * Node has:
 * - Super class Object
 * - Two instances of itself -> getNext(), getPrev()
 * - One long -> getID()
 * 
 * @author JJ
 */
public class NodeAnalyzer extends AbstractAnalyzer{

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is object
		if(node.superName.equals("java/lang/Object")){
			int selfCount = 0, longCount = 0;
			
			// Looping through all fields
			List<FieldNode> fieldNodes = node.fields;
			for(FieldNode fieldNode : fieldNodes){
				if(fieldNode.desc.equals("L" + node.name + ";")){ // Instance of itself
					selfCount++;
				}else if(fieldNode.desc.equals("J")){ // Long
					longCount++;
				}
			}
			//System.out.println(node.name + " has " + self + " fields of itself & " + longs + " fields of type long");
	
			// A node has two instances of itself and a long
			return selfCount == 2 && longCount == 1;
		}
		
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Node ]\n^ " + node.name + " extends " + node.superName);
	
		/*
		// FIELDS
		List<FieldNode> fieldNodes = node.fields;
		for(FieldNode fieldNode : fieldNodes){
			System.out.println("Field: " + fieldNode.name + ", desc: " + fieldNode.desc + ", value: " + fieldNode.value);
			
			
			if(fieldNode.desc.equals("J")){ // Long
				System.out.println("\tgetId() ::: " + fieldNode.name); // getId() is a long
			}else if(fieldNode.desc.equals("L" + node.name + ";")){ // Instance of itself
				System.out.println("\tgetNode() ::: " + fieldNode.name);
			}
		}
		
		
		
		// METHODS
		List<MethodNode> methodNodes = node.methods;
		for(MethodNode methodNode : methodNodes){
			System.out.println("Method name: " + methodNode.name + ", desc: " + methodNode.desc + ", signature: " + methodNode.signature);
			
			List<LocalVariableNode> lvNodes = methodNode.localVariables;
			for(LocalVariableNode lvNode : lvNodes){
				System.out.println("Local variable node " + lvNode.name + ", desc: " + lvNode.desc + ", index: " + lvNode.index);
			}
			
		}
		*/
		
		classnames.put("Node", node.name);
		classnode = node;
		return "Node";
	}

}
