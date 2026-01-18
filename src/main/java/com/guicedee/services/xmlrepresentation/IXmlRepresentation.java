package com.guicedee.services.xmlrepresentation;

import com.guicedee.client.utils.Pair;
import jakarta.xml.bind.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * Defines XML marshalling and unmarshalling behavior for a type.
 * <p>
 * Implementors gain default helpers that serialize the implementing instance
 * to XML or create a new instance from XML using JAXB. JAXB contexts are cached
 * in {@link XmlContexts} to avoid repeated initialization overhead.
 *
 * @param <J> the concrete type represented by this interface
 */
public interface IXmlRepresentation<J>
{
	
	@SuppressWarnings("unchecked")
	/**
	 * Creates an instance of the supplied type from the provided XML string.
	 * <p>
	 * If the target type is not annotated with a root element, the unmarshaller
	 * wraps the XML with a {@link JAXBElement} created from the simple class name.
	 * Parser settings for external entities and DTD support are taken from
	 * {@link #isResolveExternalEntities()} and {@link #isSupportDTD()}.
	 *
	 * @param xml  the XML document or fragment to parse
	 * @param type the class to instantiate and populate
	 * @return a populated instance of the requested type
	 * @throws XmlRenderException if reflection, JAXB, or streaming fails
	 */
	default J fromXml(String xml, Class<J> type)
	{
		try
		{
			J instance = type.getDeclaredConstructor()
							.newInstance();
			JAXBContext context = null;
			if (XmlContexts.JAXB.containsKey(type))
			{
				context = XmlContexts.JAXB.get(type);
			} else
			{
				context = JAXBContext.newInstance(type);
				XmlContexts.JAXB.put(type, context);
			}
			JAXBIntrospector introspector = context.createJAXBIntrospector();
			Unmarshaller unmarshaller = context.createUnmarshaller();
			try (StringReader sr = new StringReader(xml))
			{
				if (null == introspector.getElementName(instance))
				{
					XMLInputFactory factory = XMLInputFactory.newFactory();
					factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, isResolveExternalEntities());
					factory.setProperty(XMLInputFactory.SUPPORT_DTD, isSupportDTD());
					XMLStreamReader streamReader = factory.createXMLStreamReader(
									sr);
					JAXBElement<J> customer = unmarshaller.unmarshal(streamReader, type);
					instance = customer.getValue();
				} else
				{
					instance = (J) unmarshaller.unmarshal(sr);
				}
			}
			return instance;
		} catch (IllegalAccessException T)
		{
			throw new XmlRenderException("Unable to IllegalAccessException ", T);
		} catch (IllegalArgumentException T)
		{
			throw new XmlRenderException("Unable to IllegalArgumentException ", T);
		} catch (InstantiationException T)
		{
			throw new XmlRenderException("Unable to InstantiationException ", T);
		} catch (NoSuchMethodException T)
		{
			throw new XmlRenderException("Unable to NoSuchMethodException ", T);
		} catch (SecurityException T)
		{
			throw new XmlRenderException("Unable to SecurityException ", T);
		} catch (InvocationTargetException T)
		{
			throw new XmlRenderException("Unable to InvocationTargetException ", T);
		} catch (JAXBException T)
		{
			throw new XmlRenderException("Unable to JAXBException ", T);
		} catch (XMLStreamException T)
		{
			throw new XmlRenderException("Unable to XMLStreamException ", T);
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Marshals this instance to XML using JAXB.
	 * <p>
	 * If the instance has no root element annotation, a synthetic root element
	 * is created using the simple class name. Pair instances are handled by
	 * building a JAXB context that includes the key and value types.
	 * Output formatting and fragment handling are governed by
	 * {@link #isFormattedOutput()} and {@link #isFragment()}.
	 *
	 * @return the serialized XML for this instance
	 * @throws XmlRenderException if marshalling fails
	 */
	default String toXml()
	{
		Object requestObject = this;
		try (StringWriter stringWriter = new StringWriter())
		{
			JAXBContext context = null;
			if (XmlContexts.JAXB.containsKey(requestObject.getClass()))
			{
				context = XmlContexts.JAXB.get(requestObject.getClass());
			} else
			{
				context = JAXBContext.newInstance(requestObject.getClass());
				XmlContexts.JAXB.put(requestObject.getClass(), context);
			}
			if (requestObject instanceof Pair)
			{
				Pair<?, ?> p = (Pair<?, ?>) requestObject;
				Class<?> keyType = p.getKey()
								.getClass();
				Class<?> valueType = p.getValue()
								.getClass();
				context = JAXBContext.newInstance(requestObject.getClass(), keyType, valueType);
			}
			JAXBIntrospector introspector = context.createJAXBIntrospector();
			Marshaller marshaller = context.createMarshaller();
			if (null == introspector.getElementName(requestObject))
			{
				
				@SuppressWarnings("rawtypes")
				JAXBElement<?> jaxbElement = new JAXBElement(new QName(requestObject.getClass()
								.getSimpleName()),
								requestObject.getClass(), requestObject);
				marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormattedOutput());
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, isFragment());
				marshaller.marshal(jaxbElement, stringWriter);
			} else
			{
				marshaller.marshal(requestObject, stringWriter);
			}
			return stringWriter.toString();
		} catch (Exception e)
		{
			throw new XmlRenderException("Unable to marshal string writer from log intercepter", e);
		}
	}
	
	/**
	 * Whether the XML parser should resolve external entities.
	 *
	 * @return {@code true} to allow resolving external entities
	 */
	default boolean isResolveExternalEntities()
	{
		return false;
	}
	
	/**
	 * Whether the XML parser should support DTD processing.
	 *
	 * @return {@code true} to allow DTD support
	 */
	default boolean isSupportDTD()
	{
		return false;
	}
	
	/**
	 * Whether the marshalled XML should be formatted with indentation and line breaks.
	 *
	 * @return {@code true} to enable formatted output
	 */
	default boolean isFormattedOutput()
	{
		return false;
	}
	
	/**
	 * Whether the marshaller should generate document level events.
	 * <p>
	 * When {@code true}, JAXB writes fragments without startDocument/endDocument
	 * events and without an XML declaration.
	 *
	 * @return {@code true} to marshal as a fragment
	 */
	default boolean isFragment()
	{
		return true;
	}
}
