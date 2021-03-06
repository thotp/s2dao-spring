/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dao.spring.autoregister;

import java.io.File;

import org.seasar.framework.container.autoregister.ClassPattern;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

public class FileSystemBeanAutoRegister extends AbstractBeanAutoRegister {

    public void registerAll() {
        addClassPattern();
        addIgnoreClassPattern();
        for (int i = 0; i < getClassPatternSize(); ++i) {
            final ClassPattern cp = getClassPattern(i);
            register(cp);
        }
    }

    protected void register(final ClassPattern classPattern) {
        final String packageName = classPattern.getPackageName();
        final File packageDir = getRootDir();
        ClassTraversal.forEach(packageDir, packageName, this);
    }

    protected File getRootDir() {
        final String path = "beanRefContext.xml";
        File file = ResourceUtil.getResourceAsFile(path);
        final String[] names = StringUtil.split(path, "/");
        for (int i = 0; i < names.length; ++i) {
            file = file.getParentFile();
        }
        return file;
    }

}