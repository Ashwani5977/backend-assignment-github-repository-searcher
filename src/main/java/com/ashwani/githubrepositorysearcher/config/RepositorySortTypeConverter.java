package com.ashwani.githubrepositorysearcher.config;

import com.ashwani.githubrepositorysearcher.enums.RepositorySortType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RepositorySortTypeConverter
        implements Converter<String, RepositorySortType> {

    @Override
    public RepositorySortType convert(String source) {

        return RepositorySortType.fromValue(source);
    }
}