package io.github.cepr0.demo;

import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseEntity implements Serializable, Persistable<Long> {

	@Id
	@GeneratedValue
	private Long id;

	public BaseEntity() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BaseEntity that = (BaseEntity) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{id=" + getId() + '}';
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}
