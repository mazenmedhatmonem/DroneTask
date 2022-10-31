package com.musalasoft.musalsoftDrone.validator.persist;

import com.musalasoft.musalsoftDrone.entity.Base;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

public abstract class OccurenceValidator {

    @PersistenceContext // or even @Autowired
    protected final EntityManager entityManager;


    protected OccurenceValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    Long count(@NotNull Object value, ConstraintValidatorContextImpl context, Class<? extends Base> entity){
        String  attributeName =  context
                .getConstraintViolationCreationContexts()
                .get(0).getPath().getLeafNode().asString();

        return count(value,attributeName,entity);
    }
    Long count(@NotNull Object value, String attributeName, Class<? extends Base> entity){

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<? extends Base> root = query.from(entity);
        query.select(builder.count(root));

        query.where(builder.equal(root.get(attributeName), value));
        return entityManager.createQuery(query).getSingleResult();
    }
}
