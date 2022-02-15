import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.jupiter.api.Assertions;

import java.io.File;

class CITest {

    // If CompileTest.class exists, deletes it and compiles the test folder, 
    // then checks that the new class-file exists. Expects output true.
    @Test
    void P1CompilerTest() throws MavenInvocationException {
        ProjectBuilder projectBuilder = new ProjectBuilder("pom.xml");
        File testFile = new File("target/test-classes/CompileTest.class");
        if (testFile.exists()) testFile.delete();
        projectBuilder.compileMaven("test-compile");
        assertTrue(testFile.exists());
    }
}
