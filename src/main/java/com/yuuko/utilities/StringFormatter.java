package com.yuuko.utilities;

import java.util.Arrays;

public final class StringFormatter {

    private String string = "";

    @Override
    public String toString() {
        return string;
    }

    /**
     * Builder class for StringFormatter
     */
    public static class Builder {
        private String string;

        public Builder() {
        }

        public Builder string(String string) {
            if(!sane(string)) {
                return this;
            }

            this.string = string;
            return this;
        }

        public Builder encase(String string) {
            if(!sane(string)) {
                return this;
            }

            this.string = string + this.string + string;
            return this;
        }

        public Builder prepend(String string) {
            if(!sane(string)) {
                return this;
            }

            this.string = string + this.string;
            return this;
        }

        public Builder append(String string) {
            if(!sane(string)) {
                return this;
            }

            this.string = this.string + string;
            return this;
        }

        /**
         * Rudimentary formatting for integers. (adding the ,'s)

         * @return formatted string.
         */
        public Builder formatIntegers() {
            String[] splitStrings = string.split(" ");
            String[] formattedSplitStrings = new String[splitStrings.length];

            for(int i = 0; i < splitStrings.length; i++) {
                if(isNumber(splitStrings[i])) {
                    char[] splitInt = splitStrings[i].toCharArray();
                    StringBuilder formattedInt = new StringBuilder();
                    for(int x = 0; x < splitInt.length; x++) {
                        if((splitInt.length - x) % 3 == 0 && x != 0) {
                            formattedInt.append(",");
                        }
                        formattedInt.append(splitInt[x]);
                    }

                    formattedSplitStrings[i] = formattedInt.toString();
                } else {
                    formattedSplitStrings[i] = splitStrings[i];
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            for(String string: formattedSplitStrings) {
                stringBuilder.append(string).append(" ");
            }
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");

            this.string = stringBuilder.toString();

            return this;
        }

        public StringFormatter build() {
            StringFormatter stringFormatter = new StringFormatter();
            stringFormatter.string = this.string;

            return stringFormatter;
        }

        /**
         * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
         *
         * @param string String
         * @return boolean
         */
        private boolean isNumber(String string) {
            return Arrays.stream(string.split("")).allMatch(character -> Character.isDigit(character.charAt(0)));
        }

        private boolean sane(String string) {
            return string != null;
        }
    }
}
