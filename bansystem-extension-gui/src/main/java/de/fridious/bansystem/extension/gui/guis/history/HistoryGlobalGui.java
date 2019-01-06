package de.fridious.bansystem.extension.gui.guis.history;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 06.01.19 20:47
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GUIS;
import de.fridious.bansystem.extension.gui.guis.ban.BanSelfGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;

public class HistoryGlobalGui extends PrivateGUI<Player> {

    public static String INVENTORY_TITLE;
    public static List<Class<? extends Event>> UPDATE_EVENTS = Arrays.asList(PlayerJoinEvent.class, PlayerQuitEvent.class);

    public HistoryGlobalGui(Player owner) {
        super(owner);
        String title = INVENTORY_TITLE;
        getUpdateEvents().addAll(UPDATE_EVENTS);
        createInventory(title, 54);
        setPageEntries(GuiExtensionUtils.getInteractOnlinePlayers(owner));
    }

    @Override
    public void setPageItem(int slot, Player player) {
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        setItem(slot, new ItemBuilder(ItemStorage.get("globalhistory_skull", networkPlayer)).setGameProfile(networkPlayer.getName()));
    }

    @Override
    public void beforeUpdatePage() {
        setPageEntries(GuiExtensionUtils.getInteractOnlinePlayers(getOwner()));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        Player target = getEntryBySlot().get(event.getSlot());
        if(target != null) {
            if(player.hasPermission("dkbans.history")) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                .create(GUIS.HISTORY_ALL, new HistoryAllGui(player, target.getUniqueId())).open());

            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {

    }
}