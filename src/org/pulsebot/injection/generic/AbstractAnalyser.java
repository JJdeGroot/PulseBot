package org.pulsebot.injection.generic;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created with IntelliJ IDEA.
 * User: Chris
 * Date: 3/1/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public  abstract class AbstractAnalyser {

    public String run(ClassNode node){
        if(this.canRun(node))
            return this.analyse(node);
        return null;
    }
    protected abstract boolean canRun(ClassNode node);
    protected abstract String analyse(ClassNode node);
}
