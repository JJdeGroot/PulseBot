package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * Model has
 * - Super class Renderable
 * - Class type public and extends
 * - Two instances of itself
 * - Method that does something with an instance of itself
 * 
 * @author JJ
 */
public class ModelAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is Renderable
		if(node.superName.equals(classnames.get("Renderable"))){
			// Checking if the class types are correct
			if(node.access == (Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER)){				
				// Checking how many instances of itself
				int selfCount = 0;
				List<FieldNode> fieldNodes = node.fields;
				for(FieldNode fieldNode : fieldNodes){
					if(fieldNode.desc.equals("L" + node.name + ";")){ // Instance of itself
						selfCount++;
					}
				}
				//System.out.println(node.name + " has " + fieldSelf + " fields of itself");
				
				// Needs to have to instances of itself
				if(selfCount == 2){
					// Checking if there is a method that does something with an instance of itself
					List<MethodNode> methodNodes = node.methods;
					for(MethodNode methodNode : methodNodes){
		                if(methodNode.desc.contains("L" + node.name + ";")){ // Does something with itself
		                	return true;
						}
		            }
				}
			}
		}
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Model ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("Model", node.name);
		classnode = node;
		return "Model";
	}

}
