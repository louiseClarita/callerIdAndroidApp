/*package com.example.calleridapplication;

import android.util.Log;

import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.List;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.MessageCollectionPage;
import com.microsoft.graph.requests.extensions.MessageCollectionRequestBuilder;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

public class GraphHelper {

    private static GraphHelper INSTANCE = null;
    private GraphServiceClient mClient = null;


    private GraphHelper() {

     //   AuthenticationHelper authProvider = AuthenticationHelper.getInstance();

     //   mClient =signin_fragment.graphClient;

    }

    public static synchronized GraphHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GraphHelper();
        }

        return INSTANCE;
    }


    public CompletableFuture<List<Message>> getMessageView() {
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



    }

 */
