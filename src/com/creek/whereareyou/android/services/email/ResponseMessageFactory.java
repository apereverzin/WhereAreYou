package com.creek.whereareyou.android.services.email;

import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.message.ResponseMessage;
import com.creek.whereareyoumodel.valueobject.OwnerRequestResponse;

/**
 * 
 * @author Andrey Pereverzin
 */
public class ResponseMessageFactory implements MessageFactory<OwnerRequestResponse> {
    @Override
    public AbstractMessage createMessage(OwnerRequestResponse payload, String senderEmail) {
        return new ResponseMessage(payload, senderEmail);
    }
}
