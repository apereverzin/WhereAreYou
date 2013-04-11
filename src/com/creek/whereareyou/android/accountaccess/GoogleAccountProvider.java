package com.creek.whereareyou.android.accountaccess;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public class GoogleAccountProvider {
    private static final String TAG = GoogleAccountProvider.class.getSimpleName();
    private static final String GOOGLE_ACCOUNT_PREFIX = "com.google";
    private Account emailAccount = null;

    public Account getEmailAccount(Context context) {
        if (!isEmailAccountAlreadyFound()) {
            emailAccount = findEmailAccount(context);
        }

        return emailAccount;
    }

    private boolean isEmailAccountAlreadyFound() {
        return emailAccount != null;
    }

    private Account findEmailAccount(Context context) {
        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = manager.getAccounts();

        Log.d(TAG, "======================================");
        for (Account account : accounts) {
            Log.d(TAG, "-----------");
            Log.d(TAG, account.name);
            Log.d(TAG, account.type);
            Log.d(TAG, account.toString());
            if (account.type.startsWith(GOOGLE_ACCOUNT_PREFIX)) {
                return account;
            }
        }

        return null;
    }
}
