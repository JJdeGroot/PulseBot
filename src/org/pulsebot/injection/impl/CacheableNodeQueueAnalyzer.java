package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;

/**
 * CacheableNodeQue has
 * - Public, final, extend
 * - Atleast one CacheableNode field
 * - More than 10 methods to do with CacheableNode
 * 
 * @author JJ
 *
 */
public class CacheableNodeQueueAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Checking class type
		if(node.access == (Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_SUPER)){
			// Counting CacheableNode fields
			int cnFields = 0;
			String cnField = "L" + classnames.get("CacheableNode") + ";";
			List<FieldNode> fieldNodes = node.fields;
			for(FieldNode fieldNode : fieldNodes){
				if(fieldNode.desc.equals(cnField)){ // Type CacheableNode
					cnFields++;
				}
			}
			
			// Must have atleast one CacheableNode field
			if(cnFields > 0){			
				// Counting CacheableNode methods
				List<MethodNode> methodNodes = node.methods;
				int cnMethods = 0;
				for(MethodNode methodNode : methodNodes){
	                if(methodNode.desc.contains(cnField)){ // Does something with CacheableNode
	                	cnMethods++;
					}
	            }
				
				//System.out.println("CN Fields: " + cnFields + ", CN methods: " + cnMethods);
				return cnMethods > 10;
			}
		}

		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ CacheableNodeQueue ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("CacheableNodeQueue", node.name);
		classnode = node;
		return "CacheableNodeQueue";
	}

}
