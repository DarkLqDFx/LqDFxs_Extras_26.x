package work.lqdfxnet.lqdfxsextras.modules.VillagerBucket;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Consumer;

public class InaBucketDropoff extends Item {
    public InaBucketDropoff(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.isShiftKeyDown()) { return InteractionResult.PASS; }

        if (level.isClientSide()) { return InteractionResult.SUCCESS; }

        CompoundTag villagerData = stack.get(InaBucketData.VILLAGER_DATA.get());
        if (villagerData == null) { return InteractionResult.PASS; }

        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        BlockPos targetPos = hit.getBlockPos();
        BlockState targetState = level.getBlockState(targetPos);
        BlockPos spawnPos = targetState.canBeReplaced()
                ? targetPos
                : targetPos.relative(hit.getDirection());

        if (!level.getBlockState(spawnPos).canBeReplaced()) {
            return InteractionResult.FAIL;
        }

        ValueInput input = TagValueInput.create(
                ProblemReporter.DISCARDING, level.registryAccess(), villagerData);

        Villager villager = new Villager(EntityType.VILLAGER, level);
        villager.load(input);

        villager.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
        villager.getBrain().eraseMemory(MemoryModuleType.HOME);
        villager.getBrain().eraseMemory(MemoryModuleType.MEETING_POINT);

        villager.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
        villager.setYRot(player.getYRot());
        villager.setXRot(0.0F);
        villager.yHeadRot = player.getYRot();
        villager.yBodyRot = player.getYRot();

        level.addFreshEntity(villager);
        level.playSound(null, spawnPos, SoundEvents.VILLAGER_CELEBRATE, SoundSource.NEUTRAL, 1.0F, 1.0F);

        ItemStack resultStack = player.getAbilities().instabuild
                ? stack
                : new ItemStack(Items.BUCKET);

        return InteractionResult.SUCCESS.heldItemTransformedTo(resultStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
        CompoundTag villagerData = itemStack.get(InaBucketData.VILLAGER_DATA.get());

        // Build a throwaway Villager purely to read its fields — never added to a level
        ValueInput input = TagValueInput.create(
                ProblemReporter.DISCARDING, context.registries(), villagerData);
        Villager villager = new Villager(EntityType.VILLAGER, Minecraft.getInstance().level);
        villager.load(input);

        if (villager.hasCustomName()) {
            builder.accept(villager.getCustomName().copy().withStyle(ChatFormatting.BLUE));
        }

        VillagerType type = villager.getVillagerData().type().value();
        Identifier typeId = BuiltInRegistries.VILLAGER_TYPE.getKey(type);
        String biomeName = capitalize(typeId.getPath());
        builder.accept(Component.translatable("tooltip.lqdfxsextras.villager_type", biomeName)
                .withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));

        if (villager.isBaby()) {
            builder.accept(Component.translatable("tooltip.lqdfxsextras.baby"));
        }
        else {
            Holder<VillagerProfession> profession = villager.getVillagerData().profession();
            Identifier professionId = BuiltInRegistries.VILLAGER_PROFESSION.getKey(profession.value());
            Component professionName = Component.translatable("entity.minecraft.villager." + professionId.getPath());

            boolean showsLevel = !professionId.getPath().equals("none") && !professionId.getPath().equals("nitwit");
            if (showsLevel) {
                builder.accept(Component.translatable("tooltip.lqdfxsextras.villager_profession_level",
                        professionName, villager.getVillagerData().level()).withStyle(ChatFormatting.AQUA));
            }
            else if (professionId.getPath().equals("nitwit"))
                builder.accept(Component.translatable("entity.minecraft.villager.nitwit").withStyle(ChatFormatting.DARK_GREEN));
            else
                builder.accept(Component.translatable("tooltip.lqdfxsextras.unemployed").withStyle(ChatFormatting.DARK_GRAY));

        }

        super.appendHoverText(itemStack, context, display, builder, flag);
    }

    private static String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}