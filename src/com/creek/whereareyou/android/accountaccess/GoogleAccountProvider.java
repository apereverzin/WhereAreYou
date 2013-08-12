package com.creek.whereareyou.android.accountaccess;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

/**
 * 
 * @author Andrey Pereverzin
 */
public final class GoogleAccountProvider {
    private static final String TAG = GoogleAccountProvider.class.getSimpleName();
    private static final String GOOGLE_ACCOUNT_PREFIX = "com.google";
    private static final GoogleAccountProvider instance = new GoogleAccountProvider();

    private GoogleAccountProvider() {
        //
    }
    
    public static final GoogleAccountProvider getInstance() {
        return instance;
    }
    
    public List<Account> getEmailAccount(Context context) {
        List<Account> googleAccounts = new ArrayList<Account>();
        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = manager.getAccounts();

        Log.d(TAG, "======================================");
        for (int i = 0; i < accounts.length; i++) {
            System.out.println("-----------");
            Log.d(TAG, accounts[i].name + ", " + accounts[i].type + ", " + accounts[i].toString());
            if (accounts[i].type.startsWith(GOOGLE_ACCOUNT_PREFIX)) {
                googleAccounts.add(accounts[i]);
            }
        }

        return googleAccounts;
    }
}
