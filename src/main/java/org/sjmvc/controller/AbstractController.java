/**
 * Copyright (c) 2010 Ignasi Barrera
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.sjmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sjmvc.binding.BindingResult;
import org.sjmvc.binding.RequestParameterBinder;
import org.sjmvc.error.Error;
import org.sjmvc.error.ErrorType;
import org.sjmvc.error.Errors;
import org.sjmvc.validation.JPAValidator;
import org.sjmvc.validation.Validator;

/**
 * Base class for {@link Controller} implementations.
 * 
 * @author Ignasi Barrera
 */
public abstract class AbstractController implements Controller
{
	/** The attribute name where the model will be published. */
	public static final String MODEL_ATTRIBUTE = "model";

	/** The attribute name where the controller errors will be published. */
	public static final String ERRORS_ATTRIBUTE = "errors";

	/** List of errors produced during method execution. */
	final Errors errors = new Errors();

	/** The validator used to validate model objects. */
	private Validator validator;

	/** The view to return. */
	private String returnView;

	/** The model to render. */
	private Object model;

	/**
	 * Creates a new <code>AbstractController</code>.
	 */
	public AbstractController()
	{
		super();
		validator = new JPAValidator();
	}

	/**
	 * Internal method to implement the controller logic.
	 * <p>
	 * This method returns void and expects this method to set the view to
	 * render by calling the {@link #setView(String)} method.
	 * <p>
	 * The model must be also populated using the
	 * {@link #addModel(String, Object)} method in order to let the views render
	 * properly.
	 * 
	 * @param request The request.
	 * @param response The response.
	 * @throws Exception If an exception occurs during controller logic
	 *             execution.
	 */
	protected abstract void doExecute(final HttpServletRequest request,
			final HttpServletResponse response) throws Exception;

	@Override
	public String execute(final HttpServletRequest request,
			final HttpServletResponse response) throws ControllerException
	{
		// Clear previous data
		errors.clear();
		returnView = null;
		model = null;

		try
		{
			// Execute controller logic
			doExecute(request, response);

			if (returnView == null)
			{
				throw new Exception("There was no view set. "
						+ "Use the setView method to set the view to render");
			}
		}
		catch (Exception ex)
		{
			if (ex.getCause() instanceof ControllerException)
			{
				// If it is a Controller exception, just propagate it
				throw (ControllerException) ex.getCause();
			}

			throw new ControllerException(
					"Could not execute the Controller logic at "
							+ this.getClass().getName(),
					ex.getCause() == null ? ex : ex.getCause());
		}

		// Populate model and errors
		if (model != null)
		{
			request.setAttribute(MODEL_ATTRIBUTE, model);
		}

		request.setAttribute(ERRORS_ATTRIBUTE, errors.getErrors());

		return returnView;
	}

	/**
	 * Binds the request parameters to the given model object and populates the
	 * binding errors in the {@link #errors} property.
	 * 
	 * @param <T> The type of the model object to bind.
	 * @param model The model object where to bind the request parameters.
	 * @param request The request containing the input parameters.
	 */
	protected <T> void bind(T model, HttpServletRequest request)
	{
		RequestParameterBinder<T> binder = new RequestParameterBinder<T>(model,
				request);
		BindingResult<T> bindingErrors = binder.bind();
		errors.addAll(bindingErrors.getErrors());
	}

	/**
	 * Validate the given object model and populates the validation errors in
	 * the {@link #errors} property.
	 * 
	 * @param <T> The type of the model object to validate.
	 * @param model The object model to validate.
	 */
	protected <T> void validate(T model)
	{
		errors.addAll(validator.validate(model));
	}

	/**
	 * Binds the request parameters to the model object and validates it.
	 * 
	 * @param <T> The type of the model object to bind and validate.
	 * @param model The model object where to bind the request parameters.
	 * @param request The request containing the input parameters.
	 */
	protected <T> void bindAndValidate(T model, HttpServletRequest request)
	{
		bind(model, request);

		// Only validate if there are no binding errors
		if (!errors())
		{
			validate(model);
		}
	}

	/**
	 * Adds the given error to the {@link #errors} list.
	 * 
	 * @param error The error to add.
	 */
	protected void error(final String error)
	{
		errors.add(new Error(ErrorType.CONTROLLER, error));
	}

	/**
	 * Checks if there are any errors.
	 * 
	 * @return Boolean indicating if there are any errors.
	 */
	protected boolean errors()
	{
		return errors.hasErrors();
	}

	// Getters and setters

	protected void setView(String viewName)
	{
		returnView = viewName;
	}

	protected String getView()
	{
		return returnView;
	}

	public Object getModel()
	{
		return model;
	}

	public void setModel(Object model)
	{
		this.model = model;
	}

}
