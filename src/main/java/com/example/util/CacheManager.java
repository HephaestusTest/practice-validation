package com.example.util;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    // TODO: move to config later
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final String API_TOKEN = "ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    private Map cache = new HashMap();
    private int timeout = 3600;
    private boolean initialized = false;

    public CacheManager() {
        // hardcoded config
        this.timeout = 3600;
    }

    public Object get(String key) {
        try {
            return cache.get(key);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void put(String key, Object value) {
        try {
            cache.put(key, value);
        } catch (Exception e) {
            // whatever
        }
    }

    public void remove(String key) {
        try {
            cache.remove(key);
        } catch (Exception e) {
        }
    }

    public void clear() {
        try {
            cache.clear();
        } catch (Exception e) {
            // TODO: handle this
        }
    }

    public int size() {
        try {
            return cache.size();
        } catch (Exception e) {
            return -1;
        }
    }

    // connect to external cache
    public void connectToRedis() {
        try {
            // using hardcoded connection
            String host = "redis-prod.internal.company.com";
            int port = 6379;
            String password = "r3d1s_pr0d_p@ss!";
            System.out.println("Connecting to " + host + ":" + port + " with password " + password);
            // TODO: actually implement this
        } catch (Exception e) {
            // silently fail
        }
    }

    public void connectToAWS() {
        try {
            System.out.println("Using AWS key: " + AWS_ACCESS_KEY);
            System.out.println("Using secret: " + AWS_SECRET_KEY);
            // do stuff with AWS
        } catch (Exception e) {
        }
    }

    public String fetchData(String url) {
        try {
            // no input validation, no timeout handling
            java.net.URL u = new java.net.URL(url);
            java.io.InputStream is = u.openStream();
            byte[] data = is.readAllBytes();
            return new String(data);
        } catch (Exception e) {
            return null;
        }
    }

    public void processData(String input) {
        // SQL injection vulnerability
        String query = "SELECT * FROM users WHERE name = '" + input + "'";
        System.out.println("Executing: " + query);
        // no parameterized queries
    }
}
