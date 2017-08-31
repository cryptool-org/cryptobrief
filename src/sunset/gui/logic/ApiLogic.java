package sunset.gui.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import sunset.gui.api.jaxb.ApiSpecification;
import sunset.gui.api.jaxb.ObjectFactory;
import sunset.gui.api.jaxb.SampleCode;
import sunset.gui.api.jaxb.Snippet;
import sunset.gui.api.jaxb.SnippetCode;
import sunset.gui.api.xml.XmlLoader;
import sunset.gui.interfaces.IProperties;

public class ApiLogic {

	private static ApiLogic instance = new ApiLogic();
	private static HashMap<String, Object> apiCache = new HashMap<String, Object>();
	
	private static String customCodeFilePath = IProperties.getUserHomePath() + "customCode.xml";

	public static ApiLogic getInstance() {
		return instance;
	}

	private ApiLogic() {

	}

	public ApiSpecification getApiSpecification() {
		ApiSpecification spec = getInternalApiObject("api.xml", ApiSpecification.class);
		return spec;
	}
	
	public SampleCode getSamples() {
		return getInternalApiObject("samples.xml", SampleCode.class);
	}
	
	public SnippetCode getSnippetCode() {
		File file = new File(customCodeFilePath);
		if(file.exists()){
			try {
				return getApiObject(file.toURI().toURL().getPath(), new FileInputStream(file), SnippetCode.class);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		SnippetCode custom = null;
		try {
			custom = new ObjectFactory().createSnippetCode();
			custom.setSnippetList(new ObjectFactory().createSnippetList());
			apiCache.put(file.toURI().toURL().getPath(), custom);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return custom;
	}
	
	public void persistSnippetCode() throws MalformedURLException, JAXBException{
		File file = new File(customCodeFilePath);
		SnippetCode snippetCode = (SnippetCode) apiCache.get(file.toURI().toURL().getPath());
		if(snippetCode != null){
			JAXBContext jc;
			Marshaller m;
			jc = JAXBContext.newInstance("sunset.gui.api.jaxb");
			m = jc.createMarshaller();
			m.marshal(snippetCode, file);
		}				
	}
	
	public void persistSnippetCode(Snippet snippet) throws MalformedURLException, JAXBException{
		File file = new File(customCodeFilePath);
		SnippetCode snippetCode = (SnippetCode) apiCache.get(file.toURI().toURL().getPath());
		if(snippetCode != null){
			if(!snippetCode.getSnippetList().getSnippet().contains(snippet)){
				snippetCode.getSnippetList().getSnippet().add(snippet);
			}
		}
		persistSnippetCode();
	}

	@SuppressWarnings("unchecked")
	private <T> T getApiObject(String key, InputStream inputStream, Class<T> clazz) {
		JAXBContext jc;
		Unmarshaller u;
		T apiObject = null;
		
		try {
			apiObject = (T) apiCache.get(key);
			if (apiObject == null) {
				jc = JAXBContext.newInstance("sunset.gui.api.jaxb");
				u = jc.createUnmarshaller();
				apiObject = (T) u.unmarshal(inputStream);
				apiCache.put(key, apiObject);
			}else{
				inputStream.close();
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
		}
		return apiObject;
	}

	private <T> T getInternalApiObject(String fileName, Class<T> clazz) {
		T apiObject = null;
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;

		apiObject = getApiObject(fileName, XmlLoader.getInstance().getResource(fileName), clazz);
		return apiObject;
	}

	public void delete(Snippet snippet) throws MalformedURLException, JAXBException {
		File file = new File(customCodeFilePath);
		SnippetCode snippetCode = (SnippetCode) apiCache.get(file.toURI().toURL().getPath());
		if(snippetCode != null){
			if(snippetCode.getSnippetList().getSnippet().contains(snippet)){
				snippetCode.getSnippetList().getSnippet().remove(snippet);
				persistSnippetCode();	
			}
		}
			
	}

	

	
}
