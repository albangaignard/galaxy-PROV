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
package fr.univnantes.galaxyld;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class GHistAPI {

    private static Logger logger = Logger.getLogger(GHistAPI.class);
    private static String gURL;
    private static String gApiKey;
    private static WebResource service;

    public GHistAPI(String gURL, String gApiKey) {
        try {
            this.gURL = gURL;
            this.gApiKey = gApiKey;

            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            this.service = client.resource(new URI(gURL));
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Map<String, String> listHistories() {
        HashMap<String, String> res = new HashMap<>();

        ClientResponse responseHist = service.path("/api/histories").queryParam("key", GHistAPI.gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
        String rH = responseHist.getEntity(String.class);
        JsonArray arrayHist = new JsonParser().parse(rH).getAsJsonArray();

        for (JsonElement eH : arrayHist) {
            String nameH = eH.getAsJsonObject().get("name").getAsString();
            String idH = eH.getAsJsonObject().get("id").getAsString();
            res.put(idH, nameH);
        }

        return res;
    }

    public String getProv(String histID) throws GalaxyProvenanceException, JSONException {
        logger.info("Fetching PROV");
        if (histID == null) {
            throw new GalaxyProvenanceException("Null Galaxy history ID");
        }

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        String s = "@base         <http://fr.symetric> .\n"
                + "@prefix xsd:  <http://www.w3.org/2001/XMLSchema#> .\n"
                + "@prefix foaf: <http://xmlns.com/foaf/0.1/> .\n"
                + "@prefix sioc: <http://rdfs.org/sioc/ns#> .\n"
                + "@prefix prov: <http://www.w3.org/ns/prov#> .\n"
                + "@prefix sym:   <http://fr.symetric/vocab#> .\n"
                + "@prefix dcterms: <http://purl.org/dc/terms/> .\n"
                + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"
                + "\n"
                + "<> \n"
                + "   a prov:Bundle, prov:Entity;\n"
                + "   prov:wasAttributedTo <#galaxy2prov>;\n"
                + "   prov:generatedAtTime \"" + fmt.format(Calendar.getInstance().getTime()) + "\"^^xsd:dateTime;\n"
                + ". \n\n";
//        logger.debug(s);
        sb.append(s);

        ArrayList<String> writtenOutputs = new ArrayList<>();

        ClientResponse responseHist = service.path("/api/histories").queryParam("key", GHistAPI.gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
        String rH = responseHist.getEntity(String.class);
        JsonArray arrayHist = new JsonParser().parse(rH).getAsJsonArray();

        // Navigate though list of histories
        // TODO useless ? 
        for (JsonElement eH : arrayHist) {
            String nameH = eH.getAsJsonObject().get("name").getAsString();
            String idH = eH.getAsJsonObject().get("id").getAsString();

            if (idH.equals(histID)) {

                ClientResponse responseHistContent = service.path("/api/histories/" + idH + "/contents").queryParam("key", GHistAPI.gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
                String rC = responseHistContent.getEntity(String.class);
                JsonArray arrayHistContent = new JsonParser().parse(rC).getAsJsonArray();

                // Navigate though datasets for a given history
                for (JsonElement eC : arrayHistContent) {
                    String idC = eC.getAsJsonObject().get("id").getAsString();
                    String nameC = eC.getAsJsonObject().get("name").getAsString();

                    ClientResponse responseHistContentProv = service.path("/api/histories/" + idH + "/contents/" + idC + "/provenance").queryParam("key", GHistAPI.gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
                    String rCProv = responseHistContentProv.getEntity(String.class);
                    //jsonPP(rCProv);
                    JsonObject prov = new JsonParser().parse(rCProv).getAsJsonObject();
                    String jobId = prov.get("job_id").getAsString();
                    String toolId = prov.get("tool_id").getAsString();
                    JsonObject params = prov.get("parameters").getAsJsonObject();

                    StringBuilder sbActivity = new StringBuilder("<#" + jobId + ">\n"
                            + "    a prov:Activity ;\n"
//                            + "    prov:wasAssociatedWith <#" + toolId + "> ;\n");
                            + "    prov:wasAssociatedWith \"" + toolId + "\" ;\n");

                    ClientResponse responseJob = service.path("/api/jobs/" + jobId).queryParam("key", GHistAPI.gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
                    String rJob = responseJob.getEntity(String.class);
                    JsonObject job = new JsonParser().parse(rJob).getAsJsonObject();
                    // TODO track command line as 'PROV plan'
                    sbActivity.append("    prov:startedAtTime \"").append(job.get("create_time").getAsString()).append("\"^^xsd:dateTime; \n");
                    sbActivity.append("    prov:endedAtTime \"").append(job.get("update_time").getAsString()).append("\"^^xsd:dateTime; \n");

//                    logger.debug("\n"+s);
                    StringBuilder sbOutput = new StringBuilder("<#" + idC + ">\n"
                            + "    a prov:Entity;\n"
                            + "    prov:wasGeneratedBy <#" + jobId + ">;\n"
//                            + "    prov:wasAttributedTo <#" + toolId + ">;\n");
                            + "    prov:wasAttributedTo \"" + toolId + "\" ;\n");
                    
                    if (eC.getAsJsonObject().get("name") != null) {
                        sbOutput.append("    rdfs:label \"" + eC.getAsJsonObject().get("name").getAsString() + "\";\n");

                    }
                    if (eC.getAsJsonObject().get("download_url") != null) {
                        sbOutput.append("    rdfs:label \"" + eC.getAsJsonObject().get("download_url").getAsString() + "\";\n\n");
                    }
//                            + "    rdfs:label \"" + nameC + "\";\n");
//                    logger.debug("\n"+s);

                    StringBuilder sbInput = new StringBuilder();

                    writtenOutputs.add(idC);

                    for (Map.Entry e : params.entrySet()) {
//                        System.out.println("\t\t" + e.getKey().toString() + " | " + e.getValue().toString());
                        String k = e.getKey().toString();
                        if (k.contains("input") || k.contains("rep_factor") || k.contains("file")) {
                            String jsonString = params.get(k).toString();
                            if (jsonString.startsWith("{")) {
                                JSONObject json = new JSONObject(jsonString);

                                if (json.has("id")) {
                                    String inputId = (String) json.get("id");
                                    if (!writtenOutputs.contains(inputId)) {

                                        ClientResponse responseDS = service.path("/api/datasets/" + inputId).queryParam("key", GHistAPI.gApiKey).accept("application/json").type("application/json").get(ClientResponse.class);
                                        String rDS = responseDS.getEntity(String.class);
                                        JsonObject ds = new JsonParser().parse(rDS).getAsJsonObject();

                                        writtenOutputs.add(inputId);
                                        sbInput.append("<#" + inputId + ">\n"
                                                + "    a prov:Entity;\n");
//                                        logger.debug(ds);
                                        if (ds.get("name") != null) {
                                            sbInput.append("    rdfs:label \"" + ds.get("name").getAsString() + "\";\n");

                                        }
//                                        if (ds.get("file_name") != null) {
//                                            sbInput.append("    rdfs:label \"" + ds.get("file_name").getAsString() + "\";\n\n");
//                                        }
                                        if (ds.get("download_url") != null) {
                                            sbInput.append("    rdfs:label \"" + ds.get("download_url").getAsString() + "\";\n\n");
                                        }
                                        sbInput.append(".\n");
                                    }

                                    sbActivity.append("    prov:used <#" + inputId + ">;\n");
                                    sbOutput.append("    prov:wasDerivedFrom <#" + inputId + ">;\n");
                                }
                            }
                        }
                        if (k.contains("refGenome")) {
                            String jsonString = params.get(k).toString();
                            if (jsonString.startsWith("{")) {
                                JSONObject json = new JSONObject(jsonString);

                                String genomeId = (String) json.get("index");
                                if (!writtenOutputs.contains(genomeId)) {
                                    writtenOutputs.add(genomeId);

                                    sbInput.append("<#" + genomeId + ">\n"
                                            + "    a prov:Entity;\n"
                                            + "    rdfs:label \"" + genomeId + "\".\n\n");
                                }

                                sbActivity.append("    prov:used <#" + genomeId + ">;\n");
                                sbOutput.append("    prov:wasDerivedFrom <#" + genomeId + ">;\n");
                            }
                        }
                    }

                    sbActivity.append(".\n\n");
                    sbOutput.append(".\n\n");

                    sb.append(sbActivity.toString());
                    sb.append(sbInput.toString());
                    sb.append(sbOutput.toString());
//                    System.out.println("");
//                    System.out.println(sb.toString());
//                    System.out.println("");
                }
            }
        }
        
        return sb.toString();
    }
}
