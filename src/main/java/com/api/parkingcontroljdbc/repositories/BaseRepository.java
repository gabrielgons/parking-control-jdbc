package com.api.parkingcontroljdbc.repositories;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.api.parkingcontroljdbc.annotations.IgnoreField;
import com.api.parkingcontroljdbc.models.Entity;

public abstract class BaseRepository<T extends Entity> {

	protected final String table;

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	protected RowMapper<T> rowMapper;

	BaseRepository(String table, RowMapper<T> rowMapper) {
		this.table = table;
		this.rowMapper = rowMapper;
	}

	public T save(T entity) throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = entity.getClass().getDeclaredFields();
		List<Object> fieldValues = new ArrayList<>();
		StringBuilder query = new StringBuilder();

		if (entity.getId() == null) {
			query.append(MessageFormat.format("INSERT INTO {0} (", table));

			entity.setId(UUID.randomUUID());
			query.append("id, ");

			for (Field field : fields) {
				if (field.isAnnotationPresent(IgnoreField.class))
					continue;

				query.append(MessageFormat.format("{0},", toSnakeCase(field.getName())));
				field.setAccessible(true);
				fieldValues.add(field.get(entity));
			}

			query.deleteCharAt(query.length() - 1).append(") VALUES (");
			query.append("?,".repeat(fields.length));
			query.deleteCharAt(query.length() - 1).append(");");

			entity.setId(UUID.randomUUID());
			fieldValues.add(0, entity.getId());
			jdbcTemplate.update(query.toString(), fieldValues.toArray());

		} else {
			query.append(MessageFormat.format("UPDATE {0} SET ", table));

			for (Field field : fields) {
				if (field.isAnnotationPresent(IgnoreField.class))
					continue;

				query.append(MessageFormat.format("{0}=?,", toSnakeCase(field.getName())));
				field.setAccessible(true);
				fieldValues.add(field.get(entity));
			}

			query.deleteCharAt(query.length() - 1);
			query.append(" WHERE id = ?");
			fieldValues.add(entity.getId());

			jdbcTemplate.update(query.toString(), fieldValues.toArray());
		}

		return entity;
	}

	protected String toSnakeCase(String camelCase) {
		return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
	}

	public void delete(T entity) {
		final String deleteQuery = MessageFormat.format("DELETE FROM {0} WHERE id = ?", table);

		jdbcTemplate.update(deleteQuery, entity.getId());
	}

	public Optional<T> findById(UUID id) {

		final String query = MessageFormat.format("SELECT * FROM {0} WHERE id = ?", table);

		List<T> results = jdbcTemplate.query(query, rowMapper, id);

		if (results.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(results.get(0));
		}
	}

	public Page<T> findAll(Pageable pageable) {
		final String rowCountQuery = MessageFormat.format("SELECT COUNT(*) FROM {0}", table);
		final String query = MessageFormat.format("SELECT * FROM {0} LIMIT ? OFFSET ?", table);

		int total = jdbcTemplate.queryForObject(rowCountQuery, Integer.class);

		int limit = pageable.getPageSize();
		long offset = pageable.getOffset();

		List<T> entities = jdbcTemplate.query(query, rowMapper, limit, offset);

		return new PageImpl<>(entities, pageable, total);
	}
}
