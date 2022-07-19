using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.WebJobs;
using Microsoft.Azure.WebJobs.Extensions.Http;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using Microsoft.IdentityModel.Clients.ActiveDirectory;
using System.Net.Http.Headers;
using System.Net.Http;
using System.Net;

namespace calleridFunction
{

    public static class D365
    {
        [FunctionName("ConnecttoD365")]
        public static async Task<IActionResult> Run(
            [HttpTrigger(AuthorizationLevel.Anonymous, "get", "post", Route = null)] HttpRequest req,
            ILogger log)
        {
            log.LogInformation("C# HTTP trigger function processed a request.");

            string contactEmail = req.Query["email"];
            string responseMessage = string.Empty;
            string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
            dynamic data = JsonConvert.DeserializeObject(requestBody);
            contactEmail = contactEmail ?? data?.email;
            #region Auth Code
            //
            string cloud = "https://login.microsoftonline.com";

            //This is the Domain!

            string tenantId = "c8cadb98-0b22-4524-b3e5-bc33f011a8fc";
            string authority = $"{cloud}/{tenantId}";

            // ApplicationID in the new UI

            string clientId = "faafb467-7ca0-4534-a088-c266b8f2b69f";

            //Created from scratch in Keys

            string clientsecret = "MP38Q~qCHXobSNDro1g4GLaztpUkS5rUslIPUaPP";

            ClientCredential clientcred = new ClientCredential(clientId, clientsecret);

            // Application ID of the Resource (could also be the Resource URI)

               string resource = "https://orgc452f0fa.crm4.dynamics.com/";
           // string resource = "https://org7667b684.crm4.dynamics.com/";
            AuthenticationContext ac = new AuthenticationContext(authority);
            AuthenticationResult result = null;
            var bearerToken = string.Empty;
            string ErrorMessege = string.Empty;
            try

            {
                //already having token
                result = await ac.AcquireTokenSilentAsync(resource, clientId);
                if (result != null)
                {
                    bearerToken = result.AccessToken;
                    log.LogInformation("Token Acquired:");
                }
            }

            catch (AdalException adalException)

            {//Acquire token
                if (adalException.ErrorCode == AdalError.FailedToAcquireTokenSilently

                || adalException.ErrorCode == AdalError.InteractionRequired)

                {

                    result = await ac.AcquireTokenAsync(resource, clientcred);
                    if (result != null)
                    {
                        bearerToken = result.AccessToken;
                        log.LogInformation("Token Acquired");

                    }
                }
                else
                {
                    log.LogWarning("Failed to acquire Bearer Token :-" + adalException.Message);
                    var AdalException = new { adalexception = "Failed to acquire Bearer Token :-" + adalException.Message };
                    return new BadRequestObjectResult(JsonConvert.SerializeObject(AdalException));
                    throw adalException;

                }

            }

            #endregion

            #region getContact



            string outputString = string.Empty;
            //Next use a HttpClient object to connect to specified CRM Web service.
            var httpClient = new HttpClient();
            //Define the Web API base address, the max period of execute time, the 
            // default OData version, and the default response payload format.
           
              httpClient.BaseAddress = new Uri("https://orgc452f0fa.crm4.dynamics.com/api/data/v9.1/");
           
           //  httpClient.BaseAddress = new Uri("https://org7667b684.crm4.dynamics.com/");

            httpClient.Timeout = new TimeSpan(0, 2, 0);
            httpClient.DefaultRequestHeaders.Add("OData-MaxVersion", "4.0");
            httpClient.DefaultRequestHeaders.Add("OData-Version", "4.0");
            httpClient.DefaultRequestHeaders.Add("Prefer", "odata.include-annotations=\"*\"");
            httpClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            httpClient.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", (bearerToken));

            //Query to retrieve contacts
            //  var queryOptions = "contacts?$filter=mobilephone eq '" + contactEmail + "'";

            //    var queryOptions="contacts?statuscode @OData.Community.Display.V1.FormattedValue  eq 'Active'";
            var queryOptions = "contacts?";
            HttpResponseMessage retrieveResponse1 = httpClient.GetAsync(queryOptions).Result;


            if (retrieveResponse1.StatusCode == HttpStatusCode.OK)
            {




                responseMessage = retrieveResponse1.Content.ReadAsStringAsync().Result.ToString();


            }
            else
            {

                return new BadRequestObjectResult(JsonConvert.SerializeObject("an error occured while retriving contacts"));
            }



            #endregion

            if (string.IsNullOrEmpty(contactEmail) && responseMessage == String.Empty)
            {
                responseMessage = "Pass an email in the query string or in the request body";
            }

            return new OkObjectResult(responseMessage);
        }
    }
}
    

