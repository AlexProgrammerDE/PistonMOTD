package net.pistonmaster.pistonmotd.shared.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;
import net.pistonmaster.pistonmotd.shared.FaviconMode;

import java.util.List;

@Getter
@Configuration
public class PistonMOTDServerConfig {
  @Comment({
    "You can find color codes here: https://minecraft.tools/en/color-code.php",
    "Formatting comes after the color! &d&l will work, but not &l&d.",
    "MiniMessage formatting IS supported: https://docs.adventure.kyori.net/minimessage.html#template",
    "MiniMessage allows you to make rainbow colors and gradients!",
    "HEX/RGB colors ARE supported. (Only the MOTD)",
    "Hex format: &#FFFFFF or <#FFFFFF>",
    "Placeholders: <online> (Players online)",
    "<max> (Server max slots)",
    "<newline> adds a newline to your MOTD.",
    "<tps> shows the tps (Paper only)",
    "<center> centers the text of a line. Needs to be at the exact start of a line."
  })
  private Description description = new Description();

  @Comment({"", "Not supported on: Spigot (Paper works), Sponge"})
  private Version version = new Version();

  private Players players = new Players();

  @Comment({"", "The image of a server in the server list."})
  private Favicon favicon = new Favicon();

  @Comment({"", "Extensions to the functionality of PistonMOTD"})
  private Extensions extensions = new Extensions();

  @Comment({"", "Advanced features that should not be messed with unless you know what you're doing."})
  private Advanced advanced = new Advanced();

  @Getter
  @Configuration
  public static class Description {
    private boolean activated = true;
    private List<String> text = List.of(
      "Hello! <newline>World!",
      "Use &c<underlined>color <bold><yellow>codes!",
      "&ka &rFormatting &nworks&r too! &ka",
      "<#800080>Players: <online>/<max>",
      "<rainbow>RAINBOW COLOR SUPPORT! :D</rainbow>",
      "<center><yellow><bold>Centered text!<bold></yellow><newline>Second line!"
    );
  }

  @Getter
  @Configuration
  public static class Version {
    @Comment({
      "The version name is shown if the version of the client doesn't match the version of the server.",
      "<after_icon> adds a bunch of spaces so the text is after the icon. (Only for protocol)"
    })
    private Name name = new Name();

    @Comment({
      "Dangerous! This can show a wrong server version to the client.",
      "When the client is on a different protocol, it will show the version name set above."
    })
    private Protocol protocol = new Protocol();

    @Getter
    @Configuration
    public static class Name {
      private boolean activated = true;
      private String text = "&1My custom version name!";
    }

    @Getter
    @Configuration
    public static class Protocol {
      private boolean activated = false;
      private int value = 1;
    }
  }

  @Getter
  @Configuration
  public static class Players {
    @Comment({"Override your actual player count", "Not supported on: Spigot (Paper works)"})
    private Online online = new Online();

    @Comment("Override your max player count")
    private Max max = new Max();

    @Comment({"Will show ??? as the player count.", "Not supported on: Spigot (Paper works)"})
    private boolean hide = false;

    @Comment({"Box shown when you hover over the player count", "Not supported on: Spigot (Paper works)"})
    private Sample sample = new Sample();

    @Getter
    @Configuration
    public static class Online {
      private boolean activated = false;
      private int value = 0;
    }

    @Getter
    @Configuration
    public static class Max {
      private boolean activated = false;
      private int value = 10;
    }

    @Getter
    @Configuration
    public static class Sample {
      @Comment({
        "Restores the vanilla behavior for the sample box.",
        "The name of every player on the server will be shown in the sample box.",
        "Overrides the text feature from below."
      })
      private Vanilla vanilla = new Vanilla();

      private boolean activated = true;
      private List<String> text = List.of(
        "&3Hello world!",
        "&eNewline!",
        "&6    spaces!",
        "&d&lCombined",
        "&1Players: <online>/<max>"
      );

      @Getter
      @Configuration
      public static class Vanilla {
        private boolean activated = false;
        private List<String> hidden = List.of("Notch");
      }
    }
  }

  @Getter
  @Configuration
  public static class Favicon {
    private boolean activated = false;

    @Comment({
      "Favicons are stored in the /plugins/PistonMOTD/favicons folder.",
      "Favicons are 64x64 pixels and must be PNG. The file name does not matter for random mode.",
      "Valid: RANDOM, SINGLE"
    })
    private FaviconMode mode = FaviconMode.RANDOM;

    @Comment("The single favicon shown when SINGLE is selected.")
    private String single = "example.png";
  }

  @Getter
  @Configuration
  public static class Extensions {
    @Comment("Extensions for vanishing plugins")
    private Vanish vanish = new Vanish();

    @Comment("Player prefixes for the player sample")
    private Prefix prefix = new Prefix();

    @Getter
    @Configuration
    public static class Vanish {
      @Comment("Support for SuperVanish/PremiumVanish. Hide players from the player count/sample on Bukkit/BungeeCord/Velocity.")
      private boolean vanishApi = false;

      @Comment("Hide players from the vanilla player sample feature.")
      private boolean hideSample = true;

      @Comment("Hide players from the online player count. (Most vanish plugins already have that built in)")
      private boolean hideCount = false;
    }

    @Getter
    @Configuration
    public static class Prefix {
      @Comment("Use LuckPerms player prefixes if it is installed")
      private boolean luckperms = false;
    }
  }

  @Getter
  @Configuration
  public static class Advanced {
    @Comment("If activated the server will only be shown as supported for the following protocols.")
    private SupportedProtocol supportedProtocol = new SupportedProtocol();

    @Getter
    @Configuration
    public static class SupportedProtocol {
      private boolean activated = false;

      @Comment("Shown when the protocol is not supported.")
      private int unsupportedNumber = -1;

      @Comment({
        "The supported protocol versions.",
        "Protocol version numbers can be found here: https://wiki.vg/Protocol_version_numbers"
      })
      private List<Integer> numbers = List.of(757, 756);
    }
  }

  // Convenience accessor methods to maintain API compatibility
  public boolean isDescriptionActivated() {
    return description.isActivated();
  }

  public List<String> getDescriptionText() {
    return description.getText();
  }

  public boolean isPlayersHide() {
    return players.isHide();
  }

  public boolean isPlayersMaxActivated() {
    return players.getMax().isActivated();
  }

  public int getPlayersMaxValue() {
    return players.getMax().getValue();
  }

  public boolean isPlayersOnlineActivated() {
    return players.getOnline().isActivated();
  }

  public int getPlayersOnlineValue() {
    return players.getOnline().getValue();
  }

  public boolean isPlayersSampleActivated() {
    return players.getSample().isActivated();
  }

  public List<String> getPlayersSampleText() {
    return players.getSample().getText();
  }

  public boolean isPlayersSampleVanillaActivated() {
    return players.getSample().getVanilla().isActivated();
  }

  public List<String> getPlayersSampleVanillaHidden() {
    return players.getSample().getVanilla().getHidden();
  }

  public boolean isVersionNameActivated() {
    return version.getName().isActivated();
  }

  public String getVersionNameText() {
    return version.getName().getText();
  }

  public boolean isVersionProtocolActivated() {
    return version.getProtocol().isActivated();
  }

  public int getVersionProtocolValue() {
    return version.getProtocol().getValue();
  }

  public boolean isAdvancedSupportedProtocolActivated() {
    return advanced.getSupportedProtocol().isActivated();
  }

  public List<Integer> getAdvancedSupportedProtocolNumbers() {
    return advanced.getSupportedProtocol().getNumbers();
  }

  public int getAdvancedSupportedProtocolUnsupportedNumber() {
    return advanced.getSupportedProtocol().getUnsupportedNumber();
  }

  public boolean isExtensionVanishAPI() {
    return extensions.getVanish().isVanishApi();
  }

  public boolean isExtensionVanishHideSample() {
    return extensions.getVanish().isHideSample();
  }

  public boolean isExtensionVanishHideCount() {
    return extensions.getVanish().isHideCount();
  }

  public boolean isExtensionPrefixLuckperms() {
    return extensions.getPrefix().isLuckperms();
  }

  public boolean isFaviconActivated() {
    return favicon.isActivated();
  }

  public FaviconMode getFaviconMode() {
    return favicon.getMode();
  }

  public String getFaviconSingle() {
    return favicon.getSingle();
  }
}
