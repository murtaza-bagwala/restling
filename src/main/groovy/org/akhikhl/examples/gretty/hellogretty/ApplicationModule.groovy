package org.akhikhl.examples.gretty.hellogretty;
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.akhikhl.serviceapi.routers.RootRouter
import restling.guice.modules.RestlingApplicationModule

@Slf4j
@CompileStatic
class ApplicationModule extends RestlingApplicationModule {

  Class<RootRouter> routerClass = RootRouter

  @Override
  void configureCustomBindings() {

  }
}