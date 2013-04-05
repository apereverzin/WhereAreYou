package com.creek.whereareyou.android.accountaccess;

import java.io.IOException;
import java.util.Properties;

import android.util.Log;

import static com.creek.accessemail.connector.mail.MailPropertiesStorage.MAIL_PASSWORD_PROPERTY;

import com.creek.whereareyou.ApplManager;
import com.creek.whereareyou.android.util.CryptoException;
import com.creek.whereareyou.android.util.CryptoUtil;

/**
 * 
 * @author Andrey Pereverzin
 */
public class MailAccountPropertiesProvider {
    private static final String TAG = MailAccountPropertiesProvider.class.getSimpleName();
    private static final String WHEREAREYOU_PROPERTIES_FILE_PATH = "/Android/whereareyou.properties";
    private static final String PASSWORD_ENCRYPTION_SEED = "tobeornottobe";
    private Properties mailProperties = null;

    public void persistMailProperties(Properties propsToPersist) throws IOException, CryptoException {
        String password = propsToPersist.getProperty(MAIL_PASSWORD_PROPERTY);
        String cryptPassword = CryptoUtil.encrypt(PASSWORD_ENCRYPTION_SEED, password);
        propsToPersist.setProperty(MAIL_PASSWORD_PROPERTY, cryptPassword);
        
        ApplManager.getInstance().getFileProvider().persistPropertiesToFile(WHEREAREYOU_PROPERTIES_FILE_PATH, propsToPersist);

        propsToPersist.setProperty(MAIL_PASSWORD_PROPERTY, password);
        mailProperties = propsToPersist;
    }

    public Properties getMailProperties() throws IOException, CryptoException {
        Log.i(TAG, "-----getMailProperties");
        if (mailProperties == null) {
            mailProperties = ApplManager.getInstance().getFileProvider().getPropertiesFromFile(WHEREAREYOU_PROPERTIES_FILE_PATH);
            if (mailProperties != null) {
                decryptPassword(mailProperties);
            }
        }
        return mailProperties;
    }
    
    private void decryptPassword(Properties props) throws CryptoException {
        String cryptPassword = props.getProperty(MAIL_PASSWORD_PROPERTY);
        String password = CryptoUtil.decrypt(PASSWORD_ENCRYPTION_SEED, cryptPassword);
        props.setProperty(MAIL_PASSWORD_PROPERTY, password);
    }
}
