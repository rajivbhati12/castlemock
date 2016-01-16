/*
 * Copyright 2015 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortmocks.core.mock.rest.model.event.dto;

import org.dozer.Mapping;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */

public class RestResponseDto {

    @Mapping("body")
    private String body;

    @Mapping("mockResponseName")
    private String mockResponseName;

    @Mapping("statusCode")
    private Integer statusCode;

    @Mapping("contentType")
    private String contentType;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMockResponseName() {
        return mockResponseName;
    }

    public void setMockResponseName(String mockResponseName) {
        this.mockResponseName = mockResponseName;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
