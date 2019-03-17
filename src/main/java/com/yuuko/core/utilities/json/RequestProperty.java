package com.yuuko.core.utilities.json;

public class RequestProperty {

    private final String header;
    private final String directive;

    public RequestProperty(String header, String directive) {
        this.header = header;
        this.directive = directive;
    }

    String getHeader() {
        return header;
    }

    String getDirective() {
        return directive;
    }
}
