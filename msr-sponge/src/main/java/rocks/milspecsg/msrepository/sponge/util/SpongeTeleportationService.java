/*
 *     MSRepository - MilSpecSG
 *     Copyright (C) 2019 Cableguy20
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package rocks.milspecsg.msrepository.sponge.util;

import com.google.inject.Inject;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import rocks.milspecsg.msrepository.api.util.TeleportationService;
import rocks.milspecsg.msrepository.api.util.UserService;

import java.util.Optional;
import java.util.UUID;

public class SpongeTeleportationService implements TeleportationService {

    @Inject
    UserService<User, Player> userService;

    @Override
    public boolean teleport(UUID teleportingUserUUID, UUID targetUserUUID) {
        final Optional<User> teleporter = userService.get(teleportingUserUUID);
        final Optional<User> target = userService.get(targetUserUUID);

        if (!teleporter.isPresent() || !target.isPresent()) {
            return false;
        }

        return target.flatMap(User::getWorldUniqueId)
            .filter(uuid -> teleporter.get().setLocation(target.get().getPosition(), uuid))
            .isPresent();
    }
}
