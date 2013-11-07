package edu.sjsu.cmpe.procurement.jobs;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;
import edu.sjsu.cmpe.procurement.MQLogic.Listener;
import edu.sjsu.cmpe.procurement.utils.HttpMethods;

/**
 * This job will run at every 5 minutes.
 */
@Every("30s") // NOTE: to be changed to 300sec
public class ProcurementSchedulerJob extends Job {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doJob() {
    	ArrayList<String> listOfLostBooks;
    	System.out.println("Procurement jobs was called.....");
    	//Call the listner and get all the books from the queue. 
    	Listener listen = new Listener();
    	listOfLostBooks = listen.getBooksFromQueue();
    	if(listOfLostBooks.size() == 0) 
    	{
    		return;
    	} else {
    		//Send the list of lost books to the publisher. 
    		
    		log.debug("Response from publisher: {}", HttpMethods.sentPost(listOfLostBooks));
    	}
    	
    	//Post the list of books to the Publisher. 
    	
	//String strResponse = ProcurementService.jerseyClient.resource(
		//"http://ip.jsontest.com/").get(String.class);
	//log.debug("Response from jsontest.com: {}", strResponse);
    }
}
