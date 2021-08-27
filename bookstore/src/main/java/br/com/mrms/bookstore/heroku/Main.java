package br.com.mrms.bookstore.heroku;

import java.io.File;
import java.util.Arrays;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * This class launches the web application in an embedded Jetty container. This
 * is the entry point to your application. The Java command that is used for
 * launching should fire this main method.
 */
public class Main {

	public static void main(String[] args) throws Exception {

		File keystoreFile = new File("server.keystore");

		final Server server = new Server();

		HttpConfiguration httpConfiguration = new HttpConfiguration();
		httpConfiguration.setSecureScheme("https");
		httpConfiguration.setSecurePort(8443);

		ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(httpConfiguration));
		http.setPort(8080);

		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(keystoreFile.getAbsolutePath());
		sslContextFactory.setKeyStorePassword("bookstore");
		sslContextFactory.setKeyManagerPassword("bookstore");

		HttpConfiguration httpsConfiguration = new HttpConfiguration(httpConfiguration);

		ServerConnector https = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
				new HttpConnectionFactory(httpsConfiguration));
		https.setPort(8443);

		server.setConnectors(new ServerConnector[] { http, https });

		final WebAppContext root = new WebAppContext();

		root.setContextPath("/bookstore");
		// Parent loader priority is a class loader setting that Jetty accepts.
		// By default Jetty will behave like most web containers in that it will
		// allow your application to replace non-server libraries that are part of the
		// container. Setting parent loader priority to true changes this behavior.
		// Read more here: http://wiki.eclipse.org/Jetty/Reference/Jetty_Classloading
		root.setParentLoaderPriority(true);

		final String webappDirLocation = "src/main/webapp/";
		root.setDescriptor(webappDirLocation + "/WEB-INF/web.xml");
		root.setResourceBase(webappDirLocation);

		// ==== Authentication
		HashLoginService loginService = new HashLoginService("MyRealm");
		loginService.setConfig("myrealm.properties");
		server.addBean(loginService);
		// ====  Authentication
		
		// ====  Authorization
		Constraint constraint = new Constraint();
		constraint.setName("auth");
		constraint.setAuthenticate(true);
		constraint.setRoles(new String[] { "admin", "user" });

		ConstraintMapping mapping = new ConstraintMapping();
		mapping.setPathSpec("/*");
		mapping.setConstraint(constraint);
		
		// ==== Manipulador
		ConstraintSecurityHandler security = new ConstraintSecurityHandler();
		server.setHandler(security);
		
		security.setConstraintMappings(Arrays.asList(mapping));
		security.setAuthenticator(new BasicAuthenticator());
		security.setLoginService(loginService);
		// ==== Manipulador
		
		security.setHandler(root);
		
		// ==== Authorization
		
		server.start();
		server.join();
	}
}
