/*
 *   Anvil - AnvilPowered.org
 *   Copyright (C) 2019-2024 Contributors
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.anvilpowered.anvil.core.command.config

import org.anvilpowered.anvil.core.command.CommandSource
import org.anvilpowered.anvil.core.config.ConfigurateRegistry
import org.anvilpowered.anvil.core.config.ConfigurateRegistryExporter
import org.anvilpowered.anvil.core.config.KeyNamespace
import org.anvilpowered.anvil.core.config.Registry
import org.anvilpowered.kbrig.builder.ArgumentBuilder
import org.anvilpowered.kbrig.tree.LiteralCommandNode
import org.spongepowered.configurate.serialize.TypeSerializerCollection

class ConfigCommandFactory(
    val registry: Registry,
    val configurateRegistryClosure: ConfigurateRegistry.Factory.DiscoveryClosure,
    val keyNamespace: KeyNamespace,
    val exporters: List<ConfigurateRegistryExporter>,
    val serializers: TypeSerializerCollection,
) {
    fun create(): LiteralCommandNode<CommandSource> {
        return ArgumentBuilder.literal<CommandSource>("config")
            .then(createGet())
            .then(createGenerate())
            .build()
    }
}
