/*******************************************************************************
 * Copyright � 2018 Atos Spain SA. All rights reserved.
 * This file is part of SLAM.
 * SLAM is free software: you can redistribute it and/or modify it under the terms of Apache 2.0
 * THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * See LICENSE file for full license information in the project root.
 *******************************************************************************/
package eu.atos.sla.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.atos.sla.datamodel.IAgreement;
import eu.atos.sla.datamodel.IGuaranteeTerm;
import eu.atos.sla.datamodel.ICompensationDefinition.IPenaltyDefinition;
import eu.atos.sla.datamodel.bean.PenaltyDefinition;
import eu.atos.sla.parser.data.wsag.Context.ServiceProvider;
import eu.atos.sla.util.ModelConversion.ServiceLevelParser;

public class ModelConversionTest {

	private IModelConverter modelConverter = new ModelConversion();

	public ModelConversionTest() throws ModelConversionException {
		((ModelConversion)modelConverter).setBusinessValueListParser(new BusinessValueListParser());
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private String parseServiceLevel(String slo) throws ModelConversionException {
		
		return ServiceLevelParser.parse(slo).getConstraint();
	}
	
	@Test
	public void testParserServiceLevel() throws ModelConversionException {
		String expected;
		String actual;
		expected = "Performance GT 0.1";
		
		/*
		 * accepted constraint
		 */
		actual = parseServiceLevel(
				String.format("{\"constraint\" : \"%s\"}", expected));
		
		assertEquals(expected, actual);
		
		/*
		 * constraint does not exist
		 */
		try {
			actual = parseServiceLevel(
					String.format("{\"thereisnoconstraint\" : \"%s\"}", expected));
			fail("ModelConversionException not thrown");
		} catch (ModelConversionException e) {
			assertTrue(true);
		}
		
		/*
		 * constraint is a json object, instead of string
		 */
		expected = "{\"hasMaxValue\":1.0}";
		actual = parseServiceLevel(
				String.format("{\"constraint\" : %s}", expected));
		
	}
	
	@Test
	public void testParseServiceLevelShouldPass() throws ModelConversionException {

		String expected;
		String actual;
		
		expected = "Performance GT 0.1";
		
		actual = parseServiceLevel(
				String.format("{\"constraint\" : \"%s\"}", expected));
		
		assertEquals(expected, actual);
	}

	private void checkParseSloFails(String slo) {
		try {
			parseServiceLevel(slo);
			fail("Parse of '"+ slo + "' should fail");
		} catch (ModelConversionException e) {
			/* 
			 * Does nothing
			 */
		}
	}
	@Test
	public void testParseServiceLevelShouldFail() {
		checkParseSloFails("{\"thereisnoconstraint\" : \"dont'care\"}");
	}
	
	private eu.atos.sla.parser.data.wsag.Agreement readXml(File f) 
			throws JAXBException, FileNotFoundException {
		
		JAXBContext jaxbContext = JAXBContext
				.newInstance(eu.atos.sla.parser.data.wsag.Agreement.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		eu.atos.sla.parser.data.wsag.Agreement result = 
				(eu.atos.sla.parser.data.wsag.Agreement) jaxbUnmarshaller.unmarshal(
						new FileReader(f));
		
		return result;
	}
	
	private File getResourceFile(String path) {
		
		return new File(this.getClass().getResource(path).getFile());
	}
	
	private void checkParseAgreementContext(eu.atos.sla.parser.data.wsag.Agreement agreementXML, 
			ServiceProvider rol) throws JAXBException, FileNotFoundException, ModelConversionException {
		
		
		String expectedProvider;
		String expectedConsumer;
		
		if (rol == null) {
			agreementXML.getContext().setServiceProvider("invalid value here");
			expectedProvider = null;
			expectedConsumer = null;
		}
		else {
			agreementXML.getContext().setServiceProvider(rol.toString());
			if (rol == ServiceProvider.AGREEMENT_INITIATOR) {
				expectedProvider = "initiator";
				expectedConsumer = "responder";
			} else if (rol == ServiceProvider.AGREEMENT_RESPONDER) {
				expectedConsumer = "initiator";
				expectedProvider = "responder";
			}
			else {
				throw new AssertionError();
			}
		}

		String actualProvider;
		String actualConsumer;
		try {
			IAgreement a = modelConverter.getAgreementFromAgreementXML(agreementXML, "");
			actualProvider = a.getProvider().getUuid();
			actualConsumer = a.getConsumer();
		} catch (ModelConversionException e) {
			actualProvider = null;
			actualConsumer = null;
		}
		
		/*
		 * Match provider
		 */
		if (rol == null) {
			assertNull(actualProvider);
			assertNull(actualConsumer);
		}
		else {
			assertEquals(expectedProvider, actualProvider);
			assertEquals(expectedConsumer, actualConsumer);
		}
	}
	
	@Test
	public void testParseAgreementContext() throws JAXBException, FileNotFoundException, ModelConversionException {
		File file = getResourceFile("/samples/test_parse_context.xml");
		eu.atos.sla.parser.data.wsag.Agreement agreementXML = readXml(file);
		
		checkParseAgreementContext(agreementXML, null);
		checkParseAgreementContext(agreementXML, ServiceProvider.AGREEMENT_RESPONDER);
		checkParseAgreementContext(agreementXML, ServiceProvider.AGREEMENT_INITIATOR);
	}
	
	@Test
	public void testCustomBusinessValue() throws JAXBException, FileNotFoundException, ModelConversionException {
		
		File file = getResourceFile("/samples/test_parse_business.xml");
		eu.atos.sla.parser.data.wsag.Agreement agreementXML = readXml(file);
		IAgreement a = modelConverter.getAgreementFromAgreementXML(agreementXML, "");
		
		IPenaltyDefinition[] expected = new IPenaltyDefinition[] {
			new PenaltyDefinition(1, new Date(0), "discount", "%", "35", "P1D"),
			new PenaltyDefinition(5, new Date(24*60*60*1000), "service", "", "sms", "P1M")
		};
		for (IGuaranteeTerm gt : a.getGuaranteeTerms()) {
			int i = 0;
			for (IPenaltyDefinition actual : gt.getBusinessValueList().getPenalties()) {
				
				assertEquals(expected[i], actual);
				i++;
			}
		}
	}
}
