/*
 *   Anvil - AnvilPowered
 *   Copyright (C) 2020-2021
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.anvilpowered.anvil.sponge8.module

import org.anvilpowered.anvil.api.command.CommandExecuteService
import org.anvilpowered.anvil.api.command.SimpleCommandService
import org.anvilpowered.anvil.api.entity.RestrictionService
import org.anvilpowered.anvil.api.misc.bind
import org.anvilpowered.anvil.api.misc.to
import org.anvilpowered.anvil.api.server.LocationService
import org.anvilpowered.anvil.api.util.AudienceService
import org.anvilpowered.anvil.api.util.KickService
import org.anvilpowered.anvil.api.util.TextService
import org.anvilpowered.anvil.api.util.UserService
import org.anvilpowered.anvil.common.PlatformImpl
import org.anvilpowered.anvil.common.entity.CommonRestrictionService
import org.anvilpowered.anvil.common.util.CommonTextService
import org.anvilpowered.anvil.common.util.SendTextService
import org.anvilpowered.anvil.sponge.module.ApiSpongeModule
import org.anvilpowered.anvil.sponge8.command.Sponge8CommandExecuteService
import org.anvilpowered.anvil.sponge8.command.Sponge8SimpleCommandService
import org.anvilpowered.anvil.sponge8.server.Sponge8LocationService
import org.anvilpowered.anvil.sponge8.util.Log4jAdapter
import org.anvilpowered.anvil.sponge8.util.Sponge8AudienceService
import org.anvilpowered.anvil.sponge8.util.Sponge8KickService
import org.anvilpowered.anvil.sponge8.util.Sponge8SendTextService
import org.anvilpowered.anvil.sponge8.util.Sponge8UserService
import org.spongepowered.api.Platform
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandCause
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.entity.living.player.server.ServerPlayer

class ApiSponge8Module : ApiSpongeModule(
  PlatformImpl(
    "sponge",
    false,
    //TODO Fix the version
    { "${Sponge.platform().container(Platform.Component.IMPLEMENTATION).metadata().version().majorVersion}.${Sponge.platform().container(Platform.Component.IMPLEMENTATION).metadata().version().minorVersion} " },
    Log4jAdapter::bindLogger,
  )
) {

  override fun configure() {
    super.configure()
    with(binder()) {
      bind<RestrictionService>().to<CommonRestrictionService>()
      bind<UserService<User, ServerPlayer>>().to<Sponge8UserService>()
      bind<SendTextService<CommandCause>>().to<Sponge8SendTextService>()
      bind<SendTextService<*>>().to<Sponge8SendTextService>()
      bind<TextService<CommandCause>>().to<CommonTextService<CommandCause>>()
      bind<TextService<*>>().to<CommonTextService<*>>()
      bind<AudienceService<CommandCause>>().to<Sponge8AudienceService>()
      bind<CommandExecuteService>().to<Sponge8CommandExecuteService>()
      bind<KickService>().to<Sponge8KickService>()
      bind<LocationService>().to<Sponge8LocationService>()
      bind<SimpleCommandService<CommandCause>>().to<Sponge8SimpleCommandService>()
    }
  }
}
