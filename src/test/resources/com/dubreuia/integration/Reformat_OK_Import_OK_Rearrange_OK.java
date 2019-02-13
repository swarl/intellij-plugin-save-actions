package com.dubreuia.integration;

import com.dubreuia.model.Storage;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static com.intellij.testFramework.LightProjectDescriptor.EMPTY_PROJECT_DESCRIPTOR;

public class Class {

    static String STATIC = "static";

    static Consumer<CodeInsightTestFixture> SAVE_ACTION_MANAGER = fixture ->
            new WriteCommandAction.Simple(fixture.getProject()) {
                @Override
                protected void run() {
                    ((PsiFileImpl) fixture.getFile()).clearCaches();
                }
            }.execute();
    Storage storage;
    private CodeInsightTestFixture fixture;

    protected void assertFormat(final String beforeFilename, final Consumer<CodeInsightTestFixture> saveActionManager,
                                final String afterFilename) {
        fixture.configureByFile(beforeFilename + ".java");
        saveActionManager.accept(fixture);
        fixture.checkResultByFile(afterFilename + ".java");
    }

    @BeforeEach
    public void before() throws Exception {
        final IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        final IdeaProjectTestFixture fixture = factory.createLightFixtureBuilder(EMPTY_PROJECT_DESCRIPTOR).getFixture();
    }

    private String getTestDataPath() {
        final Path classes = Paths.get(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        final Path resources = Paths.get(classes.getParent().toString(), "resources");
        final Path root = Paths.get(resources.toString(), getClass().getPackage().getName().split("[.]"));
        return root.toString();
    }

}
