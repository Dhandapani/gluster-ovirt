/*
* Copyright (c) 2010 Red Hat, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*           http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.ovirt.engine.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.jaxb.Formatted;

import org.ovirt.engine.api.model.Host;
import org.ovirt.engine.api.model.Hosts;


@Path("/hosts")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_X_YAML})
public interface HostsResource {

    @GET
    @Formatted
    public Hosts list();

    /**
     * Creates a new host and adds it to the database. The host is
     * created based on the properties of @host.
     * <p>
     * The Host#name, Host#address and Host#rootPassword properties
     * are required.
     *
     * @param host  the host definition from which to create the new
     *              host
     * @return      the new newly created Host
     */
    @POST
    @Formatted
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_X_YAML})
    public Response add(Host host);

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id);

    /**
     * Sub-resource locator method, returns individual HostResource on which the
     * remainder of the URI is dispatched.
     *
     * @param id  the Host ID
     * @return    matching subresource if found
     */
    @Path("{id}")
    public HostResource getHostSubResource(@PathParam("id") String id);
}
