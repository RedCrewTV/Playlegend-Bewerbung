package dev.redcrew.playlegend.language;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public final class TranslationHelper {

    @SafeVarargs
    final TranslatableText fetchTranslatableText(@NotNull String id, @NotNull Language language, Tuple<String, String>... args) {
        String translation = language.getMappings().get(id);
        if(translation == null) return new TranslatableText(Component.text(id));
        Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(translation);

        for (Tuple<String, String> arg : args) {
            component = component.replaceText(config -> config
                    .match(arg.getFist())
                    .replacement(Component.text(arg.getSecond()))
            );
        }

        return new TranslatableText(component);
    }

}
