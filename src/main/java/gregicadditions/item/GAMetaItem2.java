package gregicadditions.item;

import gregicadditions.GAValues;
import gregicadditions.capabilities.GAElectricStats;
import gregicadditions.item.behaviors.ProspectingToolBehaviour;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.IElectricItem;
import gregtech.api.items.metaitem.ElectricStats;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import gregtech.api.items.metaitem.stats.IItemBehaviour;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static gregicadditions.item.GAMetaItems.*;

public class GAMetaItem2 extends StandardMetaItem {

    public GAMetaItem2(short metaItemOffset) {
        super(metaItemOffset);
    }

    @Override
    public void registerSubItems() {
        BATTERY_SMALL_VANADIUM = addItem(1, "small.vanadium.battery").addComponents(GAElectricStats.createRechargeableBattery(7200000, GAValues.EV)).setModelAmount(8);
        BATTERY_MEDIUM_VANADIUM = addItem(3, "medium.vanadium.battery").addComponents(GAElectricStats.createRechargeableBattery(28800000, GAValues.IV)).setModelAmount(8);
        BATTERY_LARGE_VANADIUM = addItem(6, "large.vanadium.battery").addComponents(GAElectricStats.createRechargeableBattery(115200000, GAValues.LuV)).setModelAmount(8);

        BATTERY_MEDIUM_NAQUADRIA = addItem(4, "medium.naquadria.battery").addComponents(GAElectricStats.createRechargeableBattery(460800000, GAValues.ZPM)).setModelAmount(8);
        BATTERY_LARGE_NAQUADRIA = addItem(7, "large.naquadria.battery").addComponents(GAElectricStats.createRechargeableBattery(1843200000, GAValues.UV)).setModelAmount(8);

        BATTERY_SMALL_NEUTRONIUM = addItem(2, "small.neutronium.battery").addComponents(GAElectricStats.createRechargeableBattery(7372800000L, GAValues.UHV)).setModelAmount(8);
        BATTERY_MEDIUM_NEUTRONIUM = addItem(5, "medium.neutronium.battery").addComponents(GAElectricStats.createRechargeableBattery(29491200000L, GAValues.UEV)).setModelAmount(8);
        BATTERY_LARGE_NEUTRONIUM = addItem(8, "large.neutronium.battery").addComponents(GAElectricStats.createRechargeableBattery(117964800000L, GAValues.UIV)).setModelAmount(8);

        GAMetaItems.PROSPECT_TOOL_MV = addItem(100, "tool.prospect.mv").addComponents(new ProspectingToolBehaviour(2)).addComponents(ElectricStats.createElectricItem(18000, 2)).setMaxStackSize(1);
        GAMetaItems.PROSPECT_TOOL_HV = addItem(101, "tool.prospect.hv").addComponents(new ProspectingToolBehaviour(3)).addComponents(ElectricStats.createElectricItem(27000, 3)).setMaxStackSize(1);
        GAMetaItems.PROSPECT_TOOL_LuV = addItem(102, "tool.prospect.luv").addComponents(new ProspectingToolBehaviour(6)).addComponents(ElectricStats.createElectricItem(63000, 6)).setMaxStackSize(1);
        GAMetaItems.PROSPECT_TOOL_ZPM = addItem(103, "tool.prospect.zpm").addComponents(new ProspectingToolBehaviour(7)).addComponents(ElectricStats.createElectricItem(72000, 7)).setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> lines, ITooltipFlag tooltipFlag) {
        MetaItem<?>.MetaValueItem item = getItem(itemStack);
        if (item == null) return;
        String unlocalizedTooltip = "metaitem." + item.unlocalizedName + ".tooltip";
        if (I18n.hasKey(unlocalizedTooltip)) {
            lines.addAll(Arrays.asList(I18n.format(unlocalizedTooltip).split("/n")));
        }

        IElectricItem electricItem = itemStack.getCapability(GregtechCapabilities.CAPABILITY_ELECTRIC_ITEM, null);
        if (electricItem != null) {
            lines.add(I18n.format("metaitem.generic.electric_item.tooltip",
                    electricItem.getCharge(),
                    electricItem.getMaxCharge(),
                    GAValues.VN[electricItem.getTier()]));
        }

        IFluidHandlerItem fluidHandler = ItemHandlerHelper.copyStackWithSize(itemStack, 1)
                .getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
        if (fluidHandler != null) {
            IFluidTankProperties fluidTankProperties = fluidHandler.getTankProperties()[0];
            FluidStack fluid = fluidTankProperties.getContents();
            if (fluid != null) {
                lines.add(I18n.format("metaitem.generic.fluid_container.tooltip",
                        fluid.amount,
                        fluidTankProperties.getCapacity(),
                        fluid.getLocalizedName()));
            } else lines.add(I18n.format("metaitem.generic.fluid_container.tooltip_empty"));
        }

        for (IItemBehaviour behaviour : getBehaviours(itemStack)) {
            behaviour.addInformation(itemStack, lines);
        }
    }
}
