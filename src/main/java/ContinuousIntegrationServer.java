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

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;

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
            try {
                cloneRepository(json.getJSONObject("repository").getString("clone_url"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GitAPIException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 

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

    public static void logCurrentBuild(String updated_at, String commit_url, String buildInfo) throws Exception
    {
        JSONObject current_build = new JSONObject();
        current_build.put("Build date", updated_at);
        current_build.put("Link to commit", commit_url);
        current_build.put("Build results", buildInfo);
        String currentDirectory = System.getProperty("user.dir");
        try{
            File logFile = new File(currentDirectory + "\\logs.txt");
            if (logFile.createNewFile()) {
                System.out.println("First entry in log");
              } else {
                System.out.println("Adding new entry to log");
              }
        } catch(IOException e){
            System.out.println("Not able to create or find file");
            e.printStackTrace();
        }
        
        FileWriter openLogFile = new FileWriter("logs.txt");
        openLogFile.write(current_build.toString());
        openLogFile.close();       
        
    }

    public static void printLogFile(HttpServletResponse response){
        try {
            FileReader log = new FileReader("logFile.txt");
            BufferedReader reader = new BufferedReader(log);
            String line;
            while ((line = reader.readLine()) != null) {
                response.getWriter().println(line);
            }
            reader.close();
          } catch(Exception e) {
            e.getStackTrace();
          }
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
}