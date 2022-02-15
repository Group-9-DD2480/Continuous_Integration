import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;



public class ProjectBuilder {
    
    String pomPath;

    public ProjectBuilder(String pomPath) {
        this.pomPath = pomPath;
    }
    
    private InvocationRequest createInvocationRequest(String goal){
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(this.pomPath));
        request.setGoals(Collections.singletonList(goal));
        return request;
    }

    public int compileMaven(String goal) throws MavenInvocationException {
        InvocationRequest request = createInvocationRequest(goal);
        Invoker invoker = new DefaultInvoker();
        //invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        InvocationResult result = invoker.execute(request);

        if ( result.getExitCode() != 0 ) {
            throw new IllegalStateException( "Build failed.");
            // We can also return result.getExecutionException() for more info on where it failed
        }
        result.getExecutionException();
        return result.getExitCode();
    }
}
