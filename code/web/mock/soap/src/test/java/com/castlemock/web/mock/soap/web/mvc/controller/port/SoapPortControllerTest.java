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

package com.castlemock.web.mock.soap.web.mvc.controller.port;


import com.castlemock.core.basis.model.ServiceProcessor;
import com.castlemock.core.mock.soap.model.project.dto.SoapOperationDto;
import com.castlemock.core.mock.soap.model.project.dto.SoapPortDto;
import com.castlemock.core.mock.soap.model.project.dto.SoapProjectDto;
import com.castlemock.core.mock.soap.model.project.service.message.input.ReadSoapOperationInput;
import com.castlemock.core.mock.soap.model.project.service.message.input.ReadSoapPortInput;
import com.castlemock.core.mock.soap.model.project.service.message.input.UpdateSoapOperationInput;
import com.castlemock.core.mock.soap.model.project.service.message.input.UpdateSoapOperationsStatusInput;
import com.castlemock.core.mock.soap.model.project.service.message.output.ReadSoapOperationOutput;
import com.castlemock.core.mock.soap.model.project.service.message.output.ReadSoapPortOutput;
import com.castlemock.web.basis.web.mvc.controller.AbstractController;
import com.castlemock.web.mock.soap.config.TestApplication;
import com.castlemock.web.mock.soap.model.project.SoapOperationDtoGenerator;
import com.castlemock.web.mock.soap.model.project.SoapPortDtoGenerator;
import com.castlemock.web.mock.soap.model.project.SoapProjectDtoGenerator;
import com.castlemock.web.mock.soap.web.mvc.command.operation.SoapOperationModifierCommand;
import com.castlemock.web.mock.soap.web.mvc.controller.AbstractSoapControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
@WebAppConfiguration
public class SoapPortControllerTest extends AbstractSoapControllerTest {

    private static final String PAGE = "partial/mock/soap/port/soapPort.jsp";
    private static final String SOAP_OPERATIONS = "soapOperations";
    private static final String UPDATE_SOAP_OPERATIONS_ENDPOINT_COMMAND = "updateSoapOperationsEndpointCommand";
    private static final String UPDATE_SOAP_OPERATIONS_ENDPOINT_PAGE = "partial/mock/soap/operation/updateSoapOperationsEndpoint.jsp";

    @InjectMocks
    private SoapPortController soapPortController;

    @Mock
    private ServiceProcessor serviceProcessor;

    @Override
    protected AbstractController getController() {
        return soapPortController;
    }

    @Test
    public void getSoapPort() throws Exception {
        final SoapProjectDto soapProjectDto = SoapProjectDtoGenerator.generateSoapProjectDto();
        final SoapPortDto soapPortDto = SoapPortDtoGenerator.generateSoapPortDto();
        final SoapOperationDto soapOperationDto = SoapOperationDtoGenerator.generateSoapOperationDto();
        final List<SoapOperationDto> operationDtos = new ArrayList<SoapOperationDto>();
        operationDtos.add(soapOperationDto);
        soapPortDto.setOperations(operationDtos);
        when(serviceProcessor.process(any(ReadSoapPortInput.class))).thenReturn(new ReadSoapPortOutput(soapPortDto));
        final MockHttpServletRequestBuilder message = MockMvcRequestBuilders.get(SERVICE_URL + PROJECT + SLASH + soapProjectDto.getId() + SLASH + PORT + SLASH + soapPortDto.getId() + SLASH);
        ResultActions result = mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(4 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PORT, soapPortDto));
        SoapPortDto soapPortDtoResponse = (SoapPortDto) result.andReturn().getModelAndView().getModel().get(SOAP_PORT);
    }


    @Test
    public void testServiceFunctionalityUpdate() throws Exception {
        final String projectId = "projectId";
        final String portId = "portId";
        final String[] soapOperationIds = {"Operation1", "Operation2"};

        final SoapOperationModifierCommand soapOperationModifierCommand = new SoapOperationModifierCommand();
        soapOperationModifierCommand.setSoapOperationIds(soapOperationIds);
        soapOperationModifierCommand.setSoapOperationStatus("MOCKED");

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH + PORT + SLASH + portId)
                        .param("action", "update").flashAttr("soapOperationModifierCommand", soapOperationModifierCommand);

        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/web/soap/project/" + projectId + "/port/" + portId));


        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.isA(UpdateSoapOperationsStatusInput.class));
    }

    @Test
    public void testServiceFunctionalityUpdateEndpoint() throws Exception {
        final String projectId = "projectId";
        final String portId = "portId";
        final String[] soapOperationIds = {"Operation1", "Operation2"};


        final SoapOperationDto soapOperation1 = new SoapOperationDto();
        soapOperation1.setId("SoapOperation1");

        final SoapOperationDto soapOperation2 = new SoapOperationDto();
        soapOperation2.setId("SoapOperation2");

        Mockito.when(serviceProcessor.process(Mockito.any(ReadSoapOperationInput.class)))
                .thenReturn(new ReadSoapOperationOutput(soapOperation1))
                .thenReturn(new ReadSoapOperationOutput(soapOperation2));


        final List<SoapOperationDto> operations = Arrays.asList(soapOperation1, soapOperation2);


        final SoapOperationModifierCommand soapOperationModifierCommand = new SoapOperationModifierCommand();
        soapOperationModifierCommand.setSoapOperationIds(soapOperationIds);
        soapOperationModifierCommand.setSoapOperationStatus("ENABLED");

        final MockHttpServletRequestBuilder message =
                MockMvcRequestBuilders.post(SERVICE_URL + PROJECT + SLASH + projectId + SLASH + PORT + SLASH + portId)
                        .param("action", "update-endpoint").flashAttr("soapOperationModifierCommand", soapOperationModifierCommand);

        mockMvc.perform(message)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(5 + GLOBAL_VIEW_MODEL_COUNT))
                .andExpect(MockMvcResultMatchers.forwardedUrl(INDEX))
                .andExpect(MockMvcResultMatchers.model().attribute(PARTIAL, UPDATE_SOAP_OPERATIONS_ENDPOINT_PAGE))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PROJECT_ID, projectId))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_PORT_ID, portId))
                .andExpect(MockMvcResultMatchers.model().attribute(SOAP_OPERATIONS, operations))
                .andExpect(MockMvcResultMatchers.model().attributeExists(UPDATE_SOAP_OPERATIONS_ENDPOINT_COMMAND));

        Mockito.verify(serviceProcessor, Mockito.times(2)).process(Mockito.isA(ReadSoapOperationInput.class));


    }

}
