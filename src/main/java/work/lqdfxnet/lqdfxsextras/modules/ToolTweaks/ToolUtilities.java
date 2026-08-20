package work.lqdfxnet.lqdfxsextras.modules.ToolTweaks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

import static net.neoforged.neoforge.common.ItemAbilities.HOE_TILL;

public class ToolUtilities {

    /* -----------------------------------------------------------
     *  Check for Correct Tool based on Config
     * ----------------------------------------------------------- */
    public static boolean isConfiguredTool(ItemStack tool, List<? extends String> allowedTools) {
        Identifier toolId = tool.get(DataComponents.ITEM_MODEL);
        //debugInfo("Tool in hand: {}", toolId);
        if (toolId == null) return  false;
        String toolKey = toolId.toString();
        return allowedTools.contains(toolKey);
    }

    /* -----------------------------------------------------------
     * Check for Efficiency (return level)
     * ----------------------------------------------------------- */
    public static int checkEfficiency(ItemStack tool, LevelAccessor world) {
        return tool.getEnchantmentLevel(
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.EFFICIENCY)
        );
    }

    /* -----------------------------------------------------------
     *  Crop Age
     * ----------------------------------------------------------- */

    public static IntegerProperty getAgeProperty(Block block) {
        if (block instanceof BeetrootBlock) return BeetrootBlock.AGE;
        if (block instanceof CocoaBlock) return CocoaBlock.AGE;
        if (block instanceof CropBlock) return CropBlock.AGE;
        return null;
    }

    public static int getMaxAge(Block block) {
        if (block instanceof BeetrootBlock beetroot) return beetroot.getMaxAge();
        if (block instanceof CocoaBlock) return CocoaBlock.MAX_AGE;
        if (block instanceof CropBlock crop) return crop.getMaxAge();
        return -1;
    }

    /* -----------------------------------------------------------
     *  Consume Speed
     * ----------------------------------------------------------- */

    public static void consumeOneSeed(Player player, Item seedItem) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() == seedItem) {
                stack.shrink(1);
                return;
            }
        }
    }

    /* -----------------------------------------------------------
     *  Get Seed for Crop
     * ----------------------------------------------------------- */

    public static Item getSeedFromCrop(ServerLevel level, BlockState state, BlockPos pos) {
        Block block = state.getBlock();
        if (!(block instanceof CropBlock)) return null;

        // Build loot context
        LootParams.Builder builder = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, pos.getCenter())
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.TOOL, ItemStack.EMPTY);

        List<ItemStack> drops = state.getDrops(builder);

        // Find the seed item in the drops
        for (ItemStack drop : drops) {
            Item item = drop.getItem();
            ItemStack itemStack = new ItemStack(item);
            if (itemStack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) { return item; }
            // Carrots & potatoes drop themselves as seeds
            if (item instanceof Item) {
                if (block instanceof CropBlock crop) {
                    if (item == crop.asItem()) return item;
                }
            }
        }
        return null;
    }

    /* -----------------------------------------------------------
     *  REPLANTING LOGIC
     * ----------------------------------------------------------- */

    public static void replantCrop(ServerLevel level, BlockPos pos, BlockState oldState) {
        Block block = oldState.getBlock();
        if (!(block instanceof CropBlock crop)) return;
        BlockState newState = crop.getStateForAge(0);
        level.setBlock(pos, newState, Block.UPDATE_ALL);
    }

    /* -----------------------------------------------------------
     *  TIER RADIUS MAPPING
     * ----------------------------------------------------------- */

    public static int getHoeRadius(String hoeKey) {
        if (hoeKey == null) return 0;

        // You can move this to config if you want dynamic sizes
        if (hoeKey.contains("copper")) return 1;        // Low Tier 1 (3x3)
        if (hoeKey.contains("gold")) return 1;          // Low Tier 1 (3x3)
        if (hoeKey.contains("iron")) return 1;          // Low Tier 1 (3x3)
        if (hoeKey.contains("diamond")) return 2;       // High Tier 2 (5x5)
        if (hoeKey.contains("netherite")) return 2;     // High Tier 2 (5x5)

        return 0; // default No Tier 0 (1x1)
    }

    /* -----------------------------------------------------------
     *  UN-TILL FARMLAND: Converts farmland back to dirt.
     * ----------------------------------------------------------- */

    public static void unTill(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof FarmlandBlock)) return;
        BlockState dirt = BuiltInRegistries.BLOCK.getValue(Identifier.parse("minecraft:dirt")).defaultBlockState();
        level.setBlock(pos, dirt, Block.UPDATE_ALL);
    }

    /* -----------------------------------------------------------
     *  Checks block if it can be Tilled to farmland
     * ----------------------------------------------------------- */

    public static boolean canBeFarmland(Player player, BlockState state, BlockHitResult hit) {

        UseOnContext context = new UseOnContext(player, player.getUsedItemHand(), hit);
        BlockState modifiedState = state.getToolModifiedState(context, HOE_TILL, true);
        if (modifiedState == null) return false;
        return modifiedState.toString().contains("farmland");
    }
}