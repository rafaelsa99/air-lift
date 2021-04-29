
package Common;

/**
 *    Parametric FIFO derived from a parametric memory.
 *    Errors are reported.
 *
 *    @param <R> data type of stored objects
 *    @author Based on the professor implementation
 */

public class MemFIFO<R> extends MemObject<R> {
  /**
   *   Pointer to the first empty location.
   */

   private int inPnt;

  /**
   *   Pointer to the first occupied location.
   */

   private int outPnt;

  /**
   *   Signaling FIFO empty state.
   */

   private boolean empty;
   
   /**
   *   Counter of the number of objects in FIFO.
   */

   private int count;

  /**
   *   FIFO instantiation.
   *   The instantiation only takes place if a valid memory size exists.
   *   Otherwise, an error is reported.
   *
   *     @param nElem size of the memory to be created
   *     @throws MemException when the memory size is not valid
   */

   public MemFIFO (int nElem) throws MemException {
       super (nElem);
       inPnt = outPnt = 0;
       empty = true;
       count = 0;
   }

  /**
   *   FIFO insertion.
   *   A parametric object is written into it.
   *   If the FIFO is full, an error is reported.
   *
   *    @param val parametric object to be written
   *    @throws MemException when the FIFO is full
   */

   @Override
   public void write (R val) throws MemException {
        if ((inPnt != outPnt) || empty) {
            mem[inPnt] = val;
            inPnt = (inPnt + 1) % mem.length;
            empty = false;
            count++;
        } else
            throw new MemException ("Fifo full!");
   }

  /**
   *   FIFO retrieval.
   *   A parametric object is read from it.
   *   If the FIFO is empty, an error is reported.
   *
   *    @return first parametric object that was written
   *    @throws MemException when the FIFO is empty
   */

   @Override
   public R read () throws MemException{
        R val;
        if (!empty){ 
           val = mem[outPnt];
           outPnt = (outPnt + 1) % mem.length;
           empty = (inPnt == outPnt);
           count--;
        } else
            throw new MemException ("Fifo empty!");
        return val;
   }

  /**
   *   Test FIFO current full status.
   *
   *    @return true, if FIFO is full -
   *            false, otherwise
   */

   public boolean full () {
       return !((inPnt != outPnt) || empty);
   }

  /**
   *   Test FIFO current empty status.
   *
   *    @return true, if FIFO is empty -
   *            false, otherwise
   */

   public boolean empty () {
       return empty;
   }
   
   /**
   *   FIFO counter.
   *
   *    @return number of objects in FIFO
   */

   public int getCounter () {
       return count;
   }
}