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
	private static final String SNIPPETSFILE = "customCode.bin";
	private static final String SNIPPETS_XML = "customCode.xml";

	private static String customCodeFilePath = IProperties.getUserHomePath() + SNIPPETSFILE;
	private static String customCodeFilePath_XML = IProperties.getUserHomePath() + SNIPPETS_XML;
	
	public static ApiLogic getInstance() {
		return instance;
	}

	private ApiLogic() {
	}

	public ApiSpecification getApiSpecification() {
		return getInternalApiObject(APISPECFILE, ApiSpecification.class);
	}
	
	public SampleCode getSamples() {
		return getInternalApiObject(SAMPLESFILE, SampleCode.class);
	}
	
	public SnippetCode getSnippetCode() {
		File fileXML = new File(customCodeFilePath_XML);
		File fileBin = new File(customCodeFilePath);
		
		// if customCode.XML file exists, convert it to binary file and delete it afterwards
		if(fileXML.exists()) {
			convertXMLtoBin(fileXML, fileBin);
			
			fileXML.delete();
		}
		
		if (fileBin.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(fileBin); 
				
				return getApiObject(SNIPPETSFILE, fileInputStream, SnippetCode.class);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		SnippetCode custom = new SnippetCode();
		custom.setSnippetList(new SnippetList());
		apiCache.put(SNIPPETSFILE, custom);
		
		return custom;
	}
	
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
	
	public void persistSnippetCode() throws IOException {
		SnippetCode snippetCode = (SnippetCode) apiCache.get(SNIPPETSFILE);
		
		if(snippetCode != null) {
			File file = new File(customCodeFilePath);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(snippetCode);
			objectOutputStream.flush();
			objectOutputStream.close();
		}
	}
	
	public void persistSnippetCode(Snippet snippet) throws IOException {
		SnippetCode snippetCode = (SnippetCode) apiCache.get(SNIPPETSFILE);
		
		if(snippetCode != null) {
			if(!snippetCode.getSnippetList().getSnippet().contains(snippet)) {
				snippetCode.getSnippetList().getSnippet().add(snippet);
			}
		}
		
		persistSnippetCode();
	}

	public void deleteSnippetCode(Snippet snippet) throws IOException {
		SnippetCode snippetCode = (SnippetCode) apiCache.get(SNIPPETSFILE);
		
		if(snippetCode != null){
			if(snippetCode.getSnippetList().getSnippet().contains(snippet)) {
				snippetCode.getSnippetList().getSnippet().remove(snippet);
				persistSnippetCode();	
			}
		}	
	}

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

	private <T> T getInternalApiObject(String fileName, Class<T> clazz) {
		T apiObject = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;

		apiObject = getApiObject(fileName, BinaryFileLocator.getInstance().getResource(fileName), clazz);
		return apiObject;
	}
}
