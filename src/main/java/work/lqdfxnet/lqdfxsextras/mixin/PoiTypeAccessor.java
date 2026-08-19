package work.lqdfxnet.lqdfxsextras.mixin;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PoiType.class)
public interface PoiTypeAccessor {
    @Accessor("maxTickets")
    @Mutable
    void setMaxTickets(int maxTickets);
}
