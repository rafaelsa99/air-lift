/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.*;
import java.net.*;

/**
 * 
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class ProxyCom {
   /**
   *  Listening socket
   *    @serialField listeningSocket
   */

   private ServerSocket listeningSocket = null;

  /**
   *  Communication Socket
   *    @serialField commSocket
   */

   private Socket commSocket = null;

  /**
   *  Number of listening port
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;
  
   /**
   *  Time till timeout
   *    @serialField socketTimeout
   */
   private int socketTimeout;

  /**
   *  Input Stream de entrada from communication channel
   *    @serialField in
   */

   private ObjectInputStream in = null;

  /**
   *  Output Stream from communication channel
   *    @serialField out
   */

   private ObjectOutputStream out = null;

  /**
   * Instantiation of communication channel (1)
   *
   *    @param portNumb Number of listening port of server
   */

   public ProxyCom (int portNumb)
   {
      serverPortNumb = portNumb;
      socketTimeout = 0;
   }

  /**
   * Instantiation of communication channel (2)
   *
   *    @param portNumb Number of listening port of server
   *    @param lSocket Listening socket
   */

   public ProxyCom (int portNumb, ServerSocket lSocket)
   {
      serverPortNumb = portNumb;
      listeningSocket = lSocket;
      socketTimeout = 0;
   }

     /**
   *  Instantiation of communication channel (3)
   *
   *    @param portNumb Number of listening port of server
   *    @param socketTimeout Timeout
   */

  public ProxyCom (int portNumb, int socketTimeout)
  {
     serverPortNumb = portNumb;
     this.socketTimeout = socketTimeout;
  }

  /**
   *  Service initialization
   *  Initialization of socket, association with local machine address 
   *  and public listening port
   */

   public void start ()
   {
      try
      { listeningSocket = new ServerSocket (serverPortNumb);
        listeningSocket.setSoTimeout(socketTimeout);
      }
      catch (BindException e)                         // erro fatal --- port já em uso
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível a associação do socket de escuta ao port: " +
                                 serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)                           // erro fatal --- outras causas
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um erro indeterminado na associação do socket de escuta ao port: " +
                                 serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Service shutdown
   *  Closure of listening socket
   */

   public void end ()
   {
      try
      { listeningSocket.close ();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível fechar o socket de escuta!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Listening process
   *  Criation of communication channel for pending requests
   *  Instantiation of communication socket and association of client address
   *  Input and output stream opening
   *
   *    @return Communication socket
   *    @throws SocketTimeoutException if the socket timeouts
   */

   public ProxyCom accept() throws SocketTimeoutException
   {
      ProxyCom scon;                                      // Com channel

      scon = new ProxyCom(serverPortNumb, listeningSocket);
      try
      { scon.commSocket = listeningSocket.accept();
      }
      catch (SocketException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - foi fechado o socket de escuta durante o processo de escuta!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch(SocketTimeoutException e) 
      {
        throw e;
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível abrir um canal de comunicação para um pedido pendente!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { scon.in = new ObjectInputStream (scon.commSocket.getInputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível abrir o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { scon.out = new ObjectOutputStream (scon.commSocket.getOutputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível abrir o canal de saída do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      return scon;
   }

  /**
   *  Communication channel closure
   *  Input and output stream closure
   *  Communication socket closure
   *  
   */

   public void close ()
   {
      try
      { in.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível fechar o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { out.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível fechar o canal de saída do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { commSocket.close();
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível fechar o socket de comunicação!");
        e.printStackTrace ();
        System.exit (1);
      }
   }

  /**
   *  Communication Channel Object reading 
   *
   *    @return object readed
   */
   public Object readObject ()
   {
      Object fromClient = null;                            // objecto

      try
      { fromClient = in.readObject ();
      }
      catch (InvalidClassException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto lido não é passível de desserialização!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - erro na leitura de um objecto do canal de entrada do socket de comunicação!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (ClassNotFoundException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto lido corresponde a um tipo de dados desconhecido!");
        e.printStackTrace ();
        System.exit (1);
      }

      return fromClient;
   }

  /**
   *  Communication Channel object reading
   *
   *    @param toClient object to read
   */

   public void writeObject (Object toClient)
   {
      try
      { out.writeObject (toClient);
      }
      catch (InvalidClassException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto a ser escrito não é passível de serialização!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NotSerializableException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o objecto a ser escrito pertence a um tipo de dados não serializável!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - erro na escrita de um objecto do canal de saída do socket de comunicação!");
        e.printStackTrace ();
        System.exit (1);
      }
   }
}
