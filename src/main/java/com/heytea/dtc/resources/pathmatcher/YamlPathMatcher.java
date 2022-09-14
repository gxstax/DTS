package com.heytea.dtc.resources.pathmatcher;

import org.springframework.util.PathMatcher;

import java.util.Comparator;
import java.util.Map;

/**
 * <p>
 * Yml文件路径匹配
 * </p>
 *
 * @author Ant
 * @since 2022/1/6 8:44 下午
 */
public class YamlPathMatcher implements PathMatcher, ConfigPathMatcher {

    @Override
    public boolean isPattern(String path) {
        return path.endsWith("yml") || path.endsWith("yaml");
    }

    @Override
    public boolean match(String pattern, String path) {
        return false;
    }

    @Override
    public boolean matchStart(String pattern, String path) {
        return false;
    }

    @Override
    public String extractPathWithinPattern(String pattern, String path) {
        return null;
    }

    @Override
    public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
        return null;
    }

    @Override
    public Comparator<String> getPatternComparator(String path) {
        return null;
    }

    @Override
    public String combine(String pattern, String path) {
        return null;
    }
}
