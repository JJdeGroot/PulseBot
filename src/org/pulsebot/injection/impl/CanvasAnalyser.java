package org.pulsebot.injection.impl;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyser;
import org.pulsebot.injection.utils.GenericUtils;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 6/2/13
 * Time: 12:40 PM
 */
public class CanvasAnalyser extends AbstractAnalyser {
    @Override
    protected boolean canRun(ClassNode node) {
        if(node.superName.contains("Canvas"))
            return true;
        return false;
    }

    @Override
    protected String analyse(ClassNode node) {
        System.out.println("Canvas is: "+node.superName);
        GenericUtils.setSuper(node,"org/pulsebot/injection/generic/RSCanvas");
        System.out.println("Canvas is now: "+node.superName);
        System.out.println("Canvas successfully hacked.");
        return "Canvas";
    }


}

