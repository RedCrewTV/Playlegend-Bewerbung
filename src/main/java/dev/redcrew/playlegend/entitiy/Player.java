package dev.redcrew.playlegend.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

@Entity
@Table(name = "t_Player")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 32)
    private String name;
}
