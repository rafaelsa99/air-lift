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
 * 
 * @author Rafael Sá (104552), José Brás (74029)
 */
public class StubCom {
  /**
   *  Communication Socket
   *    @serialField commSocket
   */

   private Socket commSocket = null;

  /**
   *  Name of machine where server is located
   *  @serialField serverHostName
   */

   private String serverHostName = null;

  /**
   *  Listening port number
   *  @serialField serverPortNumb
   */

   private int serverPortNumb;

  /**
   * Input stream communication Channel
   *  @serialField ObjectInputStream
   */

   private ObjectInputStream in = null;

  /**
   *  Output stream communication Channel
   *  @serialField ObjectOutputStream
   */

   private ObjectOutputStream out = null;

  /**
   *  Instantiation of Communication channel
   *
   *    @param hostName Name of machine where server is located
   *    @param portNumb Listening port number
   */

   public StubCom (String hostName, int portNumb)
   {
      serverHostName = hostName;
      serverPortNumb = portNumb;
   }

  /**
   *  Communication Channel opening
   *  instantiation of communication socket and association with target server address
   *  Input stream and output socket opening
   *
   *    @return true, if channel is open; <br>
   *            false, otherwise.
   */

   public boolean open ()
   {
      boolean success = true;
      SocketAddress serverAddress = new InetSocketAddress (serverHostName, serverPortNumb);

      try
      { commSocket = new Socket();
        commSocket.connect (serverAddress);
      }
      catch (UnknownHostException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o nome do sistema computacional onde reside o servidor é desconhecido: " +
                                 serverHostName + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (NoRouteToHostException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o nome do sistema computacional onde reside o servidor é inatingível: " +
                                 serverHostName + "!");
        e.printStackTrace ();
        System.exit (1);
      }
      catch (ConnectException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - o servidor não responde em: " + serverHostName + "." + serverPortNumb + "!");
        if (e.getMessage ().equals ("Connection refused"))
           success = false;
           else { System.out.println (e.getMessage () + "!");
                  e.printStackTrace ();
                  System.exit (1);
                }
      }
      catch (SocketTimeoutException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um time out no estabelecimento da ligação a: " +
                                 serverHostName + "." + serverPortNumb + "!");
        success = false;
      }
      catch (IOException e)                          
      { System.out.println (Thread.currentThread ().getName () +
                                 " - ocorreu um erro indeterminado no estabelecimento da ligação a: " +
                                 serverHostName + "." + serverPortNumb + "!");
        e.printStackTrace ();
        System.exit (1);
      }

      if (!success) return (success);

      try
      { out = new ObjectOutputStream (commSocket.getOutputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível abrir o canal de saída do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      try
      { in = new ObjectInputStream (commSocket.getInputStream ());
      }
      catch (IOException e)
      { System.out.println (Thread.currentThread ().getName () +
                                 " - não foi possível abrir o canal de entrada do socket!");
        e.printStackTrace ();
        System.exit (1);
      }

      return (success);
   }

  /**
   *  Communication channel closure
   *  Input stream and output socket closure
   *  Communication socket closure
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
   *  Communication channel Object reading
   *
   *    @return readed object
   */

   public Object readObject ()
   {
      Object fromServer = null;                            

      try
      { fromServer = in.readObject ();
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

      return fromServer;
   }

  /**
   *  Communication channel object writing
   *
   *    @param toServer object to be written
   */

   public void writeObject (Object toServer)
   {
      try
      { out.writeObject (toServer);
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