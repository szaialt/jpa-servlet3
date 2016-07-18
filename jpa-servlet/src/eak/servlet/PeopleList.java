package eak.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.*;
import eak.entity.*;
import eak.*;
import java.util.List;
import java.util.Date;

// Registers this class as a servlet.
@WebServlet(
	name = "list-servlet",
	// Specifies the patterns for the URLs where this servlet will be accessible. 
	urlPatterns = { "/list" })

public class PeopleList extends HttpServlet {
  /**
	 * 
	 */
	private static final long serialVersionUID = -1146293708117880254L;
//this is thread-safe
  @PersistenceUnit(name="people-unit")
  private EntityManagerFactory emf;

  protected void doGet(
  HttpServletRequest request,
  HttpServletResponse response) throws ServletException, IOException {
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

	
    try {
      //InitialContext ic = new InitialContext();
      PersonStorage ps = new PersonStorage(emf);
      //Kezdődhet a munka!

      java.util.List<Person> people = ps.getPeople(); 
      if (people != null){
        for (int i = 0; i < people.size(); i++) {
              Person somebody = people.get(i);
              String lastName = somebody.getLastName();
              pw.println("Vezetéknév: "+lastName);
              String firstName = somebody.getFirstName();
              pw.println("Keresztnév: "+firstName);
              Date birthDate = somebody.getBirthDate();
              int year = birthDate.getYear();
              int month = birthDate.getMonth();
              int day = birthDate.getDay();
              pw.println("Születési idő: "+year + " " + (month+1) + " " + day);
              String address = somebody.getAddress();
              pw.println("Lakcím: "+address);
              BloodGroup bloodGroup = somebody.getBloodGroup();
              if (bloodGroup != null){
                System.out.print("Vércsoportja: ");
                if (bloodGroup == BloodGroup.A){
                  pw.println("A");
                }
                else if (bloodGroup == BloodGroup.B){
                  pw.println("B");
                }
                else if (bloodGroup == BloodGroup.AB){
                  pw.println("AB");
                }
                else if (bloodGroup == BloodGroup.Zero){
                  pw.println("0");
                }
              }
              List<GpsCoordinate> coordinates = somebody.getCoordinates();
              if (coordinates != null){
                for (int ii = 0; ii < coordinates.size(); ii++) {
                  GpsCoordinate coord = coordinates.get(i);
                  pw.println("Szélesség: " + coord.getLatitude());
                  pw.println("Hosszúság: " + coord.getLongitude());
                  pw.println("Magasság: " + coord.getHeight());
                  Date date = coord.getTime();
                  int year2 = date.getYear();
                  int month2 = date.getMonth();
                  int day2 = date.getDay();
                  pw.println("Idő: "+year + " " + (month+1) + " " + day); 
                }
              }
            }
          }

    } catch (Exception ex) {
        pw.println(ex);
      }

    finally{
      pw.println("</body>");
      pw.println("</html>");
      pw.close();
    }
  }

}
