package eak;

import java.util.List;
import java.util.Vector;
import javax.persistence.*;
import javax.persistence.criteria.*;
import eak.entity.*;
import java.util.Date;

public class PersonStorage {

  private EntityManagerFactory emf;

  // Konstruktor, egy persistence unit nevét kapja, és létre is hoz egy neki
  // megfelelő `EntityManager`-t.
    public PersonStorage(String persistenceUnitName) {
       // Create an entity manager and begin a transaction
       this.emf = Persistence.createEntityManagerFactory(persistenceUnitName);
   }

  // Konstruktor, egy persistence unit nevét kapja, és létre is hoz egy neki
  // megfelelő `EntityManager`-t.
    public PersonStorage(EntityManagerFactory emf) {
       // Create an entity manager and begin a transaction
       this.emf = emf;
   }

    public Person getPersonById(long id){
      EntityManager em = emf.createEntityManager();
      Person person = em.find(Person.class, id);
      em.flush();
      em.close();
      return person;
    }
	
    // Visszaadja a megadott azonosítójú személyt.
    // Ha nincs találat, null-t ad vissza.

    //From Adam Bien's Weblog
    private List<Person> getAllPersons() {
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> rootEntry = cq.from(Person.class);
        CriteriaQuery<Person> all = cq.select(rootEntry);
        TypedQuery<Person> allQuery = em.createQuery(all);
        em.flush();
        em.close();
        return allQuery.getResultList();
    }	
	
    public java.util.List<Person> getPeople(){      
      return getAllPersons();
    }
    // Lekérdezi az összes személyt.

    public long addPerson(String firstName, String lastName, Date birthDate, BloodGroup bloodGroup) throws StorageException {
    // Hozzáad egy új személyt. Dobjunk kivételt a következő esetekben:
    // - Nem töltöttük ki valamelyik kötelező mezőt
    // - A születési dátum 1900 előtti
    // A metódus az új személy azonosítóját adja vissza.
   	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    Date date1 = null;
    try {
      date1 = sdf.parse("1900-01-01");
    }
    catch (java.text.ParseException ex){
      throw new StorageException();
    }
    if (date1.compareTo(birthDate)>0){
      throw new StorageException();
    }
    List<GpsCoordinate> coords = new Vector<GpsCoordinate>();
    Person person = new Person(0L, firstName, lastName, birthDate, null, bloodGroup, coords);
    EntityManager em = emf.createEntityManager();
    em.persist(person);
    em.flush();
    //A flush()-sal az objektumunk is megkapta az azonosítót
    em.close();
    return person.getId();
   }


    public void addCoordinate(long personId, double latitude, double longitude, double height) throws StorageException {
      // Hozzáad egy új GPS koordinátát a megadott személyhez.
      // A koordináta dátumát az aktuális rendszeridő alapján töltsük ki. // Dobjunk kivételt a következő esetekben:
      // - A megadott azonosítóhoz nincs személy
      EntityManager em = emf.createEntityManager();
      Person owner = em.find(Person.class, personId);
      if (owner == null){
        throw new StorageException();
      }
      Date date = new Date();
      GpsCoordinate coord = new GpsCoordinate(0, date, latitude, longitude,  height, owner);
      em.persist(coord);
      em.flush();
      em.close();
    }

    public boolean removePersonById(long id) {
      // Eltávolítja az azosoítójával megadott személyt.
      // Ha volt találat igazat, különben hamisat ad vissza.
      EntityManager em = emf.createEntityManager();
      Person person = em.find(Person.class, id);
      if (person == null) {		
        em.close();
        return false;
      }
      em.remove(person);
      em.flush();
      em.close();
      return true;
    }
}







