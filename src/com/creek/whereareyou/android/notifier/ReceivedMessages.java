package com.creek.whereareyou.android.notifier;

import java.util.ArrayList;

import com.creek.whereareyoumodel.message.OwnerLocationDataMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ReceivedMessages {
    private ArrayList<RequestMessage> requests = new ArrayList<RequestMessage>();
    private ArrayList<ResponseMessage> normalResponses = new ArrayList<ResponseMessage>();
    private ArrayList<OwnerLocationDataMessage> locationResponses = new ArrayList<OwnerLocationDataMessage>();
    private boolean hasMessages = false;

    public int getRequestsCount() {
        return requests.size();
    }
    
    public void addRequest(RequestMessage requestMessage) {
        hasMessages = true;
        requests.add(requestMessage);
    }

    public ArrayList<RequestMessage> getRequests() {
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

    public ArrayList<ResponseMessage> getNormalResponses() {
        return normalResponses;
    }

    public int getLocationResponsesCount() {
        return locationResponses.size();
    }

    public void addLocationResponse(OwnerLocationDataMessage locationResponse) {
        hasMessages = true;
        locationResponses.add(locationResponse);
    }

    public ArrayList<OwnerLocationDataMessage> getLocationResponses() {
        return locationResponses;
    }

    public boolean hasMessages() {
        return hasMessages;
    }
}
