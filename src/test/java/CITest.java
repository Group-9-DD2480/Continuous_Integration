import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.maven.shared.invoker.MavenInvocationException;
import org.junit.jupiter.api.Assertions;

import java.io.File;

class CITest {
    
    // If CompileTest.class exists, deletes it and compiles the test folder, 
    // then checks that the new class-file exists. Expects output true.
    // If
    @Test
    void P1CompilerTest() throws MavenInvocationException {
        ProjectBuilder projectBuilder = new ProjectBuilder("pom.xml");
        File testFile = new File("target/classes/ContinuousIntegrationServer.class");
        if (testFile.exists()) testFile.delete();
        projectBuilder.compileMaven("compile");
        assertTrue(testFile.exists());
    }
    @Test
    void P2CompilerTest() throws MavenInvocationException {
        ProjectBuilder projectBuilder = new ProjectBuilder("pom.xml");
        File testFile = new File("target/test-classes/CompileTest.class");
        if (testFile.exists()) testFile.delete();
        projectBuilder.compileMaven("test-compile");
        assertTrue(testFile.exists());
    }

    //If the mail doesn't contain @ throw excepton
    //Expected output throws exception
    @Test
    void P3EmailTest() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
                ContinuousIntegrationServer continuousIntegration = new ContinuousIntegrationServer();
                continuousIntegration.sendMail("wrongmail", "wrong test");
            }); 
    }

    //Checks that mail is sent
    // Expected output true
    @Test
    void SendEmailTest() throws Exception {
        ContinuousIntegrationServer continuousIntegration = new ContinuousIntegrationServer();
        boolean temp = continuousIntegration.sendMail("thea.noteberg@gmail.com", "test");
        assertTrue(temp);
    }
}
