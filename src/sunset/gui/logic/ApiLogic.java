package sunset.gui.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import sunset.gui.api.binary.BinaryFileLocator;
import sunset.gui.api.spec.ApiSpecification;
import sunset.gui.api.spec.SampleCode;
import sunset.gui.api.spec.Snippet;
import sunset.gui.api.spec.SnippetCode;
import sunset.gui.api.spec.SnippetList;
import sunset.gui.interfaces.IProperties;

public class ApiLogic {

	private static ApiLogic instance = new ApiLogic();
	private static HashMap<String, Object> apiCache = new HashMap<String, Object>();
	
	private static final String APISPECFILE = "FFaplAPISpec.bin";
	private static final String SAMPLESFILE = "FFaplSampleCode.bin";
	private static final String DEFAULTSNIPPETSFILE = "FFaplSnippets.bin";
	private static final String CUSTOMSNIPPETSFILE = "customCode.bin";
	private static final String SNIPPETS_XML = "customCode.xml";	// for backwards compatibility

	private static String customCodeFilePath = IProperties.getUserHomePath() + CUSTOMSNIPPETSFILE;
	private static String customCodeFilePath_XML = IProperties.getUserHomePath() + SNIPPETS_XML;
	
	public static ApiLogic getInstance() {
		return instance;
	}

	/*
	 * Constructor is private because Singleton pattern is used 
	 */
	private ApiLogic() {

	}

	/*
	 * @return deserialized Object of class ApiSpecification
	 */
	public ApiSpecification getApiSpecification() {
		return getInternalApiObject(APISPECFILE, ApiSpecification.class);
	}
	
	/*
	 * @return deserialized Object of class SampleCode
	 */
	public SampleCode getSamples() {
		return getInternalApiObject(SAMPLESFILE, SampleCode.class);
	}
	
	/*
	 * @return deserialized Object of class SnippetCode
	 * converts XML file into binary file and then deserializes the binary file
	 */
	public SnippetCode getSnippetCode() {
		File fileXML = new File(customCodeFilePath_XML);
		File fileBin = new File(customCodeFilePath);
		
		// if customCode.XML file exists, convert it to binary file and delete it afterwards
		if(fileXML.exists()) {
			convertXMLtoBin(fileXML, fileBin);
			
			fileXML.delete();
		}
		
		SnippetCode snippetCode = null;
		
		if (fileBin.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(fileBin); 
				
				snippetCode = getApiObject(CUSTOMSNIPPETSFILE, fileInputStream, SnippetCode.class);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		if (snippetCode == null) {
			snippetCode = new SnippetCode();
			snippetCode.setSnippetList(new SnippetList());
			apiCache.put(CUSTOMSNIPPETSFILE, snippetCode);
		}
		
		SnippetCode snippetCodeDefault = getInternalApiObject(DEFAULTSNIPPETSFILE, SnippetCode.class);
		
		if (snippetCodeDefault != null) {
			for (Snippet snippet : snippetCodeDefault.getSnippetList().getSnippet()) {
				if (!snippetCode.getSnippetList().getSnippet().contains(snippet))
					snippetCode.getSnippetList().addSnippet(snippet);
			}
		}
		
		return snippetCode;
	}
	
	/*
	 * @param fileXML the XML file which needs to be converted
	 * @param fileBin the binary file which is the result of the conversion
	 * reads the XML file, parses the content and creates an object of type SnippetCode which is serialized into the binary file
	 */
	private void convertXMLtoBin(File fileXML, File fileBin) {
		try {
			FileReader fileReader = new FileReader(fileXML);
			char[] buffer = new char[(int) fileXML.length()];
			
			fileReader.read(buffer);
			fileReader.close();
			
			String[] snippetStrings = String.valueOf(buffer).split("<snippet>");
			SnippetList snippetList = new SnippetList();
			
			for (int i = 1; i < snippetStrings.length; i++) {	// first string is no snippet
				Snippet snippet = getSnippetFromText(snippetStrings[i]);
				
				if (snippet != null)
					snippetList.addSnippet(snippet);
			}

			SnippetCode snippetCode = new SnippetCode();
			snippetCode.setSnippetList(snippetList);
			
			FileOutputStream fileOutputStream = new FileOutputStream(fileBin);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(snippetCode);
			objectOutputStream.flush();
			objectOutputStream.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * @param text the code snippet text which needs to be parsed
	 * @returns the parsed Snippet object
	 */
	private Snippet getSnippetFromText(String text) {
		final String NAME_TAG_START = "<name>";
		final String NAME_TAG_END = "</name>";
		final String DESC_TAG_START = "<description>";
		final String DESC_TAG_END = "</description>";
		final String BODY_TAG_START = "<body>";
		final String BODY_TAG_END = "</body>";
		
		try {
			String name, desc, body;
			
			name = text.substring(text.indexOf(NAME_TAG_START) + NAME_TAG_START.length(), 
					text.indexOf(NAME_TAG_END));
			
			desc = text.substring(text.indexOf(DESC_TAG_START) + DESC_TAG_START.length(), 
					text.indexOf(DESC_TAG_END));
			
			body = text.substring(text.indexOf(BODY_TAG_START) + BODY_TAG_START.length(), 
					text.indexOf(BODY_TAG_END));
			
			body = body.replace("&#13;", "");	// eliminate carriage return code from body
			
			return new Snippet(name, desc, body);
			
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * @throws IOException if Java Serialization fails
	 * serializes the SnippetCode Object from the API cache into the binary file
	 */
	public void persistSnippetCode() throws IOException {
		SnippetCode snippetCode = (SnippetCode) apiCache.get(CUSTOMSNIPPETSFILE);
		
		if(snippetCode != null) {
			File file = new File(customCodeFilePath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(snippetCode);
			objectOutputStream.flush();
			objectOutputStream.close();
		}
	}
	
	/*
	 * @param snippet the snippet which needs to be persisted
	 * @throws IOException if Java Serialization fails
	 */
	public void persistSnippetCode(Snippet snippet) throws IOException {
		SnippetCode snippetCode = (SnippetCode) apiCache.get(CUSTOMSNIPPETSFILE);
		
		if(snippetCode != null) {
			if(!snippetCode.getSnippetList().getSnippet().contains(snippet)) {
				snippetCode.getSnippetList().getSnippet().add(snippet);
			}
		}
		
		persistSnippetCode();
	}

	/*
	 * @param snippet the snippet which needs to be deleted
	 * @throws IOException if Java Serialization fails
	 */
	public void deleteSnippetCode(Snippet snippet) throws IOException {
		SnippetCode snippetCode = (SnippetCode) apiCache.get(CUSTOMSNIPPETSFILE);
		
		if(snippetCode != null){
			if(snippetCode.getSnippetList().getSnippet().contains(snippet)) {
				snippetCode.getSnippetList().getSnippet().remove(snippet);
				persistSnippetCode();	
			}
		}	
	}

	/*
	 * @param key the filename which of the according API object
	 * @param inputStream the InputStream for the specified file
	 * @param clazz the Class type T which should be returned
	 * @returns an Object of type T
	 * if API Object specified by the key does not exist in the API cache the object is deserialized using the inputStream and the spezified class type T
	 */
	@SuppressWarnings("unchecked")
	private <T> T getApiObject(String key, InputStream inputStream, Class<T> clazz) {
		T apiObject = null;
		
		try {
			apiObject = (T) apiCache.get(key);
			
			if (apiObject == null) {
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				apiObject = (T) objectInputStream.readObject();
				objectInputStream.close();
				apiCache.put(key, apiObject);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return apiObject;
	}

	/*
	 * @param fileName the file name which should be used for deserializations
	 * @param clazz the Class type T which should be returned
	 * @returns an object of Class T
	 */
	private <T> T getInternalApiObject(String fileName, Class<T> clazz) {
		T apiObject = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;

		apiObject = getApiObject(fileName, BinaryFileLocator.getInstance().getResource(fileName), clazz);
		return apiObject;
	}
}
