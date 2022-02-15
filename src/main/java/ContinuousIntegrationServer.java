import java.util.*;
import java.util.stream.Collectors;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.util.Arrays; //Might be able to be removed
import java.util.List; // might be able to be removed

import org.json.*; 

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import org.apache.maven.shared.invoker.MavenInvocationException;
 
import org.eclipse.jetty.server.Server;
import org.apache.maven.shared.invoker.MavenInvocationException;
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
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException {
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
            
            JSONObject head = json.getJSONObject("head_commit");
            // String statuses_url = repo.getString("statuses_url");
            JSONArray commits = json.getJSONArray("commits");
            JSONObject info = commits.getJSONObject(0);
            String mail = info.getJSONObject("author").getString("email");
            
            //setGitStatus(true, statuses_url, sha);
            try {
                //Clones the repository
                cloneRepository(json.getJSONObject("repository").getString("clone_url"));
                //Compiles the repository
                compileRepository();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GitAPIException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MavenInvocationException e) {
                e.printStackTrace();
            }
            sendMail(mail, "test mail"); 

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
        File myObj = new File(currentDirectory + "/temp");
        if (!myObj.exists()){
            myObj.mkdirs();
        }
        //Git.cloneRepository().setURI(url).setDirectory(Paths.get("/path/to/temp").toFile()).call();
        Git.cloneRepository().setURI(url).setDirectory(myObj).call();

    }

    // Sends email notification to the author
    public boolean sendMail(String to, String build) throws IOException, IllegalArgumentException{
        //checks that it is a valid email address
        if (!(to.contains("@"))){
            throw new IllegalArgumentException("not email address");
        }
        
        String from = "dd2480group9@gmail.com";
        String host = "imap.gmail.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "testtestdd2480");
            }
        });
        try {
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from));

         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
         message.setSubject("Latest push");
         message.setText(build);
         Transport.send(message);
         return true;
        } catch (MessagingException mex) {
         mex.printStackTrace();
         return false;
      }
    }
    

    public static void compileRepository() throws MavenInvocationException {
        String testDirectory = System.getProperty("user.dir");
        ProjectBuilder projectBuilder = new ProjectBuilder(testDirectory + "/temp/pom.xml");
        projectBuilder.compileMaven("compile");
    }

}