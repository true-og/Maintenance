/*
 * This file is part of Maintenance - https://github.com/kennytv/Maintenance
 * Copyright (C) 2018-2024 kennytv (https://github.com/kennytv)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.kennytv.maintenance.paper.listener;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import eu.kennytv.maintenance.core.Settings;
import eu.kennytv.maintenance.paper.MaintenancePaperPlugin;
import eu.kennytv.maintenance.paper.util.ComponentUtil;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class PaperServerListPingListener implements Listener {
    private final MaintenancePaperPlugin plugin;
    private final Settings settings;

    public PaperServerListPingListener(final MaintenancePaperPlugin plugin, final Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void serverListPing(final PaperServerListPingEvent event) {
        if (!settings.isMaintenance()) {
            return;
        }

        if (settings.isEnablePingMessages()) {
            if (ComponentUtil.PAPER) {
                event.motd(ComponentUtil.toPaperComponent(settings.getRandomPingMessage()));
            } else {
                event.setMotd(ComponentUtil.toLegacy(settings.getRandomPingMessage()));
            }
        }

        if (settings.hasCustomPlayerCountMessage()) {
            event.setProtocolVersion(-1);
            event.setVersion(settings.getLegacyParsedPlayerCountMessage());
        }

        if (settings.hasCustomPlayerCountHoverMessage()) {
            final List<PlayerProfile> sample = event.getPlayerSample();
            sample.clear();
            for (final String string : settings.getLegacyParsedPlayerCountHoverLines()) {
                // Use Bukkit.createProfile so Paper's internal cast to CraftPlayerProfile succeeds.
                sample.add(Bukkit.createProfile(UUID.randomUUID(), string));
            }
        }

        if (settings.hasCustomIcon() && plugin.getFavicon() != null) {
            event.setServerIcon(plugin.getFavicon());
        }
    }
}
