// DEFINICIÓN DE LOS OPERADORES


import java.util.ArrayList;
import ia.comparticion.usuario;
import ia.comparticion.usuarios;


public class CarBoard {

	public static String ADD = "add";

	public static String REMOVE = "remove";

	public static String SWAP_EVENT = "swap_event";

	public static String SWAP_DRIVER = "swap_driver";

  //Auxiliary functions
  public usuario getDriverOf(usuario A){
    events = getEvents();
    for (int i = 0; i < events.size(); i++) {
			for (int j = 0; j < events[i].size(); j++){
        if (events.get(i).get(j) == A) {
          return i;
        }
      }
		}
    //It has no driver assigned. The user is the driver itself.
    return A;
  }

  public int[] getPositions(usuario A, usuario B){
    int pos = 0;
    int id1 = 0;
    int id2 = 0;
    events_driver = getEvents(B);
    for (int i = 0; i < events_driver.size(); i++){
      if (events_driver.get(i) == A){
        if (pos == 0){
          id1 = i;
          pos = pos + 1;
        } else {
          id2 = i;
        }
      }
    }
    return new int[] {id1, id2};
  }


  //Operator functions
  public void addPassenger(usuario p, usuario d){
    events.get(d).add(p);
    events.get(d).add(p);
  }

  public void deletePassenger(usuario p){
    usuario driver = getDriverOf(p);
    ids = getPositions(p, driver);
    events.get(driver).remove(ids[1]);
    events.get(driver).remove(ids[0]);
  }

  public void addDriver(usuario d){
    deletePassenger(d);
    events.get(d).add(d);
    events.get(d).add(d);
  }

  public void removeDriver(usuario d, usuario d2){
    deletePassenger(d);
    addPassenger(d, d2);
  }

  public void swapDriver(usuario p, usuario d2){
    deletePassenger(p);
    addPassenger(p, d2);
  }

  public ArrayList swapEvent(usuario d, int id1, int id2){
    events_driver = getEvents(d);
    int a = events_driver.get(id1);
    int b = events_driver.get(id2);
    events_driver.set(id1, b);
    events_driver.set(id2, a);
    return events_driver;
  }

}
