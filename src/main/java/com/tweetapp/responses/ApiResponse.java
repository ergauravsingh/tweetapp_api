package com.tweetapp.responses;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ApiResponse {

    private Map<String, Object> body = new LinkedHashMap<>();
    
    public ApiResponse()
    {
    	body.put("timestamp", null);
    	body.put("message", null);
    	body.put("data", null);
    	body.put("error", null);    }
    
    public void setMessage(String message)
    {
    	body.put("message", message);	
    }
    
    public void setData(Object data)
    {
    	body.put("data", data);
    }
    
    public void setError(String error)
    {
    	body.put("error", error);
    }    
    
    public Map<String,Object> getBodyResponse()
    {
    	body.put("timestamp", LocalDateTime.now());
    	return body;
    }
        
}
