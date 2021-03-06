/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.datamodel;



public interface ITemplate {

	/*
	 * Internal generated ID
	 */
	Long getId();
	void setId(Long id);
	
	/**
	 * This template is recognized by external parties by this internally generated UUID. 
	 */
	
	String getUuid();
	void setUuid(String uuid);
	
	/**
	 * Template body. This is an ws-agreement-compliant xml.
	 * NOTE: String? Maybe there is a better type.
	 */
	String getText();
	void setText(String text);
	

	/** 
	 * Service from the context
	 */
	public String getServiceId();
	public void setServiceId(String serviceId);
	
	/** 
	 * Name from the template
	 */
	public String getName();
	public void setName(String name);
	
	
	/** 
	 * Provider from the template
	 */
	public IProvider getProvider();
	public void setProvider(IProvider provider); 
	
}
