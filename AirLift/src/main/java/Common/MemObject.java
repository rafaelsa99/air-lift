
package Common;

/**
 *    Parametric memory.
 *    Non-instantiatable data type. It must be derived.
 *    Errors are reported.
 *
 *    @param <R> data type of stored objects
 *    @author Based on the professor implementation
 */

public abstract class MemObject<R> {
  /**
   *   Memory size.
   */
    
   protected int nMax;
    
  /**
   *   Internal storage area.
   */

   protected R [] mem;

  /**
   *   Memory instantiation.
   *   The instantiation only takes place if a valid memory size exists.
   *
   *     @param nElem size of the memory to be created
   *     @throws MemException when the memory size is not valid
   */
   @SuppressWarnings("unchecked")
   protected MemObject (int nElem) throws MemException {
       if(nElem > 0)
           mem = (R []) new Object[nElem];
       else
           throw new MemException("Illegal storage size!");
       nMax = nElem;
   }

  /**
   *   Memory write.
   *   A parametric object is written into it.
   *   Virtual method, it has to be overridden in a derived data type.
   *
   *    @param val parametric object to be written
   *    @throws MemException when the memory is full
   */

   protected abstract void write (R val) throws MemException;

  /**
   *   Memory read.
   *   A parametric object is read from it.
   *   Virtual method, it has to be overridden in a derived data type.
   *
   *    @return last parametric object that was written
   *    @throws MemException when the memory is empty
   */

   protected abstract R read () throws MemException;
}