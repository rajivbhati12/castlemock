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
package com.castlemock.web.mock.rest.web.mvc.controller.resource;

import com.castlemock.core.basis.model.ServiceProcessor;
import com.castlemock.core.mock.rest.model.project.dto.RestApplicationDto;
import com.castlemock.core.mock.rest.model.project.dto.RestProjectDto;
import com.castlemock.core.mock.rest.model.project.dto.RestResourceDto;
import com.castlemock.core.mock.rest.model.project.service.message.input.ReadRestResourceInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.UpdateRestResourceInput;
import com.castlemock.core.mock.rest.model.project.service.message.input.UpdateRestResourcesForwardedEndpointInput;
import com.castlemock.core.mock.rest.model.project.service.message.output.ReadRestResourceOutput;
import com.castlemock.core.mock.rest.model.project.service.message.output.UpdateRestResourceOutput;
import com.castlemock.web.basis.web.mvc.controller.AbstractController;
import com.castlemock.web.mock.rest.config.TestApplication;
import com.castlemock.web.mock.rest.model.project.RestApplicationDtoGenerator;
import com.castlemock.web.mock.rest.model.project.RestProjectDtoGenerator;
import com.castlemock.web.mock.rest.model.project.RestResourceDtoGenerator;
import com.castlemock.web.mock.rest.web.mvc.command.method.UpdateRestMethodsEndpointCommand;
import com.castlemock.web.mock.rest.web.mvc.command.resource.UpdateRestResourcesEndpointCommand;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class UpdateRestResourceControllerTest extends AbstractRestControllerTest {

    private static final String PAGE = "partial/mock/rest/resource/updateRestResource.jsp";
    private static final String UPDATE = "update";

    @InjectMocks
    private UpdateRestResourceController updateRestResourceController;

    @Mock
    private ServiceProcessor serviceProcessor;

    @Override
    protected AbstractController getController() {
        return updateRestResourceController;
    }

    @Test
    public void testGetRestResource() throws Exception {
        final RestProjectDto restProjectDto = RestProjectDtoGenerator.generateRestProjectDto();
        final RestApplicationDto restApplicationDto = RestApplicationDtoGenerator.generateRestApplicationDto();
        final RestResourceDto restResourceDto = RestResourceDtoGenerator.generateRestResourceDto();
        when(serviceProcessor.process(any(ReadRestResourceInput.class))).thenReturn(new ReadRestResourceOutput(restResourceDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + restProjectDto.getId() + SLASH + APPLICATION + SLASH + restApplicationDto.getId() + SLASH + RESOURCE + SLASH + restResourceDto.getId() + SLASH + UPDATE);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_PROJECT_ID, restProjectDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_APPLICATION_ID, restApplicationDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_RESOURCE_ID, restResourceDto.getId()))
                .andExpect(MockMvcResultMatchers.model().attribute(REST_RESOURCE, restResourceDto));
    }

    @Test
    public void testUpdateRestResource() throws Exception {
        final RestProjectDto restProjectDto = RestProjectDtoGenerator.generateRestProjectDto();
        final RestApplicationDto restApplicationDto = RestApplicationDtoGenerator.generateRestApplicationDto();
        final RestResourceDto restResourceDto = RestResourceDtoGenerator.generateRestResourceDto();
        when(serviceProcessor.process(any(UpdateRestResourceInput.class))).thenReturn(new UpdateRestResourceOutput(restResourceDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + restProjectDto.getId() + SLASH + APPLICATION + SLASH + restApplicationDto.getId() + SLASH + RESOURCE + SLASH + restResourceDto.getId() + SLASH + UPDATE);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1));
    }

    @Test
    public void testUpdateEndpoint() throws Exception {
        final String projectId = "projectId";
        final String applicationId = "applicationId";

        final UpdateRestResourcesEndpointCommand updateRestResourcesEndpointCommand = new UpdateRestResourcesEndpointCommand();
        updateRestResourcesEndpointCommand.setForwardedEndpoint("http://localhost:8080/web");

        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH + APPLICATION + SLASH + applicationId + SLASH + RESOURCE + SLASH + "update/confirm")
                .flashAttr("updateRestResourcesEndpointCommand", updateRestResourcesEndpointCommand);
        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/web/rest/project/" + projectId + "/application/" + applicationId));

        Mockito.verify(serviceProcessor, Mockito.times(1)).process(Mockito.any(UpdateRestResourcesForwardedEndpointInput.class));
    }

}
