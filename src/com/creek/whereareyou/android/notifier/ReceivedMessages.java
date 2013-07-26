package com.creek.whereareyou.android.notifier;

import java.util.ArrayList;
import java.util.List;

import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ReceivedMessages {
    private List<RequestMessage> requests = new ArrayList<RequestMessage>();
    private List<ResponseMessage> normalResponses = new ArrayList<ResponseMessage>();
    private List<OwnerLocationDataMessage> locationResponses = new ArrayList<OwnerLocationDataMessage>();
    private boolean hasMessages = false;

    public int getRequestsCount() {
        return requests.size();
    }
    
    public void addRequest(RequestMessage requestMessage) {
        hasMessages = true;
        requests.add(requestMessage);
    }

    public List<RequestMessage> getRequests() {
        return requests;
    }

    public int getNormalResponsesCount() {
        hasMessages = true;
        return normalResponses.size();
    }

    public void addNormalResponse(ResponseMessage responseMessage) {
        hasMessages = true;
        normalResponses.add(responseMessage);
    }

    public List<ResponseMessage> getNormalResponses() {
        return normalResponses;
    }

    public int getLocationResponsesCount() {
        return locationResponses.size();
    }

    public void addLocationResponse(OwnerLocationDataMessage locationResponse) {
        hasMessages = true;
        locationResponses.add(locationResponse);
    }

    public List<OwnerLocationDataMessage> getLocationResponses() {
        return locationResponses;
    }

    public boolean hasMessages() {
        return hasMessages;
    }
}
