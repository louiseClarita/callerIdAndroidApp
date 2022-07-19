package com.example.calleridapplication;
/*
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
/*import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.List;
import com.microsoft.graph.models.extensions.Message;


import com.microsoft.graph.models.extensions.Drive;

import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
/*
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.MessageCollectionResponse;


import com.microsoft.graph.requests.extensions.IMessageCollectionPage;
import com.microsoft.graph.requests.extensions.MessageCollectionResponse;
/*
import com.microsoft.graph.requests.extensions.GraphServiceClient;
 */
import com.microsoft.graph.requests.extensions.MessageCollectionPage;
import com.microsoft.graph.requests.extensions.MessageCollectionRequestBuilder;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
/*
public class GraphHelper {

    private static GraphHelper INSTANCE = null;
    private IGraphServiceClient mClient = null;


    private GraphHelper() {

        AuthenticationHelper authProvider = AuthenticationHelper.getInstance();

        mClient =signin_fragment.graphClient;

    }

    public static synchronized GraphHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphHelper();
        }

        return INSTANCE;
    }
/*
    public void getUser() {
        // GET /me (logged in user)
        String Message;
        return mClient.me()
                .buildRequest()
                .get(new ICallback<Drive>() {
                    @Override
                    public void success(final Drive drive) {
                        Log.d("TAG", "Found Drive " + drive.id);

                        Log.d("TAG JSON FILE",drive.getRawObject().toString());

                        first.isSignedIn=true;
                        return drive.getRawObject().toString();

                    }

                    @Override
                    public void failure(ClientException ex) {
                        return ex.toString();
                    }
                });
    }

    private void getRecentEmails(String email) {
        final List<Option> options = new LinkedList<>();
        // options.add(new QueryOption("search",
        //3.3     "mobilePhone:\""+number+"\""));

        // Start and end times adjusted to user's time zone

        options.add(new QueryOption("filter",
                "(from/emailAddress/address) eq '"+email+"'"));
        mClient
                .me()
                .messages()
                .buildRequest(options)
                .top(2)
                .get(new ICallback<IMessageCollectionPage>() {

                    @Override
                    public void success(IMessageCollectionPage iMessageCollectionPage) {
                        Log.d("response",iMessageCollectionPage.getRawObject().toString());

                        displayEmail(iMessageCollectionPage.getRawObject());

                    }

                    @Override
                    public void failure(ClientException ex) {
                        Log.d("error",ex.getMessage().toString());
                    }
                });





    }
    private List<EmailModel> displayEmail(JsonObject rawObject) {

        List<EmailModel> Emails=null;
        EmailModel emailsss = null;
     //   EmailSubjects = new String[]{};
        JsonArray emails = rawObject.getAsJsonArray("value");

        if(emails.size()==0){
            Emails = new ArrayList<>();
            emailsss = new EmailModel("no emails","");
            Emails.add(emailsss);
            return Emails;
        }
        for(int i =0 ;i<emails.size();i++){
            Emails = new ArrayList<>();
            JsonObject dataObject = (JsonObject) emails.get(i);

          String  subject = dataObject.get("subject").toString();
        /*dateFormat= new Date(Long.valueOf(callDate));"
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));*/
   //     String    time = dataObject.get("sentDateTime").toString().replaceAll("\"","").trim();

    /*  Date date = new Date(Long.valueOf(time));
              SimpleDateFormat formatter = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            String dateString = formatter.format(new Date(Long
                    .parseLong(String.valueOf(date))));
            Log.d("unmodified","date: "+time);
         Log.d("time",dateString);

            emailsss = new EmailModel(subject,time);
            Emails.add(emailsss);
        }
        return Emails;

    }
/*
    public CompletableFuture<List<>> getMessageView() {
     //   LinkedList<Option> requestOptions = new LinkedList<Option>();
     //   requestOptions.add(new QueryOption("sender", "charbel.mattar@javista.com"));
        final LinkedList<Option> requestOptions = new LinkedList<Option>();
        // options.add(new QueryOption("search",
        //3.3     "mobilePhone:\""+number+"\""));

        // Start and end times adjusted to user's time zone
       // requestOptions.add(new HeaderOption("ConsistencyLevel",
       //         "eventual"));

        final LinkedList<Message> allEvents = new LinkedList<Message>();
        // Create a separate list of options for the paging requests
        // paging request should not include the query parameters from the initial
        // request, but should include the headers.
        final LinkedList<Option> pagingOptions = new LinkedList<Option>();
        // options.add(new HeaderOption("search",
        //         "mobilePhone:\"" + number + "\""));

        return mClient.me().contacts()
                .buildRequest().get()
                .getAsync()
                .thenCompose(eventPage -> processPage2(eventPage, allEvents, pagingOptions));

    }

        private CompletableFuture<List<Message>> processPage2(MessageCollectionPage currentPage,
                                                              LinkedList<Message> eventList,
                                                              LinkedList<Option> options) {
            eventList.addAll(currentPage.getCurrentPage());
            Log.d("TAGGRAPHHELPER",currentPage.toString());
            // Check if there is another page of results
            MessageCollectionRequestBuilder nextPage = currentPage.getNextPage();
            if (nextPage != null) {
                // Request the next page and repeat
                return nextPage.buildRequest(options)
                        .getAsync()
                        .thenCompose(eventPage -> processPage2(eventPage, eventList, options));
            } else {
                // No more pages, complete the future
                // with the complete list
                return CompletableFuture.completedFuture(eventList);
            }
        }
*/


   // }


