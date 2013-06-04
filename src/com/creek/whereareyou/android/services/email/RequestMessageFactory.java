package com.creek.whereareyou.android.services.email;

import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.message.RequestMessage;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;

/**
 * 
 * @author andreypereverzin
 */
public class RequestMessageFactory implements MessageFactory<OwnerRequestResponse> {
    @Override
    public AbstractMessage createMessage(OwnerRequestResponse payload, String senderEmail) {
        return new RequestMessage(payload, senderEmail);
    }
}
