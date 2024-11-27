package com.luch.spring.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class SpringUserSpringRole implements Serializable {

	SpringUser createdBy, lastUpdatedBy;
	Date dateCreated,  lastUpdated;

	private static final long serialVersionUID = 1

	SpringUser springUser
	SpringRole springRole

	@Override
	boolean equals(other) {
		if (other instanceof SpringUserSpringRole) {
			other.springUserId == springUser?.id && other.springRoleId == springRole?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (springUser) {
            hashCode = HashCodeHelper.updateHash(hashCode, springUser.id)
		}
		if (springRole) {
		    hashCode = HashCodeHelper.updateHash(hashCode, springRole.id)
		}
		hashCode
	}

	static SpringUserSpringRole get(long springUserId, long springRoleId) {
		return (SpringUserSpringRole)(criteriaFor(springUserId, springRoleId).get());
	}

	static boolean exists(long springUserId, long springRoleId) {
		criteriaFor(springUserId, springRoleId).count()
	}

	private static DetachedCriteria criteriaFor(long springUserId, long springRoleId) {
		SpringUserSpringRole.where {
			springUser == SpringUser.load(springUserId) &&
			springRole == SpringRole.load(springRoleId)
		}
	}

	static SpringUserSpringRole create(SpringUser springUser, SpringRole springRole, boolean flush = false) {
		def instance = new SpringUserSpringRole(springUser: springUser, springRole: springRole)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(SpringUser u, SpringRole r) {
		if (u != null && r != null) {
			SpringUserSpringRole.where { springUser == u && springRole == r }.deleteAll()
		}
	}

	static int removeAll(SpringUser u) {
		u == null ? 0 : SpringUserSpringRole.where { springUser == u }.deleteAll() as int
	}

	static int removeAll(SpringRole r) {
		r == null ? 0 : SpringUserSpringRole.where { springRole == r }.deleteAll() as int
	}

	static constraints = {
	    springUser nullable: false
		springRole nullable: false, validator: { SpringRole r, SpringUserSpringRole ur ->
			if (ur.springUser?.id) {
				if (SpringUserSpringRole.exists(ur.springUser.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		//id composite: ['springUser', 'springRole']
		id generator: 'identity'
		version true
	}

}
