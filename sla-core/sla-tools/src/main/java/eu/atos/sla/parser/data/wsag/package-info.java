/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
@XmlSchema(
    namespace="http://www.ggf.org/namespaces/ws-agreement",
    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
    attributeFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED,
    xmlns={
       @javax.xml.bind.annotation.XmlNs(prefix="wsag", namespaceURI="http://www.ggf.org/namespaces/ws-agreement"), 
       @javax.xml.bind.annotation.XmlNs(prefix="sla", namespaceURI="http://sla.atos.eu")
   }
)
@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type=Date.class,value=DateTimeAdapter.class)
    })
package eu.atos.sla.parser.data.wsag;

import java.util.Date;

import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import eu.atos.sla.parser.DateTimeAdapter;


