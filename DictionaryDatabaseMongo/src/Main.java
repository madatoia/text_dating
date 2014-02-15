import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Set;

import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;

public class Main {

	/**
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {

		// To directly connect to a single MongoDB server (note that this will
		// not auto-discover the primary even
		// if it's a member of a replica set:
		MongoClient mongoClient = new MongoClient();

		mongoClient.dropDatabase("dict");
		DB db = mongoClient.getDB("dict");

		Set<String> str = db.getCollectionNames();

		for (String s : str) {
			System.out.println(s);
		}
		DBCollection col = db.getCollection("dict");

		Scanner l;
		String line, cuv;
		char lit;
		int an;
		long[] aparitiiPeAni = new long[510];

		//PrintWriter writer = new PrintWriter(System.out);
		// writer.println(inputFileInfo);
		// writer.close();*/

		/*for (int i = 0; i < 26; i++) {
			lit = (char) ('a' + i);
			try {
				l = new Scanner(
						new File(
								"D:\\laptop vechi\\androidWorkspace\\TextDating\\googlebooks-eng-all-1gram-20120701-"
										+ lit));
				System.out.println("am deschis " + lit);

				while (l.hasNextLine()) {
					line = l.nextLine();
					//System.out.println(line);
					String[] splited = line.split("\\t");
					cuv = splited[0];
					// aici parsez cuvantul
					// daca gasesc caractere diferite de litere, nu il mai iau
					// in
					// considerare
					boolean ok = true;
					for (int j = 0; j < cuv.length(); j++) {
						if (cuv.charAt(j) < 'A' && cuv.charAt(j) != '_') {
							ok = false;
							break;
						} else if (cuv.charAt(j) > 'Z' && cuv.charAt(j) < 'a'
								&& cuv.charAt(j) != '_') {
							ok = false;
							break;
						} else if (cuv.charAt(j) > 'z' && cuv.charAt(j) != '_') {
							ok = false;
							break;
						}
					}
					if (splited.length > 2 && ok) {
						an = Integer.parseInt(splited[1]);
						if (an >= 1500) {
							// writer.write(cuv + " " + an + "\n", 0,
							// (cuv.length()
							// + 6));
							aparitiiPeAni[an - 1500] += Long
									.parseLong(splited[2]); // numar
															// toate
															// aparitiile
						}
					}
				}
				l.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	*/
		//BufferedReader totalApFile;
		try {
			/*totalApFile = new BufferedReader(new FileReader(new File(
					"aparitii.txt")));*/

			/*for (int i = 0; i < aparitiiPeAni.length; i++) {
				totalApFile.write(i + "\t" + aparitiiPeAni[i] + "\n");

			}*/
			Scanner s = new Scanner(new File("aparitii.txt"));
			while(s.hasNextInt()){
				an = s.nextInt();
				System.out.println(an);
				aparitiiPeAni[an] = s.nextLong();
			}
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// columns to insert in database
		//String prevWord = "";
		BasicDBObject toInsert = new BasicDBObject();
		/*toInsert.append("word", "cuv");
		String ins = "[[1900, 1]";
		for(int i=0; i<500; i++){
			ins += ", [1999, "+i+"]";
		}
		toInsert.append("aparitii", ins+"]");
		col.insert(toInsert);
		DBCursor cr = col.find();
		while(cr.hasNext()){
			System.out.println(cr.next());
		}*/
		DecimalFormat number_format = new DecimalFormat("##.#########");
		for (int i = 0; i < 26; i++) {
			lit = (char) ('a' + i);
			try {
				l = new Scanner(
						new File(
								"D:\\laptop vechi\\androidWorkspace\\TextDating\\googlebooks-eng-all-1gram-20120701-"
										+ lit));
				System.out.println("am deschis " + lit);
				//String apparitions = "";
				while (l.hasNextLine()) {
					line = l.nextLine();
					String[] splited = line.split("\\t");
					cuv = splited[0];
					// aici parsez cuvantul
					// daca gasesc caractere diferite de litere, nu il mai iau
					// in
					// considerare
				
					boolean ok = true;

					for (int j = 0; j < cuv.length(); j++) {
						if (cuv.charAt(j) < 'A'
								&& cuv.charAt(j) != '.') {
							ok = false;
							break;
						} else if (cuv.charAt(j) > 'Z'
								&& cuv.charAt(j) < 'a'
								&& cuv.charAt(j) != '_') {
							ok = false;
							break;
						} else if (cuv.charAt(j) > 'z') {
							ok = false;
							break;
						}
					}
					if ( ok ) {
						toInsert.append("word", cuv);
						if (splited.length > 2) {
							an = Integer.parseInt(splited[1]);
							if (an >= 1500) {
								long nrAp = Long
										.parseLong(splited[2]);
								toInsert.append("app", "["+ an+", "
										+ number_format
										.format(+nrAp
												* 10000.0
												/ aparitiiPeAni[an - 1500])+"]");
								col.insert(toInsert);
							}
						}
						toInsert.clear();
					}
				}
				l.close();
				// writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
