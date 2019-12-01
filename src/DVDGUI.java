import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**
 *  This class is an implementation of DVDUserInterface
 *  that uses JOptionPane to display the menu of command choices. 
 */

public class DVDGUI implements DVDUserInterface {
	 
	 private DVDCollection dvdlist;
	 private JFrame frame;
	 private JPanel panel;
	 JList dvdJList;
	 JScrollPane dvdListPane;
	 String titleText = "Title: ";
	 String ratingText = "Rating: ";
	 String runtimeText = "Runtime: ";
	 JLabel titleLabel, ratingLabel, runtimeLabel;
	 JLabel dvdImage;
	 
	 public DVDGUI(DVDCollection dl)
	 {
		 dvdlist = dl;
		 buildGUIComponents();
	 }
	 
	 public void buildGUIComponents() {
		 frame = new JFrame("DVDGUI");
		 frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		 JPanel listPanel = new JPanel();
		 frame.add(listPanel);
		 
		 //DVD Info Display
		 Box infoBox = Box.createVerticalBox();
		 //infoBox.add(Box.createVerticalStrut(10));
		 titleLabel = new JLabel(titleText);
		 infoBox.add(titleLabel);
		 ratingLabel = new JLabel(ratingText);
		 infoBox.add(ratingLabel);
		 runtimeLabel = new JLabel(runtimeText);
		 infoBox.add(runtimeLabel);
		 dvdImage = new JLabel("[no image]");
		 infoBox.add(dvdImage);
		 JButton modifyButton = new JButton("Modify DVD");
		 modifyButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 try {
					 String title = dvdlist.getDVDArray()[dvdJList.getSelectedIndex()].getTitle();
					 addOrModifyDVD(title);
				 } catch (Exception error) {
					 
				 }
				 
	        }
		 });
		 infoBox.add(modifyButton);
		 listPanel.add(infoBox);
		 
		 //DVD List
		 dvdJList = new JList();
		 dvdListPane = new JScrollPane(dvdJList);
		 refreshDVDJList();
		 dvdJList.setSelectedIndex(0);
		 updateDVDInfo(0);
		 listPanel.add(dvdListPane);
		 dvdJList.addListSelectionListener(new ListSelectionListener() {
			 @Override
			 public void valueChanged(ListSelectionEvent arg0) {
				 if (!arg0.getValueIsAdjusting()) {
					 try {
						 updateDVDInfo(dvdJList.getSelectedIndex());
					 } catch (Exception e) {
						 dvdJList.setSelectedIndex(0);
						 updateDVDInfo(0);
					 }
				 }
			 }
		 });
		 
		 //DVD Option Buttons
		 Box buttonBox = Box.createVerticalBox();
		 JButton addButton = new JButton("Add New DVD");
		 addButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 addOrModifyDVD();
	        }
		 });
		 buttonBox.add(addButton);
		 buttonBox.add(Box.createVerticalStrut(10));
		 JButton removeButton = new JButton("Remove Selected DVD");
		 removeButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 doRemoveDVD();
	        }
		 });
		 buttonBox.add(removeButton);
		 buttonBox.add(Box.createVerticalStrut(10));
		 JButton ratingButton = new JButton("Filter DVD List By Rating");
		 ratingButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 doGetDVDsByRating();
	        }
		 });
		 buttonBox.add(ratingButton);
		 buttonBox.add(Box.createVerticalStrut(10));
		 JButton runTimeButton = new JButton("Get Total Running Time");
		 runTimeButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 doGetTotalRunningTime();
	        }
		 });
		 buttonBox.add(runTimeButton);
		 buttonBox.add(Box.createVerticalStrut(10));
		 JButton saveButton = new JButton("Save Collection & Close");
		 saveButton.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 doSave();
	        }
		 });
		 buttonBox.add(saveButton);
		 buttonBox.add(Box.createVerticalStrut(10));
		 
		 listPanel.add(buttonBox);
		 
		 //Show the Frame
		 frame.setVisible(true);
		 frame.pack();
	 }
	 
	 private void updateDVDInfo(int index) {
		 DVD selected = dvdlist.getDVDArray()[index];
		 titleLabel.setText(titleText + selected.getTitle());
		 ratingLabel.setText(ratingText + selected.getRating());
		 runtimeLabel.setText(runtimeText + selected.getRunningTime());
		 try {
	            BufferedImage img = ImageIO.read(new File(selected.getTitle() + ".jpg"));
	            dvdImage.setIcon(new ImageIcon(img));
	            dvdImage.setText(null);
	         } catch (IOException e) {
	            dvdImage.setText("[no image]");
	            dvdImage.setIcon(null);
	         }
		 frame.pack();
	 }
	 
	 private void refreshDVDJList() {
		 dvdJList.setListData(dvdlist.getDVDArray());
	 }
	 
	public void processCommands()
	 {
		 
	 }
	
	private void addOrModifyDVD() {
		addOrModifyDVD(null);
	}
	private void addOrModifyDVD(String title) {

		// Request the title
		if(title == null)
			title = JOptionPane.showInputDialog(frame, "Enter title");
		
		if (title == null) {
			return;		// dialog was cancelled
		}
		title = title.toUpperCase();
		
		// Request the rating
		String rating = JOptionPane.showInputDialog(frame, "Enter rating for " + title);
		if (rating == null) {
			return;		// dialog was cancelled
		}
		rating = rating.toUpperCase();
		
		// Request the running time
		String time = JOptionPane.showInputDialog(frame, "Enter running time for " + title);
		if (time == null) {
		}

		dvdlist.addOrModifyDVD(title, rating, time);
		refreshDVDJList();	
	}
	
	private void doRemoveDVD() {
		
		DVD selected = dvdlist.getDVDArray()[dvdJList.getSelectedIndex()];
		dvdlist.removeDVD(selected.getTitle());     
		refreshDVDJList();
	}
	
	private void doGetDVDsByRating() {

		// Request the rating
		String rating = JOptionPane.showInputDialog(frame, "Enter rating");
		if (rating == null) {
			return;		// dialog was cancelled
		}
		rating = rating.toUpperCase();
		
        String results = dvdlist.getDVDsByRating(rating);
        if(results == "")
        	results = "No DVDs in collection with rating " + rating;
        JOptionPane.showMessageDialog(frame, results);

	}

        private void doGetTotalRunningTime() {
                 
        	int total = dvdlist.getTotalRunningTime();
        	JOptionPane.showMessageDialog(frame, "Total Running Time of DVDs: " + total + " minutes.");   
        }

	private void doSave() {
		dvdlist.save();
		System.exit(0);
	}
		
}
