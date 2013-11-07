package edu.sjsu.cmpe.procurement.MQLogic;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;

import edu.sjsu.cmpe.procurement.config.ProcurementServiceConfiguration;


/**
 * The listner listens to the queue and 
 * extracts messages. 
 * @author snehakulkarni
 *
 */
public class Listener {
	
	private final ProcurementServiceConfiguration configuration;
	
	    private String apolloUser;
	    private String apolloPassword;
	    private String apolloHost;
	    private String apolloPort;
	    private String stompTopicName;
	    private String stompQueueName;
	    private String stompTopicPrefix;
	
 public Listener(ProcurementServiceConfiguration configuration)
 {
	 this.configuration= configuration;
	 //Now set rest of the fields using the configuration object. 
 }
 
 /**
  * I could not find a way to pass the ProcurementServiceConfiguration 
  * object back to this Listener. Hence setting the required queue fields manually.
  */
 public Listener() {
	configuration = null;
	apolloUser = "admin";
	apolloPassword = "password";
	apolloHost = "54.215.210.214";
	apolloPort = "61613";
	stompTopicName = "";  // not used this anywhere.
	stompQueueName = "/queue/11536.book.orders";
	stompTopicPrefix = ""; // not used this anywhere.
 }

	public ArrayList<String> getBooksFromQueue() {
	 ArrayList<String> lostBooksArray = new ArrayList<String>();
		try {

			StompJmsConnectionFactory factory = new StompJmsConnectionFactory();

			factory.setBrokerURI("tcp://" + apolloHost + ":" + apolloPort);

			Connection connection = factory.createConnection(apolloUser,apolloPassword);
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			String destination = stompQueueName;
			Destination dest = new StompJmsDestination(destination);
			MessageConsumer consumer = session.createConsumer(dest);
			long waitUntil = 5000; //for 5 secs
	        try{
	        while(true) {
	            Message msg = consumer.receive(waitUntil);
	            if( msg instanceof  TextMessage ) {
	                String body = ((TextMessage) msg).getText();
	                System.out.println("Received message = " + body);
	                String bookIsbn = body.split(":")[1];
	                lostBooksArray.add(bookIsbn);
	                } else if(msg == null){ 
	                        System.out.println("No new messages. Existing due to timeout - " + waitUntil / 1000 + " sec");
	                        break;

	            } else {
	                System.out.println("Unexpected message type: "+msg.getClass());
	            }
	        }
	        }catch(NullPointerException e){
	                e.printStackTrace();
	        }
	        connection.close();
	        System.out.println("The connection closed");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lostBooksArray;
	}
}
