package Exercise2;

import java.util.ArrayList;

public class NLJoin implements DBIterator {

    private DBIterator left;
    private DBIterator right;
    private ArrayList<Integer> joinIndicesLeft;
    private ArrayList<Integer> joinIndicesRight;

    public NLJoin(DBIterator left, DBIterator right) {
        this.left = left;
        this.right = right;
        joinIndicesLeft=new ArrayList();
        joinIndicesRight=new ArrayList();

    }

    @Override
    public ArrayList<String> open() {
        ArrayList<String> attributeNames = new ArrayList();

        //all the attributes from the left tuple will be in the result relation too
        ArrayList<String> attributeNamesLeft = left.open();
        attributeNames.addAll(attributeNamesLeft);

        ArrayList<String> attributeNamesRight = right.open();
        int leftIndex;
        for (int i = 0; i < attributeNamesRight.size(); i++) {
            leftIndex=attributeNamesLeft.indexOf(attributeNamesRight.get(i));
            if (leftIndex==-1){
                //add attribute if the left relation doesn't contain it...
                attributeNames.add(attributeNamesRight.get(i));
            }
            else{
                //...otherwise, the attributes are join attributes. Memorize this.
                joinIndicesLeft.add(leftIndex);
                joinIndicesRight.add(i);
            }
        }

        return attributeNames;
    }

    @Override
    public ArrayList<Register> next() {
        ArrayList<Register> resultTuple=null;
        ArrayList<Register> leftTuple;
        ArrayList<Register> rightTuple;
        
        /*
        Perform the Nested Loop Join.
        Join tuples until we find join partners (e.g. joinTuples returns a value not null)
        or we reached the end of both input relations
        */
        while((leftTuple=left.next())!=null&&resultTuple==null){
            while((rightTuple=right.next())!=null && resultTuple==null){
                resultTuple=joinTuples(leftTuple, rightTuple);
            }
            right.open();
        }
        
        return resultTuple;
    }

    @Override
    public void close() {
        //todo
    }
    
    /**
     * Joins both input tuples
     * @param leftTuple a tuple of the left relation
     * @param rightTuple a tuple of the right relation
     * @return the joined tuple if the input tuples are join partners, null otherwise
     */
    private ArrayList<Register> joinTuples(ArrayList<Register> leftTuple, ArrayList<Register> rightTuple){
        ArrayList<Register> joinedTuple = new ArrayList();
        int indexLeft;
        int indexRight;
        boolean join=true;
        for(int i=0; i<joinIndicesLeft.size() && join; i++){
            indexLeft=joinIndicesLeft.get(i);
            indexRight=joinIndicesRight.get(i);

            if(!leftTuple.get(indexLeft).equals(rightTuple.get(indexRight))){
                join=false;
            }
        }
        
        if(join){
            joinedTuple.addAll(leftTuple);
            for(int i=0; i<rightTuple.size(); i++){
                if(!joinIndicesRight.contains(i)){
                    /*
                    if the attribute at this index is not a join attribute
                    (e.g. the index is not contained in the joinRightIndices list)
                    add this attribute to the result relation
                    */
                    joinedTuple.add(rightTuple.get(i));
                }
            }
        }
        else{
            joinedTuple= null;
        }
        return joinedTuple;
    }
    

}
