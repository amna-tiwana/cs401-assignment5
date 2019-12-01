import java.util.*;

/**
 * 	Program to display and modify a simple DVD collection
 */

public class DVDManager {

	public static void main(String[] args) {
		
		DVDUserInterface dlInterface;
		DVDCollection dl = new DVDCollection();
		
		dl.loadData("dvddata.txt");

		dlInterface = new DVDGUI(dl);
	}

}
