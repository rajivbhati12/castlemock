<%--
 Copyright 2016 Karl Dahlgren

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>

<%@ include file="../../../../includes.jspf"%>
<c:url var="update_soap_operation_url"  value="/web/soap/project/${soapProjectId}/port/${soapPortId}/operation/${soapOperationId}/update" />
<div class="navigation">
    <ol class="breadcrumb">
        <li><a href="${context}/web"><spring:message code="general.breadcrumb.home"/></a></li>
        <li><a href="${context}/web/soap/project/${soapProjectId}"><spring:message code="soap.breadcrumb.project"/></a></li>
        <li><a href="${context}/web/soap/project/${soapProjectId}/port/${soapPortId}"><spring:message code="soap.breadcrumb.port"/></a></li>
        <li><a href="${context}/web/soap/project/${soapProjectId}/port/${soapPortId}/operation/${soapOperationId}"><spring:message code="soap.breadcrumb.operation"/></a></li>
        <li class="active"><spring:message code="soap.updatesoapoperation.header.updateoperation" arguments="${command.name}"/></li>
    </ol>
</div>
<div class="container">
    <section>
        <div class="content-top">
            <h1><spring:message code="soap.updatesoapoperation.header.updateoperation" arguments="${command.name}"/></h1>
        </div>
        <form:form action="${update_soap_operation_url}" method="POST">
            <table class="formTable">
                <tr>
                    <td class="column1"><spring:message code="soap.updatesoapoperation.label.status"/></td>
                    <td>
                        <form:select path="status">
                            <c:forEach items="${soapOperationStatuses}" var="soapOperationStatus">
                                <spring:message var="label" code="soap.type.soapoperationstatus.${soapOperationStatus}"/>
                                <form:option value="${soapOperationStatus}" label="${label}"/>
                            </c:forEach>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td class="column1"><spring:message code="soap.updatesoapoperation.label.responsestrategy"/></td>
                    <td>
                        <form:select path="responseStrategy">
                            <c:forEach items="${soapMockResponseStrategies}" var="soapMockResponseStrategy">
                                <spring:message var="label" code="soap.type.responsestrategy.${soapMockResponseStrategy}"/>
                                <form:option value="${soapMockResponseStrategy}" label="${label}"/>
                            </c:forEach>
                        </form:select>
                    </td>
                </tr>
                <tr>
                    <td class="column1"><label path="originalEndpoint"><spring:message code="soap.updatesoapoperation.label.originalforwardedenpoint"/></label></td>
                    <td class="column2"><label path="originalEndpoint">${command.originalEndpoint}</label></td>
                </tr>
                <tr>
                    <td class="column1"><label path="forwardedEndpoint"><spring:message code="soap.updatesoapoperation.label.forwardedendpoint"/></label></td>
                    <td class="column2"><form:input path="forwardedEndpoint" value="${command.forwardedEndpoint}"/></td>
                </tr>
                <tr>
                    <td class="column1"><form:label path="simulateNetworkDelay"><spring:message code="soap.updatesoapoperation.label.simulatenetworkdelay"/></form:label></td>
                    <td class="column2"><span class="checkbox"><form:checkbox path="simulateNetworkDelay"/></span></td>
                </tr>
                <tr>
                    <td class="column1"><label path="name"><spring:message code="soap.updatesoapoperation.label.networkdelay"/> (ms)</label></td>
                    <td class="column2"><form:input path="networkDelay" value="${command.networkDelay}"/></td>
                </tr>
                <tr>
                    <td class="column1"><spring:message code="soap.updatesoapoperation.label.defaultxpathresponse"/></td>
                    <td>
                        <form:select path="defaultXPathMockResponseId">
                            <spring:message var="label" code="soap.updatesoapoperation.dropdown.option.selectresponse"/>
                            <form:option value="" label="${label}"/>
                            <c:forEach items="${command.mockResponses}" var="mockResponse">
                                <form:option value="${mockResponse.id}" label="${mockResponse.name}"/>
                            </c:forEach>
                        </form:select>
                    </td>
                </tr>
            </table>

            <button class="btn btn-success" type="submit" name="submit"><i class="fa fa-check-circle"></i> <spring:message code="soap.updatesoapoperation.button.updateoperation"/></button>
            <a href="<c:url value="/web/soap/project/${soapProjectId}/port/${soapPortId}/operation/${soapOperationId}"/>" class="btn btn-danger"><i class="fa fa-times"></i> <spring:message code="soap.updatesoapoperation.button.cancel"/></a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </section>
</div>