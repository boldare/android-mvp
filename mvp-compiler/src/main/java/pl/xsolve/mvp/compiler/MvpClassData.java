package pl.xsolve.mvp.compiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import pl.xsolve.mvp.api.MvpPresenter;

import static com.google.auto.common.MoreElements.getPackage;
import static pl.xsolve.mvp.compiler.MvpProcessor.BINDER_NAME;

class MvpClassData {
    private final TypeElement typeElement;
    Map<String, Element> viewStateElements = new HashMap<>();
    Set<Element> presenterElements = new HashSet<>();

    MvpClassData(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    void collectViewState(Element element) {
        String objectType = element.getSimpleName().toString();
        viewStateElements.put(objectType, element);
    }

    void collectPresenter(Element element) {
        presenterElements.add(element);
    }

    String getPackageName() {
        return getPackage(typeElement).getQualifiedName().toString();
    }

    String getClassName() {
        String packageName = getPackageName();
        String enclosingClassName = typeElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        return enclosingClassName + "$" + BINDER_NAME;
    }

    String getCanonicalClassName() {
        String packageName = getPackageName();
        String className = getClassName();
        return new StringBuilder(packageName)
                .append(".")
                .append(className)
                .toString();
    }

    public void write(StringBuilder builder) {
        builder.append("package ").append(getPackageName()).append(";\n")
                .append("public class ").append(getClassName())
                .append("{\n}");
        builder.append("//").append(typeElement.getQualifiedName().toString()).append("\n");
        builder.append("//").append(typeElement.getSimpleName().toString()).append("\n");
        builder.append("//").append(typeElement.getNestingKind().isNested()).append("\n");
        builder.append("//").append(typeElement.getNestingKind().toString()).append("\n");
        if (typeElement.getNestingKind().isNested()) {
            builder.append("//\t")
                    .append(typeElement.getEnclosingElement().getSimpleName().toString())
                    .append("\n");
        }
        builder.append("//viewStates:").append("\n");
        viewStateElements.entrySet().stream().forEach(entry -> {
            Element element = entry.getValue();
            append(builder, element);
        });
        builder.append("//presenters:").append("\n");
        presenterElements.stream().forEach(element -> {
            appendPresenter(builder, element);
        });
    }

    private void append(StringBuilder builder, Element element) {
        String objectType = element.getSimpleName().toString();

        builder.append("//\t").append(objectType)
                .append("\n");
    }

    private void appendPresenter(StringBuilder builder, Element element) {
        append(builder, element);
        String viewState = element.getAnnotation(MvpPresenter.class).viewState();
        builder.append("//\t\tviewState - ")
                .append(viewState)
                .append("\n");
    }
}