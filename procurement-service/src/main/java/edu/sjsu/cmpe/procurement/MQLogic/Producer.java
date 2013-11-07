package edu.sjsu.cmpe.procurement.MQLogic;



import javax.jms.*;

import org.fusesource.stomp.jms.*;

public class Producer {
	
	    private  String apolloUser;
	    private  String apolloPassword;
	    private  String apolloHost;
	    private  String apolloPort;
	    private  String stompTopicName;
	    private  String stompQueueName;
	    private  String stompTopicPrefix;
	    
	    public Producer() {
	    	
	    	apolloUser = "admin";
	    	apolloPassword = "password";
	    	apolloHost = "54.215.210.214";
	    	apolloPort = "61613";
	    	stompTopicName = "";  
	    	stompQueueName = "/queue/11536.book.orders"; //not using this here.
	    	stompTopicPrefix = "/topic/11536.book."; 
	    	
	     }


	public  void putMessage(String category, String message) {
		try {

			StompJmsConnectionFactory factory = new StompJmsConnectionFactory();

			factory.setBrokerURI("tcp://"+ apolloHost + ":" + apolloPort);

			Connection connection = factory.createConnection(apolloUser,apolloPassword);
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			String destination = stompTopicPrefix+category;
			
			Destination dest = new StompJmsDestination(destination);
			MessageProducer producer = session.createProducer(dest);
			//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			TextMessage msg = session.createTextMessage(message);
			producer.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
