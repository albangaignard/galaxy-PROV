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
import fr.univnantes.galaxyld.GHistAPI;
import fr.univnantes.galaxyld.GalaxyProvenanceException;
import fr.univnantes.galaxyld.Main;
import fr.univnantes.galaxyld.Util;
import org.apache.commons.lang.time.StopWatch;
import org.codehaus.jettison.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;

/**
 *
 * @author Alban Gaignard <alban.gaignard@cnrs.fr>
 */
public class TestGalaxAPI {

    private static String gURL;
    private static String gApiKey;

    public TestGalaxAPI() {
    }

     @BeforeClass
    public static void setUpClass() throws GalaxyProvenanceException {
        TestGalaxAPI.gURL = Util.getProperty("URL");
        TestGalaxAPI.gApiKey = Util.getProperty("API-key");
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
    public void directApiTest() throws JSONException, GalaxyProvenanceException {
        GHistAPI gAPI = new GHistAPI("https://galaxy-bird.univ-nantes.fr/galaxy/", "dd3b7fce727d53ac00512ea19a8f5d4f");
        StopWatch sw = new StopWatch();
        sw.start();
        String provTTL = gAPI.getProv("491a45f0d6ea6596");
        sw.stop();
        System.out.println("Direct REST : done in " + sw.getTime() + " ms");
        System.out.println(provTTL);
    }
    
    // https://usegalaxy.org | d75134deaabcda270120e146b41672e4
}
