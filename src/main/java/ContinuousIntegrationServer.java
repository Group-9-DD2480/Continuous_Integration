import java.util.stream.Collectors;
import java.util.Arrays; //Might be able to be removed
import java.util.List; // might be able to be removed

import org.json.*; 

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.File;
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.Paths;

/** 
 Skeleton of a src.main.java.ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/


public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);
        String parameter = request.getMethod();

        if(request.getMethod()=="POST") {
            String requestData = request.getReader().lines().collect(Collectors.joining());
            //response.getWriter().println(requestData);
            JSONObject json = new JSONObject(requestData); 
            //System.out.println(json.getString("ref"));
            //JSONObject json = new JSONObject(requestData); 
            String test = json.getString("ref");
            JSONObject repo = json.getJSONObject("repository");
            
            response.getWriter().println(repo);
            // try {
            //     cloneRepository(json.getJSONObject("repository").getString("clone_url"));
            // } catch (JSONException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // } catch (GitAPIException e) {
            //     // TODO Auto-generated catch block
            //     e.printStackTrace();
            // } 


            JSONObject head = json.getJSONObject("head_commit");
            String statuses_url = repo.getString("statuses_url");
            String sha = head.getString("id");
            setGitStatus(true, statuses_url, sha);
        }

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        response.getWriter().println("requestData");
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }

    public static void cloneRepository(String url) throws IOException, GitAPIException  {
        String currentDirectory = System.getProperty("user.dir");
        File myObj = new File(currentDirectory + "\\temp");
        if (!myObj.exists()){
            myObj.mkdirs();
        }
        //Git.cloneRepository().setURI(url).setDirectory(Paths.get("/path/to/temp").toFile()).call();
        Git.cloneRepository().setURI(url).setDirectory(myObj).call();

    }

    public void sendMail(String email, String buildResult) throws IOException{
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", "mail -s \"Your push\" " + email +"  <<< '" + buildResult + "'");
        pb.start();
    }

    public static void setGitStatus(Boolean result, String statuses_url, String sha) throws IOException {
        // We set either error, pending, failure, success in a post req.
        String status;
        String https = "https:\\";
        statuses_url.substring(0, statuses_url.length() - 5);
        statuses_url = https + statuses_url + sha;

        if(result) status = "success";
        else status = "failure";
        
        String command = String.format("curl -X POST -H 'Content-Type: application/json' --data 'state:&s' &s &s", status, https, statuses_url);
                                      //curl -X POST -H 'Content-Type: application/json' --data '{"state": "success", ...}' https://<token>:x-oauth-basic@api.github.com/repos/politrons/proyectV/statuses/6dcb09b5b57875f334f61aebed695e2e4193db5e

        Process process = Runtime.getRuntime().exec(command);
        process.destroy();
    }
}