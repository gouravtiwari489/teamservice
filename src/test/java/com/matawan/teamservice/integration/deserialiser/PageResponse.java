package com.matawan.teamservice.integration.deserialiser;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageResponse<T> extends PageImpl<T> {

    @JsonDeserialize(using = PageableDeserializer.class)
    private final Pageable pageable;

    public PageResponse() {
        super(List.of());
        this.pageable = Pageable.unpaged();
    }

    public PageResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.pageable = pageable != null ? pageable : Pageable.unpaged();
    }

    @Override
    public Pageable getPageable() {
        return pageable;
    }

}
