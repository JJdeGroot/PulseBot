package org.pulsebot.injection.impl;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * Renderable has:
 * - Super class CacheableNode
 * - Access: public, abstract, extends
 * 
 * @author JJ
 *
 */
public class RenderableAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is CacheNode
		if(node.superName.equals(classnames.get("CacheableNode"))){
			//System.out.println(node.name + " has super class CacheNode");
			// Check if the access is public, abstract, extends
			if(node.access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_SUPER)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Renderable ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("Renderable", node.name);
		classnode = node;
		return "Renderable";
	}

}
