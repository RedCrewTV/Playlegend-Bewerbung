package dev.redcrew.playlegend.entitiy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "t_Group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String name;

    @Column(length = 32)
    private String prefix;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int priority;

    public Group(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public Group(String name, int priority, String prefix) {
        this.name = name;
        this.priority = priority;
        this.prefix = prefix;
    }
}
