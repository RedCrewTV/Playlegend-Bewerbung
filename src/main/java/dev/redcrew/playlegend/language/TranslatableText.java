package dev.redcrew.playlegend.language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;


@Getter
@AllArgsConstructor
public final class TranslatableText {

    private final Component text;


    @SafeVarargs
    public static TranslatableText of(@NotNull String id, @NotNull Language language, Tuple<String, String>... args) {
        return new TranslationHelper().fetchTranslatableText(id, language, args);
    }

}
