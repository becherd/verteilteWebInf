package Exercise2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class TableScan implements DBIterator {

    String filename;
    int index;
    BufferedReader reader;

    ArrayList<String> attributeNames;
    ArrayList<String> attributeTypes;

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
        attributeNames = new ArrayList(Arrays.asList(attributeNamesArray));
        attributeTypes = new ArrayList(Arrays.asList(attributeTypesArray));

        return attributeNames;
    }

    @Override
    public ArrayList<Register> next() {
        String line = null;
        try {

            line = reader.readLine();

        } catch (Exception e) {
                //todo
        }

        ArrayList<Register> attributesList = new ArrayList();

        if (line != null) {
            ArrayList<String> attributes;

            attributes = new ArrayList(Arrays.asList(line.split("\\t")));

            attributesList = new ArrayList();
            Register r;
            for (int i = 0; i < attributeTypes.size(); i++) {
                r = fillRegister(attributes.get(i), attributeTypes.get(i));
                attributesList.add(r);
            }
        }

        return attributesList;
    }

    /**
     * Fills the register depending on given data tpye
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
            //todo
    }

}
