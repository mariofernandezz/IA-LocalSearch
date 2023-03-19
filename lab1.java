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
  public int getDriverOf(int A){
    for (int i = 0; i < events.size(); i++) {
			for (int j = 0; j < events.get(i).size(); j++){
        if (events.get(i).get(j) == A) {
          return i;
        }
      }
		}
    return -1;
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
  public void addPassenger(int p, int d){
    events.get(d).add(p);
    events.get(d).add(p);
  }

  public void deletePassenger(int p){
    int driver = getDriverOf(p);
    events.get(driver).remove(p);
    events.get(driver).remove(p);
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
