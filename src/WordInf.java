import java.util.ArrayList;
import java.util.Iterator;

class WordInf implements Comparable{
	
	String word;
	int apparitions;
	int[] yearData = new int[510];
	ArrayList<Interval> intervalList = new ArrayList<Interval>();
	ArrayList<Interval> intervalListGauss = new ArrayList<Interval>();
	
	static final int EPSILON = 3;
	static final int DICTIONAR_DIM = 510;
	
	public WordInf(String w, int a){
		word = w;
		apparitions = a;
	}
	
	public WordInf() {
		// TODO Auto-generated constructor stub
	}

	public String toString(){
			return "("+word+", "+apparitions+ ")\n";
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof WordInf)
			return word.equals(((WordInf)obj).word);
		else if(obj instanceof String)
			return word.equals(obj);
		return false;
	}
	
	public void findSpikesSimple(){

		float avg = 0;
		float deviation = 0;
	
		for(int i=0; i < DICTIONAR_DIM; i++)
			avg += yearData[i];		
		
		avg = avg / DICTIONAR_DIM;
		for(int i=0; i < DICTIONAR_DIM; i++){
			deviation += Math.abs(yearData[i] - avg); 
		}
		deviation = deviation / DICTIONAR_DIM;
		
		//si mai parcurg odata pt spike-uri
		int start = -1;
		int stop = 0, delta, peek = 0;
		Boolean spike = false;
		
		for(int i=0; i < DICTIONAR_DIM; i++){
			
			if(yearData[i] < avg){
				start = i;
				peek = yearData[i];
			} else {
				if(start != -1){
					delta = Math.abs(yearData[i] - yearData[i-1]);
					if (delta >= EPSILON * deviation){
						spike = true;	
						
						if(yearData[i] > peek)	//detrmine peek
							peek = yearData[i];
					}			
				}
				if(spike){
					while(true){
						i++;
						
						if(yearData[i] < avg){
							stop = i;
							break;
						}
						
						if(yearData[i] > peek)	//detrmine peek
							peek = yearData[i];
					}
					intervalList.add(new Interval(start, stop, peek));
					Main.inputFileInfo.adIntervalSimple(new Interval(start, stop, peek));
					
					start = i;
					peek = yearData[i];
					stop = -1;
					spike = false;
				}
			}
		}
	}

	public void findSpikesGauss() {
		
		Iterator<Interval> it = intervalList.iterator();
		Interval aux;
		
		//DIFERENTA INTRE ANI SI POZITII 
		//TRECI SI STANDARDUL IN POZITII PE VECTOR
		
		while(it.hasNext()){
			aux = it.next();
			get_param(aux.start, aux.stop);
		}
		
		
		
	}
	
	private double EMD(int l, int r, int sum, int min, double miu, double sigma, double sigma_sq){
		
		double acc = 0;
		double distance = 0;
		
		for(int i=l; i<=r; i++){
			acc += (yearData[i] - min)/sum - Math.exp(-(i-miu)*(i-miu)/(2*sigma_sq))/(Math.sqrt(2*Math.PI)*sigma);
			distance += Math.abs(acc);
		}
		
		return distance;
	}
	
	private void get_param(int l, int r){
		
		int min;
		int k;
		
		min = yearData[l];
		if(yearData[r] < min)
			min = yearData[r];
		
		int sum = 0;
		for(k = l; k <= r; k++){
			sum += yearData[k];
		}
		sum -= (r-l)*min;
		
		double miu = 0;
		double sigma_sq = 0;
		
		for(k = l; k <= r; k++){
			miu = miu + ((double)(yearData[k] - min)*k/sum);
		}
		
		miu =  miu/(r-l+1);
		
		for(k = l; k <= r; k++){
			sigma_sq = sigma_sq + ((double)(yearData[k] - min)*(k-miu)*(k-miu)/sum);
		}
		
		double sigma = Math.sqrt(sigma_sq);
		
		double coef = EMD(l, r, sum, min, miu, sigma, sigma_sq);
	
		//DE VERIFICAT SI ADAUGAT LA LISTA INTERVALE
		if(coef <= 0.03){
			Main.inputFileInfo.adIntervalGauss(new Interval(l, r, (yearData[(int) (l + 0.5*(r-l))])));
		}
	}

	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof WordInf)
			return word.compareTo(((WordInf) arg0).word);
		else return -1;
	}
}

class Interval{
	int start, stop, peek;
	
	public Interval(int start, int stop, int peek){
		this.start = start;
		this.stop = stop;
		this.peek = peek;
	}
}
