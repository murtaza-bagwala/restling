package org.akhikhl.resources

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.akhikhl.resources.pojo.HashTag
import org.akhikhl.resources.pojo.Quotes
import org.akhikhl.serviceapi.routers.Service
import org.restlet.data.Method
import org.restlet.resource.Options
import org.restlet.resource.Post
import org.restlet.resource.ServerResource

@Slf4j
@CompileStatic
class PingResource extends ServerResource {

    Service service = Service.getService()

    @Post
    Quotes pinged(HashTag hashTag) {
        return service.getData(hashTag.getCategory(), hashTag.getPage_no())
    }

	@Options
     boolean pinged() {
         Set<Method> methods = new HashSet();
         methods.add(Method.ALL)
         response.accessControlAllowOrigin = "*"
         response.accessControlAllowMethods = methods
         return true
     }
}
