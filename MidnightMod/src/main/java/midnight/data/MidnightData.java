package midnight.data;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import net.minecraft.data.DataGenerator;
import midnight.client.MidnightClient;
import midnight.common.Midnight;
import midnight.common.proxy.BlockItemProxy;
import midnight.data.blockstates.MnBlockstateProvider;
import midnight.data.loottables.MnLootTableProvider;
import midnight.data.models.BlockItemModelTable;
import midnight.data.models.MnModelProvider;
import midnight.data.proxy.DataBlockItemProxy;

/**
 * The data-generation-only main class of the Midnight, to handle initialization during data generation and to prevent
 * unnecessary initialization done by the client which is not used during data-generation.
 */
public class MidnightData extends MidnightClient {
    @Override
    public Dist getRuntimeDist() {
        return Dist.CLIENT;
    }

    @Override
    protected BlockItemProxy makeBlockItemProxy() {
        return new DataBlockItemProxy();
    }

    /**
     * Handles the data generation event ({@link GatherDataEvent}).
     */
    @SubscribeEvent
    public void onGenerateData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        if (event.includeClient()) {
            gen.addProvider(new MnBlockstateProvider(gen));
            gen.addProvider(new MnModelProvider(gen).withTable(new BlockItemModelTable()));
        }
        if (event.includeServer()) {
            gen.addProvider(new MnLootTableProvider(gen));
        }
    }

    /**
     * Returns the direct instance of {@link MidnightData}, or throws a {@link ClassCastException} when not running in
     * the data generator mode (that would already have caused a class loading failure in most cases).
     */
    public static MidnightData get() {
        return (MidnightData) Midnight.get();
    }
}
