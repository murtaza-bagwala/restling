package org.akhikhl.serviceapi.routers


import org.akhikhl.resources.PingResource
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import restling.restlet.RestlingRouter

@Slf4j
@CompileStatic
class MetaRouter extends RestlingRouter {
    @Override
    void init() throws Exception {
        attach("/ping", PingResource)
    }
}