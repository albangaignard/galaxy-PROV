/* 
 * The MIT License
 *
 * Copyright 2016 Alban Gaignard <alban.gaignard@univ-nantes.fr>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class GalaxyApiTest {

    private static Logger logger = Logger.getLogger(GalaxyApiTest.class);

    public GalaxyApiTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static String gURL = "https://galaxy-bird.univ-nantes.fr/galaxy/";
    private static String gApiKey = "dd3b7fce727d53ac00512ea19a8f5d4f";

    public void jsonPP(String json) {
//        JsonArray o = new JsonParser().parse(json).getAsJsonArray();
//        JsonObject o = new JsonParser().parse(json).getAsJsonObject();

        Gson gson = new Gson();
        Object ob = gson.fromJson(json, Object.class);
        logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(ob));
    }

    @Test
    public void galaxyRestConnect() throws URISyntaxException, MalformedURLException, IOException, JSONException {
        //http://galaxy.readthedocs.io/en/master/lib/galaxy.webapps.galaxy.api.html#module-galaxy.webapps.galaxy.api.histories

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        WebResource service = client.resource(new URI(gURL));

//        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
//        params.add("key", gKey);
        ClientResponse responseHist = service.path("/api/histories").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
        System.out.println("----------");
        String r = responseHist.getEntity(String.class);
        jsonPP(r);
        System.out.println("----------");

//        /api/tools
        ClientResponse responseTools = service.path("/api/tools").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
        System.out.println("----------");
        r = responseTools.getEntity(String.class);
//        jsonPP(r);

        JsonArray jarray = new JsonParser().parse(r).getAsJsonArray();
        for (JsonElement e : jarray) {
            JsonArray toolsArray = e.getAsJsonObject().get("elems").getAsJsonArray();
            for (JsonElement t : toolsArray) {
                if (t.getAsJsonObject().get("name") != null) {
                    System.out.println("\t\t\t" + t.getAsJsonObject().get("name").getAsString());
                }
            }
        }
        System.out.println("----------");

//        ClientResponse responseJob = service.path("/api/jobs/2773e5acaa7ffd7f").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
//        System.out.println("----------");
//        r = responseJob.getEntity(String.class);
//        jsonPP(r);
//        System.out.println("----------");
//
//        ClientResponse responseJobIn = service.path("/api/jobs/2773e5acaa7ffd7f/inputs").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
//        System.out.println("----------");
//        r = responseJobIn.getEntity(String.class);
//        jsonPP(r);
//        System.out.println("----------");
//
//        ClientResponse responseJobOut = service.path("/api/jobs/2773e5acaa7ffd7f/outputs").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
//        System.out.println("----------");
//        r = responseJobOut.getEntity(String.class);
//        jsonPP(r);
//        System.out.println("----------");
//
//        ClientResponse responseDS = service.path("/api/datasets/694ed270b1759660").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
//        System.out.println("----------");
//        r = responseDS.getEntity(String.class);
//        jsonPP(r);
//        System.out.println("----------");
    }

    @Test
    public void listHistories() throws URISyntaxException {

        String gURL = "https://galaxy-bird.univ-nantes.fr/galaxy/";
        String gKey = "dd3b7fce727d53ac00512ea19a8f5d4f";

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        WebResource service = client.resource(new URI(gURL));

        ClientResponse responseHist = service.path("/api/histories").queryParam("key", gKey).accept("application/json").type("application/json").get(ClientResponse.class);
        String rH = responseHist.getEntity(String.class);
        JsonArray arrayHist = new JsonParser().parse(rH).getAsJsonArray();

        // Navigate though list of histories
        for (JsonElement eH : arrayHist) {
            String nameH = eH.getAsJsonObject().get("name").getAsString();
            String idH = eH.getAsJsonObject().get("id").getAsString();

            ClientResponse responseHistContent = service.path("/api/histories/" + idH + "/contents").queryParam("key", gKey).accept("application/json").type("application/json").get(ClientResponse.class);
            String rC = responseHistContent.getEntity(String.class);
//            jsonPP(rC);
            JsonArray arrayHistContent = new JsonParser().parse(rC).getAsJsonArray();

            // Navigate though datasets for a given history
            for (JsonElement eC : arrayHistContent) {
                String idC = eH.getAsJsonObject().get("id").getAsString();
                String nameC = eH.getAsJsonObject().get("name").getAsString();

                ClientResponse responseHistContentProv = service.path("/api/histories/" + idH + "/contents/" + idC + "/provenance").queryParam("key", gKey).accept("application/json").type("application/json").get(ClientResponse.class);
                String rCProv = responseHistContentProv.getEntity(String.class);
                //jsonPP(rCProv);
                JsonObject prov = new JsonParser().parse(rCProv).getAsJsonObject();
                String jobId = prov.get("job_id").getAsString();
                String toolId = prov.get("tool_id").getAsString();

                //JobDetails
                ClientResponse responseJob = service.path("/api/jobs/2773e5acaa7ffd7f").queryParam("key", gKey).accept("application/json").type("application/json").get(ClientResponse.class);
                String rJob = responseJob.getEntity(String.class);
                JsonObject job = new JsonParser().parse(rJob).getAsJsonObject();
                String start = job.get("create_time").getAsString();
                String stop = job.get("update_time").getAsString();
                System.out.println("\t\tSTART=" + start + " | STOP=" + stop);

                JsonObject params = prov.get("parameters").getAsJsonObject();

                for (Entry e : params.entrySet()) {
                    System.out.println("\t\t" + e.getKey().toString() + " | " + e.getValue().toString());
                }
                System.out.println("");
            }

            System.out.println("");
        }
    }
}
