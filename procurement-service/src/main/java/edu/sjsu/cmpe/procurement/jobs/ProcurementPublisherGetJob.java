package edu.sjsu.cmpe.procurement.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.Every;
import edu.sjsu.cmpe.procurement.MQLogic.Producer;
import edu.sjsu.cmpe.procurement.utils.HttpMethods;

import org.eclipse.jetty.util.ajax.JSONObjectConvertor;
import org.json.*;


@Every("35s")  //NOTE: To be changed back to 350s ;Executes every 5minutes50secs
public class ProcurementPublisherGetJob extends Job {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doJob() {
    	System.out.println("ProcurementPublisher jobs was called.....");
    	String publisherResponse = HttpMethods.sendGet();
    	JSONObject jobj = new JSONObject(publisherResponse);
    	JSONArray jarr = jobj.getJSONArray("shipped_books");
    
    	// put these items into the queue. 
    	for(int i=0; i<jarr.length(); i++) {
    		JSONObject temp = (JSONObject) jarr.get(i);
    		String message = temp.getDouble("isbn")+":"+temp.getString("title")+":"+temp.getString("category")+":"+temp.getString("coverimage");
    		Producer prod = new Producer();
    		prod.putMessage(temp.getString("category"), message);
    	}
    	  
    	

}
}
