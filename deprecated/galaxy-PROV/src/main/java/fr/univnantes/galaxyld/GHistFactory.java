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

import org.apache.log4j.Logger;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class GHistFactory {

    private static Logger logger = Logger.getLogger(GHistFactory.class);

    private static GHistAPI instance = null;
    private static String gUrl = null;
    private static String gKey = null;

    public static void init(String gUrl, String gKey) {
        GHistFactory.gUrl = gUrl;
        GHistFactory.gKey = gKey;
    }

    public static GHistAPI getInstance() throws GalaxyProvenanceException {
        if ((gUrl == null) || (gKey == null)) {
            logger.error("Please init the GHistFactory with a Galaxy URL and a Galaxy API key.");
            throw new GalaxyProvenanceException("Please init the GHistFactory with a Galaxy URL and a Galaxy API key.");
        } else if (instance != null) {
            return instance;
        } else {
            return new GHistAPI(gUrl, gKey);
        }
    }
}
