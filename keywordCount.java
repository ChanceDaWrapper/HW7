package hw7;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class keywordCount {
	//declarations
	public static ArrayList<String> words = new ArrayList<>();
	
	//Using multiple hashmaps
	public static HashMap<String, Integer> wordsValuesMap = new HashMap<>();
	public static HashMap<String, Integer> testValuesMap = new HashMap<>();
	public static HashMap<Character, Integer> letterValuesMap = new HashMap<>();
	public static HashMap<Character, Integer> gValuesMap = new HashMap<>();
	
	//All counters and such
	public static int totalLines = 0;
	public static int numComments = 0;
	public static int numBlank = 0;
	public static int totLines = 0;
	public static int numFor = 0;
	public static int numWhile = 0;
	public static int numIfState = 0;
	//Creating our file path
	public static String filePath = "";
	public static String[] keywordArr = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
			"class", "continue", "const", "default", "do", "double", "else", "enum", "exports", "extends", "final",
			"finally", "float", "for", "goto", "if", "implements", "imports", "instanced", "int", "interface", "long",
			"module", "native", "new", "package", "private", "protected", "public", "requires", "return", "short",
			"static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try",
			"var", "void", "volatile", "while" };
	
	//Hashmap for keywords
	public static HashMap<String, Integer> keyCounter = new HashMap<>();
	//Inputting our "g" value given
	public static int g = 26; 
	public static int max = 0;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//Starting the stopwatch
		Stopwatch watch = new Stopwatch();
		watch.start();
		
		//reads in file
		readFile();
		lineCounter();

		keyCounter = sortByValues(keyCounter);
		watch.stop();
		// Chceking to make sure the file wasn't cancelled
		if (!filePath.equals("selected cancel")) {
			System.out.println("\nTotal lines: " + totalLines);
			System.out.println("Number of blank lines: " + numBlank);
			System.out.println("Number of comments: " + numComments);
			System.out.println("Number of for loops: " + numFor);
			System.out.println("Number of while loops: " + numWhile);
			System.out.println("Number of if statements: " + numIfState);
			
			//Getting the total number of actual code.
			totLines = totalLines - numBlank - numComments;

			
			System.out.println("Total number of lines without comments and blanks: " + totLines + "\n");
			System.out.println("Time taken: " + watch.getElapsedTime() / 1000 + " seconds");
		}
	}

	//Getting our h value
	public static int h(String word) {

		char first = word.charAt(0);
		char last = word.charAt(word.length() - 1);
		return (word.length() + (g * first) + (g * last)) % keywordArr.length;
	}

	//Sorting our hashmap values

	private static HashMap sortByValues(HashMap map) {

		List<?> list = new LinkedList(map.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		HashMap sortedHashMap = new LinkedHashMap();
		for (java.util.Iterator it = list.iterator(); ((java.util.Iterator) it).hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	//Reading our file

	public static void readFile() {
		filePath = chooseFile();
		System.out.println();
	}


	//getting the file (reusing our JFileChooser
	@SuppressWarnings("deprecation")
	public static String chooseFile() {

		// file chooser
		JButton open = new JButton();
		JFileChooser fc = new JFileChooser();
		
		//Looking for java files
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JAVA FILES", "java", "java");
		fc.setFileFilter(filter);
		fc.setCurrentDirectory(new java.io.File("/Users/Maggie/eclipse-workspace/"));
		fc.setDialogTitle("Select a File: ");
		if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile().getAbsolutePath();
		} else {
			fc.hide();
			return "canceled";
		}
	}

	public static void lineCounter() {
		//As long as the file wasn't cancelled it runs
		if (!filePath.equals("selected cancel")) {
			System.out.println("File Path: " + filePath + "\n\n");

			//Reading the file with LineNumberReader
			LineNumberReader reader = null;

			try {

				reader = new LineNumberReader(new FileReader(new File(filePath)));
				String str;
				while ((str = reader.readLine()) != null) {

					str = str.replaceAll("\\s+", "");
					totalLines++;

					//seeing if the file has the keywords we're looking for

					for (int i = 0; i < keywordArr.length; i++) {
						if (str.contains(keywordArr[i])) {							
							if (keyCounter.containsKey(keywordArr[i])) {
								keyCounter.put(keywordArr[i],
										keyCounter.get(keywordArr[i]) + 1);
							} else {
								keyCounter.put(keywordArr[i], 1);
							}

						}
					}

					//Checking about blanks, comments, ifs, whiles, and fors

					if (str.equals("")) { 
						numBlank++;
					}
					if ((str.length() >= 2) && (str.startsWith("//"))) {
						numComments++;
					}

					//checking the for, while, and ifs

					if (str.contains("for")) {
						numFor++;
					}
					if (str.contains("while")) {
						numWhile++;
					}
					if (str.contains("if")) {
						numIfState++;
					}
					//going through the /* */ type comments
					else if (str.contains("/*")) {
						numComments++;

						while (((str = reader.readLine()) != null) && !(str.endsWith("*/"))) {
							totalLines++;
							numComments++;
						}

						//incrementing the end
						totalLines++;
						numComments++;
					}

					//checking 0 values 

					for (int i = 0; i < keyCounter.size(); i++) {
						for (int j = 0; j < keywordArr.length; j++) {
							if (!keyCounter.containsKey(keywordArr[i])) {
								keyCounter.put(keywordArr[i], 0);
							}
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("CANCEL BUTTON WAS SELECTED.");
		}
	}
}