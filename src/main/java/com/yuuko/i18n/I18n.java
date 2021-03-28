package com.yuuko.i18n;

import com.yuuko.events.entity.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class I18n {
    private static final Logger log = LoggerFactory.getLogger(I18n.class);
    private static final LinkedHashMap<String, Language> languages = new LinkedHashMap<>();

    public I18n() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("lang/")))) {
            Yaml yaml = new Yaml();
            while(reader.ready()) {
                String langFile = reader.readLine();
                Language langData = yaml.load(getClass().getClassLoader().getResourceAsStream("lang/"+langFile));
                languages.put(langFile.split("\\.")[0], langData);
            }
        } catch(Exception e) {
            log.error("An error occurred while parsing i18n files, message: {}", e.getMessage(), e);
        }
    }

    /**
     * Get a list of supported languages by language code.
     * @return {@link Set<String>}
     */
    public static Set<String> getSupportedLanguages() {
        return languages.keySet();
    }

    /**
     * Returns localised text based on given language
     * @param e {@link MessageEvent}
     * @param text {@link String}
     * @return {@link String}
     */
    public static String getText(MessageEvent e, String text) {
        return languages.get(e.getLanguage()).getCommands().get(e.getCommand().getName()).getText().get(text);
    }

    /**
     * Returns localised error based on given language
     * @param e {@link MessageEvent}
     * @param text {@link String}
     * @return {@link String}
     */
    public static String getError(MessageEvent e, String text) {
        return languages.get(e.getLanguage()).getCommands().get(e.getCommand().getName()).getError().get(text);
    }

    /**
     * Language class used when parsing lang/ YAML files to create usable objects.
     */
    public static class Language {
        private HashMap<String, Command> commands;

        public Language() {
        }

        public HashMap<String, Command> getCommands() {
            return commands;
        }
        public void setCommands(HashMap<String, Command> commands) {
            this.commands = commands;
        }

        public static class Command {
            private HashMap<String, String> text;
            private HashMap<String, String> error;

            public Command() {
            }

            public HashMap<String, String> getText() {
                return text;
            }
            public HashMap<String, String> getError() {
                return error;
            }
            public void setText(HashMap<String, String> text) {
                this.text = text;
            }
            public void setError(HashMap<String, String> error) {
                this.error = error;
            }
        }
    }
}
