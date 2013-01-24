package com.creek.whereareyou.android.accountaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.os.Environment;
import android.util.Log;

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

    public static final String MAIL_PASSWORD_PROPERTY = "mail.password";
    
    public void persistProperties(Properties propsToPersist) throws IOException, CryptoException {
        File f = new File(getPropertiesFilePath());
        f.createNewFile();
        String password = propsToPersist.getProperty(MAIL_PASSWORD_PROPERTY);
        String cryptPassword = CryptoUtil.encrypt(PASSWORD_ENCRYPTION_SEED, password);
        propsToPersist.setProperty(MAIL_PASSWORD_PROPERTY, cryptPassword);
        propsToPersist.store(new FileOutputStream(f), "");
        propsToPersist.setProperty(MAIL_PASSWORD_PROPERTY, password);
        mailProperties = propsToPersist;
    }

    public Properties getMailProperties() throws IOException, CryptoException {
        Log.i(TAG, "-----getMailProperties");
        if (mailProperties == null) {
            File f = new File(getPropertiesFilePath());
            if (f.exists()) {
                mailProperties = new Properties();
                mailProperties.load(new FileInputStream(f));
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
    
    private String getPropertiesFilePath() {
        return Environment.getExternalStorageDirectory().getPath() + WHEREAREYOU_PROPERTIES_FILE_PATH;
    }
}
