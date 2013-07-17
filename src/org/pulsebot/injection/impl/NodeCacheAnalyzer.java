package org.pulsebot.injection.impl;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * NodeCache has
 * - Class type public, final, extend
 * - One or more NodeHashTable fields
 * - One or more CacheableNode fields
 * - One or more CacheableNodeQueue fields
 * 
 * @author JJ
 *
 */
public class NodeCacheAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		if(node.access == (Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_SUPER)){
			// Field names
			String nhtField = "L" + classnames.get("NodeHashTable") + ";";
			String cnField = "L" + classnames.get("CacheableNode") + ";";
			String cnqField = "L" + classnames.get("CacheableNodeQueue") + ";";
			int nhtCount = 0, cnCount = 0, cnqCount = 0;
			
			// Looping through all fields
			List<FieldNode> fieldNodes = node.fields;
			for(FieldNode fieldNode : fieldNodes){
				if(fieldNode.desc.equals(nhtField)){ // NodeHashTable
					nhtCount++;
				}else if(fieldNode.desc.equals(cnField)){ // CacheableNode
					cnCount++;
				} else if(fieldNode.desc.equals(cnqField)){ // CacheableNodeQueue
					cnqCount++;
				}
			}
			//System.out.println("NHT count: " + nhtCount + ", CN count: " + cnCount + ", CNQ count: " + cnqCount);
			
			return nhtCount > 0 && cnCount > 0 && cnqCount > 0;
		}
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ NodeCache ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("NodeCache", node.name);
		classnode = node;
		return "NodeCache";
	}

}
