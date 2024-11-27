package com.luch.spring.security

import grails.plugins.orm.auditable.Auditable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class SpringUser implements Serializable, Auditable {

    SpringUser createdBy, lastUpdatedBy;
    Date dateCreated,  lastUpdated;

    private static final long serialVersionUID = 1

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Set<SpringRole> getAuthorities() {
        (SpringUserSpringRole.findAllBySpringUser(this) as List<SpringUserSpringRole>)*.springRole as Set<SpringRole>
    }

    static constraints = {
        password nullable: true, password: true;
        username nullable: false, blank: false, unique: true
        lastUpdatedBy nullable: true;
        createdBy nullable: true;
    }

    static mapping = {
        id generator: 'identity'
        version true
	    password column: '"PASSWORD"'
    }

}
