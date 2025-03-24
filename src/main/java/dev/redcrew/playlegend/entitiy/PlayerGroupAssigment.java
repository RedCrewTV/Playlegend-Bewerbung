package dev.redcrew.playlegend.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
@Entity
@Table(name = "t_PlayerGroupAssignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerGroupAssigment {

    @EmbeddedId
    private PlayerGroupAssignmentId id;

    @Column(nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    private LocalDateTime expiresAt;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public PlayerGroupAssigment(Group group, Player player, LocalDateTime expiresAt) {
        this.group = group;
        this.player = player;
        this.expiresAt = expiresAt;
    }
}
