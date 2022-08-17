package com.smushytaco.solar_apocalypse;

import com.mojang.logging.LogUtils;
import com.smushytaco.solar_apocalypse.configuration_support.ModConfiguration;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.stream.Collectors;

import static com.smushytaco.solar_apocalypse.SolarApocalypse.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class SolarApocalypse
{
//    @SubscribeEvent
//    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
//        Player newPlayer = event.getPlayer();
//        Level world = newPlayer.getLevel();
//        if (!WorldDayCalculation.isOldEnough(world, config.getMobsAndPlayersBurnInDaylightDay()) || !newPlayer.isAlive() || world.isRaining() || newPlayer.isSpectator() || newPlayer.isCreative() || world.isNight() || world.isClientSide || !world.canSeeSky(newPlayer.blockPosition()) || newPlayer.hasEffect(Sunscreen.INSTANCE)) newPlayer.addEffect(EffectInstance(Sunscreen.INSTANCE, 2400, 0, false, false, true));
//
//    }

    public static final SolarApocalypse INSTANCE = new SolarApocalypse();
    public static final String MOD_ID = "solar_apocalypse";
    ModConfiguration config;
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final RegistryObject<Block> DUST = BLOCKS.register("dust", () -> new FallingBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.5f).sound(SoundType.SAND)));
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> DUST_BLOCK_ITEM = ITEMS.register("dust", () -> new BlockItem(DUST.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> SUNSCREEN_EFFECT = MOB_EFFECT_DEFERRED_REGISTER.register("sunscreen", () -> Sunscreen.INSTANCE);

    @Contract("_, _ -> new")
    private static final @NotNull ConfigSerializer onInitializeLambda(Config definition, Class configClass) {
        return (ConfigSerializer) (new GsonConfigSerializer(definition, configClass));
    }

    public SolarApocalypse()
    {

        configRegister(ModConfiguration.class);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        config = AutoConfig.getConfigHolder(ModConfiguration.class).getConfig();

        register();
    }
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var world = event.getPlayer().level;
        var newPlayer = event.getPlayer();
        if (!WorldDayCalculation.isOldEnough(world, config.getMobsAndPlayersBurnInDaylightDay()) || !newPlayer.isAlive() || world.isRaining() || newPlayer.isSpectator() || newPlayer.isCreative() || world.isNight() || world.isClientSide || !world.canSeeSky(newPlayer.blockPosition()) || newPlayer.hasEffect(Sunscreen.INSTANCE))
        {
            newPlayer.addEffect(new MobEffectInstance(SUNSCREEN_EFFECT.get(), 2400, 0, false, false, true));
            //System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }
    }
    private boolean configRegister(Class<ModConfiguration> modConfigurationClass) {
        try {
            AutoConfig.register(modConfigurationClass, SolarApocalypse::onInitializeLambda);
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }

    public void register() {
        try {
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }catch (Exception e){}
        try {
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        }catch (Exception e){}
        try {
            MOB_EFFECT_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        }catch (Exception e){}
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo(MOD_ID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    public ModConfiguration getConfig() {
        return config;
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            //blockRegistryEvent.getRegistry().register(SolarApocalypse.INSTANCE.DUST.get());
            LOGGER.info("HELLO from Register Block");
        }
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            // Register a new item here
            //itemRegistryEvent.getRegistry().register(new BlockItem(SolarApocalypse.INSTANCE.DUST, new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
            LOGGER.info("HELLO from Register Item");
        }
    }
}
