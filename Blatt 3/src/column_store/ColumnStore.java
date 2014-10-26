package column_store;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
/**
 *
 * @author David
 */
public class ColumnStore {
    //store the columns in a LinkedHashMap. Key will be the column-name.
    private LinkedHashMap<String, ArrayList<Register>> columns;
    private String[] attributes;
    
    public ColumnStore(Tablescan table){
        buildColumnStore(table);
    }
        
    /**
     * stores the given table into the LinkedHashMap columns
     * @param table the table to be converted into a column store
     */
    private void buildColumnStore(Tablescan table){
        columns = new LinkedHashMap();
        this.attributes = table.open();
        
        for(int i=0; i<attributes.length; i++){
            //add ArrayLists as Columns
            ArrayList<Register> column = new ArrayList();
            columns.put(attributes[i], column);
        }
        
        Register[] tuple;
        while((tuple=table.next()) != null){
            for(int i=0; i<attributes.length; i++){
                ArrayList<Register> column = columns.get(attributes[i]);
                column.add(tuple[i]);
                columns.put(attributes[i], column);
            }          
        }
    }
    
    /**
     * 
     * @return the whole LinkedHashMap which contains the columns
     */
    public LinkedHashMap<String, ArrayList<Register>> getColumns(){
        return columns;
    }
    
    /**
     * 
     * @param columnName the requested column name
     * @return the requested column as a list
     */
    public ArrayList<Register> getColumn(String columnName){
        ArrayList<Register> column=columns.get(columnName);
        if(column == null){
            System.out.println("Column name not found!");
        }
        
        return column;
    }
    
    /**
     * prints each column
     */
    public void print(){
        Iterator<String> it = columns.keySet().iterator();
        while(it.hasNext()){
           String columnName=it.next();
        System.out.println("Column " + columnName);
        ArrayList<Register> column = columns.get(columnName);
            for(int i=0; i<column.size(); i++){
                System.out.println(column.get(i));
            }
        }
        
    }
    
}
