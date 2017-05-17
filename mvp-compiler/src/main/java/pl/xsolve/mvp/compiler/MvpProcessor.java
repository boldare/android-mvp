package pl.xsolve.mvp.compiler;

import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import pl.xsolve.mvp.api.MvpPresenter;
import pl.xsolve.mvp.api.MvpViewState;

@AutoService(Processor.class)
public class MvpProcessor extends AbstractProcessor {

    static final String BINDER_SUPERCLASS_PACKAGE = "pl.xsolve.mvp";
    static final String BINDER_SUPERCLASS_NAME = "MvpBinder.ActivityBinder";
    static final String BINDER_SUPERCLASS_CANONICAL_NAME =
            BINDER_SUPERCLASS_PACKAGE + "." + BINDER_SUPERCLASS_NAME;
    static final String BINDER_NAME = "MvpActivityBinder";

    private Collector collector = new Collector();
    private MvpClassWriter mvpClassWriter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mvpClassWriter = new MvpClassWriter(processingEnvironment);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(MvpPresenter.class.getCanonicalName());
        set.add(MvpViewState.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        collect(roundEnvironment);
        write();

        return true;
    }

    private void collect(RoundEnvironment roundEnvironment) {

        for (Element element : roundEnvironment.getElementsAnnotatedWith(MvpPresenter.class)) {
            collector.collectPresenter(element);
        }
        for (Element element : roundEnvironment.getElementsAnnotatedWith(MvpViewState.class)) {
            collector.collectViewState(element);
        }
    }

    private void write() {
        collector.getClassesData()
                .forEach(mvpClassData -> mvpClassWriter.write(mvpClassData));

    }
}
