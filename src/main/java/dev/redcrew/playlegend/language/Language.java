package dev.redcrew.playlegend.language;

import dev.redcrew.playlegend.Playlegend;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@Getter
public enum Language {
    ENGLISH,
    GERMAN;

    private final Map<String, String> mappings = new HashMap<>();

    public static void loadLanguages() {
        for (Language language : values()) {
            YamlConfiguration configuration = new YamlConfiguration();
            try {
                configuration.load(
                        new File(Playlegend.getInstance().getDataFolder().getAbsolutePath() + File.separator + "languages") + File.separator + language.name().toLowerCase() + ".yml");
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }

            configuration.getKeys(false).forEach(key -> language.mappings.put(key, configuration.getString(key)));
        }
    }

    public static Language getPreferredLanguage(Player player) {
        if(player.locale().equals(Locale.GERMAN) || player.locale().equals(Locale.GERMANY)) {
            return GERMAN;
        }
        return ENGLISH;
    }

}
