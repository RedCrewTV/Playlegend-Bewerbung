package dev.redcrew.playlegend.entitiy;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

/**
 * This file is a JavaDoc!
 * Created: 3/29/2025
 * <p>
 * Belongs to Playlegend
 * <p>
 *
 * @author RedCrew <p>
 * Discord: redcrew <p>
 * Website: <a href="https://redcrew.dev/">https://redcrew.dev/</a>
 */
@Entity
@Table(name = "t_PlayerDisplay")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDisplay {

    @EmbeddedId
    private PlayerDisplayId id;

    @MapsId("playerId")
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Player player;
}
