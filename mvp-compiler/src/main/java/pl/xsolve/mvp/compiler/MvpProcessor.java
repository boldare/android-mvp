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


    private Collector collector = new Collector();
    private MvpClassWriter mvpClassWriter;
    private MvpBinderWriter mvpBinderWriter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        initWriters(processingEnvironment);
    }

    private void initWriters(ProcessingEnvironment processingEnvironment) {
        mvpClassWriter = new MvpClassWriter(processingEnvironment);
        mvpBinderWriter = new MvpBinderWriter(processingEnvironment);
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

        mvpBinderWriter.write(collector.getClassesData());
    }
}
