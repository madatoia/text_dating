import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;

public class FileInf {

	String fileName;
	int wordsNo;
	
	LinkedBlockingDeque<ArrayList<WordInf>> words;
	ArrayList<WordInf> distinctWords = new ArrayList<WordInf>(); 
	long[] finalResutsSimple = new long[510]; 
	long[] finalResutsGauss = new long[510]; 
	
	boolean relevant = false;
	float relevancy;
	
	public FileInf(String f){
		fileName = f;
		wordsNo = 0;
		//words = new HashMap<String, Integer>();
		
		words = new LinkedBlockingDeque<ArrayList<WordInf>>();
		//rank = r;
	}
	
	public String toString(){
		String str = "";
		for(int i=0; i<distinctWords.size(); i++){
			str += distinctWords.get(i);
		}
		return str;
	}
	
	public synchronized void adIntervalSimple(Interval interv){
		for(int i = interv.start; i <= interv.stop; i++)
			finalResutsSimple[i]++;
	}
	
	public synchronized void adIntervalGauss(Interval interv){
		for(int i = interv.start; i <= interv.stop; i++)
			finalResutsGauss[i]++;
	}
	
	public void computePossibleResult(){
		
		long maxSimple = -1;
		long maxGauss = -1;
		ArrayList<Integer> posYearsSimple = new ArrayList<Integer>();
		ArrayList<Integer> posYearsGauss = new ArrayList<Integer>();
		
		for(int i=0; i<509; i++){
			if(finalResutsSimple[i] > maxSimple){
				posYearsSimple.clear();
				maxSimple = finalResutsSimple[i];
				posYearsSimple.add(i+1500);
			} else if(finalResutsSimple[i] == maxSimple)
				posYearsSimple.add(i+1500);
			
			if(finalResutsGauss[i] > maxGauss){
				posYearsGauss.clear();
				maxGauss = finalResutsGauss[i];
				posYearsGauss.add(i+1500);
			} else if(finalResutsSimple[i] == maxGauss)
				posYearsGauss.add(i+1500);
		}
		
		System.out.print("Possible periods from simple peek detection: "+maxSimple+"\n");
		Iterator<Integer> itSimple = posYearsSimple.iterator();
		
		while(itSimple.hasNext())
			System.out.print(itSimple.next() + ", ");
		
		System.out.print("\n\nPossible periods from EMD peek detection: "+maxGauss+"\n");
		Iterator<Integer> itGauss = posYearsGauss.iterator();
		
		while(itGauss.hasNext())
			System.out.print(itGauss.next() + ", ");
	}
	
	@SuppressWarnings("unchecked")
	public void reduce() {
	
		int ind;
		Iterator<ArrayList<WordInf>> listIt = words.iterator();
		Iterator<WordInf> jobIt;
		
		WordInf searchWord;
		
		while(listIt.hasNext()){
			jobIt = listIt.next().iterator();
			while(jobIt.hasNext()){
				searchWord = jobIt.next();
				wordsNo += searchWord.apparitions;
				ind = distinctWords.indexOf(searchWord);
				
				if(ind == -1){
					distinctWords.add(searchWord);
				} else {
					distinctWords.get(ind).apparitions += searchWord.apparitions;
				}
			}
		}
		Collections.sort(distinctWords);
	}
}

