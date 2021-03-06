/* 
 * Serposcope - SEO rank checker https://serposcope.serphacker.com/
 * 
 * Copyright (c) 2016 SERP Hacker
 * @author Pierre Nogues <support@serphacker.com>
 * @license https://opensource.org/licenses/MIT MIT License
 */
package com.serphacker.serposcope.models.google;

import static com.serphacker.serposcope.models.google.GoogleTarget.PatternType.DOMAIN;
import static com.serphacker.serposcope.models.google.GoogleTarget.PatternType.SUBDOMAIN;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class GoogleTarget {
    
    public enum PatternType {
        DOMAIN,
        SUBDOMAIN,
        REGEX
    };
    
    protected final static Pattern PATTERN_DOMAIN = Pattern.compile("^[a-zA-Z0-9-.]{1,61}\\.[a-zA-Z]{2,}$");

    int id;
    int groupId;
    String name;
    PatternType type;
    String pattern;
    
    Pattern compiledPattern;

    public GoogleTarget(int groupId, String name, PatternType type, String pattern) {
        this.groupId = groupId;
        this.name = name;
        setPattern(type, pattern);
    }

    public GoogleTarget(int id, int groupId, String name, PatternType type, String pattern) {
        this(groupId, name, type, pattern);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean match(String url){
        return compiledPattern.matcher(url).find();
    }
    
    public final void setPattern(PatternType type, String pattern) throws PatternSyntaxException {
        this.type = type;
        this.pattern = pattern;
        this.compiledPattern = GoogleTarget.compile(type, pattern);
    }
    
    public static boolean isValidPattern(PatternType type, String pattern){
        if(pattern == null || pattern.isEmpty()){
            return false;
        }
        
        if(DOMAIN.equals(type) || SUBDOMAIN.equals(type)){
            return PATTERN_DOMAIN.matcher(pattern).find();
        }
        
        try {
            if(Pattern.compile(pattern) != null){
                return true;
            }
        } catch(Exception ex){
        }
        
        return false;
    }
    
    public static Pattern compile(PatternType type, String pattern) throws PatternSyntaxException {
        switch(type){
            case DOMAIN:
                return Pattern.compile("^https?://" + Pattern.quote(pattern) + "(/|$)");
            case SUBDOMAIN:
                return Pattern.compile("^https?://([^/]+\\.)?" + Pattern.quote(pattern) + "(/|$)");
            default:
                return Pattern.compile(pattern);
        }
    }
    
    public PatternType getType() {
        return type;
    }

    public String getPattern() {
        return pattern;
    }
    
    
    
    
}
