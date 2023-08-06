import com.opencsv.*;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//Book Variable Guide
// "System Key"                  : key
// "Unique Record Number"       : unique_num
// "Title"                      : title
// "ISBN"                       : isbn
// "ISSN"                       : issn
// "ISSN-L"                     : issn_l
// "OCLC Control Number"        : oclc_control
// "OCLC Number"                : oclc_num
// "System Control Number"      : sys_control
// "Collection Notes"           : collection
// "Date/Time Created"          : date_created
// "Publisher"                  : publisher
// "Dates of Publication"       : date_published
// "Duplicate"                  : dup
public class Main {
    //This method takes a folder and returns a LinkedList with the filepaths for all csv files in that folder
    public static LinkedList<String> listFilesForFolder(final File folder) {
        LinkedList<String> filenames = new LinkedList<String>();
        if(folder.listFiles() == null) {
            System.out.println("Empty Folder...");
            return null;
        }
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                if(fileEntry.getName().contains(".csv"))
                    filenames.add(fileEntry.getName());
            }
        }
        return filenames;
    }

    //main: This runs the program
    public static void main(String[] args) throws IOException, CsvException {
        //Runs the OpenFile to use the UI to find the folder's filepath
        OpenFile o = new OpenFile();
        String folderPath = o.selectFolder();
        //Runs listFilesForFolder() to return a list of filepaths for the csv files
        final File folder = new File(folderPath);
        LinkedList<String> filenames = listFilesForFolder(folder);
        //Makes a Result directory for the result CSV files to go
        //***Directory could already exist, but it won't successfully create another if this is the case***
        File newFolder = new File(folderPath + File.separator + "CSV_Results");
        if(newFolder.mkdir()) System.out.println("Directory has been created successfully");
        else System.out.println("Directory cannot be created");
        //JFrame with a very simple loading screen
        JFrame frame = new JFrame("Loading...");
        frame.setSize(new Dimension(200, 100));
        JLabel label = new JLabel("          Loading... Please wait...");
        frame.getContentPane().add(label);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //Loops through all the filepaths found and creates their Files
        ArrayList<File> files = new ArrayList<>();
        if(filenames != null) for(String s : filenames) {
            File f = new File(folderPath + File.separator + s);
            files.add(f);
        }
        //Scans each file
        int dupCount = 0;
        int total = 0;
        for(File f : files){
            //Parses and reads a file
            CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
            CSVReader reader = new CSVReaderBuilder(new FileReader(f)).withCSVParser(parser).build();
            List<String[]> lines = new ArrayList<>();
            lines = reader.readAll();
            reader.close();
            //Creates each Book object and checks for duplicates
            ArrayList<Book> springer = new ArrayList<>();
            ArrayList<Book> ebscoUnlimited = new ArrayList<>();
            ArrayList<Book> ebscoPermanent = new ArrayList<>();
            ArrayList<Book> ebscoOther = new ArrayList<>();
            ArrayList<Book> gutenberg = new ArrayList<>();
            ArrayList<Book> netLibrary = new ArrayList<>();
            ArrayList<Book> IEEE = new ArrayList<>();
            ArrayList<Book> ebrary = new ArrayList<>();
            ArrayList<Book> miscBooks = new ArrayList<>();
            ArrayList<Book> titleBooks = new ArrayList<>();
            for(String[] fields : lines){
                Book b = new Book(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6],
                        fields[7], fields[8], fields[9], fields[10], fields[11], fields[12]);
                total++;
                //Duplicate checking and collection sorting
                if(fields[9].toLowerCase().contains("springer")){
                    if(b.compareList(springer, false)) dupCount++;
                    springer.add(b);
                } else if(fields[9].toLowerCase().contains("ebsco")){
                    if(fields[9].toLowerCase().contains("unlimited")){
                        if(b.compareList(ebscoUnlimited, false)) dupCount++;
                        ebscoUnlimited.add(b);
                    } else if(fields[9].toLowerCase().contains("permanent")){
                        if(b.compareList(ebscoPermanent, false)) dupCount++;
                        ebscoPermanent.add(b);
                    } else{
                        if(b.compareList(ebscoOther, false)) dupCount++;
                        ebscoOther.add(b);
                    }
                } else if(fields[9].toLowerCase().contains("gutenberg")){
                    if(b.compareList(gutenberg, false)) dupCount++;
                    gutenberg.add(b);
                } else if(fields[9].toLowerCase().contains("netlibrary")){
                    if(b.compareList(netLibrary, false)) dupCount++;
                    netLibrary.add(b);
                } else if(fields[9].toUpperCase().contains("IEEE")){
                    if(b.compareList(IEEE, false)) dupCount++;
                    IEEE.add(b);
                } else if(fields[9].toLowerCase().contains("ebrary")){
                    if(b.compareList(ebrary, false)) dupCount++;
                    ebrary.add(b);
                } else{
                    if(b.compareList(miscBooks, true)) dupCount++;
                    if(!b.isTitle()) miscBooks.add(b);
                    else titleBooks.add(b);
                }
            }
            //Export resulting CSV File
            File newFile = new File(newFolder.getAbsolutePath() + File.separator + "result_" + f.getName());
            FileWriter outputFile = new FileWriter(newFile);
            CSVWriter writer = new CSVWriter(outputFile);
            //Write the CSV
            writer.writeNext(titleBooks.get(0).toStringArray());
            for(Book b : springer) writer.writeNext(b.toStringArray());
            for(Book b : ebscoUnlimited) writer.writeNext(b.toStringArray());
            for(Book b : ebscoPermanent) writer.writeNext(b.toStringArray());
            for(Book b : ebscoOther) writer.writeNext(b.toStringArray());
            for(Book b : gutenberg) writer.writeNext(b.toStringArray());
            for(Book b : netLibrary) writer.writeNext(b.toStringArray());
            for(Book b : IEEE) writer.writeNext(b.toStringArray());
            for(Book b : ebrary) writer.writeNext(b.toStringArray());
            for(Book b : miscBooks) writer.writeNext(b.toStringArray());
            writer.close();
        }
        System.out.println("Duplicates: " + dupCount + "/" + total);
        label.setText("     Duplicates: " + dupCount + " / " + total);
    }
}