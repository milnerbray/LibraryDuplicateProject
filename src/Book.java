import java.util.ArrayList;

public class Book {
    //"System Key"                  : key
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
    private String key, unique_num, title, isbn, issn, issn_l, oclc_control, oclc_num, sys_control, collection, date_created, publisher, date_published;
    private boolean dup = false;

    public Book(String k, String un, String t, String i1, String i2, String i3, String o1, String o2, String sc, String c, String dc, String p, String dp){
        this.key = k;
        this.unique_num = un;
        if(t.equals("Title")) this.title = t;
        else this.title = t.substring(4);
        this.isbn = i1;
        this.issn = i2;
        this.issn_l = i3;
        this.oclc_control = o1;
        this.oclc_num = o2;
        this.sys_control = sc;
        this.collection = c;
        this.date_created = dc;
        this.publisher = p;
        this.date_published = dp;
    }

    //Printing is used for testing purposes only
    public void printBook(){
        System.out.print(this.key + " | ");
        System.out.print(this.unique_num + " | ");
        System.out.print(this.title + "  |");
        System.out.print(this.isbn + " | ");
        System.out.print(this.issn + " | ");
        System.out.print(this.issn_l + " | ");
        System.out.print(this.oclc_control + " | ");
        System.out.print(this.oclc_num + " | ");
        System.out.print(this.sys_control + " | ");
        System.out.print(this.collection + " | ");
        System.out.print(this.date_created + " | ");
        System.out.print(this.publisher + " | ");
        System.out.print(this.date_published + "\n");
    }

    //Used for converting back into a CSV file
    public String[] toStringArray(){
        String dupString = "";
        if(this.dup) dupString = "dup";
        if(this.key.equals("System Key")) dupString = "Duplicate";
        String[] data = {this.key, this.unique_num, this.title, this.isbn, this.issn, this.issn_l,
                this.oclc_control, this.oclc_num, this.sys_control, this.collection, this.date_created,
                this.publisher, this.date_published, dupString};
        return data;
    }

    //Method that checks to see if a book is a duplicate in an ArrayList
    public boolean compareList(ArrayList<Book> books, boolean misc){
        ArrayList<String> values = new ArrayList<>();
        values.add(this.title);
        values.add(this.isbn);
        values.add(this.issn);
        values.add(this.issn_l);
        values.add(this.oclc_control);
        values.add(this.oclc_num);
        values.add(this.publisher);
        values.add(this.date_published);
        values.add(this.collection);
        for(Book b : books){
            if(b.compare(values, misc)) {
                this.dup = true;
                return true;
            }
        }
        return false;
    }

    //Method that checks to see if a list of strings matches anything in the book
    //0         : Title
    //1         : ISBN
    //2         : ISSN
    //3         : ISSN_L
    //4         : OCLC_CONTROL
    //5         : OCLC_NUM
    //6         : PUBLISHER
    //7         : DATE_PUBLISHED
    //8         : COLLECTION
    public boolean compare(ArrayList<String> values, boolean misc){
        if(this.title.contains(values.get(0)) || values.get(0).contains(this.title)){
            if(this.isbn.contains(values.get(1)) || values.get(1).contains(this.isbn)
                    || this.issn.contains(values.get(2)) || values.get(2).contains(this.issn)
                    || this.issn_l.contains(values.get(3)) || values.get(3).contains(this.issn_l)
                    || this.oclc_control.contains(values.get(4)) || values.get(4).contains(this.oclc_control)
                    || this.oclc_num.contains(values.get(5)) || values.get(5).contains(this.oclc_num)){
                if((this.publisher.contains(values.get(6)) || values.get(6).contains(this.publisher))
                        && (this.date_published.contains(values.get(7)) || values.get(7).contains(this.date_published))){
                    if(misc){
                        return this.collection.equals(values.get(8));
                    } else return true;
                }
            }
        }
        return false;
    }

    public boolean isTitle(){
        return this.title.equalsIgnoreCase("title");
    }
}
