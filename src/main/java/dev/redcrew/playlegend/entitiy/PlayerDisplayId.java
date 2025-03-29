package dev.redcrew.playlegend.entitiy;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.UUID;

/**
 * This file is a JavaDoc!
 * Created: 3/23/2025
 * <p>
 * Belongs to Playlegend
 * <p>
 *
 * @author RedCrew <p>
 * Discord: redcrew <p>
 * Website: <a href="https://redcrew.dev/">https://redcrew.dev/</a>
 */

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDisplayId implements Serializable {

    private UUID playerId;
    private String world;
    private int x;
    private int y;
    private int z;
}
