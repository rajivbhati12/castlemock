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

package com.castlemock.web.mock.rest.web.mvc.controller.mockresponse;

import com.castlemock.core.basis.model.ServiceProcessor;
import com.castlemock.core.mock.rest.model.project.dto.*;
import com.castlemock.core.mock.rest.model.project.service.message.input.DeleteRestMockResponseInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.DeleteRestMockResponsesInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.ReadRestMockResponseInput;
import com.castlemock.core.mock.rest.model.project.service.message.output.DeleteRestMockResponsesOutput;
import com.castlemock.core.mock.rest.model.project.service.message.output.ReadRestMockResponseOutput;
import com.castlemock.web.basis.web.mvc.controller.AbstractController;
import com.castlemock.web.mock.rest.config.TestApplication;
import com.castlemock.web.mock.rest.model.project.*;
import com.castlemock.web.mock.rest.web.mvc.command.mockresponse.DeleteRestMockResponsesCommand;
import com.castlemock.web.mock.rest.web.mvc.controller.AbstractRestControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * @author: Karl Dahlgren
 * @since: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class DeleteRestMockResponseControllerTest extends AbstractRestControllerTest {

    private static final String PAGE = "partial/mock/rest/mockresponse/deleteRestMockResponse.jsp";
    private static final String DELETE = "delete";
    private static final String CONFIRM = "confirm";


    @InjectMocks
    private DeleteRestMockResponseController deleteSoapMockResponseController;

    @Mock
    private ServiceProcessor serviceProcessor;

    @Override
    protected AbstractController getController() {
        return deleteSoapMockResponseController;
    }

    @Test
    public void testDeleteMockResponse() throws Exception {
        final RestProjectDto restProjectDto = RestProjectDtoGenerator.generateRestProjectDto();
        final RestApplicationDto restApplicationDto = RestApplicationDtoGenerator.generateRestApplicationDto();
        final RestResourceDto restResourceDto = RestResourceDtoGenerator.generateRestResourceDto();
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();
        final RestMockResponseDto restMockResponseDto = RestMockResponseDtoGenerator.generateRestMockResponseDto();
        when(serviceProcessor.process(any(ReadRestMockResponseInput.class))).thenReturn(new ReadRestMockResponseOutput(restMockResponseDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + restProjectDto.getId() + SLASH + APPLICATION + SLASH + restApplicationDto.getId() + SLASH + RESOURCE + SLASH + restResourceDto.getId() + SLASH + METHOD + SLASH + restMethodDto.getId() + SLASH + RESPONSE + SLASH + restMockResponseDto.getId() + SLASH + DELETE);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_PROJECT_ID, restProjectDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_APPLICATION_ID, restApplicationDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_RESOURCE_ID, restResourceDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_METHOD_ID, restMethodDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_MOCK_RESPONSE, restMockResponseDto));
    }

    @Test
    public void testDeleteMockResponseConfirm() throws Exception {
        final RestProjectDto restProjectDto = RestProjectDtoGenerator.generateRestProjectDto();
        final RestApplicationDto restApplicationDto = RestApplicationDtoGenerator.generateRestApplicationDto();
        final RestResourceDto restResourceDto = RestResourceDtoGenerator.generateRestResourceDto();
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();
        final RestMockResponseDto restMockResponseDto = RestMockResponseDtoGenerator.generateRestMockResponseDto();
        when(serviceProcessor.process(any(DeleteRestMockResponseInput.class))).thenReturn(new DeleteRestMockResponsesOutput());
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + restProjectDto.getId() + SLASH + APPLICATION + SLASH + restApplicationDto.getId() + SLASH + RESOURCE + SLASH + restResourceDto.getId() + SLASH + METHOD + SLASH + restMethodDto.getId() + SLASH + RESPONSE + SLASH + restMockResponseDto.getId() + SLASH + DELETE + SLASH + CONFIRM);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0));
    }



    @Test
    public void testConfirmDeletationOfMultipleMockResponses() throws Exception {
        final RestProjectDto restProjectDto = RestProjectDtoGenerator.generateRestProjectDto();
        final RestApplicationDto restApplicationDto = RestApplicationDtoGenerator.generateRestApplicationDto();
        final RestResourceDto restResourceDto = RestResourceDtoGenerator.generateRestResourceDto();
        final RestMethodDto restMethodDto = RestMethodDtoGenerator.generateRestMethodDto();
        final RestMockResponseDto restMockResponseDto = RestMockResponseDtoGenerator.generateRestMockResponseDto();
        final DeleteRestMockResponsesCommand deleteRestMockResponsesCommand = new DeleteRestMockResponsesCommand();
        deleteRestMockResponsesCommand.setRestMockResponses(new ArrayList<RestMockResponseDto>());
        deleteRestMockResponsesCommand.getRestMockResponses().add(restMockResponseDto);

        when(serviceProcessor.process(any(DeleteRestMockResponsesInput.class))).thenReturn(new DeleteRestMockResponsesOutput());
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + restProjectDto.getId() + SLASH + APPLICATION + SLASH + restApplicationDto.getId() + SLASH + RESOURCE + SLASH + restResourceDto.getId() + SLASH + METHOD + SLASH + restMethodDto.getId() + SLASH + RESPONSE + SLASH + DELETE + SLASH + CONFIRM);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1));
    }
}
