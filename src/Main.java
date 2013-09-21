import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

	
public class Main {

	public static int threadsNo;
	public static String fIn;
	
	static FileInf inputFileInfo;
	public static boolean done = false;		//devine true cand nu mai sunt task-uri de adaugat in coada
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		threadsNo = Runtime.getRuntime().availableProcessors(); //DE TESTAT si cu alte variante (2:1, 4:1)
		fIn = args[0];
		
		
		WorkPool map = new WorkPool(threadsNo);
		MapWorker[] mp = new MapWorker[threadsNo]; 
		
		WorkPool searchMap = new WorkPool(threadsNo);
		SearchMapWorker[] sMp = new SearchMapWorker[threadsNo]; 
		
		
		//initializare workeri
		for(int i=0; i<threadsNo; i++){
			mp[i] = new MapWorker(map);
			sMp[i] = new SearchMapWorker(searchMap);
		}
		//create MapJobs
		//oppen file
		File inputFile = new File(fIn);
		inputFileInfo = new FileInf(fIn);
		long fDim = inputFile.length();
		Scanner sIn = new Scanner(inputFile);
		
		String line = "";
		String jobInput = "";
		int jobSize = (int) (fDim/threadsNo);
		int actSize = 0;
		
		//read file
		while(sIn.hasNextLine()){
			line = sIn.nextLine();
			jobInput += line + " ";
			if( actSize + line.length() > jobSize){
				//have to divide line
				//go to jobSize position and add caracters until the end of the last word
				
				int lastPos = jobSize;
				char c = jobInput.charAt(lastPos);
				while ((c<='z'&&c>='a') || (c<='Z'&&c>='A') || (c=='\'') || (c=='-') && lastPos < line.length()){
					lastPos++;
					c = jobInput.charAt(lastPos);
				}
				
				map.putWork(new MapJob(jobInput.substring(0, lastPos), inputFileInfo));
				jobInput = jobInput.substring(lastPos+1, jobInput.length());
				actSize = jobInput.length();				
			} else {
				actSize += line.length();
			}
		}
		sIn.close();
		
		if(jobInput.length()>0)
			map.putWork(new MapJob(jobInput, inputFileInfo));
		for(int i=0; i<threadsNo; i++){
			mp[i].start();
		}
		
		done = true;
		for(int i=0; i<threadsNo; i++){
			try {
				mp[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inputFileInfo.reduce();
		
		int wordsNo = inputFileInfo.distinctWords.size();
		
		/*PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
		writer.println(inputFileInfo);
		writer.close();*/
		
		for(int i=0; i<wordsNo; i++){
			searchMap.putWork(new SearchMapJob(inputFileInfo.distinctWords.get(i)));
		}
		for(int i=0; i<threadsNo; i++){
			sMp[i].start();
		}
		done = true;
		for(int i=0; i<threadsNo; i++){
			try {
				sMp[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		done = true;
		
		inputFileInfo.computePossibleResult();
		
	}
}
