package com.luch.spring.security

import com.luch.BaseService
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityService


interface ISpringUserService {

    SpringUser get(Serializable id)

    List<SpringUser> list(Map args)

    Long count()

    void delete(Serializable id)

    SpringUser save(SpringUser springUser)

}

@Transactional
@Service(SpringUser)
abstract class SpringUserService extends BaseService implements ISpringUserService {
    SpringSecurityService springSecurityService;

    public SpringUser getCurrentUser() {
        return springSecurityService.getCurrentUser();
    }

    public SpringUser getCurrentUserIncludingSystem() {
        SpringUser springUser = springSecurityService.getCurrentUser();
        if (!springUser) {
            springUser = SpringUser.findByUsername("SYSTEM");
        }
        return springUser;
    }

}


