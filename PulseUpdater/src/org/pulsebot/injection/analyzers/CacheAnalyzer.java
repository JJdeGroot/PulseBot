package org.pulsebot.injection.analyzers;

import org.objectweb.asm.tree.ClassNode;
import org.pulsebot.injection.generic.AbstractAnalyzer;
import org.pulsebot.injection.generic.Hook;

/**
 * Created with IntelliJ IDEA.
 * User: NKN
 * Date: 9/24/13
 * Time: 9:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class CacheAnalyzer extends AbstractAnalyzer {
    @Override
    protected boolean canRun(ClassNode node) {
        return false;
    }

    @Override
    protected Hook analyse(ClassNode node) {
        return null;
    }
}
