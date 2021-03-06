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

package com.castlemock.web.mock.rest.web.mvc.controller.method;

import com.castlemock.core.basis.model.ServiceProcessor;
import com.castlemock.core.mock.rest.model.event.dto.RestEventDto;
import com.castlemock.core.mock.rest.model.event.service.message.input.ReadRestEventWithMethodIdInput;
import com.castlemock.core.mock.rest.model.event.service.message.output.ReadRestEventWithMethodIdOutput;
import com.castlemock.core.mock.rest.model.project.dto.*;
import com.castlemock.core.mock.rest.model.project.service.message.input.ReadRestMethodInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.ReadRestMockResponseInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.ReadRestResourceInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.UpdateRestMockResponseInput;
import com.castlemock.core.mock.rest.model.project.service.message.output.ReadRestMethodOutput;
import com.castlemock.core.mock.rest.model.project.service.message.output.ReadRestMockResponseOutput;
import com.castlemock.core.mock.rest.model.project.service.message.output.ReadRestResourceOutput;
import com.castlemock.web.basis.web.mvc.controller.AbstractController;
import com.castlemock.web.mock.rest.config.TestApplication;
import com.castlemock.web.mock.rest.model.project.RestApplicationDtoGenerator;
import com.castlemock.web.mock.rest.model.project.RestMethodDtoGenerator;
import com.castlemock.web.mock.rest.model.project.RestProjectDtoGenerator;
import com.castlemock.web.mock.rest.model.project.RestResourceDtoGenerator;
import com.castlemock.web.mock.rest.web.mvc.command.method.RestMethodModifierCommand;
import com.castlemock.web.mock.rest.web.mvc.command.mockresponse.RestMockResponseModifierCommand;
import com.castlemock.web.mock.rest.web.mvc.controller.AbstractRestControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;


/**
 * @author: Karl Dahlgren
 * @since: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class RestMethodControllerTest extends AbstractRestControllerTest {

    private static final String PAGE = "partial/mock/rest/method/restMethod.jsp";
    private static final String DELETE_REST_MOCK_RESPONSES_COMMAND = "deleteRestMockResponsesCommand";
    private static final String DELETE_MOCK_RESPONSES_PAGE = "partial/mock/rest/mockresponse/deleteRestMockResponses.jsp";
    protected static final String REST_MOCK_RESPONSES = "restMockResponses";

    @InjectMocks
    private RestMethodController restMockResponseController;

    @Mock
    private ServiceProcessor serviceProcessor;

    @Override
    protected AbstractController getController() {
        return restMockResponseController;
    }

    @Test
    public void testGetMethod() throws Exception {
        final RestProjectDto restProjectDto = RestProjectDtoGenerator.generateRestProjectDto();
        final RestApplicationDto restApplicationDto = RestApplicationDtoGenerator.generateRestApplicationDto();
        final RestResourceDto restResourceDto = RestResourceDtoGenerator.generateRestResourceDto();
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();
        when(serviceProcessor.process(isA(ReadRestResourceInput.class))).thenReturn(new ReadRestResourceOutput(restResourceDto));
        when(serviceProcessor.process(isA(ReadRestMethodInput.class))).thenReturn(new ReadRestMethodOutput(restMethodDto));
        when(serviceProcessor.process(isA(ReadRestEventWithMethodIdInput.class))).thenReturn(new ReadRestEventWithMethodIdOutput(new ArrayList<RestEventDto>()));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + restProjectDto.getId() + SLASH + APPLICATION + SLASH + restApplicationDto.getId() + SLASH + RESOURCE + SLASH + restResourceDto.getId() + SLASH + METHOD + SLASH + restMethodDto.getId());
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(7 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_PROJECT_ID, restProjectDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_APPLICATION_ID, restApplicationDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_RESOURCE_ID, restResourceDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_METHOD, restMethodDto));
    }

    @Test
    public void projectFunctionalityUpdate() throws Exception {
        final String projectId = "projectId";
        final String applicationId = "applicationId";
        final String resourceId = "resourceId";
        final String methodId = "methjodId";
        final String[] mockResponses = {"restMethod1", "restMethod2"};

        final RestMockResponseDto restMockResponse1 = new RestMockResponseDto();
        restMockResponse1.setName("restMockResponse1");

        final RestMockResponseDto restMockResponse2 = new RestMockResponseDto();
        restMockResponse2.setName("restMockResponse2");

        Mockito.when(serviceProcessor.process(Mockito.any(ReadRestMockResponseInput.class)))
                .thenReturn(new ReadRestMockResponseOutput(restMockResponse1))
                .thenReturn(new ReadRestMockResponseOutput(restMockResponse2));

        final RestMockResponseModifierCommand restMockResponseModifierCommand = new RestMockResponseModifierCommand();
        restMockResponseModifierCommand.setRestMockResponseIds(mockResponses);
        restMockResponseModifierCommand.setRestMockResponseStatus("ENABLED");

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH + APPLICATION + SLASH + applicationId + SLASH + RESOURCE + SLASH + resourceId + SLASH + METHOD + SLASH + methodId)
                        .param("action", "update").flashAttr("restMockResponseModifierCommand", restMockResponseModifierCommand);

        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/web/rest/project/" + projectId + "/application/" + applicationId + "/resource/" + resourceId + "/method/" + methodId));

        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.isA(ReadRestMockResponseInput.class));
        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.isA(UpdateRestMockResponseInput.class));
    }

    @Test
    public void projectFunctionalityDelete() throws Exception {
        final String projectId = "projectId";
        final String applicationId = "applicationId";
        final String resourceId = "resourceId";
        final String methodId = "methjodId";
        final String[] mockResponseIds = {"restMethod1", "restMethod2"};

        final RestMockResponseModifierCommand restMockResponseModifierCommand = new RestMockResponseModifierCommand();
        restMockResponseModifierCommand.setRestMockResponseIds(mockResponseIds);


        final RestMockResponseDto restMockResponse1 = new RestMockResponseDto();
        restMockResponse1.setName("restMockResponse1");

        final RestMockResponseDto restMockResponse2 = new RestMockResponseDto();
        restMockResponse2.setName("restMockResponse2");

        final List<RestMockResponseDto> restMockResponses = Arrays.asList(restMockResponse1, restMockResponse2);

        Mockito.when(serviceProcessor.process(Mockito.any(ReadRestMockResponseInput.class)))
                .thenReturn(new ReadRestMockResponseOutput(restMockResponse1))
                .thenReturn(new ReadRestMockResponseOutput(restMockResponse2));

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH + APPLICATION + SLASH + applicationId + SLASH + RESOURCE + SLASH + resourceId + SLASH + METHOD + SLASH + methodId)
                        .param("action", "delete").flashAttr("restMockResponseModifierCommand", restMockResponseModifierCommand);


        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(7 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, DELETE_MOCK_RESPONSES_PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_PROJECT_ID, projectId))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_APPLICATION_ID, applicationId))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_RESOURCE_ID, resourceId))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_METHOD_ID, methodId))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_MOCK_RESPONSES, restMockResponses))
                .andExpect(MockMvcResultMatchers.model().attributeExists(DELETE_REST_MOCK_RESPONSES_COMMAND));


        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.any(ReadRestResourceInput.class));

    }

}
