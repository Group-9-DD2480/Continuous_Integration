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
 The continuous intergration server. When a webhook is activated it clones the repository, compiles it and sends the result to the author who pushed the code.
*/


public class ContinuousIntegrationServer extends AbstractHandler
{
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);
        if(request.getMethod()=="POST") {
            String requestData = request.getReader().lines().collect(Collectors.joining());
            
            JSONObject json = new JSONObject(requestData); 
            JSONObject repo = json.getJSONObject("repository");
            
            response.getWriter().println(repo);
            
            //Extracts the email from payload
            JSONArray commits = json.getJSONArray("commits");
            JSONObject info = commits.getJSONObject(0);
            String mail = info.getJSONObject("author").getString("email");

            int result = -1;
            try {
                //Clones the repository
                cloneRepository(json.getJSONObject("repository").getString("clone_url"));
                //Compiles the repository
                result = compileRepository();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (GitAPIException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (MavenInvocationException e) {
                e.printStackTrace();
            }

            if (result != 0) {
                sendMail(mail, "failure");
            }else{
                sendMail(mail, "sucess");
            }
        }

        response.getWriter().println("requestData");
    }
 
    // used to start the CI server in command line
            /**
     * This function starts and joins a local server on the given port.
     * */
    public static void main(String[] args) throws Exception
    {
        
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
        /**
     * This function clones down the given github repo to a temporary folder located where JVM was started.
     * 
     *
     * @param url String link to the github repo that is to be cloned.
     * */
    public static void cloneRepository(String url) throws IOException, GitAPIException  {
        String currentDirectory = System.getProperty("user.dir");
        File myObj = new File(currentDirectory + "/temp");
        if (!myObj.exists()){
            myObj.mkdirs();
        }
        Git.cloneRepository().setURI(url).setDirectory(myObj).call();

    }
            /**
     * This function notifies the author about the build result via email. The email is sent to the one linked with the authors github account.
     * 
     * 
     *
     * @param to String path to were the pom-file for the maven project exists.
     * @param build String path to were the pom-file for the maven project exists.
     * @return Return a boolean. True if the email was successfully sent and false otherwise.
     * */
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
    
           /**
     * This function creates an instance of the ProjectBuilder class and compiles the code.
     * 
     * @return Returns a boolean result from projectBuilder.Compilemaven which reports the results of the compilation.
     * */
    public static int compileRepository() throws MavenInvocationException {
        String testDirectory = System.getProperty("user.dir");
        ProjectBuilder projectBuilder = new ProjectBuilder(testDirectory + "/temp/pom.xml");
        return projectBuilder.compileMaven("test");   
    }

}