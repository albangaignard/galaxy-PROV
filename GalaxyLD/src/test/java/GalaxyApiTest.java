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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import fr.univnantes.galaxyld.GalaxyProvenanceException;
import fr.univnantes.galaxyld.Util;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class GalaxyApiTest {

    private static Logger logger = Logger.getLogger(GalaxyApiTest.class);
    private static String gURL = null;
    private static String gApiKey = null;

    public GalaxyApiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws GalaxyProvenanceException {
        GalaxyApiTest.gURL = Util.getProperty("URL");
        GalaxyApiTest.gApiKey = Util.getProperty("API-key");
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
        Util.jsonPP(r);
        System.out.println("----------");

//        /api/tools
        ClientResponse responseTools = service.path("/api/tools").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
        System.out.println("----------");
        r = responseTools.getEntity(String.class);
        Util.jsonPP(r);
    }

    @Test
    public void listHistories() throws URISyntaxException {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        WebResource service = client.resource(new URI(gURL));

        ClientResponse responseHist = service.path("/api/histories").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
        String rH = responseHist.getEntity(String.class);
        JsonArray arrayHist = new JsonParser().parse(rH).getAsJsonArray();

        // Navigate though list of histories
        for (JsonElement eH : arrayHist) {
            String nameH = eH.getAsJsonObject().get("name").getAsString();
            String idH = eH.getAsJsonObject().get("id").getAsString();

            ClientResponse responseHistContent = service.path("/api/histories/" + idH + "/contents").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
            String rC = responseHistContent.getEntity(String.class);
//            jsonPP(rC);
            JsonArray arrayHistContent = new JsonParser().parse(rC).getAsJsonArray();

//            // Navigate though datasets for a given history
//            for (JsonElement eC : arrayHistContent) {
//                String idC = eH.getAsJsonObject().get("id").getAsString();
//                String nameC = eH.getAsJsonObject().get("name").getAsString();
//
//                ClientResponse responseHistContentProv = service.path("/api/histories/" + idH + "/contents/" + idC + "/provenance").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
//                String rCProv = responseHistContentProv.getEntity(String.class);
//                //jsonPP(rCProv);
//                JsonObject prov = new JsonParser().parse(rCProv).getAsJsonObject();
//                String jobId = prov.get("job_id").getAsString();
//                String toolId = prov.get("tool_id").getAsString();
//
//                //JobDetails
//                ClientResponse responseJob = service.path("/api/jobs/2773e5acaa7ffd7f").queryParam("key", gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
//                String rJob = responseJob.getEntity(String.class);
//                JsonObject job = new JsonParser().parse(rJob).getAsJsonObject();
//                String start = job.get("create_time").getAsString();
//                String stop = job.get("update_time").getAsString();
//                System.out.println("\t\tSTART=" + start + " | STOP=" + stop);
//
//                JsonObject params = prov.get("parameters").getAsJsonObject();
//
//                for (Entry e : params.entrySet()) {
//                    System.out.println("\t\t" + e.getKey().toString() + " | " + e.getValue().toString());
//                }
//                System.out.println("");
//            }

//            System.out.println("");
        }
    }
}
