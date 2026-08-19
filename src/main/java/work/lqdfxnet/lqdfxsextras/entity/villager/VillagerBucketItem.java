package work.lqdfxnet.lqdfxsextras.entity.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jspecify.annotations.NonNull;
import work.lqdfxnet.lqdfxsextras.ModData.ModDataComponents;

public class VillagerBucketItem extends Item {
    public VillagerBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS; // let normal block interaction happen instead
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        CompoundTag villagerData = stack.get(ModDataComponents.VILLAGER_DATA.get());
        if (villagerData == null) {
            return InteractionResult.PASS;
        }

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
}