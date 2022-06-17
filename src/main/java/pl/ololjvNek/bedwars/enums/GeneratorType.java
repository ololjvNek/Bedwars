package pl.ololjvNek.bedwars.enums;

import lombok.Getter;
import org.bukkit.Material;


public enum GeneratorType {

    IRON(Material.IRON_INGOT),
    GOLD(Material.GOLD_INGOT),
    DIAMOND(Material.DIAMOND),
    EMERALD(Material.EMERALD),
    FURNACE(Material.NETHER_STAR);

    @Getter private final Material generation;

    GeneratorType(Material material){
        generation = material;
    }
}
