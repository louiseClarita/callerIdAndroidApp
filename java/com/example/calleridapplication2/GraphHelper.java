/*package com.example.calleridapplication2;

import android.util.Log;


import com.google.android.gms.common.api.Api;
import com.microsoft.graph.models.Attendee;
import com.microsoft.graph.models.Contact;
import com.microsoft.graph.models.DateTimeTimeZone;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.AttendeeType;
import com.microsoft.graph.models.Contact;
import com.microsoft.graph.models.BodyType;
import com.microsoft.graph.models.User;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.ContactCollectionPage;
import com.microsoft.graph.requests.ContactCollectionRequestBuilder;
import com.microsoft.graph.requests.EventCollectionPage;
import com.microsoft.graph.requests.EventCollectionRequestBuilder;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.common.internal.net.HttpClient;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.LinkedList;
import java.util.List;

// Singleton class - the app only needs a single instance
// of the Graph client
public class GraphHelper {

    private static GraphHelper INSTANCE = null;
    private GraphServiceClient mClient = null;

    IAuthenticationResult res;

    private GraphHelper() {

        AuthenticationHelper authProvider = AuthenticationHelper.getInstance();

        mClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider).buildClient();
    }

    public static synchronized GraphHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphHelper();
        }

        return INSTANCE;
    }

    public CompletableFuture<User> getUser() {
        // GET /me (logged in user)
        return mClient.me().buildRequest()
                .select("displayName,mail,mailboxSettings,userPrincipalName")
                .getAsync();
    }
}*/