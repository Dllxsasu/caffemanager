package com.jeremias.dev.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.jeremias.dev.models.EntityBase;

public class CommonUtils {
	public static Date convert(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static <T extends EntityBase> Collection<Long> findDublicates(List<T> entities) {
		
		return entities.stream().collect(Collectors.toMap(EntityBase::getId, u -> false, (x, y) -> true)).entrySet()
				.stream().filter(Map.Entry::getValue).map(Map.Entry::getKey).collect(Collectors.toSet());
	}

	public static <T extends EntityBase> boolean filterForDublicates(T myEntity, Collection<Long> dublicates) {
		return dublicates.stream().filter(myId -> myId.equals(myEntity.getId())).findAny().isPresent();
	}

	public static <T extends EntityBase> Collection<T> filterDublicates(Collection<T> myCol) {
		return myCol.stream().collect(Collectors.toMap(EntityBase::getId, d -> d, (T x, T y) -> x == null ? y : x))
				.values();
	}
}
