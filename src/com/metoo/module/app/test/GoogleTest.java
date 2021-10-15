package com.metoo.module.app.test;

import org.junit.Test;

import com.google.cloud.translate.Detection;
import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;

public class GoogleTest {
	
	 private static Translate translate;
	 
	@Test
	  public void testTranslateText() {
	    // [START translate_translate_text]
	    // TODO(developer): Uncomment these lines.
	    // import com.google.cloud.translate.*;
	    // Translate translate = TranslateOptions.getDefaultInstance().getService();

	   /* Translation translation = translate.translate("¡Hola Mundo!");
	    System.out.printf("Translated Text:\n\t%s\n", translation.getTranslatedText());*/
		
	
	    // [END translate_translate_text]
	  }
}
