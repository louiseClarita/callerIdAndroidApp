// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

// <AuthHelperSnippet>
/*
package com.example.calleridapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.CallerIdApplication.R;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

// Singleton class - the app only needs a single instance
// of PublicClientApplication
@RequiresApi(api = Build.VERSION_CODES.N)
public class AuthenticationHelper implements IAuthenticationProvider {
    private static AuthenticationHelper INSTANCE = null;
    private ISingleAccountPublicClientApplication mPCA = null;
    private String[] mScopes = { "User.Read","Contacts.Read" };

    private AuthenticationHelper(Context ctx, final IAuthenticationHelperCreatedListener listener) {
        PublicClientApplication.createSingleAccountPublicClientApplication(ctx, R.raw.auth_config_single_account,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        mPCA = application;
                        listener.onCreated(INSTANCE);
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.e("AUTHHELPER", "Error creating MSAL application", exception);
                        listener.onError(exception);
                    }
                });
    }


    public static synchronized CompletableFuture<AuthenticationHelper> getInstance(Context ctx) {

        if (INSTANCE == null) {
            CompletableFuture<AuthenticationHelper> future = new CompletableFuture<>();
            INSTANCE = new AuthenticationHelper(ctx, new IAuthenticationHelperCreatedListener() {
                @Override
                public void onCreated(AuthenticationHelper authHelper) {
                    future.complete(authHelper);
                }

                @Override
                public void onError(MsalException exception) {
                    future.completeExceptionally(exception);
                }
            });

            return future;
        } else {
            return CompletableFuture.completedFuture(INSTANCE);
        }
    }

    // Version called from fragments. Does not create an
    // instance if one doesn't exist
    public static synchronized AuthenticationHelper getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException(
                    "AuthenticationHelper has not been initialized from MainActivity");
        }

        return INSTANCE;
    }

    public CompletableFuture<IAuthenticationResult> acquireTokenInteractively(Activity activity) {
        CompletableFuture<IAuthenticationResult> future = new CompletableFuture<>();
        mPCA.signIn(activity, null, mScopes, getAuthenticationCallback(future));

        return future;
    }

    public CompletableFuture<IAuthenticationResult> acquireTokenSilently() {
        // Get the authority from MSAL config
        String authority = mPCA.getConfiguration()
                .getDefaultAuthority().getAuthorityURL().toString();

        CompletableFuture<IAuthenticationResult> future = new CompletableFuture<>();
        mPCA.acquireTokenSilentAsync(mScopes, authority, getAuthenticationCallback(future));
        return future;
    }

    public void signOut() {
        mPCA.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                Log.d("AUTHHELPER", "Signed out");
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                Log.d("AUTHHELPER", "MSAL error signing out", exception);
            }
        });
    }

    private AuthenticationCallback getAuthenticationCallback(
            CompletableFuture<IAuthenticationResult> future) {
        return new AuthenticationCallback() {
            @Override
            public void onCancel() {
                future.cancel(true);
            }

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                future.complete(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                future.completeExceptionally(exception);
            }
        };
    }




    @Override
    public void authenticateRequest(IHttpRequest request) {
        acquireTokenSilently();
    }
}


 */