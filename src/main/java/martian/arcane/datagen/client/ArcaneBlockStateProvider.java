package martian.arcane.datagen.client;

import martian.arcane.ArcaneMod;
import martian.arcane.common.block.connector.BlockAuraConnector;
import martian.arcane.common.block.pedestal.BlockPedestal;
import martian.arcane.common.block.basin.BlockAuraBasin;
import martian.arcane.common.block.infuser.BlockAuraInfuser;
import martian.arcane.common.registry.ArcaneBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ArcaneBlockStateProvider extends BlockStateProvider {
    private static final ResourceLocation TRANSLUCENT = new ResourceLocation("translucent");

    public ArcaneBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ArcaneMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Models
        {
            // Generators
            topBottom(ArcaneBlocks.HEAT_COLLECTOR.get(), texture("machines/ignis_collector"), new ResourceLocation("minecraft", "block/polished_deepslate"));
            topBottom(ArcaneBlocks.AQUA_COLLECTOR.get(), texture("machines/aqua_collector"), new ResourceLocation("minecraft", "block/polished_deepslate"));

            // General Blocks
            simpleTranslucent(ArcaneBlocks.AURAGLASS.get());
            cubeAll(ArcaneBlocks.SOUL_MAGMA.get());
            withExistingParent(ArcaneBlocks.AURA_TORCH.get(), new ResourceLocation("minecraft", "block/template_torch"))
                    .renderType("minecraft:cutout")
                    .texture("torch", "arcane:block/aura_torch");

            // Conjured Blocks
            simpleTranslucent(ArcaneBlocks.CONJURED_BLOCK.get());

            // Ores
            cubeAll(ArcaneBlocks.LARIMAR_ORE.get());
            cubeAll(ArcaneBlocks.FADING_LARIMAR_ORE.get());
            cubeAll(ArcaneBlocks.FADED_LARIMAR_ORE.get());
            cubeAll(ArcaneBlocks.DEEPSLATE_LARIMAR_ORE.get());
            cubeAll(ArcaneBlocks.FADING_DEEPSLATE_LARIMAR_ORE.get());
            cubeAll(ArcaneBlocks.FADED_DEEPSLATE_LARIMAR_ORE.get());

            cubeAll(ArcaneBlocks.IDOCRASE_ORE.get());
            cubeAll(ArcaneBlocks.DEEPSLATE_IDOCRASE_ORE.get());
            cubeAll(ArcaneBlocks.NETHER_IDOCRASE_ORE.get());
            cubeAll(ArcaneBlocks.BLACKSTONE_IDOCRASE_ORE.get());

            // Storage Blocks
            cubeAll(ArcaneBlocks.FADED_LARIMAR_BLOCK.get());
            cubeAll(ArcaneBlocks.FADING_LARIMAR_BLOCK.get());
            cubeAll(ArcaneBlocks.LARIMAR_BLOCK.get());
            cubeAll(ArcaneBlocks.AURACHALCUM_BLOCK.get());
        }

        // Block States
        {
            makeBlockState(ArcaneBlocks.SOUL_MAGMA);
            makeBlockState(ArcaneBlocks.AURA_TORCH);

            // Machines
            makeRotatableModel(ArcaneBlocks.AURA_CONNECTOR, BlockAuraConnector.FACING, false);
            makeRotatableModel(ArcaneBlocks.AURA_BASIN, BlockAuraBasin.FACING, true);
            makeRotatableModel(ArcaneBlocks.AURA_INFUSER, BlockAuraInfuser.FACING, true);
            makeRotatableModel(ArcaneBlocks.PEDESTAL, BlockPedestal.FACING, true);
            makeBlockState(ArcaneBlocks.SPELL_CIRCLE);

            // Generators
            makeBlockState(ArcaneBlocks.HEAT_COLLECTOR);
            makeBlockState(ArcaneBlocks.AQUA_COLLECTOR);

            // Conjured Blocks
            makeBlockState(ArcaneBlocks.CONJURED_CRAFTING_TABLE);

            // Ores
            makeBlockState(ArcaneBlocks.LARIMAR_ORE);
            makeBlockState(ArcaneBlocks.FADING_LARIMAR_ORE);
            makeBlockState(ArcaneBlocks.FADED_LARIMAR_ORE);
            makeBlockState(ArcaneBlocks.DEEPSLATE_LARIMAR_ORE);
            makeBlockState(ArcaneBlocks.FADING_DEEPSLATE_LARIMAR_ORE);
            makeBlockState(ArcaneBlocks.FADED_DEEPSLATE_LARIMAR_ORE);

            makeBlockState(ArcaneBlocks.IDOCRASE_ORE);
            makeBlockState(ArcaneBlocks.DEEPSLATE_IDOCRASE_ORE);
            makeBlockState(ArcaneBlocks.NETHER_IDOCRASE_ORE);
            makeBlockState(ArcaneBlocks.BLACKSTONE_IDOCRASE_ORE);

            // Storage Blocks
            makeBlockState(ArcaneBlocks.FADED_LARIMAR_BLOCK);
            makeBlockState(ArcaneBlocks.FADING_LARIMAR_BLOCK);
            makeBlockState(ArcaneBlocks.LARIMAR_BLOCK);
            makeBlockState(ArcaneBlocks.AURACHALCUM_BLOCK);
        }
    }

    private void makeBlockState(DeferredBlock<?> block) {
        getMultipartBuilder(block.get())
                .part()
                .modelFile(models().getExistingFile(modLoc(block.getId().getPath())))
                .addModel();
    }

    private void makeRotatableModel(DeferredBlock<?> block, Property<Direction> property, boolean horizontal) {
        ModelFile model = models().getExistingFile(modLoc(block.getId().getPath()));
        getVariantBuilder(block.get()).forAllStates(state -> {
            Direction facing = state.getValue(property);
            if (horizontal && (facing == Direction.DOWN || facing == Direction.UP)) {
                return ConfiguredModel.builder().modelFile(model).build();
            }

            int x = 0, y = 0;
            switch (facing) {
                case NORTH -> { x = 90; y = 180; }
                case EAST -> { x = 90; y = 270; }
                case SOUTH -> x = 90;
                case WEST -> x = y = 90;
                case UP -> x = 180;
            }

            if (horizontal) x = 0;

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }

    private void simpleTranslucent(Block block) {
        simpleBlockWithRenderType(block, TRANSLUCENT);
    }

    private void topBottom(Block block, ResourceLocation sides, ResourceLocation topAndBottom) {
        models().cubeBottomTop(name(block), sides, topAndBottom, topAndBottom);
    }

    private BlockModelBuilder withExistingParent(Block block, ResourceLocation parent) {
        return models().withExistingParent(name(block), parent);
    }

    @SuppressWarnings("SameParameterValue")
    private void simpleBlockWithRenderType(Block block, ResourceLocation renderType) {
        getVariantBuilder(block)
                .partialState()
                .setModels(new ConfiguredModel(models()
                        .cubeAll(name(block), blockTexture(block))
                        .renderType(renderType)
                ));
    }

    private ResourceLocation texture(String path) {
        return new ResourceLocation(ArcaneMod.MODID, "block/" + path);
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private String name(Block block) {
        return key(block).getPath();
    }
}
