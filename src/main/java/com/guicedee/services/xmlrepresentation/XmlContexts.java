package com.guicedee.modules.services.xmlrepresentation;

import jakarta.xml.bind.JAXBContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared JAXB context cache for XML representation operations.
 * <p>
 * This cache avoids repeated {@link JAXBContext} creation when marshalling or
 * unmarshalling the same types. The map is package-private to keep the cache
 * close to the XML representation implementation details.
 */
class XmlContexts
{
	/**
	 * Cache of JAXB contexts keyed by the represented class.
	 */
	public static final Map<Class<?>, JAXBContext> JAXB = new HashMap<>();

	/**
	 * Utility class; no instances.
	 */
	private XmlContexts()
	{
	}
}
