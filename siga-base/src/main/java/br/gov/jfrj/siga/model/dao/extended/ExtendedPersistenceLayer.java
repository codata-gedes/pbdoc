package br.gov.jfrj.siga.model.dao.extended;

import java.util.List;

import javax.persistence.EntityManager;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public interface ExtendedPersistenceLayer extends EntityManager {

	/*
	 * EntityManager extended methods.
	 */

	<T> List<T> findAll(Class<T> type);

	<T> T persistAndGet(T entity);

	<T> T removeAndGet(T entity);

	<T> List<T> persistMany(Iterable<T> entities);

	<T> List<T> mergeMany(Iterable<T> entities);

	/*
	 * QueryDSL methods.
	 */

	<R> Page<R> findAll(PageRequest pageRequest, EntityPath<R> root, Predicate predicates[], OrderSpecifier<? extends Comparable<?>>[] orders);

	<R> Page<R> findAll(PageRequest pageRequest, EntityPath<R> root, Predicate... predicates);

	<R> List<R> findAll(EntityPath<R> root, Predicate[] predicates, OrderSpecifier<? extends Comparable<?>>[] orders);

	<R> List<R> findAll(EntityPath<R> root, Predicate... predicates);

	<R> R find(EntityPath<R> root, Predicate... predicates);

}
