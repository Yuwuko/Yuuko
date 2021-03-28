package com.yuuko.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class I18n {
    private static final Logger log = LoggerFactory.getLogger(I18n.class);
    private static final LinkedHashMap<String, Language> languages = new LinkedHashMap<>();

    public I18n() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("lang/")))) {
            Yaml yaml = new Yaml();
            while(reader.ready()) {
                String langFile = reader.readLine();
                Language langData = yaml.load(getClass().getClassLoader().getResourceAsStream("lang/"+langFile));
                System.out.println(Arrays.toString(langData.getCommands().values().toArray()));
                languages.put(langFile.split("\\.")[0], langData);
            }
        } catch(Exception e) {
            log.error("An error occurred while parsing i18n files, message: {}", e.getMessage(), e);
        }
    }

    public String getText(String language, String command, String text) {
        return languages.get(language).getCommands().get(command).getText().get(text);
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
