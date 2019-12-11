/**
 * Observable represents an object that will be observed
 * by other objects, and when Observable object changes state,
 * it notifies it's observers
 * @author zionchilagan
 *
 */
public interface Observable {
	void add(Observer o);
	void alert();

}
