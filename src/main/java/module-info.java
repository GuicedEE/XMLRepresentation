module com.guicedee.xmlrepresentation {
	exports com.guicedee.modules.services.xmlrepresentation;
	
	requires transitive org.glassfish.jaxb.runtime;

	requires transitive com.guicedee.client;
	
	requires static lombok;
	requires java.logging;
	requires java.xml;
	requires jakarta.xml.bind;
	requires jakarta.validation;
}