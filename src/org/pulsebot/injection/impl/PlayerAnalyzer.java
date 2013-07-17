package org.pulsebot.injection.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.RSClient;

/**
 * Player has:
 * - Super class Actor
 * - Field type PlayerDefinition
 * 
 * @author JJ
 *
 */
public class PlayerAnalyzer extends AbstractAnalyzer {

	@Override
	protected boolean canRun(ClassNode node) {
		// Check if the super class is Actor
		if(node.superName.equals(classnames.get("Actor"))){
			//System.out.println(node.name + " has super class Actor");
			
			// Find a field with type Model
			String modelName = "L" + classnames.get("Model") + ";";
			List<FieldNode> fieldNodes = node.fields;
			for(FieldNode fieldNode : fieldNodes){
				if(fieldNode.desc.equals(modelName)){ // Model type
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected String analyse(ClassNode node) {
		System.out.println("[ Player ]\n^ " + node.name + " extends " + node.superName);

		// Player interface
		node.interfaces.add("org/pulsebot/injection/interfaces/IPlayer");
		
		// Player definition is the only special type together with model
		String modelName = "L" + classnames.get("Model") + ";";
		Pattern defPattern = Pattern.compile("L(\\w+);");
		List<FieldNode> fieldNodes = node.fields;
		for(FieldNode fieldNode : fieldNodes){
			if(fieldNode.desc.equals(modelName)){
				System.out.println("* getModel() -> " + fieldNode.desc + " " + node.name + "." + fieldNode.name);
			}else if(fieldNode.desc.equals("Ljava/lang/String;")){
				System.out.println("* getName() -> " + fieldNode.desc + " " + node.name + "." + fieldNode.name);
				createNormalMethod(node, fieldNode, "getName", "()Ljava/lang/String;");
			}else{
				Matcher defMatcher = defPattern.matcher(fieldNode.desc);
				if(defMatcher.find()){
					System.out.println("* getDefinition() -> " + fieldNode.desc + " " + node.name + "." + fieldNode.name);
					createNormalMethod(node, fieldNode, "getDefinition", "()Lorg/pulsebot/injection/interfaces/IPlayerDefinition;");
					classnames.put("PlayerDefinition", defMatcher.group(1));
				}
			}
		}


		
		classnames.put("Player", node.name);
		classnode = node;
		return "Player";
	}

}
