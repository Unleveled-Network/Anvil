/*
 *   MSRepository - MilSpecSG
 *   Copyright (C) 2019 Cableguy20
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

package rocks.milspecsg.mscore.model.core.coremember;

import rocks.milspecsg.msrepository.model.data.dbo.ObjectWithId;

import java.util.Date;
import java.util.UUID;

public interface CoreMember<TKey> extends ObjectWithId<TKey> {

    UUID getUserUUID();
    void setUserUUID(UUID userUUID);

    String getUserName();
    void setUserName(String userName);

    String getIpAddress();
    void setIpAddress(String ipAddress);

    Date getLastJoinedUtc();
    void setLastJoinedUtc(Date joinedUtc);
}
