import java.io.FileNotFoundException;
import java.util.LinkedList;


public class WorkPool {
	int nThreads; // nr total de thread-uri worker
	int nWaiting = 0; // nr de thread-uri worker care sunt blocate asteptand un task
	public boolean ready = false; // daca s-a terminat complet rezolvarea problemei 
	String content = "";
	long fDim;
	LinkedList<Job> tasks = new LinkedList<Job>();
	

	/**
	 * Constructor pentru clasa WorkPool.
	 * @param nThreads - numarul de thread-uri worker
	 * @throws FileNotFoundException 
	 */
	public WorkPool(int nThreads) {
		
		this.nThreads = nThreads;
				
	}

	/**
	 * Functie care incearca obtinera unui task din workpool.
	 * Daca nu sunt task-uri disponibile, functia se blocheaza pana cand 
	 * poate fi furnizat un task sau pana cand rezolvarea problemei este complet
	 * terminata
	 * @return Un task de rezolvat, sau null daca rezolvarea problemei s-a terminat 
	 */
	public synchronized Job getWork() {
		if (tasks.size() == 0) { // workpool gol
			nWaiting++;
			/* condtitie de terminare:
			 * nu mai exista nici un task in workpool si nici un worker nu e activ 
			 */
			if (nWaiting == nThreads && Main.done) {
				ready = true;
				/* problema s-a terminat, anunt toti ceilalti workeri */
				notifyAll();
				return null;
			} else {
				while (!ready && tasks.size() == 0) {
					try {
						this.wait();
					} catch(Exception e) {e.printStackTrace();}
				}
				
				if (ready)
				    /* s-a terminat prelucrarea */
				    return null;
				
				nWaiting--;	
			}
		}
		return tasks.remove();

	}


	/**
	 * Functie care introduce un task in workpool.
	 * @param sp - task-ul care trebuie introdus 
	 */
	synchronized void putWork(Job sp) {
		//System.out.println("WorkPool - adaugare task: " + sp);
		tasks.add(sp);
		/* anuntam unul dintre workerii care asteptau */
		this.notify();

	}
}
