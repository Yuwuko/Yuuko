package com.yuuko.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

public class I18n {
    private static final Logger log = LoggerFactory.getLogger(I18n.class);
    private static final HashMap<String, Language> languages = new HashMap<>();

    public static void setup() {
        try {
            Path dirPath = Paths.get(I18n.class.getClassLoader().getResource("./lang").toURI());
            Yaml yaml = new Yaml();
            Files.list(dirPath).forEach(path -> {
                File file = path.toFile();
                try(InputStream inputStream = new FileInputStream(file)) {
                    Language data = yaml.load(inputStream);
                    languages.put(file.getName().replace(".yaml", ""), data);
                } catch(Exception e) {
                    log.error("An error occurred while parsing i18n files, message: {}", e.getMessage(), e);
                }
            });
        } catch(Exception e) {
            log.error("An error occurred while loading language files, error: {}", e.getMessage(), e);
            System.exit(1);
        }

        log.info("Successfully loaded lang files: {}", languages.keySet());
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
    public static String get(String language, String module, String command, String key) {
        return languages.getOrDefault(language, languages.get("en")).getModule(module).getCommand(command).getValue(key);
    }

    /**
     * Returns localised text based on given language
     * @param language {@link String}
     * @param mod {@link String}
     * @param key {@link String}
     * @return {@link String}
     */
    public static String get(String language, String mod, String key, boolean... flag) {
        return languages.getOrDefault(language, languages.get("en")).getAuxiliary().get(mod).getText().get(key);
    }

    /**
     * Language class used when parsing lang/ YAML files to create usable objects.
     * Command and Auxiliary are functionally the same but Auxiliary is used for non-command based feedback.
     */
    public static class Language {
        private HashMap<String, Module> modules = new HashMap<>();
        private HashMap<String, Auxiliary> auxiliary = new HashMap<>();
        private final Module dummy = new Module().addDummy(); // dummy module for when things go really really wrong

        public Language() {
        }

        public HashMap<String, Module> getModules() {
            return modules;
        }
        public Module getModule(String module) {
            return modules.getOrDefault(module, dummy);
        }
        public void setModules(HashMap<String, Module> modules) {
            this.modules = modules;
        }

        public HashMap<String, Auxiliary> getAuxiliary() {
            return auxiliary;
        }
        public void setAuxiliary(HashMap<String, Auxiliary> auxiliary) {
            this.auxiliary = auxiliary;
        }

        /**
         *
         *
         *
         */
        public static class Module {
            private HashMap<String, Command> commands = new HashMap<>();
            private final Command dummy = new Command().addDummy(); // dummy command for when things go really wrong

            public Module() {
            }

            public Command getCommand(String command) {
                return commands.getOrDefault(command, dummy);
            }

            public HashMap<String, Command> getCommands() {
                return commands;
            }

            public void setCommands(HashMap<String, Command> commands) {
                this.commands = commands;
            }

            public Module addDummy() {
                commands.put("missing", dummy);
                return this;
            }

        }

        /**
         *
         *
         *
         */
        public static class Command {
            private HashMap<String, String> values = new HashMap<>();

            public Command() {
            }

            public String getValue(String key) {
                return values.getOrDefault(key, "CONTACT DEV");
            }

            public HashMap<String, String> getValues() {
                return values;
            }

            public void setValues(HashMap<String, String> values) {
                this.values = values;
            }

            public Command addDummy() {
                values.put("missing", "CONTACT DEV");
                return this;
            }
        }

        /**
         *
         *
         *
         */
        public static class Auxiliary {
            private HashMap<String, String> text = new HashMap<>();

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
