import java.io.*;
import java.util.Arrays;
import java.util.Comparator; 

public class DVDCollection {

	// Data fields
	
	/** The current number of DVDs in the array */
	private int numdvds;
	
	/** The array to contain the DVDs */
	private DVD[] dvdArray;
	
	/** The name of the data file that contains dvd data */
	private String sourceName;
	
	/** Boolean flag to indicate whether the DVD collection was
	    modified since it was last saved. */
	private boolean modified;
	
	/**
	 *  Constructs an empty directory as an array
	 *  with an initial capacity of 7. When we try to
	 *  insert into a full array, we will double the size of
	 *  the array first.
	 */
	public DVDCollection() {
		numdvds = 0;
		dvdArray = new DVD[7];
	}
	
	public String toString() {
		// Return a string containing all the DVDs in the
		// order they are stored in the array along with
		// the values for numdvds and the length of the array.
		// See homework instructions for proper format.
		String dvdString = "";
		dvdString += ("numdvds = " + numdvds + '\n');
		dvdString += ("dvdArray.length = "+ dvdArray.length + '\n');
		for(int i = 0; i < numdvds; i++) {
			dvdString += ("dvdArray[" + i + "] = " + dvdArray[i].toString() + '\n');
		}
		return dvdString;
	}

	public void addOrModifyDVD(String title, String rating, String runningTime) {
		// NOTE: Be careful. Running time is a string here
		// since the user might enter non-digits when prompted.
		// If the array is full and a new DVD needs to be added,
		// double the size of the array first.
		if(!validRating(rating)) {
			return;
		}
		int runTime = validateRunningTime(runningTime);
		if(runTime <= 0) {
			return;
		}
		
		modified = true;
			
		title = title.toUpperCase(); //force user input to upper case
		
		//Search for existing element
		int index = findDVD(title);
		if(index >= 0) { //dvd exists in collection, update it
			dvdArray[index].setTitle(title);
			dvdArray[index].setRating(rating);
			dvdArray[index].setRunningTime(runTime);
			System.out.println("found duplicate at index " + index);
		}
		else { //dvd not in collection, add it
			if(numdvds < dvdArray.length) {
				dvdArray[numdvds] = new DVD(title, rating, runTime);
				System.out.println("[" + numdvds + "]" + dvdArray[numdvds].toString());
				numdvds++;
				Arrays.sort(dvdArray, 0, numdvds, new DVDTitleSort());
			}
			else {
				//todo double array size and try again
				System.out.println("need to increase array size first");
				doubleDVDArraySize();
				System.out.println(toString());
				//addOrModifyDVD(title, rating, runningTime);
				//System.out.println(toString());
			}
		}
	}
	
	public void removeDVD(String title) {
		int index = findDVD(title);
		if(index >= 0) {
			modified = true;
	        DVD[] newArray = new DVD[dvdArray.length - 1]; 
	        System.arraycopy(dvdArray, 0, newArray, 0, index); 
	        System.arraycopy(dvdArray, index + 1, 
	        		newArray, index, 
	        		dvdArray.length - index - 1);
	        dvdArray = newArray;
	        numdvds--;
		}
		else {
			//todo throw exception
			System.out.println("DVD not found");
		}
	}
	
	public String getDVDsByRating(String rating) {
		rating = rating.toUpperCase();
		String list = "";
		
		for(int i = 0; i < numdvds; i++) {
			if(dvdArray[i].getRating().equals(rating))
				list += (dvdArray[i].toString() + '\n');
		}

		return list;
	}

	public int getTotalRunningTime() {
		int total = 0;
		
		for(int i = 0; i < numdvds; i++) {
			total += dvdArray[i].getRunningTime();
		}
		
		return total;
	}

	
	public void loadData(String filename) {
		try {
		  // put file in project main folder, not the src folder
		  FileReader fin = new FileReader(filename);
		  BufferedReader bis = new BufferedReader(fin);
		  
		  String line;
		  while ((line = bis.readLine()) != null) {
			  String[] values = line.split(",");
			  if(values.length != 3) {
				  System.out.println("Error: Invalid DVD entry in file \"" + line + "\"");
				  return;
			  }
			  addOrModifyDVD(values[0], values[1], values[2]);
		  }
		  modified = false;
		  sourceName = filename;
		} 
		catch (Exception e) {
			System.out.println("File not found. Starting new collection.");
		}
	}
	
	public void save() {
		try {
			if(!modified) {
				System.out.println("Notice: No changes to save. File not modified.");
				return;
			}
			
			FileWriter fw = new FileWriter(sourceName);
			if(numdvds > 0) {
				for(int i = 0; i < numdvds; i++) {
					fw.write(dvdArray[i].getTitle() + "," + dvdArray[i].getRating() + "," + dvdArray[i].getRunningTime());
					fw.write(System.lineSeparator());
				}
			}
			
			fw.close();
			System.out.println("Notice: Changes to " + sourceName + " saved successfully.");
			modified = false;
		} 
		catch (IOException e) {
			System.err.println("Error saving file: " + e);
		}
	}

	// Additional private helper methods go here:
	
	/**	
	 * Doubles the size of the dvdArray to make room for more DVDs
	 */
	private void doubleDVDArraySize() {
		DVD[] newArray = new DVD[dvdArray.length * 2]; 
		System.arraycopy(dvdArray, 0, newArray, 0, numdvds); 
        dvdArray = newArray;
	}
	
	/**
	 * Checks given string against a known good list of movie ratings. 
	 * Returns true for valid rating, false for anything else
	 */
	private boolean validRating(String rating) {
		if(rating.equals("NC-17") || rating.equals("R") || rating.equals("PG-13") || rating.equals("PG") || rating.equals("G"))
			return true;
		//todo throw exception?
		System.out.println("invalid movie rating");
		return false;
	}
	
	/** 
	 * Takes string input of runningTime and validates it by first trying to convert to int and then checking the result.
	 * @param runningTime
	 * @return int
	 */
	private int validateRunningTime(String runningTime) {
		int runTime = 0;
		try {
			runTime = Integer.parseInt(runningTime);
		}
		catch(NumberFormatException e) {
			//todo throw exception?
			System.out.println("invalid runtime");
		}
		return runTime;
	}
	
	/**
	 * Performs a simple search of the sorted DVD collection array and returns an index of the result (if found)
	 * @param title - DVD title
	 * @return int index
	 */
	private int findDVD(String title ) {
		if(numdvds > 0) {
			for(int i = 0; i < numdvds; i++) {
				if(dvdArray[i].getTitle().equals(title))
					return i;
			}
		}
		return -1;
	}
}

/** 
 * Implements a sort comparator for DVD objects so that we can easily sort the dvdArray by Title
 */
class DVDTitleSort implements Comparator<DVD> { 
    public int compare(DVD a, DVD b) { 
    	return a.getTitle().compareTo(b.getTitle()); 
    }
}
