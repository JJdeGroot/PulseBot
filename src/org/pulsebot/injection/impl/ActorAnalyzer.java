package org.pulsebot.injection.impl;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;

/**
 * Actor has:
 * - Super class Renderable
 * - Access: public, abstract, extends
 * @author JJ
 *
 */
public class ActorAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is CacheNode
		if(node.superName.equals(classnames.get("Renderable"))){
			//System.out.println(node.name + " has super class Renderable");
			// Check if the access is public, abstract, extends
			if(node.access == (Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_SUPER)){
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Actor ]\n^ " + node.name + " extends " + node.superName);
		classnames.put("Actor", node.name);
		classnode = node;
		return "Actor";
	}

}
