package work.lqdfxnet.lqdfxsextras;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Mob Rules
    public static ModConfigSpec.BooleanValue mrCreepersBurn;
    public static ModConfigSpec.BooleanValue mrVexSpawn;
    public static ModConfigSpec.BooleanValue mrEvokerDeath;
    public static ModConfigSpec.BooleanValue mrNetherSkeleton;

    // Mob Griefing
    public static ModConfigSpec.BooleanValue mrCreeperGriefing;
    public static ModConfigSpec.BooleanValue mrEndermanGriefing;
    public static ModConfigSpec.BooleanValue mrFarmLand;
    public static ModConfigSpec.BooleanValue mrGhastGriefing;
    public static ModConfigSpec.BooleanValue mrSilverfishGriefing;


    // Better Mining
    public static ModConfigSpec.BooleanValue imsEnable;
    public static ModConfigSpec.ConfigValue<List<? extends String>> imsTools;
    public static ModConfigSpec.BooleanValue imsTNTBreaksEnable;
    public static ModConfigSpec.IntValue imsTNTBreakRadius;

    // Better Hoes
    public static ModConfigSpec.BooleanValue ihuReplantEnabled;
    public static ModConfigSpec.BooleanValue ihuUnTillEnabled;
    public static ModConfigSpec.IntValue ihuEfficiencyLvl;
    public static ModConfigSpec.ConfigValue<List<? extends String>> ihuTools;

    static {

        /* -----------------------------------------------------------
         * Mob Rules
         * ----------------------------------------------------------- */
        BUILDER.comment("Mob behavior rules").push("Mob Rules");

        mrCreepersBurn = BUILDER.comment("Creepers burn in daylight").define("creepers_burn", true);
        mrVexSpawn = BUILDER.comment("Allow vexes to spawn normally (TRUE Vanilla behaviour)").define("vex_spawn", true);
        mrEvokerDeath = BUILDER.comment("Evoker death despawns Vex").define("evoker_death", false);
        mrNetherSkeleton = BUILDER.comment("Replace Skeletons in Nether with Wither Skeletons").define("all_nether_skeleton", false);

        mrCreeperGriefing = BUILDER.comment("Creeper explosions damage blocks (TRUE Vanilla behaviour)").define("creeper_griefing", true);
        mrEndermanGriefing = BUILDER.comment("Endermen pick up blocks (TRUE Vanilla behaviour)").define("enderman_griefing", true);
        mrFarmLand = BUILDER.comment("Mobs trample farmland (TRUE Vanilla behaviour)").define("farm_land_griefing", true);
        mrGhastGriefing = BUILDER.comment("Ghast fireballs damage terrain (TRUE Vanilla behaviour)").define("ghast_griefing", true);
        mrSilverfishGriefing = BUILDER.comment("Silverfish infest blocks (TRUE Vanilla behaviour)").define("silverfish_griefing", true);


        BUILDER.pop();

        /* -----------------------------------------------------------
         * Improved Mining Speeds
         * ----------------------------------------------------------- */
        BUILDER.comment("Improved Mining Speed").push("Improved Mining Speed");

        imsEnable = BUILDER.comment("Enable Improved mining speed?").define("ims_enabled", true);

        imsTools = BUILDER
                .comment("Tools that should get a speed bump:")
                .defineListAllowEmpty("ims_tools", List.of("minecraft:diamond_pickaxe", "minecraft:netherite_pickaxe"), () -> "", Config::validateItem);

        imsTNTBreaksEnable = BUILDER.comment("Enable TNT breaks Bedrock").define("ims_tnt_enabled", true);

        imsTNTBreakRadius = BUILDER
                .comment("Radius of Bedrock affected by blast.\nNote: TNT must be on Bedrock to be affected!")
                .defineInRange("ims_tnt_radius",1,1,3);

        BUILDER.pop();

        /* -----------------------------------------------------------
         * Improved Hoe Use
         * ----------------------------------------------------------- */
        BUILDER.comment("Improved Hoe Use").push("Improved Hoe Use");

        ihuReplantEnabled = BUILDER.comment("Enable Crop replanting").define("ihu_CropReplanting", true);

        ihuUnTillEnabled = BUILDER.comment("Enable Un Tilling farmland?").define("ihu_UnTilling", true);

        ihuTools = BUILDER
                .comment("Tools that use Improved Hoe Use")
                .defineListAllowEmpty("ihu_tools",
                        List.of("minecraft:wooden_hoe","minecraft:stone_hoe","minecraft:copper_hoe","minecraft:iron_hoe","minecraft:golden_hoe","minecraft:diamond_hoe", "minecraft:netherite_hoe"),
                        () -> "",
                        Config::validateItem);

        ihuEfficiencyLvl = BUILDER
                .comment("Extra efficiency level applied to tilling")
                .defineInRange("ihu_efficiency_lvl", 1, 0, 5);

        BUILDER.pop();

    }

    private static boolean validateItem(final Object obj) {
        return obj instanceof String toolName && BuiltInRegistries.ITEM.containsKey(Identifier.parse(toolName));
    }

    /*
    private static boolean validateEntity(final Object obj) {
        return obj instanceof String entityName && BuiltInRegistries.ENTITY_TYPE.containsKey(Identifier.parse(entityName));
    }

    private static boolean validateBlock(final Object obj) {
        return obj instanceof String blockName && BuiltInRegistries.BLOCK.containsKey(Identifier.parse(blockName));
    }


    public static Set<Block> bm_blocks_affected;
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        bm_blocks_affected = bmBlocksAffected.get().stream().map(blockName -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(blockName))).collect((Collectors.toSet()));
    }
     */

    public static final ModConfigSpec SPEC = BUILDER.build();
}
