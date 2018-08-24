package com.apple.demo.loader.decorator.proxyhandlers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.apple.demo.element.HtmlElement;

import static com.apple.demo.loader.HtmlElementLoader.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;


public class HtmlElementListNamedProxyHandler<T extends HtmlElement> implements InvocationHandler {
    private final Class<T> elementClass;
    private final ElementLocator locator;
    private final String name;

    public HtmlElementListNamedProxyHandler(Class<T> elementClass, ElementLocator locator, String name) {
        this.elementClass = elementClass;
        this.locator = locator;
        this.name = name;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if ("toString".equals(method.getName())) {
            return name;
        }

        List<T> elements = new LinkedList<>();
        int elementNumber = 0;
        for (WebElement element : locator.findElements()) {
            String newName = String.format("%s [%d]", name, elementNumber++);
            elements.add(createHtmlElement(elementClass, element, newName));
        }

        try {
            return method.invoke(elements, objects);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
