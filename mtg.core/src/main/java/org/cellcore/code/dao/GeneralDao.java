package org.cellcore.code.dao;

import org.cellcore.code.model.AbstractJPAEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 *
 * Copyright 2013 Freddy Munoz (freddy@cellcore.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ==============================================================
 * General database manager
 */
@Repository("generalDao")
public class GeneralDao {

    /**
     * typicall JPA manager
     */
    @PersistenceContext
    protected EntityManager em;

    public <T extends AbstractJPAEntity> T read(Class<T> class_, long id) {
        return em.find(class_, id);
    }

    public <T extends AbstractJPAEntity> T save(T entity) {
        em.persist(entity);
        return entity;
    }

    public <T extends AbstractJPAEntity> T merge(T entity) {
        return em.merge(entity);
    }

    public <T extends AbstractJPAEntity> List<T> list(Class<T> class_) {
        return em.createQuery("from " + class_.getSimpleName()).getResultList();
    }

    public <T extends AbstractJPAEntity> List<T> read(Class<T> class_, String property, Object value) {
        return em.createQuery("select obj from " + class_.getSimpleName() + " obj where obj." + property + "=:" + property + "Val").setParameter(property + "Val", value).getResultList();
    }


    public <T extends AbstractJPAEntity> List<T> read(Class<T> class_, Map<String, Object> properties) {

        StringBuffer queryString = new StringBuffer();
        queryString.append("select obj from " + class_.getSimpleName() + " obj where ");
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() == null) {
                queryString.append("obj." + entry.getKey() + " IS NULL AND ");
            } else {
                queryString.append("obj." + entry.getKey() + "=:" + entry.getKey().replaceAll("\\.", "_") + " AND ");
            }

        }
        queryString.append(" obj.id=obj.id");
        Query query = em.createQuery(queryString.toString());
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (entry.getValue() != null) {
                query.setParameter(entry.getKey().replaceAll("\\.", "_"), entry.getValue());
            }
        }
        return query.getResultList();
    }
}
