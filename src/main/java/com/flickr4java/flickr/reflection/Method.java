

package com.flickr4java.flickr.reflection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Describes a method requested with {@link ReflectionInterface#getMethodInfo(String)}.
 * 
 * @author Anthony Eden
 * @version $Id: Method.java,v 1.6 2007/11/18 22:48:09 x-mago Exp $
 */
public class Method {

    public static final int READ_PERMISSION = 1;

    public static final int WRITE_PERMISSION = 2;

    private String name;

    private boolean needsLogin;

    private boolean needsSigning;

    private int requiredPerms;

    private String description;

    private String response;

    private String explanation;

    private Collection<Argument> arguments;

    private Collection<Error> errors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean needsLogin() {
        return needsLogin;
    }

    public void setNeedsLogin(boolean needsLogin) {
        this.needsLogin = needsLogin;
    }

    public boolean needsSigning() {
        return needsSigning;
    }

    public void setNeedsSigning(boolean needsSigning) {
        this.needsSigning = needsSigning;
    }

    public int getRequiredPerms() {
        return requiredPerms;
    }

    public void setRequiredPerms(int reqiredPerms) {
        this.requiredPerms = reqiredPerms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Collection<Argument> getArguments() {
        if (arguments == null) {
            arguments = new ArrayList<Argument>();
        }
        return arguments;
    }

    public void setArguments(Collection<Argument> arguments) {
        this.arguments = arguments;
    }

    public Collection<Error> getErrors() {
        if (errors == null) {
            errors = new ArrayList<Error>();
        }
        return errors;
    }

    public void setErrors(Collection<Error> errors) {
        this.errors = errors;
    }

}
