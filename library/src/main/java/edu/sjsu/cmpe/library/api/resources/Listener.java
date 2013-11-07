package edu.sjsu.cmpe.library.api.resources;

import java.net.URL;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.fusesource.stomp.jms.StompJmsConnectionFactory;
import org.fusesource.stomp.jms.StompJmsDestination;
import org.fusesource.stomp.jms.message.StompJmsMessage;

import edu.sjsu.cmpe.library.config.LibraryServiceConfiguration;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Book.Status;
import edu.sjsu.cmpe.library.repository.BookRepositoryInterface;

public class Listener implements Runnable {

	LibraryServiceConfiguration configuration;
	BookRepositoryInterface bookRepository;
	public Listener(LibraryServiceConfiguration config, BookRepositoryInterface repository) {
		this.configuration = config;
		this.bookRepository = repository;
	}
	
	@Override
	public void run() {
		
		try {
		StompJmsConnectionFactory factory = new StompJmsConnectionFactory();

		factory.setBrokerURI("tcp://" + configuration.getApolloHost() + ":" + configuration.getApolloPort());

		Connection connection = factory.createConnection(configuration.getApolloUser(),configuration.getApolloPassword());
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		String destination = null;
	    destination = configuration.getStompTopicName();
		Destination dest = new StompJmsDestination(destination);
		MessageConsumer consumer = session.createConsumer(dest);
		
        System.currentTimeMillis();
        System.out.println("Waiting for messages...");
        
        while(true) {
            Message msg = consumer.receive();
            if( msg instanceof  TextMessage ) {
                String body = ((TextMessage) msg).getText();
                if( "SHUTDOWN".equals(body)) {
                    break;
                }
                System.out.println("Received message = " + body);

            } else if (msg instanceof StompJmsMessage) {
                StompJmsMessage smsg = ((StompJmsMessage) msg);
                String body = smsg.getFrame().contentAsString();
                if ("SHUTDOWN".equals(body)) {
                    break;
                }
                System.out.println("Received message = " + body);
                
                // Parse the body and add the book to the hash map. 
                
                //{isbn}:{title}:{category}:{coverimage}  
                //if this isbn is already present.
                String isbn = body.split(":")[0];
                String title = body.split(":")[1];
                String category = body.split(":")[2];
                String coverImage = body.split(":")[3];
                
                Book book = bookRepository.getBookByISBN(Long.parseLong(isbn));
                
                if(book == null) {
                	//Add this book to the hashmap
                   book = new Book();
                   book.setIsbn(Long.parseLong(isbn));
                   book.setStatus(Status.available);
                   book.setTitle(title);
                   book.setCategory(category);
                   book.setCoverimage(new URL(coverImage));
                   bookRepository.saveBook(book);
                	
                } else {
                   //Update the status to available
                	 if(book.getStatus().getValue().equalsIgnoreCase("lost")) {
                		 book.setStatus(Status.available);
                		 bookRepository.delete(Long.parseLong(isbn));
                		 bookRepository.saveBook(book);
                }
                }
                
                
                

            } else {
                System.out.println("Unexpected message type: "+msg.getClass());
            }
        }
        connection.close();
    

		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	
	
	
}
