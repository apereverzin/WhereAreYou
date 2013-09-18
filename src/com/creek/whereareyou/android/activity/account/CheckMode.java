package com.creek.whereareyou.android.activity.account;

/**
 * 
 * @author Andrey Pereverzin
 */
public enum CheckMode {
    SMTP(true, false, false), POP3(false, true, false), IMAP(false, false, true), SMTP_AND_POP3(true, true, false), SMTP_AND_IMAP(true, false, true);
    
    private final boolean hasSmtp;
    private final boolean hasPop3;
    private final boolean hasImap;
    
    private CheckMode(boolean hasSmtp, boolean hasPop3, boolean hasImap) {
        this.hasSmtp = hasSmtp;
        this.hasPop3 = hasPop3;
        this.hasImap = hasImap;
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
}
