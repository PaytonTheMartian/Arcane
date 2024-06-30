package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.IPreservable;
import martian.arcane.api.spell.*;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SpellPreservation extends AbstractSpell {
    private static final SpellConfig config = SpellConfig.basicConfig(ArcaneMod.id("preservation"), 4, 20, 2).build();

    @Override
    protected SpellConfig getConfig() {
        return config;
    }

    @Override
    public int getAuraCost(CastContext c) {
        return c.target.type() == CastTarget.Type.BLOCK && c.level.getBlockState(((BlockPos) c.target.value())).getBlock() instanceof IPreservable ?
                config.get("auraCost") :
                0;
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        if (c.target.type() == CastTarget.Type.BLOCK) {
            BlockPos pos = ((BlockPos) c.target.value());
            BlockState state = c.level.getBlockState(pos);
            if (state.getBlock() instanceof IPreservable preservable) {
                ArcaneFx.ON_CAST_GRAVITY.goBlock(c.level, pos.above());
                preservable.onPreserve(c.level, pos, state, c);
                return CastResult.SUCCESS;
            }
        }

        return CastResult.FAILED;
    }
}
