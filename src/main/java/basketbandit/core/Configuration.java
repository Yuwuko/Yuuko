// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core;

/**
 * Configuration class, includes vital information about the bot such as Discord token and command prefix.
 */
public class Configuration {

    // Bot's ID.
    public static String BOT_ID;

    // Bot version.
    public static final String VERSION = "1.6.0";

    // Google API key.
    public static String GOOGLE_API;

    // TFL App ID.
    public static String TFL_ID;

    // TFL API Key.
    public static String TFL_API;

    // Bot's command prefix.
    public static final String PREFIX = "?";

    // Bot's status, e.g Playing with lottie's tits.
    static String STATUS = "with lottie's tits (" + PREFIX + ")";
}
