package pl.xsolve.mvp.compiler;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

class Collector {
    private final Map<String, MvpClassData> classesData = new HashMap<>();

    Stream<MvpClassData> getClassesData() {
        return classesData.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue);
    }

    void collectViewState(Element element) {
        MvpClassData mvpClassData = getClassDataFor(element);
        mvpClassData.collectViewState(element);
    }

    void collectPresenter(Element element) {
        MvpClassData mvpClassData = getClassDataFor(element);
        mvpClassData.collectPresenter(element);
    }

    private MvpClassData getClassDataFor(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String enclosingType = enclosingElement.getQualifiedName().toString();
        if (!classesData.containsKey(enclosingType)) {
            classesData.put(enclosingType, new MvpClassData(enclosingElement));
        }
        return classesData.get(enclosingType);
    }

}
