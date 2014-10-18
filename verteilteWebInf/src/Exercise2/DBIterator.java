package Exercise2;

import java.util.ArrayList;

public interface DBIterator {
    public ArrayList<String> open();
    public ArrayList<Register> next();
    void close();
}
