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

    public TestGalaxAPI() {
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

    @Test
    public void hello() {
//         String [] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-hID", "df794a04e8de538a", "-f"};
//         String [] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-hID", "df794a04e8de538a"};
//         String [] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "1766e5ce2d5ac71079cde3b3a83316e7", "-hi", "122d1ac05929ad85"};
//         String [] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-hi", "91b109f0315f2571"}; // RNAseq Mouse (TopHat-HTSeq-Deseq)
//         String [] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-l"};
//        String[] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-hi", "3e34ed8b1c68cb50"}; // RNAseq Mouse (TopHat-Cufflinks)
        //java -jar Ga2Prov-1.0-SNAPSHOT-jar-with-dependencies.jar -f -u http://galaxy-bird.univ-nantes.fr/galaxy -k dd3b7fce727d53ac00512ea19a8f5d4f -hi 491a45f0d6ea6596 
        String[] params = {"-u", "https://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-hi", "91b109f0315f2571"}; 

        StopWatch sw = new StopWatch();
        sw.start();
        Main.main(params);
        sw.stop();
        System.out.println("DONE in " + sw.getTime() + " ms");
    }

    @Test
    @Ignore
    public void directApiTest() throws JSONException, GalaxyProvenanceException {
//        String[] params = {"-u", "http://galaxy-bird.univ-nantes.fr/galaxy/", "-k", "dd3b7fce727d53ac00512ea19a8f5d4f", "-hi", "491a45f0d6ea6596"}; // RNAseq Mouse (TopHat-Cufflinks)

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
