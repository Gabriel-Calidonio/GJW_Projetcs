wattrelos@Pavilion:/var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico$ ./mvnw test -Dtest=LoginControllerTest  
[INFO] Scanning for projects...  
[INFO]  
[INFO] --------------------------< com.gwj:projeto >---------------------------  
[INFO] Building projeto 0.0.1-SNAPSHOT  
[INFO]   from pom.xml  
[INFO] --------------------------------[ jar ]---------------------------------  
[INFO]  
[INFO] --- resources:3.3.1:resources (default-resources) @ projeto ---  
[INFO] Copying 1 resource from src/main/resources to target/classes  
[INFO] Copying 263 resources from src/main/resources to target/classes  
[INFO]  
[INFO] --- compiler:3.13.0:compile (default-compile) @ projeto ---  
[INFO] Nothing to compile - all classes are up to date.  
[INFO]  
[INFO] --- resources:3.3.1:testResources (default-testResources) @ projeto ---  
[INFO] Copying 1 resource from src/test/resources to target/test-classes  
[INFO]  
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ projeto ---  
[INFO] Nothing to compile - all classes are up to date.  
[INFO]  
[INFO] --- surefire:3.1.2:test (default-test) @ projeto ---  
[INFO] Using auto detected provider org.apache.maven.surefire.junitplatform.JUnitPlatformProvider  
[INFO]  
[INFO] -------------------------------------------------------  
[INFO]  T E S T S  
[INFO] -------------------------------------------------------  
[INFO] Running com.gwj.controller.LoginControllerTest  
[INFO] Running com.gwj.controller.LoginControllerTest$AcessoPaginaLogin  
WARNING: A Java agent has been loaded dynamically (/home/wattrelos/.m2/repository/net/bytebuddy/byte-buddy-agent/1.14.13/byte-buddy-agent-1.14.13.jar)  
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning  
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information  
WARNING: Dynamic loading of agents will be disallowed by default in a future release  
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended  
12:12:46.428 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.430 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.435 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 3 ms  
12:12:46.574 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.575 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.575 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.594 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.595 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.595 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.623 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.624 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.624 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.024 s -- in com.gwj.controller.LoginControllerTest$AcessoPaginaLogin  
[INFO] Running com.gwj.controller.LoginControllerTest$LoginAdministrativo  
12:12:46.658 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.658 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.658 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.712 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.712 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.712 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.734 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.734 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.734 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.104 s -- in com.gwj.controller.LoginControllerTest$LoginAdministrativo  
[INFO] Running com.gwj.controller.LoginControllerTest$LogoutTest  
12:12:46.765 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.765 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.765 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.849 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.850 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.850 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.105 s -- in com.gwj.controller.LoginControllerTest$LogoutTest  
[INFO] Running com.gwj.controller.LoginControllerTest$ProcessamentoLogin  
12:12:46.876 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.876 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.876 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.896 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.897 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.897 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.912 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.913 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.913 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.929 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.929 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.929 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.943 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.943 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.943 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
12:12:46.957 [main] INFO org.springframework.mock.web.MockServletContext -- Initializing Spring TestDispatcherServlet ''  
12:12:46.957 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Initializing Servlet ''  
12:12:46.957 [main] INFO org.springframework.test.web.servlet.TestDispatcherServlet -- Completed initialization in 0 ms  
[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.100 s -- in com.gwj.controller.LoginControllerTest$ProcessamentoLogin  
[INFO] Tests run: 0, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.396 s -- in com.gwj.controller.LoginControllerTest  
[INFO]  
[INFO] Results:  
[INFO]  
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0  
[INFO]  
[INFO] ------------------------------------------------------------------------  
[INFO] BUILD SUCCESS  
[INFO] ------------------------------------------------------------------------  
[INFO] Total time:  7.754 s  
[INFO] Finished at: 2026-10-08T12:12:47-03:00  