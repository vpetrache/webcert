package se.inera.webcert.persistence.fragasvar.repository;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import se.inera.webcert.persistence.fragasvar.model.FragaSvar;
import se.inera.webcert.persistence.fragasvar.model.FragaSvarFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by pehr on 10/21/13.
 */
public class FragaSvarRepositoryImpl implements FragaSvarFilteredRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    CriteriaBuilder builder;
    CriteriaQuery<FragaSvar> query;
    Root<FragaSvar> root;


    private Predicate createPredicate(FragaSvarFilter filter){
        Predicate pred= builder.conjunction();

        if (filter.isQuestionFromFK()){
            pred = builder.and(pred, builder.equal(root.get("frageStallare"), "FK"));
        }
        if (filter.isQuestionFromWC()){
            pred = builder.and(pred, builder.equal(root.get("frageStallare"), "WC"));
        }

        if(filter.getHsaId()!=null&&!filter.getHsaId().isEmpty()){
            pred = builder.and(pred, builder.equal(root.get("vardperson").get("hsaId"), filter.getHsaId()));
        }

        if (filter.getChangedFrom() != null) {
            pred = builder.and(pred, builder.greaterThan(root.<LocalDate>get("senasteHandelse"), filter.getChangedFrom())) ;
        }

        return pred;
    }

    @Override
    public List<FragaSvar> filterFragaSvar(FragaSvarFilter filter) {

        builder = entityManager.getCriteriaBuilder();
        query = builder.createQuery(FragaSvar.class);

        root = query.from(FragaSvar.class);

        query.where(createPredicate(filter));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<FragaSvar> filterFragaSvar(FragaSvarFilter filter, Pageable pages) {

        builder = entityManager.getCriteriaBuilder();
        query = builder.createQuery(FragaSvar.class);

        root = query.from(FragaSvar.class);


        query.where(createPredicate(filter));

        return entityManager.createQuery(query).setMaxResults(pages.getPageSize()).setFirstResult(pages.getPageNumber()).getResultList();
    }
}
