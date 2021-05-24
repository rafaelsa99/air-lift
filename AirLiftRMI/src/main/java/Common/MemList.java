
package Common;

/**
 *    Parametric List derived from a parametric memory.
 *    Errors are reported.
 *
 *    @param <R> data type of stored objects
 *    @author Rafael Sá (104552), José Brás (74029)
 */
public class MemList<R> extends MemObject<R> {
    /**
    *   Counter of the number of objects in the List.
    */

    private int count;
    
    /**
    *   List instantiation.
    *   The instantiation only takes place if a valid memory size exists.
    *   Otherwise, an error is reported.
    *
    *     @param nElem size of the memory to be created
    *     @throws MemException when the memory size is not valid
    */

    public MemList (int nElem) throws MemException {
        super (nElem);
        count = 0;
    }
    
    /**
    *   List insertion.
    *   A parametric object is written into it.
    *   If the List is full, an error is reported.
    *
    *    @param val parametric object to be written
    *    @throws MemException when the List is full
    */

    @Override
    public void write (R val) throws MemException {
         if (count < this.nMax) {
            this.mem[count] = val;
            count++;
         } else
            throw new MemException ("Fifo full!");
    }

   /**
    *   List retrieval.
    *   A parametric object is read from it.
    *   If the List is empty, an error is reported.
    *
    *    @return first parametric object of the list
    *    @throws MemException when the List is empty
    */

    @Override
    public R read () throws MemException{
         R val;
         if (count > 0){ 
            val = this.mem[0];
            for(int i = 0; i < count - 1; i++)
                this.mem[i] = this.mem[i+1];
            count--;
         } else
             throw new MemException ("Fifo empty!");
         return val;
    }
    
    /**
    *   List retrieval.
    *   A parametric object with a given value is read from it.
    *   If the List is empty, an error is reported.
    *
    *    @param val value to be removed from the List
    *    @throws MemException when the List is empty
    */

    public void read (R val) throws MemException{
        for(int i = 0; i < count; i++){
            if(this.mem[i] == val){
                // shifting elements
                for(int j = i; j < count - 1; j++){
                    this.mem[j] = this.mem[j+1];
                }
                break;
            }
        }
        count--;
    }
    
    /**
    *   Test if List is empty.
    *
    *    @return true, if List is empty -
    *            false, otherwise
    */

    public boolean empty () {
        return count == 0;
    }

    /**
     * Get the number of items in the list.
     * @return the current number of items
     */
    public int getCount() {
        return count;
    }
    
}
