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


import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    private static String gKey;
    private static String gHist;
    private static String gUrl;
    private static boolean isFull = false;

    public static void main(String args[]) {
        Options options = new Options();
        Option listGHistOpt = new Option("l", "listGalaxyHistories", false, "list all available Galaxy histories");
        Option apiKeyOpt = new Option("k", "galaxyApiKey", true, "the Galaxy API Key used as credential");
        Option gUrlOpt = new Option("u", "galaxyUrl", true, "the Galaxy server URL");
        Option gHistIDOpt = new Option("hi", "galaxyHistoryID", true, "the Galaxy history ID used to extract PROV RDF statements");
        Option fullOpt = new Option("f", "full_prov", false, "produces a full RDF PROV output");
        Option versionOpt = new Option("v", "version", false, "print the version information and exit");
        Option helpOpt = new Option("h", "help", false, "print the help");
        options.addOption(listGHistOpt);
        options.addOption(apiKeyOpt);
        options.addOption(gHistIDOpt);
        options.addOption(gUrlOpt);
        options.addOption(fullOpt);
        options.addOption(versionOpt);
        options.addOption(helpOpt);

        String header = "Ga2Prov is a tool to extract provenance information from Galaxy workspaces through the PROV-O ontology and a corresponding graphical representation.";
        String footer = "\nPlease report any issue to alban.gaignard@univ-nantes.fr";

        try {
            CommandLineParser parser = new BasicParser();
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ga2prov", header, options, footer, true);
                System.exit(0);
            }

            if (cmd.hasOption("v")) {
                logger.info("Ga2Prov version 0.1.0");
                System.exit(0);
            }

            if (cmd.hasOption("f")) {
                Main.isFull = true;
                logger.debug("full prov");
            } else {
                logger.debug("partial prov");
            }

            if (cmd.hasOption("k")) {
                Main.gKey = cmd.getOptionValue("k");
            } else {
                logger.warn("Please specify your Galaxy API key !");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ga2prov", header, options, footer, true);
                System.exit(0);
            }

            if (cmd.hasOption("u")) {
                Main.gUrl = cmd.getOptionValue("u");
            } else {
                logger.warn("Please specify your Galaxy server URL to connect to !");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ga2prov", header, options, footer, true);
                System.exit(0);
            }

            GHistFactory.init(Main.gUrl, Main.gKey);

            if (cmd.hasOption("l")) {
                GHistAPI gAPI;
                try {
                    gAPI = GHistFactory.getInstance();
                    Map<String, String> histories = gAPI.listHistories();
                    for (String id : histories.keySet()) {
                        String message = String.format("Found history with id %s and name %s", id, histories.get(id));
                        System.out.println(message);
                    }
                } catch (Exception ex) {
                    logger.error("An error occured while connecting to the Galaxy server. Please check the Galaxy server URL and your API key.");
                    ex.printStackTrace();
                    System.exit(1);
                }
            }

            if (cmd.hasOption("hi")) {
                StopWatch sw = new StopWatch();
                sw.start();
                
                Main.gHist = cmd.getOptionValue("hi");
                        
                GHistAPI gAPI = GHistFactory.getInstance();
                String provTTL = gAPI.getProv(Main.gHist);
                sw.stop();
                logger.info("PROV triples retrieved in "+sw.getTime()+" ms");

//                GraphStore graph = GraphStore.create(false);
//                QueryProcess exec = QueryProcess.create(graph);
//                Load ld = Load.create(graph);
//                InputStream inputStream = new ByteArrayInputStream(provTTL.getBytes(StandardCharsets.UTF_8));
//                ld.load(inputStream,".ttl");
//                logger.info("Loaded " + graph.size() + " rdf statements.");
//
//                Mappings maps = null;
//
//                if (!Main.isFull) {
//                    String filterQuery = "PREFIX prov:<http://www.w3.org/ns/prov#>\n"
//                            + "CONSTRUCT {\n"
//                            + "	?x ?p ?y .\n"
//                            + " ?x rdfs:label ?xL .\n"
//                            + " ?y rdfs:label ?yL .\n"
//                            + "} WHERE {\n"
//                            + "	?x ?p ?y .\n"
//                            + " OPTIONAL {?x rdfs:label ?xL} .\n"
//                            + " OPTIONAL {?y rdfs:label ?yL} .\n"
//                            //                        + "	FILTER (?p NOT IN (rdf:type, prov:wasGeneratedBy, prov:qualifiedAssociation, prov:hadPlan, prov:agent, rdfs:comment)) \n"
////                            + "	FILTER (?p IN (prov:wasDerivedFrom, rdfs:label, prov:wasAttributedTo, prov:startedAtTime, prov:endedAtTime )) \n"
//                            + "	FILTER (?p IN (prov:wasDerivedFrom, rdfs:label, prov:wasAttributedTo)) \n"
//                            + "} ";
//                    maps = exec.query(filterQuery);
//                } else {
//                    String filterQuery = "PREFIX prov:<http://www.w3.org/ns/prov#>\n"
//                            + "CONSTRUCT {\n"
//                            + "	?x ?p ?y .\n"
//                            + "} WHERE {\n"
//                            + "	?x ?p ?y .\n"
//                            //                        + "	FILTER (?p NOT IN (rdf:type, prov:wasGeneratedBy, prov:qualifiedAssociation, prov:hadPlan, prov:agent, rdfs:comment)) \n"
////                            + "	FILTER (?p NOT IN (rdf:type)) \n"
//                            + "} ";
//                    maps = exec.query(filterQuery);
//                }
//
//                Graph g = (Graph) maps.getGraph();
//
//                String mapsProvJson = "{ \"mappings\" : "
//                        + JSONFormat.create(maps).toString()
//                        + " , "
//                        + "\"d3\" : "
//                        + JSOND3Format.create(g).toString()
//                        + " }";
//                
////                System.out.println(mapsProvJson);
//
//                String htmlOut = Util.genHtmlViz(mapsProvJson);
//
//                Path pathHtml = Files.createTempFile("provenanceDisplay-", ".html");
//                Files.write(pathHtml, htmlOut.getBytes(), StandardOpenOption.WRITE);
//                logger.info("HTML visualization written in " + pathHtml.toString());
//                
//                Path pathProv = Files.createTempFile("provenanceRDF-", ".ttl");
//                Files.write(pathProv, provTTL.getBytes(), StandardOpenOption.WRITE);
//                logger.info("RDF provenance written in " + pathProv.toString());
            }

        } catch (GalaxyProvenanceException ex) {
            logger.error("An error occured while connecting to the Galaxy server. Please check the Galaxy server URL and your API key.");
            ex.printStackTrace();
            System.exit(1);
//        } catch (IOException ex) {
//            logger.error("An error occured while writing the visualization file.");
//            ex.printStackTrace();
//            System.exit(1);
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
