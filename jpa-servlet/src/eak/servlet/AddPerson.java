package eak.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.*;
import javax.naming.*;
import javax.persistence.*;

import eak.entity.*;
import eak.*;

// Registers this class as a servlet.
@WebServlet(
	name = "add-person",
	// Specifies the patterns for the URLs where this servlet will be accessible. 
	urlPatterns = { "/add-person" })
public class AddPerson extends HttpServlet {
  /**
	 * 
	 */
	private static final long serialVersionUID = 7950179127171707744L;
//this is thread-safe
  @PersistenceUnit(name="people-unit")
  private EntityManagerFactory emf;

  protected void doGet(
  HttpServletRequest request,
  HttpServletResponse response) throws ServletException, IOException {
		// Prepare response headers
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");

		PrintWriter pw = response.getWriter();
		pw.println("<!DOCTYPE html>");
		pw.println("<html>");
		pw.println("<head>");
		pw.println("<meta charset=\"UTF-8\">");
		pw.println("<title>Servlet test page</title>");
		pw.println("</head>");
		pw.println("<body>");


         pw.println("<form method='post'>");
         pw.println("A személy adatai:");

         pw.println("<input type='text' name='firstName'><br />");
         pw.println(" ");
         pw.println("<input type='text' name='lastName'><br />");

         pw.println("<input type='text' name='birthDate'><br />");
         pw.println(" ");
         pw.println("<input type='text' name='bloodGroup'><br />");

         pw.println("<input type='submit' value='Küldés'>");
         pw.println("</form><br />");



		pw.println("</body>");
		pw.println("</html>");
		pw.close();
  }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String bDate = request.getParameter("birthDate");
        String bGroup = request.getParameter("bloodGroup");
        try {

          java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
          java.util.Date date1 = sdf.parse(bDate);
          Date birthDate = new Date( date1.getTime() );
          BloodGroup bloodGroup = null;
              if (bGroup != null){

                if (bGroup.equalsIgnoreCase("A")){
                  bloodGroup = BloodGroup.A;
                }
                else if (bGroup.equalsIgnoreCase("B")){
                  bloodGroup = BloodGroup.B;
                }
                else if (bGroup.equalsIgnoreCase("AB")){
                  bloodGroup = BloodGroup.AB;
                }
                else if (bGroup.equalsIgnoreCase("0")){
                  bloodGroup = BloodGroup.Zero;
                }
              }
			// Retrieve a container managed transaction instance
	       Context ctx = new InitialContext();
	       UserTransaction tx = (UserTransaction) ctx.lookup("java:comp/UserTransaction");

	       // Persist the entity in a transaction
	       tx.begin();
               PersonStorage ps = new PersonStorage(emf);
               ps.addPerson(firstName, lastName, birthDate, bloodGroup);
	       tx.commit();
        }
        catch (java.text.ParseException ex){
          throw new IOException();
        }
        catch (Exception ex){
          throw new ServletException();
        }
        finally {
		response.sendRedirect(response.encodeRedirectURL(request.getRequestURI()));
        }

}

}
