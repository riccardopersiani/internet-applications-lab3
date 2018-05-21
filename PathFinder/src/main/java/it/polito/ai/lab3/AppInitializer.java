package it.polito.ai.lab3;

import org.springframework.web.servlet.support.*;

// Configurazione Java per creare un progetto Spring WebMVC facendo l'override di 3 metodi
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	// Definisce il contesto dove avviene l'accesso a tutte le basi dati
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { RootConfig.class };
	}

	// Contento Web on top del RootConfig
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class };
	}

	// Definisce su che tipo di richieste il Dispatcher Servlet può operare
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
