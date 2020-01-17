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

package rocks.milspecsg.msrepository.common.repository;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;
import org.mongodb.morphia.query.UpdateOperations;
import rocks.milspecsg.msrepository.api.model.ObjectWithId;
import rocks.milspecsg.msrepository.api.repository.MongoRepository;
import rocks.milspecsg.msrepository.common.component.CommonMongoComponent;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface CommonMongoRepository<
    T extends ObjectWithId<ObjectId>>
    extends MongoRepository<T>, CommonMongoComponent {

    @Override
    default CompletableFuture<Optional<Integer>> getCreatedUtcTimeStampSeconds(ObjectId id) {
        return CompletableFuture.completedFuture(Optional.of(id.getTimestamp()));
    }

    @Override
    default CompletableFuture<Optional<Long>> getCreatedUtcTimeStampMillis(ObjectId id) {
        return CompletableFuture.completedFuture(Optional.of(id.getTimestamp() * 1000L));
    }

    @Override
    default CompletableFuture<Optional<Date>> getCreatedUtcDate(ObjectId id) {
        return CompletableFuture.completedFuture(Optional.of(id.getDate()));
    }

    @Override
    default CompletableFuture<Optional<T>> insertOne(T item) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Datastore> optionalDataStore = getDataStoreContext().getDataStore();
            if (!optionalDataStore.isPresent()) {
                return Optional.empty();
            }
            try {
                item.setId((ObjectId) optionalDataStore.get().save(item).getId());
            } catch (RuntimeException e) {
                e.printStackTrace();
                return Optional.empty();
            }
            return Optional.of(item);
        });
    }

    @Override
    default CompletableFuture<List<T>> insert(List<T> list) {
        return CompletableFuture.supplyAsync(() ->
            getDataStoreContext().getDataStore()
                .map(dataStore -> list.stream().filter(item -> {
                    try {
                        item.setId((ObjectId) dataStore.save(item).getId());
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList())).orElse(Collections.emptyList())
        );
    }

    @Override
    default CompletableFuture<Optional<T>> getOne(ObjectId id) {
        return CompletableFuture.supplyAsync(() -> asQuery(id).map(QueryResults::get));
    }

    @Override
    default CompletableFuture<List<ObjectId>> getAllIds() {
        return CompletableFuture.supplyAsync(() ->
            asQuery()
                .map(q ->
                    q.project("_id", true)
                        .asList().stream().map(ObjectWithId::getId)
                        .collect(Collectors.toList())
                ).orElse(Collections.emptyList()));
    }

    @Override
    default CompletableFuture<WriteResult> delete(Query<T> query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Optional<Datastore> optionalDataStore = getDataStoreContext().getDataStore();
                if (!optionalDataStore.isPresent()) {
                    return WriteResult.unacknowledged();
                }
                return optionalDataStore.get().delete(query);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return WriteResult.unacknowledged();
            }
        });
    }

    @Override
    default CompletableFuture<Boolean> delete(Optional<Query<T>> query) {
        return query.map(q -> delete(q).thenApplyAsync(w -> w.getN() > 0).exceptionally(e -> false))
            .orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    default CompletableFuture<Boolean> deleteOne(ObjectId id) {
        return delete(asQuery(id));
    }

    @Override
    default Optional<UpdateOperations<T>> createUpdateOperations() {
        return getDataStoreContext().getDataStore().map(d -> d.createUpdateOperations(getTClass()));
    }

    @Override
    default Optional<UpdateOperations<T>> inc(String field, Number value) {
        return createUpdateOperations().map(u -> u.inc(field, value));
    }

    @Override
    default Optional<UpdateOperations<T>> inc(String field) {
        return inc(field, 1);
    }

    @Override
    default CompletableFuture<Boolean> runUpdateOperations(Query<T> query, Function<UpdateOperations<T>, UpdateOperations<T>> updateOperations) {
        return CompletableFuture.supplyAsync(() -> createUpdateOperations().map(updateOperations)
            .map(u -> getDataStoreContext().getDataStore()
                .map(dataStore -> dataStore.update(query, u).getUpdatedCount() > 0)
                .orElse(false)
            ).orElse(false));
    }

    @Override
    default CompletableFuture<Boolean> runUpdateOperations(Optional<Query<T>> query, Function<UpdateOperations<T>, UpdateOperations<T>> updateOperations) {
        return query.map(q -> runUpdateOperations(q, updateOperations)).orElse(CompletableFuture.completedFuture(false));
    }

    @Override
    default Optional<Query<T>> asQuery() {
        return getDataStoreContext().getDataStore().map(d -> d.createQuery(getTClass()));
    }

    @Override
    default Optional<Query<T>> asQuery(ObjectId id) {
        return asQuery().map(q -> q.field("_id").equal(id));
    }
}
