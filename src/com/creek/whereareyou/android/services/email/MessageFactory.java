package com.creek.whereareyou.android.services.email;

import com.creek.whereareyoumodel.message.AbstractMessage;
import com.creek.whereareyoumodel.valueobject.AbstractOwnerData;

/**
 * 
 * @author andreypereverzin
 */
public interface MessageFactory<T extends AbstractOwnerData> {
    AbstractMessage createMessage(T payload, String senderEmail);
}
