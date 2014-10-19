package Exercise2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class TableScan implements DBIterator {

    private String filename;
    private BufferedReader reader;
    private ArrayList<String> attributeTypes;

    public TableScan(String filename) {
        this.filename = filename;
    }

    @Override
    public ArrayList<String> open() {
        String[] attributeNamesArray = null;
        String[] attributeTypesArray = null;

        try {
            reader = new BufferedReader(new FileReader(filename));

            attributeNamesArray = reader.readLine().split("\\t");
            attributeTypesArray = reader.readLine().split("\\t");

        } catch (Exception e) {
            //todo
        }
        
        //store attributeNames and attributeTypes into ArrayLists
        ArrayList<String> attributeNames = new ArrayList(Arrays.asList(attributeNamesArray));
        attributeTypes = new ArrayList(Arrays.asList(attributeTypesArray));

        return attributeNames;
    }

    @Override
    public ArrayList<Register> next() {
        String line = null;
        try {
            //read next line
            line = reader.readLine();

        } catch (Exception e) {
            //todo
        }

        ArrayList<Register> tuple = null;

        if (line != null) {
            ArrayList<String> attributes;

            //split the input string with tab as the delimiter
            attributes = new ArrayList(Arrays.asList(line.split("\\t")));

            tuple = new ArrayList();
            Register r;
            for (int i = 0; i < attributeTypes.size(); i++) {
                r = fillRegister(attributes.get(i), attributeTypes.get(i));
                tuple.add(r);
            }
        }

        return tuple;
    }

    /**
     * Fills the register depending on given data type
     *
     * @param attribute the attribute to be put into the register
     * @param type the type to be used
     * @return the filled Register
     */
    private Register fillRegister(String attribute, String type) {
        Register r = null;
        switch (type) {
            case "String":
                r = new Register(attribute);
                break;
            case "Integer":
                r = new Register(Integer.parseInt(attribute));
                break;
            case "Float":
                r = new Register(Float.parseFloat(attribute));
                break;
            case "Double":
                r = new Register(Double.parseDouble(attribute));
                break;
        }
        return r;
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (Exception e) {
            //todo
        }
    }

}
