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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.univnantes.galaxyld.pojo.Output;
import fr.univnantes.galaxyld.pojo.Workflow;
import fr.univnantes.galaxyld.pojo.Step;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class Util {

    private static Logger logger = Logger.getLogger(Util.class);
    public static String provPrefix = "http://www.w3.org/ns/prov#";
    public static String dataPrefix = "http://data.symetric.org/";

    public static void jsonPP(String json) {
        Gson gson = new Gson();
        Object ob = gson.fromJson(json, Object.class
        );
        logger.info(new GsonBuilder().setPrettyPrinting().create().toJson(ob));
    }

    public static String getProperty(String key) throws GalaxyProvenanceException {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = Util.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(input);
            return prop.getProperty(key);
        } catch (IOException ex) {
            throw new GalaxyProvenanceException("Impossible to retrieve configuration from config.properties");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new GalaxyProvenanceException("Impossible to retrieve configuration from config.properties");
                }
            }
        }
    }

    private static String output(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

    public static String htmlOutTemplate = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "    <script type=\"text/javascript\" src=\"http://mbostock.github.com/d3/d3.js\"></script>\n"
            + "    <script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n"
            + "    <style media=\"screen\" type=\"text/css\">\n"
            + "    .node text {\n"
            + "        /*pointer-events: none;*/\n"
            + "        font: 10px sans-serif;\n"
            + "    }\n"
            + "    .link {\n"
            + "        stroke: #999;\n"
            //            + "        stroke-opacity: .6;\n"
            + "        fill: none;\n"
            + "    }\n"
            + "    .arrow {\n"
            + "        stroke: #999;\n"
            + "        stroke-width: 2;\n"
            + "        fill: #999;\n"
            //            + "        stroke-opacity: .6;\n"
            + "    }\n"
            + "    .text {\n"
            + "        fill: #000;\n"
            + "        font: 10px sans-serif;\n"
            + "        pointer-events: none;\n"
            + "    }\n"
            + "    </style>\n"
            + "</head>"
            + "<body>\n"
            + "    <div id=\"viz\"></div>\n"
            + "    <script type=\"text/javascript\">\n"
            + "\n"
            + "data = **jsonData** ;\n"
            + "renderD3(data, \"#viz\");  \n"
            + "function renderD3(data, htmlCompId) {\n"
            + "    var d3Data = data.d3;\n"
            + "    var mappings = data.mappings;\n"
            + "    var sMaps = JSON.stringify(mappings);\n"
            + "\n"
            + "    var width = $(htmlCompId).parent().width();\n"
            + "//        var height = $(\"svg\").parent().height();\n"
            + "    var height = 600;\n"
            + "    var color = d3.scale.category20();\n"
            + "\n"
            + "    var force = d3.layout.force()\n"
            + "            .charge(-300)\n"
            + "            .linkDistance(100)\n"
            + "            .friction(.8)\n"
            + "            .size([width, height]);\n"
            + "\n"
            + "    var svg = d3.select(htmlCompId).append(\"svg\")\n"
            + "//    	.attr(\"width\", width)\n"
            + "//    	.attr(\"height\", height)\n"
            + "            .attr(\"viewBox\", \"0 0 600 600\")\n"
            + "            .attr(\"width\", \"100%\")\n"
            + "            .attr(\"height\", 600)\n"
            + "            .attr(\"preserveAspectRatio\", \"xMidYMid\")\n"
            + "            .style(\"background-color\", \"#F4F2F5\");\n"
            + "// build the arrow.\n"
            + "    svg.append(\"svg:defs\").selectAll(\"marker\")\n"
            + "    .data([\"end\"])      // Different link/path types can be defined here\n"
            + "    .enter().append(\"svg:marker\")    // This section adds in the arrows\n"
            + "    .attr(\"id\", String)\n"
            + "    .attr(\"viewBox\", \"0 -5 10 10\")\n"
            + "    .attr(\"refX\", 21)\n"
            + "    .attr(\"refY\", -1.5)\n"
            + "    .attr(\"markerWidth\", 4)\n"
            + "    .attr(\"markerHeight\", 4)\n"
            + "    .attr(\"orient\", \"auto\")\n"
            + "    .attr(\"class\", \"arrow\")\n"
            + "    .append(\"svg:path\")\n"
            + "    .attr(\"d\", \"M0,-5L10,0L0,5\");"
            + "\n"
            + "    force.nodes(d3Data.nodes).links(d3Data.edges).start();\n"
            + "\n"
            + "    var link = svg.selectAll(\".link\")\n"
            + "            .data(d3Data.edges)\n"
            + "            .enter().append(\"path\")\n"
            + "            .attr(\"d\", \"M0,-5L10,0L0,5\")\n"
            + "            // .enter().append(\"line\")\n"
            + "            .attr(\"class\", \"link\")\n"
            + "            .attr(\"marker-end\", \"url(#end)\")\n"
            + "            .style(\"stroke-width\", function(d) {\n"
            + "                if (d.label.indexOf(\"prov#\") !== -1) {\n"
            + "                    return 3;\n"
            + "                }\n"
            + "                return 3;\n"
            + "            });\n"
            //            + "            .on(\"mouseout\", function(d, i) {\n"
            //            + "                d3.select(this).style(\"stroke\", \" #a0a0a0\");\n"
            //            + "            })\n"
            //            + "            .on(\"mouseover\", function(d, i) {\n"
            //            + "                d3.select(this).style(\"stroke\", \" #000000\");\n"
            //            + "            });\n"
            + "\n"
            + "    link.append(\"title\")\n"
            + "            .text(function(d) {\n"
            + "                return d.label;\n"
            + "            });\n"
            + "\n"
            + "\n"
            + "    var node_drag = d3.behavior.drag()\n"
            + "            .on(\"dragstart\", dragstart)\n"
            + "            .on(\"drag\", dragmove)\n"
            + "            .on(\"dragend\", dragend);\n"
            + "\n"
            + "    function dragstart(d, i) {\n"
            + "        force.stop() // stops the force auto positioning before you start dragging\n"
            + "    }\n"
            + "\n"
            + "    function dragmove(d, i) {\n"
            + "        d.px += d3.event.dx;\n"
            + "        d.py += d3.event.dy;\n"
            + "        d.x += d3.event.dx;\n"
            + "        d.y += d3.event.dy;\n"
            + "        tick(); // this is the key to make it work together with updating both px,py,x,y on d !\n"
            + "    }\n"
            + "\n"
            + "    function dragend(d, i) {\n"
            + "        d.fixed = true; // of course set the node to fixed so the force doesn't include the node in its auto positioning stuff\n"
            + "        tick();\n"
            + "        force.resume();\n"
            + "    }\n"
            + "\n"
            + "    var node = svg.selectAll(\"g.node\")\n"
            + "            .data(d3Data.nodes)\n"
            + "            .enter().append(\"g\")\n"
            + "            .attr(\"class\", \"node\")\n"
            + "            // .call(force.drag);\n"
            + "            .call(node_drag);\n"
            + "\n"
            + "    node.append(\"title\")\n"
            + "            .text(function(d) {\n"
            + "                return d.name;\n"
            + "            });\n"
            + "\n"
            + "    node.append(\"circle\")\n"
            + "            .attr(\"class\", \"node\")\n"
            + "            .attr(\"r\", function(d) {\n"
            + "                if (d.group === 0) {\n"
            + "                    return 6;\n"
            + "                }\n"
            + "                return 12;\n"
            + "            })\n"
            + "            .on(\"dblclick\", function(d) {\n"
            + "                d.fixed = false;\n"
            + "            })\n"
            + "            .on(\"mouseover\", fade(.1)).on(\"mouseout\", fade(1))\n"
            + "            .style(\"stroke\", function(d) {\n"
            + "                return color(d.group);\n"
            + "            })\n"
            + "            .style(\"stroke\", \"#F4F2F5\")\n"
            + "            .style(\"stroke-width\", 2)\n"
            //            + "            .style(\"stroke-width\", function(d) {\n"
            //            + "                if (sMaps.indexOf(d.name) !== -1) {\n"
            //            + "                    return 8;\n"
            //            + "                }\n"
            //            + "                return 3;\n"
            //            + "            })\n"
            + "            // 	.style(\"stroke-dasharray\",function(d) {\n"
            + "            // if (sMaps.indexOf(d.name) !== -1) {\n"
            + "            //   		return \"5,5\";\n"
            + "            // }\n"
            + "            // 		return \"none\";\n"
            + "            // 	})\n"
            + "            // .style(\"fill\", \"white\")\n"
            + "            .style(\"fill\", function(d) {\n"
            + "                return color(d.group);\n"
            + "            });\n"
            + "    // .on(\"mouseout\", function(d, i) {\n"
            + "    //  	d3.select(this).style(\"fill\", \"white\");\n"
            + "    // })\n"
            + "    // .on(\"mouseover\", function(d, i) {\n"
            + "    //  	d3.select(this).style(\"fill\", function(d) { return color(d.group); });\n"
            + "    // }) ;\n"
            + "\n"
            + "node.append(\"text\")\n"
            + "            .attr(\"x\", 20)\n"
            + "            .attr(\"dy\", \".35em\")\n"
            + "            .text(function(d) { \n"
            + "                return d.name ;\n"
            + "             });"
            + "\n"
            + "    var linkedByIndex = {};\n"
            + "    d3Data.edges.forEach(function(d) {\n"
            + "        linkedByIndex[d.source.index + \",\" + d.target.index] = 1;\n"
            + "    });\n"
            + "\n"
            + "    function isConnected(a, b) {\n"
            + "        return linkedByIndex[a.index + \",\" + b.index] || linkedByIndex[b.index + \",\" + a.index] || a.index === b.index;\n"
            + "    }\n"
            + "\n"
            + "    force.on(\"tick\", tick);\n"
            + "\n"
            + "    function tick() {\n"
            + "        link.attr(\"x1\", function(d) {\n"
            + "            return d.source.x;\n"
            + "        })\n"
            + "                .attr(\"y1\", function(d) {\n"
            + "                    return d.source.y;\n"
            + "                })\n"
            + "                .attr(\"x2\", function(d) {\n"
            + "                    return d.target.x;\n"
            + "                })\n"
            + "                .attr(\"y2\", function(d) {\n"
            + "                    return d.target.y;\n"
            + "                });\n"
            + "\n"
            + "        node.attr(\"transform\", function(d) {\n"
            + "            return \"translate(\" + d.x + \",\" + d.y + \")\";\n"
            + "        });\n"
            + "\n"
            + "        link.attr(\"d\", function(d) {\n"
            + "            var dx = d.target.x - d.source.x,\n"
            + "                    dy = d.target.y - d.source.y,\n"
            + "                    dr = Math.sqrt(dx * dx + dy * dy);\n"
            + "\n"
            + "            return \"M\" + d.source.x + \",\" + d.source.y + \"A\" + dr + \",\" + dr + \" 0 0,1 \" + d.target.x + \",\" + d.target.y;\n"
            + "        });\n"
            + "    }\n"
            + "    ;\n"
            + "\n"
            + "    function fade(opacity) {\n"
            + "        return function(d) {\n"
            + "            node.style(\"opacity\", function(o) {\n"
            + "                thisOpacity = isConnected(d, o) ? 1 : opacity;\n"
            + "                this.setAttribute('fill-opacity', thisOpacity);\n"
            + "                return thisOpacity;\n"
            + "            });\n"
            + "\n"
            + "            link.style(\"opacity\", function(o) {\n"
            + "                return o.source === d || o.target === d ? 1 : opacity;\n"
            + "            });\n"
            + "\n"
            + "        };\n"
            + "    }\n"
            + "}"
            + "    </script>\n"
            + "</body>\n"
            + "</html>";

    public static String genHtmlViz(String jsonData) {
        String out = htmlOutTemplate.replaceAll(Pattern.quote("**jsonData**"), jsonData);
        return out;
    }

    public static String simulateWfExec(Workflow wf) {
        StringBuilder toDOT = new StringBuilder();
        StringBuilder toPROV = new StringBuilder();
        toDOT.append("digraph G {\n");
        toPROV.append("PREFIX prov:<" + Util.provPrefix + "> insert data {\n");

        for (Step s : wf.getSteps().values()) {
            String step = "A" + s.getId() + "_" + s.getName().replaceAll(":", "").replaceAll(" ", "").replaceAll("/", "");
            toDOT.append("\t" + step + " [shape=box]\n");
            toPROV.append("<" + Util.dataPrefix + step + "> rdf:type prov:Activity . \n");

            for (Output o : s.getOutputs()) {
                // create produced data
                String data = "D" + s.getId() + "_" + o.getName();
                toDOT.append("\t" + data + " [shape=box]\n");
                toDOT.append("\t" + data + " -> " + step + "\n");
                toPROV.append("<" + Util.dataPrefix + data + "> rdf:type prov:Entity ; \n");
                toPROV.append("\t prov:wasGeneratedBy <" + Util.dataPrefix + step + "> . \n");

                for (String k : s.getInput_connections().keySet()) {

                    //iterate over list of outputs ?
//                    for (Connection c : s.getInput_connections().get(k)) {
//                        
//                        String origData = "D" + c.getId() + "_" + c.getOutput_name();
////                    // create data lineage
//                        toDOT.append("\t" + data + " -> " + origData + "[style=\"dotted\"]\n");
//                        toPROV.append("<" + Util.dataPrefix + data + "> prov:wasDerivedFrom <" + Util.dataPrefix + origData + "> . \n");
//                    }
                    String origData = "D" + s.getInput_connections().get(k).getId() + "_" + s.getInput_connections().get(k).getOutput_name();
                    // create data lineage
                    toDOT.append("\t" + data + " -> " + origData + "[style=\"dotted\"]\n");
                    toPROV.append("<" + Util.dataPrefix + data + "> prov:wasDerivedFrom <" + Util.dataPrefix + origData + "> . \n");
                }
            }
        }

        toPROV.append("}\n");
        toDOT.append("}\n");
//        return toDOT.toString();
        return toPROV.toString();
    }
}
