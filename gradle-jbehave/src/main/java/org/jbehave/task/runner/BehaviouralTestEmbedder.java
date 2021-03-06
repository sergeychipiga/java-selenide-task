package org.jbehave.task.runner;

import io.tapack.allure.jbehave.AllureReporter;
import org.jbehave.core.ConfigurableEmbedder;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromURL;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbehave.core.reporters.Format.CONSOLE;

/**
 * A class to fully encapsulates all of the JBehave plumbing behind a builder style API. The expected usage for this would be:
 * {@code aBehaviouralTestRunner().usingStepsFrom(this).withStory("your.story").run()}
 */
public final class BehaviouralTestEmbedder extends ConfigurableEmbedder {

    private static final Logger LOG = LoggerFactory.getLogger(BehaviouralTestEmbedder.class);

    private String wildcardStoryFilename;
    private InjectableStepsFactory stepsFactory;


    private BehaviouralTestEmbedder() {
    }

    public static BehaviouralTestEmbedder aBehaviouralTestRunner() {
        return new BehaviouralTestEmbedder();
    }

    @Override
    public void run() throws Exception {
        List<String> paths = createStoryPaths();
        if (paths == null || paths.isEmpty()) {
            throw new IllegalStateException("No story paths found for state machine");
        }
        LOG.debug("Running {} with spring_stories {}", getClass().getSimpleName(), paths);
        configuredEmbedder().runStoriesAsPaths(paths);
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        assertThat(stepsFactory).isNotNull();
        return stepsFactory;
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromURL())
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withFormats(CONSOLE)
                        .withReporters(new AllureReporter()));
    }

    private List<String> createStoryPaths() {
        return ClasspathStoryFinder.findFileNamesThatMatch(wildcardStoryFilename);
    }

    public BehaviouralTestEmbedder withStory(String aWildcardStoryFilename) {
        wildcardStoryFilename = aWildcardStoryFilename;
        return this;
    }

    public BehaviouralTestEmbedder usingStepsFrom(Object... stepsSource) {
        assertThat(stepsFactory).isNull();
        stepsFactory = new InstanceStepsFactory(configuration(), stepsSource);
        return this;
    }
}
