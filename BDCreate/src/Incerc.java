import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import dataaccess.DataBaseConnection;

public class Incerc {

	/**
	 * @param args
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			UnsupportedEncodingException {
		// TODO Auto-generated method stub

		Scanner l;
		String line, cuv;
		char lit;
		int an;
		long[] aparitiiPeAni = new long[510];

		PrintWriter writer = new PrintWriter(System.out);
		// writer.println(inputFileInfo);
		// writer.close();*/

		for (int i = 0; i < 26; i++) {
			lit = (char) ('a' + i);
			l = new Scanner(
					new File(
							"D:\\laptop vechi\\androidWorkspace\\TextDating\\googlebooks-eng-all-1gram-20120701-"
									+ lit));
			System.out.println("am deschis " + lit);
			
			while (l.hasNextLine()) {
				line = l.nextLine();
				String[] splited = line.split("\\t");
				cuv = splited[0];
				//aici parsez cuvantul
				//daca gasesc caractere diferite de litere, nu il mai iau in considerare
				boolean ok = true;
				for(int j = 0; j < cuv.length(); j++){
					if(cuv.charAt(j) < 'A' && cuv.charAt(j) != '_'){
						ok = false;
						break;
					} else if(cuv.charAt(j) > 'Z' && cuv.charAt(j) < 'a' && cuv.charAt(j) != '_'){
						ok = false;
						break;
					} else if(cuv.charAt(j) > 'z' && cuv.charAt(j) != '_'){
						ok = false;
						break;
					}
				}
				if(splited.length > 2 && ok){
					an = Integer.parseInt(splited[1]);
					if (an >= 1500) {
					//	writer.write(cuv + " " + an + "\n", 0, (cuv.length() + 6));
						aparitiiPeAni[an - 1500] += Long.parseLong(splited[2]); // numar
																				// toate
																				// aparitiile
					}
				}
			}
			l.close();
		}
		
		writer.close();
		
		//columns to insert in database
		ArrayList<String> attr = new ArrayList<String>();
		attr.add("word");
		attr.add("year_ap");
		attr.add("ap_no");
		ArrayList<String> values = new ArrayList<String>();
		DecimalFormat number_format = new DecimalFormat("##.#########");
		for (int i = 0; i < 26; i++) {
			lit = (char) ('a' + i);
			l = new Scanner(
					new File(
							"D:\\laptop vechi\\androidWorkspace\\TextDating\\googlebooks-eng-all-1gram-20120701-"
									+ lit));
			System.out.println("am deschis " + lit);
					
			while (l.hasNextLine()) {
				line = l.nextLine();
				String[] splited = line.split("\\t");
				cuv = splited[0];
				//aici parsez cuvantul
				//daca gasesc caractere diferite de litere, nu il mai iau in considerare
				boolean ok = true;
				for(int j = 0; j < cuv.length(); j++){
					if(cuv.charAt(j) < 'A'  && cuv.charAt(j) != '.'){
						ok = false;
						break;
					} else if(cuv.charAt(j) > 'Z' && cuv.charAt(j) < 'a' && cuv.charAt(j) != '_'){
						ok = false;
						break;
					} else if(cuv.charAt(j) > 'z'){
						ok = false;
						break;
					}
				}
				if(splited.length > 2 && ok){
					an = Integer.parseInt(splited[1]);
					if (an >= 1500) {
						long nrAp = Long.parseLong(splited[2]); 
						values.clear();
						values.add(cuv);
						values.add(""+an);
						values.add(""+ number_format.format(+nrAp*100000.0/aparitiiPeAni[an-1500]));
						
						try {
							DataBaseConnection.insertValuesIntoTable("words", attr, values, true);
							Thread.sleep(100);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			l.close();
			//writer.close();
		}
	}
}
