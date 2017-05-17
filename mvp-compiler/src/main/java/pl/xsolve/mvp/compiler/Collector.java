package pl.xsolve.mvp.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class Collector {
    private Map<String, MvpClassData> classesData = new HashMap<>();

    void collectViewState(Element element) {
        MvpClassData mvpClassData = getClassDataFor(element);
        mvpClassData.collectViewState(element);
    }

    void collectPresenter(Element element) {
        MvpClassData mvpClassData = getClassDataFor(element);
        mvpClassData.collectPresenter(element);
    }

    Stream<MvpClassData> getClassesData() {
        return classesData.entrySet()
                .stream()
                .map(Map.Entry::getValue);
    }

    private MvpClassData getClassDataFor(Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String enclosingType = enclosingElement.getSimpleName().toString();
        if (!classesData.containsKey(enclosingType)) {
            classesData.put(enclosingType, new MvpClassData(enclosingElement));
        }
        return classesData.get(enclosingType);
    }

}
