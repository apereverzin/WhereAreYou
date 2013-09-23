package com.creek.whereareyou.android.activity.account;

/**
 * 
 * @author Andrey Pereverzin
 */
public enum CheckMode {
    SMTP(RequestCodes.SMTP_REQUEST_CODE, true, false, false), 
    POP3(RequestCodes.POP3_REQUEST_CODE, false, true, false), 
    IMAP(RequestCodes.IMAP_REQUEST_CODE, false, false, true), 
    SMTP_AND_POP3(RequestCodes.SMTP_AND_POP3_REQUEST_CODE, true, true, false), 
    SMTP_AND_IMAP(RequestCodes.SMTP_AND_IMAP_REQUEST_CODE, true, false, true);
    
    private final int requestCode;
    private final boolean hasSmtp;
    private final boolean hasPop3;
    private final boolean hasImap;
    
    private CheckMode(int _requestCode, boolean _hasSmtp, boolean _hasPop3, boolean _hasImap) {
        this.requestCode = _requestCode;
        this.hasSmtp = _hasSmtp;
        this.hasPop3 = _hasPop3;
        this.hasImap = _hasImap;
    }
    
    public int getRequestCode() {
        return requestCode;
    }

    public boolean hasSmtp() {
        return hasSmtp;
    }

    public boolean hasPop3() {
        return hasPop3;
    }

    public boolean hasImap() {
        return hasImap;
    }
    
    public CheckMode byRequestCode(int requestCode) {
        for (CheckMode checkMode: values()) {
            if (checkMode.getRequestCode() == requestCode) {
                return checkMode;
            }
        }
        
        return null;
    }
    
    interface RequestCodes {
        public static final int SMTP_REQUEST_CODE = 0;
        public static final int POP3_REQUEST_CODE = 1;
        public static final int IMAP_REQUEST_CODE = 2;
        public static final int SMTP_AND_POP3_REQUEST_CODE = 3;
        public static final int SMTP_AND_IMAP_REQUEST_CODE = 4;

    }
}
