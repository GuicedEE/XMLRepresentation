package com.guicedee.modules.services.xmlrepresentation;

/**
 * Unchecked exception raised when XML marshalling or unmarshalling fails.
 * <p>
 * This exception wraps underlying reflection, JAXB, and streaming failures
 * to avoid leaking low-level exceptions to callers of XML rendering utilities.
 */
public class XmlRenderException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new exception without a message or cause.
	 */
	public XmlRenderException()
	{
		//No config required
	}
	
	/**
	 * Creates a new exception with the supplied message.
	 *
	 * @param message the detail message
	 */
	public XmlRenderException(String message)
	{
		super(message);
	}
	
	/**
	 * Creates a new exception with the supplied message and cause.
	 *
	 * @param message the detail message
	 * @param cause   the underlying failure
	 */
	public XmlRenderException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	/**
	 * Creates a new exception with the supplied cause.
	 *
	 * @param cause the underlying failure
	 */
	public XmlRenderException(Throwable cause)
	{
		super(cause);
	}
	
	/**
	 * Creates a new exception with full control of suppression and stack trace behavior.
	 *
	 * @param message            the detail message
	 * @param cause              the underlying failure
	 * @param enableSuppression  whether suppression is enabled
	 * @param writableStackTrace whether the stack trace should be writable
	 */
	public XmlRenderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
