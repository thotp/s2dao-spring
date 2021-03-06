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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.autoregister.ClassPattern;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractAutoRegister implements BeanFactoryAware,
    BeanFactoryPostProcessor {

    public static final String INIT_METHOD = "registerAll";

    private BeanFactory beanFactory;

    private final List classPatterns = new ArrayList();

    private final List ignoreClassPatterns = new ArrayList();

    private String addPackageName;

    private String addClassNames;

    private String ignorePackageName;

    private String ignoreClassNames;

    public void postProcessBeanFactory(
        final ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
        registerAll();
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return (DefaultListableBeanFactory) beanFactory;
    }

    public void setBeanFactory(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public int getClassPatternSize() {
        return classPatterns.size();
    }

    public void addClassPattern() {
        addClassPattern(new ClassPattern(getAddPackageName(),
            getAddClassNames()));
    }

    public void addClassPattern(final ClassPattern classPattern) {
        classPatterns.add(classPattern);
    }

    public ClassPattern getClassPattern(final int index) {
        return (ClassPattern) classPatterns.get(index);
    }

    public void addIgnoreClassPattern() {
        if (getIgnoreClassNames() != null) {
            addIgnoreClassPattern(new ClassPattern(getIgnorePackageName(),
                getIgnoreClassNames()));
        }
    }

    public void addIgnoreClassPattern(final ClassPattern classPattern) {
        ignoreClassPatterns.add(classPattern);
    }

    public abstract void registerAll();

    protected boolean hasBeanDefinition(final String name) {
        return findBeanDefinition(name) != null;
    }

    protected BeanDefinition findBeanDefinition(final String name) {
        if (name == null) {
            return null;
        }

        final String[] bdNames = getBeanFactory().getBeanDefinitionNames();
        for (int i = 0; i < getBeanFactory().getBeanDefinitionCount(); ++i) {
            final BeanDefinition bd = getBeanFactory().getBeanDefinition(
                bdNames[i]);
            if (name.equals(bd.getBeanClassName())) {
                return bd;
            }
        }
        return null;
    }

    protected boolean isIgnore(final String packageName,
        final String shortClassName) {
        if (ignoreClassPatterns.isEmpty()) {
            return false;
        }
        for (int i = 0; i < ignoreClassPatterns.size(); ++i) {
            final ClassPattern cp = (ClassPattern) ignoreClassPatterns.get(i);
            if (!cp.isAppliedPackageName(packageName)) {
                continue;
            }
            if (cp.isAppliedShortClassName(shortClassName)) {
                return true;
            }
        }
        return false;
    }

    public void setAddPackageName(final String addPackageName) {
        this.addPackageName = addPackageName;
    }

    public String getAddPackageName() {
        return addPackageName;
    }

    public void setAddClassNames(final String addClassNames) {
        this.addClassNames = addClassNames;
    }

    public String getAddClassNames() {
        return addClassNames;
    }

    public void setIgnorePackageName(final String ignorePackageName) {
        this.ignorePackageName = ignorePackageName;
    }

    public String getIgnorePackageName() {
        return ignorePackageName;
    }

    public void setIgnoreClassNames(final String ignoreClassNames) {
        this.ignoreClassNames = ignoreClassNames;
    }

    public String getIgnoreClassNames() {
        return ignoreClassNames;
    }
}