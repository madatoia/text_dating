import java.util.ArrayList;
import java.util.Scanner;


public class MapWorker extends Thread{
	
	WorkPool wp;
	
	public MapWorker(WorkPool wp){
		this.wp = wp;
	}
	
	void doWork(MapJob mj){
		int i = 0;
		String read = "";
		Scanner s = new Scanner(mj.input);
		s.useDelimiter("[^A-Za-z'-]");
		
		WordInf aux = new WordInf();
		
		while(s.hasNext()){
			read = s.next();
			while(read.equals("")){
				if(s.hasNext())
					read = s.next();
				else {
					break;
				}
			}
			if(!read.equals("")){
				//check if ends with ed, ing, ly, s
				int len = read.length();
				
				if(read.endsWith("ed"))
					read.substring(0, len-2);
				else if(read.endsWith("ing"))
					read.substring(0, len-3);
				else if(read.endsWith("ly"))
					read.substring(0, len-2);
				else if(read.endsWith("s"))
					read.substring(0, len-1);
				
				aux.word = read;
				i = mj.words.indexOf(aux);
			
				if(i != -1){
					mj.words.get(i).apparitions++;
				} else {
					mj.words.add(new WordInf(read, 1));
				}
			}
		}
		s.close();
		if(mj.words.size() > 0)
			mj.file.words.add(mj.words);
	
	}
	
	public void run(){
		while (true) {
			MapJob mj = (MapJob) wp.getWork();
			if (mj == null)
				break;
			
			doWork(mj);
		}
		
	}
}

abstract class Job{
	
}

class MapJob extends Job{
	String input;
	int totalWordsNo = 0;
	FileInf file;
	ArrayList<WordInf> words;
	
	public MapJob(String s, FileInf f){
		input = s;
		file = f;
		words = new ArrayList<WordInf>();
		
	}
}
