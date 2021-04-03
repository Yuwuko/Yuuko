package com.yuuko.i18n;

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
     * @param language {@link String}
     * @param command {@link String}
     * @param key {@link String}
     * @return {@link String}
     */
    public static String get(String language, String command, String key) {
        return languages.getOrDefault(language, languages.get("en")).getCommands().get(command).getText().get(key);
    }

    /**
     * Returns localised text based on given language
     * @param language {@link String}
     * @param auxiliary {@link String}
     * @param key {@link String}
     * @return {@link String}
     */
    public static String get(String language, String auxiliary, String key, boolean... flag) {
        return languages.getOrDefault(language, languages.get("en")).getAuxiliary().get(auxiliary).getText().get(key);
    }

    /**
     * Language class used when parsing lang/ YAML files to create usable objects.
     * Command and Auxiliary are functionally the same but Auxiliary is used for non-command based feedback.
     */
    public static class Language {
        private HashMap<String, Command> commands;
        private HashMap<String, Auxiliary> auxiliary;

        public Language() {
        }

        public HashMap<String, Command> getCommands() {
            return commands;
        }
        public void setCommands(HashMap<String, Command> commands) {
            this.commands = commands;
        }
        public HashMap<String, Auxiliary> getAuxiliary() {
            return auxiliary;
        }
        public void setAuxiliary(HashMap<String, Auxiliary> auxiliary) {
            this.auxiliary = auxiliary;
        }

        public static class Command {
            private HashMap<String, String> text;

            public Command() {
            }

            public HashMap<String, String> getText() {
                return text;
            }
            public void setText(HashMap<String, String> text) {
                this.text = text;
            }
        }

        public static class Auxiliary {
            private HashMap<String, String> text;

            public Auxiliary() {
            }

            public HashMap<String, String> getText() {
                return text;
            }
            public void setText(HashMap<String, String> text) {
                this.text = text;
            }
        }
    }
}
