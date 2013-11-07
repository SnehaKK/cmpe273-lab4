package edu.sjsu.cmpe.library.api.resources;


import javax.jms.*;

import org.fusesource.stomp.jms.*;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
//rename it to producer
public class Producer {
	private final LibraryServiceConfiguration configuration;
 public Producer(LibraryServiceConfiguration configuration)
 {
	 this.configuration= configuration;
	 
 }

	public void putMessage(String isbn) {
		try {

			StompJmsConnectionFactory factory = new StompJmsConnectionFactory();

			factory.setBrokerURI("tcp://"+ configuration.getApolloHost() + ":" + configuration.getApolloPort());

			Connection connection = factory.createConnection(configuration.getApolloUser(),
					configuration.getApolloPassword());
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			String destination = "/queue/11536.book.orders";
			Destination dest = new StompJmsDestination(destination);
			MessageProducer producer = session.createProducer(dest);
			//producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			TextMessage msg = session.createTextMessage(configuration.getLibraryName()+ ": "+isbn);
			msg.setIntProperty("id", 1);
			producer.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
