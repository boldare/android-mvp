package pl.xsolve.mvp.compiler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

import pl.xsolve.mvp.api.MvpPresenter;

import static com.google.auto.common.MoreElements.getPackage;

class MvpClassData {
    private static final String BINDER_NAME = "MvpActivityBinder";
    private final TypeElement typeElement;
    private final Map<String, Element> viewStateElements = new HashMap<>();
    private final Set<Element> presenterElements = new HashSet<>();

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

    String getBinderClassName() {
        String packageName = getPackageName();
        String enclosingClassName = typeElement.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        return enclosingClassName + "$" + BINDER_NAME;
    }

    String getActivityClassName() {
        String packageName = getPackageName();
        return typeElement.getQualifiedName().toString().substring(
                packageName.length() + 1);
    }

    List<MvpBinding> getBindings() {
        List<MvpBinding> bindings = new ArrayList<>();
        presenterElements.stream()
                .sorted(
                        Comparator.comparing(element -> element.getSimpleName().toString())
                )
                .forEach(
                        presenterElement -> {
                            addBindingForElement(bindings, presenterElement);
                        }
                );
        return bindings;
    }

    private void addBindingForElement(List<MvpBinding> bindings, Element presenterElement) {
        Optional<Element> viewElement = findViewStateFor(presenterElement);
        if (viewElement.isPresent()) {
            bindings.add(new MvpBinding(presenterElement, viewElement.get()));
        }
    }

    private Optional<Element> findViewStateFor(Element presenterElement) {
        String viewStateElementName = presenterElement.getAnnotation(MvpPresenter.class).viewState();
        Optional<Element> viewStateElement = viewStateElements.values().stream()
                .filter(element -> viewStateElementName.contentEquals(element.getSimpleName()))
                .findFirst();
        return viewStateElement;
    }

    static class MvpBinding {
        final Element presenter;
        final Element viewState;

        MvpBinding(Element presenter, Element viewState) {
            this.presenter = presenter;
            this.viewState = viewState;
        }

        TypeMirror getViewType() {
            DeclaredType presenterType = (DeclaredType) presenter.asType();
            TypeElement presenterTypeElement = (TypeElement) presenterType.asElement();
            DeclaredType supertype = (DeclaredType) presenterTypeElement.getSuperclass();
            return supertype.getTypeArguments().get(0);
        }
    }
}
