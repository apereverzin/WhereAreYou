package com.creek.whereareyou.android.services.email;

import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.valueobject.AbstractSendableData;

/**
 * 
 * @author Andrey Pereverzin
 */
public interface MessageFactory<T extends AbstractSendableData> {
    AbstractMessage createMessage(T payload, String senderEmail);
}
