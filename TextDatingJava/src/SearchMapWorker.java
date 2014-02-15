import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class SearchMapWorker extends Thread{
	
	WorkPool wp;
	
	public SearchMapWorker(WorkPool wp){
		this.wp = wp;
	}
	
	void doWork(SearchMapJob mj) throws FileNotFoundException{
		
		Scanner s = new Scanner(new File("googlebooks-eng-all-1gram-20120701-"+mj.toSearchWord.word.charAt(0)));
		String line, wordPlusType, simpleWord;
		Scanner parse;
		int lineIndex;
		
		while(s.hasNextLine()){
			line = s.nextLine();
			parse = new Scanner(line);
			parse.useDelimiter("[\\t]");
			
			//get rid of the word's type
			wordPlusType = parse.next();
			lineIndex = wordPlusType.indexOf('_');
			if(lineIndex > 0)
				simpleWord = wordPlusType.substring(0, lineIndex);
			else 
				simpleWord = wordPlusType;
			
			if(mj.toSearchWord.word.equals(simpleWord)){
				int year = parse.nextInt();
				int nrAp = parse.nextInt();
				mj.toSearchWord.yearData[year - 1500] = nrAp;
				mj.found = true;
			} else if(mj.found){
				s.close();
				break;
			}
		}
		
		if(mj.found){		
			//AICI PROCESEZ
			mj.toSearchWord.findSpikesSimple();
			mj.toSearchWord.findSpikesGauss();
		}
	}
	
	public void run(){
		while (true) {
			SearchMapJob mj = (SearchMapJob) wp.getWork();
			if (mj == null)
				break;
			
			try {
				doWork(mj);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class SearchMapJob extends Job{
	
	WordInf toSearchWord;
	boolean found = false;
	
	public SearchMapJob(WordInf word){
		
		this.toSearchWord = word;
		
	}
}
