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
package fr.univnantes.galaxyld.pojo;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Alban Gaignard <alban.gaignard@univ-nantes.fr>
 */
public class Step {

    private String annotation;
    private int id;
    private String name;
    private Map<String, Connection> input_connections;
    private List<Object> inputs;
    private List<Output> outputs;
    private String tool_id;
    private String type;
    private List<Object> user_outputs;

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Connection> getInput_connections() {
        return input_connections;
    }

    public void setInput_connections(Map<String, Connection> input_connections) {
        this.input_connections = input_connections;
    }

    public void setInputs(List<Object> inputs) {
        this.inputs = inputs;
    }

    public List<Object> getInputs() {
        return inputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Output> outputs) {
        this.outputs = outputs;
    }

    public String getTool_id() {
        return tool_id;
    }

    public void setTool_id(String tool_id) {
        this.tool_id = tool_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getUser_outputs() {
        return user_outputs;
    }

    public void setUser_outputs(List<Object> user_outputs) {
        this.user_outputs = user_outputs;
    }

    @Override
    public String toString() {
        return "Step{" + "annotation=" + annotation + ", id=" + id + ", name=" + name + ", input_connections=" + input_connections + ", inputs=" + inputs + ", outputs=" + outputs + ", tool_id=" + tool_id + ", type=" + type + ", user_outputs=" + user_outputs + '}';
    }

}
