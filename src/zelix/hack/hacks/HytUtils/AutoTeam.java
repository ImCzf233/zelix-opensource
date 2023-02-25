package zelix.hack.hacks.HytUtils;

import zelix.hack.*;
import zelix.utils.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.gameevent.*;
import zelix.hack.hacks.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;

public class AutoTeam extends Hack
{
    TimerUtils timer;
    
    public AutoTeam() {
        super("AutoTeam", HackCategory.HYT_UTILS);
        this.timer = new TimerUtils();
    }
    
    public boolean check() {
        if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(0).getHasStack()) {
            final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(0).getStack();
            if (is.getTranslationKey().contains("bed")) {
                return true;
            }
        }
        return false;
    }
    
    public boolean check2() {
        if (Wrapper.INSTANCE.player().inventoryContainer.getSlot(8).getHasStack()) {
            final ItemStack is = Wrapper.INSTANCE.player().inventoryContainer.getSlot(8).getStack();
            if (is.getItem() instanceof ItemArmor) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (!this.check() || this.check2()) {
            return;
        }
        if (this.timer.hasReached(1200.0f)) {
            Wrapper.INSTANCE.player().inventory.currentItem = 0;
            final EntityPlayerSP player = Wrapper.INSTANCE.player();
            AutoClicker.rightClickMouse();
            if (Wrapper.INSTANCE.mc().currentScreen != null) {
                Wrapper.INSTANCE.controller().windowClick(player.openContainer.windowId, 1, 1, ClickType.PICKUP, (EntityPlayer)player);
                this.timer.reset();
            }
        }
        super.onPlayerTick(event);
    }
}
